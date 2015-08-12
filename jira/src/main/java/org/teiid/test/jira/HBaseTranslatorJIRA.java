package org.teiid.test.jira;

import static org.teiid.example.util.IOUtils.findFile;
import static org.teiid.example.util.JDBCUtils.execute;
import static org.teiid.test.jira.PhoenixClient.*;

import java.io.FileInputStream;
import java.sql.Connection;
import java.util.Properties;
import java.util.logging.Level;

import javax.sql.DataSource;

import org.teiid.example.EmbeddedHelper;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.hbase.HBaseExecutionFactory;

public class HBaseTranslatorJIRA {
	
	static {
//		EmbeddedHelper.enableLogger(Level.ALL);
	}
    
    
    
    static void TEIID_3619() throws Exception {
        
        DataSource ds = EmbeddedHelper.newDataSource(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        
        EmbeddedServer server = new EmbeddedServer();
        
        HBaseExecutionFactory factory = new HBaseExecutionFactory();
        factory.start();
        server.addTranslator("translator-hbase", factory);
        
        server.addConnectionFactory("java:/hbaseDS", ds);
        
        EmbeddedConfiguration config = new EmbeddedConfiguration();
        config.setTransactionManager(EmbeddedHelper.getTransactionManager());   
        server.start(config);
        
        server.deployVDB(new FileInputStream(findFile("jira-vdb.xml")));
        
        Properties prop = new Properties();
        prop.setProperty("phoenix.connection.autoCommit", "true");
        Connection c = server.getDriver().connect("jdbc:teiid:hbasevdb", prop);
        
        
//        boolean isAutoCommit = c.getAutoCommit(); 
//        c.setAutoCommit(false);
//        execute(c, "INSERT INTO smalla5 VALUES(7, '7')", false);
//        c.commit();
//        c.setAutoCommit(isAutoCommit);
        
        
        execute(c, "SELECT * FROM smalla5", false);
        
        c.setAutoCommit(false);
        execute(c, "INSERT INTO JIRA.smalla5 VALUES(5, '5')", false);
        c.commit();
        
        execute(c, "SELECT * FROM smalla5", false);
        
        server.stop();
    }

    public static void main(String[] args) throws Exception {

    	TEIID_3623();
    }
    
    
    static void TEIID_3623() throws Exception {
    	
   	 DataSource ds = EmbeddedHelper.newDataSource(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        
        EmbeddedServer server = new EmbeddedServer();
        
        HBaseExecutionFactory factory = new HBaseExecutionFactory();
        factory.start();
        server.addTranslator("translator-hbase", factory);
        
        server.addConnectionFactory("java:/hbaseDS", ds);
        
        EmbeddedConfiguration config = new EmbeddedConfiguration();
        server.start(config);
        
        server.deployVDB(new FileInputStream(findFile("jira-vdb.xml")));
        
        Connection c = server.getDriver().connect("jdbc:teiid:hbasevdb", null);
                 
        execute(c, "INSERT INTO smalla1 VALUES(5, true)", true);
                
        server.stop();
	}

}
