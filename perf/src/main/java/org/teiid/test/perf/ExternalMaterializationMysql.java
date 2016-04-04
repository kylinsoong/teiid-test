package org.teiid.test.perf;

import static org.teiid.test.util.JDBCUtils.executeQuery;
import static org.teiid.test.util.JDBCUtils.executeUpdate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.test.util.EmbeddedHelper;
import org.teiid.test.util.JDBCUtils;
import org.teiid.translator.jdbc.mysql.MySQL5ExecutionFactory;

public class ExternalMaterializationMysql {
    
    static {
        EmbeddedHelper.enableLogger(Level.INFO);
    }
    
    static EmbeddedServer server = null;
    static Connection conn = null;
    
    static Logger logger = Logger.getLogger(ExternalMaterializationMysql.class.getName());
    
    static void startup() throws Exception {
              
        logger.info("Start");
        
        server = new EmbeddedServer();
        
        MySQL5ExecutionFactory factory = new MySQL5ExecutionFactory();
        factory.start();
        factory.setSupportsDirectQueryProcedure(true);
        server.addTranslator("translator-mysql", factory);
        
        DataSource ds = EmbeddedHelper.newDataSource("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/test", "jdv_user", "jdv_pass");
        server.addConnectionFactory("java:/accounts-ds", ds);
        
        EmbeddedConfiguration config = new EmbeddedConfiguration();
        config.setTransactionManager(EmbeddedHelper.getTransactionManager());
        server.start(config);
        
        server.deployVDB(ResultsCachingMysql.class.getClassLoader().getResourceAsStream("mat/mat-mysql-vdb.xml"));
        
        Properties info = new Properties();
        conn = server.getDriver().connect("jdbc:teiid:MatVDB", info);
    }
    
    static void teardown() throws SQLException {
        JDBCUtils.close(conn);
        server.stop();
    }
  

    @SuppressWarnings("static-access")
    public static void main(String[] args) throws Exception {
        
        startup();
        
        executeQuery(conn, "select * from Product");
        executeQuery(conn, "select * from test_mat");
        executeQuery(conn, "select * from test_mat_staging");
        executeQuery(conn, "select * from status");
        executeQuery(conn, "select * from MatView");
        
        Thread.currentThread().sleep(30 * 1000);
        
        executeQuery(conn, "select * from Product");
        executeQuery(conn, "select * from test_mat");
        executeQuery(conn, "select * from test_mat_staging");
        executeQuery(conn, "select * from status");
        executeQuery(conn, "select * from MatView");
        
        executeUpdate(conn, "INSERT INTO PRODUCT (ID,SYMBOL,COMPANY_NAME) VALUES(2000,'RHT','Red Hat Inc')");
        
        Thread.currentThread().sleep(30 * 1000);
        
        executeQuery(conn, "select * from Product");
        executeQuery(conn, "select * from test_mat");
        executeQuery(conn, "select * from test_mat_staging");
        executeQuery(conn, "select * from status");
        executeQuery(conn, "select * from MatView");
        
        executeUpdate(conn, "DELETE FROM PRODUCT  WHERE ID = 2000");
        
        Thread.currentThread().sleep(30 * 1000);
        
        executeQuery(conn, "select * from Product");
        executeQuery(conn, "select * from test_mat");
        executeQuery(conn, "select * from test_mat_staging");
        executeQuery(conn, "select * from status");
        executeQuery(conn, "select * from MatView");
        
        teardown();
    }

   

}
