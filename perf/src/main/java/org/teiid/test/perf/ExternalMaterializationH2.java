package org.teiid.test.perf;

import static org.teiid.test.Constants.H2_JDBC_DRIVER;
import static org.teiid.test.Constants.H2_JDBC_PASS;
import static org.teiid.test.Constants.H2_JDBC_URL;
import static org.teiid.test.Constants.H2_JDBC_USER;
import static org.teiid.test.util.JDBCUtils.executeQuery;
import static org.teiid.test.util.JDBCUtils.executeUpdate;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import javax.sql.DataSource;

import org.h2.tools.RunScript;
import org.teiid.example.EmbeddedHelper;
import org.teiid.query.test.TestHelper;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.test.util.JDBCUtils;
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;

public class ExternalMaterializationH2 {
    
    static EmbeddedServer server = null;
    static Connection conn = null;
    
    static void startup() throws Exception {
        
        TestHelper.enableLogger(Level.INFO);
        
        DataSource ds = EmbeddedHelper.newDataSource(H2_JDBC_DRIVER, H2_JDBC_URL, H2_JDBC_USER, H2_JDBC_PASS);
        insertSampleData(ds.getConnection());
        
        server = new EmbeddedServer();
        
        H2ExecutionFactory factory = new H2ExecutionFactory();
        factory.start();
        factory.setSupportsDirectQueryProcedure(true);
        server.addTranslator("translator-h2", factory);
        
        server.addConnectionFactory("java:/accounts-ds", ds);
        
        EmbeddedConfiguration config = new EmbeddedConfiguration();
        config.setTransactionManager(EmbeddedHelper.getTransactionManager());
        server.start(config);
        
        server.deployVDB(ResultsCachingMysql.class.getClassLoader().getResourceAsStream("mat/mat-h2-vdb.xml"));
        
        Properties info = new Properties();
        conn = server.getDriver().connect("jdbc:teiid:MatVDB", info);
    }
    
    private static void insertSampleData(Connection connection) throws SQLException, FileNotFoundException {
        RunScript.execute(connection, new InputStreamReader(new FileInputStream("src/main/resources/mat/schema.sql")));
        
    }
    
    static void teardown() throws SQLException {
        JDBCUtils.close(conn);
        server.stop();
    }

    @SuppressWarnings("static-access")
    public static void main(String[] args) throws Exception {
        
        startup();
                
        executeQuery(conn, "select * from Product");
        executeQuery(conn, "select * from h2_test_mat");
        executeQuery(conn, "select * from mat_test_staging");
        executeQuery(conn, "select * from status");
        executeQuery(conn, "select * from MatView");
        
        Thread.currentThread().sleep(30 * 1000);
        
        executeQuery(conn, "select * from Product");
        executeQuery(conn, "select * from h2_test_mat");
        executeQuery(conn, "select * from mat_test_staging");
        executeQuery(conn, "select * from status");
        executeQuery(conn, "select * from MatView");
        
        executeUpdate(conn, "INSERT INTO PRODUCT (ID,SYMBOL,COMPANY_NAME) VALUES(2000,'RHT','Red Hat Inc')");
        
        Thread.currentThread().sleep(30 * 1000);
        
        executeQuery(conn, "select * from Product");
        executeQuery(conn, "select * from h2_test_mat");
        executeQuery(conn, "select * from mat_test_staging");
        executeQuery(conn, "select * from status");
        executeQuery(conn, "select * from MatView");
        
        executeUpdate(conn, "DELETE FROM PRODUCT  WHERE ID = 2000");
        
        Thread.currentThread().sleep(30 * 1000);
        
        executeQuery(conn, "select * from Product");
        executeQuery(conn, "select * from h2_test_mat");
        executeQuery(conn, "select * from mat_test_staging");
        executeQuery(conn, "select * from status");
        executeQuery(conn, "select * from MatView");
        
        teardown();
    }

}
