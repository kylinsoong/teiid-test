package org.teiid.test.perf.buffer;

import static org.teiid.test.Constants.H2_JDBC_DRIVER;
import static org.teiid.test.Constants.H2_JDBC_PASS;
import static org.teiid.test.Constants.H2_JDBC_URL;
import static org.teiid.test.Constants.H2_JDBC_USER;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

import javax.resource.ResourceException;
import javax.sql.DataSource;

import org.teiid.adminapi.Admin;
import org.teiid.adminapi.EngineStatistics;
import org.teiid.deployers.VirtualDatabaseException;
import org.teiid.dqp.internal.datamgr.ConnectorManagerRepository.ConnectorManagerException;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.test.PerfEntity;
import org.teiid.test.perf.ResultsCachingMysql;
import org.teiid.test.util.EmbeddedHelper;
import org.teiid.test.util.Util;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;

public class BufferManagerPerf {
	
	public static void main(String[] args) throws Exception {
		
		EmbeddedHelper.enableLogger(Level.ALL);
		
		startup();
		
		
		final Object lock = new Object();
		
		final AtomicInteger index = new AtomicInteger(0);
		
		
		for(int i = 0 ; i < 10 ; i ++) {
			new Thread(new Runnable(){
				public void run() {
					Connection conn = getConnection();
					while(true){
						try {
							///*+ cache */ 
							PerfEntity entity = Util.executeQueryCount(conn, "/*+ cache */ SELECT * FROM PERFTESTVIEW");
//							System.out.println(index.incrementAndGet() + " - " + entity.getQueryTime());
							index.incrementAndGet();
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
					
				}}).start();
		}
		
		
		
		Admin admin = server.getAdmin();
		
		int old = -1;
		while(true){
			synchronized (lock) {
				lock.wait(1000);
			}
			if(old != index.get()){
				System.out.println(index.get() + " - " + getStaticString( admin.getEngineStats()));
				old = index.get();
			}
		}
		
		
		
//		teardown();
		
	}
	
	private static String getStaticString(Collection<? extends EngineStatistics> engineStats) {
		
		EngineStatistics stat = engineStats.iterator().next();
		
		return "SessionCount: " + stat.getSessionCount() + "  TotalMemoryUsedInKB: " + stat.getTotalMemoryUsedInKB() + "  MemoryUsedByActivePlansInKB: " + stat.getMemoryUsedByActivePlansInKB() + 
				"  DiskWriteCount: " + stat.getDiskWriteCount() + "  DiskReadCount: " + stat.getDiskReadCount() + " CacheReadCount: " + stat.getCacheReadCount() + "  CacheWriteCount: " + stat.getCacheWriteCount() + "  DiskSpaceUsedInMB: " + stat.getDiskSpaceUsedInMB();
	}

	static EmbeddedServer server = null;
	
    static void startup() throws TranslatorException, VirtualDatabaseException, ConnectorManagerException, IOException, SQLException, ResourceException {
        
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
        
        server.deployVDB(ResultsCachingMysql.class.getClassLoader().getResourceAsStream("resultsCaching-h2-vdb.xml"));
         
    }
    
    static Connection getConnection(){
    	Properties info = new Properties();
        try {
			return server.getDriver().connect("jdbc:teiid:ResultsCachingH2VDB", info);
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return null;
    }
    
    static void teardown() throws SQLException {
        server.stop();
    }

}
