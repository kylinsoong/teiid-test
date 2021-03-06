package org.teiid.test.perf;

import static org.teiid.test.Constants.H2_JDBC_DRIVER;
import static org.teiid.test.Constants.H2_JDBC_PASS;
import static org.teiid.test.Constants.H2_JDBC_URL;
import static org.teiid.test.Constants.H2_JDBC_USER;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.teiid.test.util.EmbeddedHelper;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.test.util.JDBCUtils;
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;

public class ResultsCachingDebugBuffer {
	
	static EmbeddedServer server = null;
    static Connection conn = null;
    
    public static void startup() throws Exception {
    	
    	server = new EmbeddedServer();
    	
    	H2ExecutionFactory factory = new H2ExecutionFactory();
        factory.start();
        factory.setSupportsDirectQueryProcedure(true);
        server.addTranslator("translator-h2", factory);
        
        DataSource ds = EmbeddedHelper.newDataSource(H2_JDBC_DRIVER, H2_JDBC_URL, H2_JDBC_USER, H2_JDBC_PASS);
        server.addConnectionFactory("java:/accounts-ds", ds);
        
        server.start(new EmbeddedConfiguration());
        
        server.deployVDB(ResultsCachingMysql.class.getClassLoader().getResourceAsStream("resultsCaching-h2-vdb.xml"));
        
        Properties info = new Properties();
        conn = server.getDriver().connect("jdbc:teiid:ResultsCachingH2VDB", info);
    	
    }
    
    static void teardown() throws SQLException {
        JDBCUtils.close(conn);
        server.stop();
    }

	public static void main(String[] args) throws Exception {
		
//		EmbeddedHelper.configureLogManager("src/main/resources/teiid-logging.properties");
//		EmbeddedHelper.enableLogger(Level.ALL, "org.teiid.BUFFER_MGR");
		
		startup();
		
		for(int i = 0 ; i < 3 ; i ++) {
			JDBCUtils.executeQuery(conn, "/*+ cache */ SELECT * FROM PERFTESTVIEW");
		}
		
		teardown();

	}

	static void executeQuery(Connection conn, String sql) throws SQLException {

		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			int size = rs.getMetaData().getColumnCount();
			while(rs.next()){
				for(int i = 1 ; i <= size ; i ++) {
					rs.getObject(i);
				}
			}
		} finally {
			JDBCUtils.close(rs, stmt);
		}
	}

}
