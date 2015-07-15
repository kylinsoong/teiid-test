package org.teiid.test.perf;

import static org.teiid.test.Constants.H2_JDBC_DRIVER;
import static org.teiid.test.Constants.H2_JDBC_PASS;
import static org.teiid.test.Constants.H2_JDBC_URL;
import static org.teiid.test.Constants.H2_JDBC_USER;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import javax.sql.DataSource;

import org.h2.tools.RunScript;
import org.teiid.example.EmbeddedHelper;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.test.util.JDBCUtils;
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;

public class ResultsCachingDebugSimple {
	
	static EmbeddedServer server = null;
	    static Connection conn = null;
	    	    
	    public static void startup() throws Exception {
	              
	        server = new EmbeddedServer();
	        
	        H2ExecutionFactory factory = new H2ExecutionFactory();
	        factory.start();
	        factory.setSupportsDirectQueryProcedure(true);
	        server.addTranslator("translator-h2", factory);
	        
	        DataSource ds = EmbeddedHelper.newDataSource(H2_JDBC_DRIVER, H2_JDBC_URL, H2_JDBC_USER, H2_JDBC_PASS);
	        insertSampleData(ds.getConnection());
	        server.addConnectionFactory("java:/accounts-ds", ds);
	        
	        server.start(new EmbeddedConfiguration());
	        
	        server.deployVDB(ResultsCachingMysql.class.getClassLoader().getResourceAsStream("resultsCaching-h2-simple-vdb.xml"));
	        
	        Properties info = new Properties();
	        conn = server.getDriver().connect("jdbc:teiid:ResultsCachingH2VDB", info);
	    }
	    
	    private static void insertSampleData(Connection connection) throws SQLException, FileNotFoundException {
	        RunScript.execute(connection, new InputStreamReader(ResultsCachingDebugSimple.class.getClassLoader().getResourceAsStream("mat/schema.sql")));
	    }
	    
	    static void teardown() throws SQLException {
	        JDBCUtils.close(conn);
	        server.stop();
	    }

    public static void main(String[] args) throws Exception {
    	
//    	EmbeddedHelper.configureLogManager("src/main/resources/teiid-logging.properties");
    	EmbeddedHelper.enableLogger(Level.ALL, "org.teiid.BUFFER_MGR", "org.teiid.PROCESSOR");

    	query(1);
    	
    }

	private static void query(int time) throws Exception {

		startup();
    	
        String sql = "/*+ cache */ SELECT * FROM PRODUCTView";
//		String sql = "SELECT * FROM PRODUCTView";
        
        for(int i = 0 ; i < time ; i ++) {
        	JDBCUtils.executeQuery(conn, sql);
        }
        
        teardown();
	}

}
