package org.teiid.test.jira;

import static org.teiid.example.util.IOUtils.findFile;
import static org.teiid.example.util.JDBCUtils.execute;
import static org.teiid.test.jira.PhoenixClient.JDBC_DRIVER;
import static org.teiid.test.jira.PhoenixClient.JDBC_PASS;
import static org.teiid.test.jira.PhoenixClient.JDBC_URL;
import static org.teiid.test.jira.PhoenixClient.JDBC_USER;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.sql.DataSource;

import org.teiid.example.EmbeddedHelper;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.hbase.HBaseExecutionFactory;

public class Teiid3620 {

	public static void main(String[] args) throws Exception {

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
        
        Connection c = server.getDriver().connect("jdbc:teiid:hbasevdb", null);
        
//        PreparedStatement pstmt = null ;
        
//        execute(c, "SELECT * FROM smalla4", false);
//        
        execute(c, "INSERT INTO smalla4(IntKey, c3) VALUES(10, '2000-01-02 03:04:05.0')", false);
        
        server.stop();
	}

}
