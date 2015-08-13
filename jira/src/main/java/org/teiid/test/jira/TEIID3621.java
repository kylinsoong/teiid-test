package org.teiid.test.jira;

import static org.teiid.example.util.IOUtils.findFile;
import static org.teiid.example.util.JDBCUtils.close;
import static org.teiid.example.util.JDBCUtils.execute;
import static org.teiid.test.jira.PhoenixClient.JDBC_DRIVER;
import static org.teiid.test.jira.PhoenixClient.JDBC_PASS;
import static org.teiid.test.jira.PhoenixClient.JDBC_URL;
import static org.teiid.test.jira.PhoenixClient.JDBC_USER;

import java.io.FileInputStream;
import java.sql.Connection;

import javax.sql.DataSource;

import org.teiid.example.EmbeddedHelper;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.hbase.HBaseExecutionFactory;

public class TEIID3621 {

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
        
        Connection conn = server.getDriver().connect("jdbc:teiid:hbasevdb", null);
        
//        execute(conn, "SELECT * FROM JIRA.smalla", false);
        
        execute(conn, "UPDATE JIRA.smalla SET StringKey = 'xxx' WHERE JIRA.smalla.StringKey IS NULL", false);
        
//        execute(conn, "UPDATE JIRA.smalla SET StringKey = 'xxxx'", false);
        
//        execute(conn, "SELECT * FROM JIRA.smalla", false);
        
        close(conn);
	}

}
