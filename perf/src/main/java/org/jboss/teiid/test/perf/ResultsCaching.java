package org.jboss.teiid.test.perf;

import static org.teiid.test.util.JDBCUtils.executeQueryCount;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.resource.ResourceException;
import javax.sql.DataSource;

import org.teiid.deployers.VirtualDatabaseException;
import org.teiid.dqp.internal.datamgr.ConnectorManagerRepository.ConnectorManagerException;
import org.teiid.example.EmbeddedHelper;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.test.util.JDBCUtils;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.jdbc.mysql.MySQL5ExecutionFactory;

/**
 * A Performance tool class for Results Caching.
 * 
 * Teiid provides the capability to cache the results of specific user queries and virtual procedure calls. This caching technique 
 * can yield significant performance gains if  users of the system submit the same queries or execute the same procedures often.
 * 
 * 
 * @author kylin
 *
 */
public class ResultsCaching {

	
	static EmbeddedServer server = null;
	static Connection conn = null;
	
	
	static void startup() throws TranslatorException, VirtualDatabaseException, ConnectorManagerException, IOException, SQLException, ResourceException {

		server = new EmbeddedServer();
		
		MySQL5ExecutionFactory factory = new MySQL5ExecutionFactory();
		factory.start();
		factory.setSupportsDirectQueryProcedure(true);
		server.addTranslator("translator-mysql", factory);
		
		DataSource ds = EmbeddedHelper.newDataSource("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/test", "jdv_user", "jdv_pass");
		server.addConnectionFactory("java:/accounts-ds", ds);
		
		server.start(new EmbeddedConfiguration());
		
		server.deployVDB(ResultsCaching.class.getClassLoader().getResourceAsStream("mysql-vdb.xml"));
		conn = server.getDriver().connect("jdbc:teiid:MysqlVDB", null);
	}
	
	static void teardown() throws SQLException {
		JDBCUtils.close(conn);
		server.stop();
	}
	
	public static void perfQuery(String sql) throws Exception {
		
		startup();
		
		for(int i = 0 ; i < 10 ; i ++) {
			PerfEntity entity = Util.executeQueryCount(conn, sql);
			Util.persitResult(entity);
		}
		
		teardown();
	}

	
	public static void simpleQuery() throws VirtualDatabaseException, TranslatorException, ConnectorManagerException, IOException, SQLException, ResourceException {
		
		startup();
				
		String sql = "SELECT * FROM PERFTESTVIEW";
		long time = executeQueryCount(conn, sql);
		System.out.println(time);
		
		teardown();
	}
	
	public static void userQueryCache() throws SQLException, VirtualDatabaseException, TranslatorException, ConnectorManagerException, IOException, ResourceException {
		
		startup();
		
		String sql = "/*+ cache */ SELECT * FROM PERFTESTVIEW";
		long time = executeQueryCount(conn, sql);
		System.out.println(time);
		
		teardown();
	}
	
	public static void main(String[] args) throws Exception {
//		simpleQuery();
//		userQueryCache();
		
		perfQuery("SELECT * FROM PERFTESTVIEW");
		
		perfQuery("/*+ cache */ SELECT * FROM PERFTESTVIEW");
	}

}
