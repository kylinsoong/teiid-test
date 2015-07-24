package org.teiid.test.perf.buffer;

import static org.teiid.test.Constants.H2_JDBC_DRIVER;
import static org.teiid.test.Constants.H2_JDBC_PASS;
import static org.teiid.test.Constants.H2_JDBC_URL;
import static org.teiid.test.Constants.H2_JDBC_USER;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

import javax.resource.ResourceException;
import javax.sql.DataSource;

import org.teiid.adminapi.Admin;
import org.teiid.adminapi.EngineStatistics;
import org.teiid.deployers.VirtualDatabaseException;
import org.teiid.dqp.internal.datamgr.ConnectorManagerRepository.ConnectorManagerException;
import org.teiid.example.EmbeddedHelper;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;

public class BufferManagerPerfDebug {
	
	public static final String SQL_1 = "SELECT * FROM PERFTESTVIEW";
	public static final String SQL_2 = "SELECT * FROM PERFTESTVIEW WHERE id = '1234'";
	public static final String SQL_3 = "SELECT * FROM PERFTESTVIEW WHERE col_a = 'abcdefghabcdefgh'";
	public static final String SQL_4 = "SELECT * FROM PERFTESTVIEW WHERE col_b = 'abcdefghigklmnopqrstabcdefghigklmnopqrst'";
	public static final String SQL_5 = "SELECT * FROM PERFTESTVIEW WHERE col_c = '1234567890123456789012345678901234567890'";
	
	public static final String CACHE_SQL_1 = "/*+ cache */ SELECT * FROM PERFTESTVIEW";
	public static final String CACHE_SQL_2 = "/*+ cache */ SELECT * FROM PERFTESTVIEW WHERE id = '1234'";
	public static final String CACHE_SQL_3 = "/*+ cache */ SELECT * FROM PERFTESTVIEW WHERE col_a = 'abcdefghabcdefgh'";
	public static final String CACHE_SQL_4 = "/*+ cache */ SELECT * FROM PERFTESTVIEW WHERE col_b = 'abcdefghigklmnopqrstabcdefghigklmnopqrst'";
	public static final String CACHE_SQL_5 = "/*+ cache */ SELECT * FROM PERFTESTVIEW WHERE col_c = '1234567890123456789012345678901234567890'";
	
	final Object lock = new Object();
	final AtomicInteger index = new AtomicInteger(0);
	
	EmbeddedServer server = null;
	
	Executor executor;
	
	private final AtomicInteger threadSeq = new AtomicInteger(1);
	
	public BufferManagerPerfDebug() throws VirtualDatabaseException, TranslatorException, ConnectorManagerException, IOException, SQLException, ResourceException {
		startup();
		executor = Executors.newCachedThreadPool(new ThreadFactory(){

			@Override
			public Thread newThread(Runnable r) {
				Thread thread = new Thread(r);
	            thread.setName(String.format("MGR debug thread %d", Integer.valueOf(threadSeq.getAndIncrement())));
				return thread;
			}});
	}
	
	private void startup() throws TranslatorException, ResourceException, VirtualDatabaseException, ConnectorManagerException, IOException {

		server = new EmbeddedServer();
        
        H2ExecutionFactory factory = new H2ExecutionFactory();
        factory.start();
        factory.setSupportsDirectQueryProcedure(true);
        server.addTranslator("translator-h2", factory);
        
        DataSource ds = EmbeddedHelper.newDataSource(H2_JDBC_DRIVER, H2_JDBC_URL, H2_JDBC_USER, H2_JDBC_PASS);
        server.addConnectionFactory("java:/accounts-ds", ds);
        
        EmbeddedConfiguration config = new EmbeddedConfiguration();
        config.setUseDisk(true);
		config.setBufferDirectory("/home/kylin/tmp/buffer");
		config.setProcessorBatchSize(256);
		
        server.start(config);
        
        server.deployVDB(BufferManagerPerfDebug.class.getClassLoader().getResourceAsStream("resultsCaching-h2-vdb.xml"));
	}

	public void startMonitorThreads() {

		new Thread(new Runnable(){

			@Override
			public void run() {
				
				Thread.currentThread().setName("MGR monitor thread");
				
				Admin admin = server.getAdmin();
				
				int old = -1;
				while(true){
					try {
						
						if(old != index.get()){
							System.out.println(index.get() + " - " + getStaticString( admin.getEngineStats()));
							old = index.get();
						}
						
						synchronized (lock) {
							lock.wait(2000);
							lock.notifyAll();
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}}).start();
	}
	
	private String getStaticString(Collection<? extends EngineStatistics> engineStats) {
		
		EngineStatistics stat = engineStats.iterator().next();
		
		return "SessionCount: " + stat.getSessionCount() + 
			   "  TotalMemoryUsedInKB: " + stat.getTotalMemoryUsedInKB() + 
			   "  MemoryUsedByActivePlansInKB: " + stat.getMemoryUsedByActivePlansInKB() + 
			   "  DiskWriteCount: " + stat.getDiskWriteCount() + 
			   "  DiskReadCount: " + stat.getDiskReadCount() + 
			   "  CacheReadCount: " + stat.getCacheReadCount() + 
			   "  CacheWriteCount: " + stat.getCacheWriteCount() + 
			   "  DiskSpaceUsedInMB: " + stat.getDiskSpaceUsedInMB() +
			   "  ActivePlanCount: " + stat.getActivePlanCount() + 
			   "  WaitPlanCount: " + stat.getWaitPlanCount() + 
			   "  MaxWaitPlanWaterMark: " + stat.getMaxWaitPlanWaterMark();
	}
	
	public Connection getConnection(){
		Properties info = new Properties();
        try {
			return server.getDriver().connect("jdbc:teiid:ResultsCachingH2VDB", info);
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return null;
	}

	public static void main(String[] args) throws VirtualDatabaseException, TranslatorException, ConnectorManagerException, IOException, SQLException, ResourceException {

		EmbeddedHelper.enableLogger(Level.ALL, "org.teiid.BUFFER_MGR");
		
		BufferManagerPerfDebug debug = new BufferManagerPerfDebug();
		
		debug.startMonitorThreads();
		
		debug.startQueryThreads();

	}

	private void startQueryThreads() {
		
		startQueryThreadsNonBlok(CACHE_SQL_1);
		
//		startQueryThreads(CACHE_SQL_1);
//		startQueryThreads(CACHE_SQL_2);
//		startQueryThreads(CACHE_SQL_3);
//		startQueryThreads(CACHE_SQL_4);
//		startQueryThreads(CACHE_SQL_5);
		
		
	}

	private void startQueryThreadsNonBlok(String cacheSql1) {

		executor.execute(new Runnable(){

			@Override
			public void run() {
				
				Connection conn = getConnection();
				while(true) {
					try(Statement stmt = conn.createStatement();ResultSet rs = stmt.executeQuery(CACHE_SQL_1);){
						index.incrementAndGet();
						
						int columns = rs.getMetaData().getColumnCount();
						while(rs.next()) {
							for (int i = 0 ; i < columns ; ++i) {
								rs.getObject(i+1);
							}
						}
						
						synchronized (lock) {
							lock.wait();
						}
					} catch (SQLException | InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}});
	}

	private void startQueryThreads(String sql) {
		
		executor.execute(new Runnable(){

			@Override
			public void run() {
				
				Connection conn = getConnection();
				try(Statement stmt = conn.createStatement();ResultSet rs = stmt.executeQuery(CACHE_SQL_1);){
					index.incrementAndGet();
					synchronized (lock) {
						lock.wait();
					}
				} catch (SQLException | InterruptedException e) {
					e.printStackTrace();
				}
				
			}});
	}

}
