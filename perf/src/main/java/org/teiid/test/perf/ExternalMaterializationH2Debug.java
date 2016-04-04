package org.teiid.test.perf;

import static org.teiid.test.Constants.H2_JDBC_DRIVER;
import static org.teiid.test.Constants.H2_JDBC_PASS;
import static org.teiid.test.Constants.H2_JDBC_URL;
import static org.teiid.test.Constants.H2_JDBC_USER;
import static org.teiid.test.util.JDBCUtils.execute;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

import javax.resource.ResourceException;
import javax.sql.DataSource;

import org.h2.tools.RunScript;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.test.util.EmbeddedHelper;
import org.teiid.test.util.JDBCUtils;
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;

public class ExternalMaterializationH2Debug {
    
    static EmbeddedServer server = null;
    static Connection conn = null;
    
    static void startup() throws Exception {
        
        EmbeddedHelper.enableLogger(Level.INFO);
        
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
                
        server.deployVDB(ExternalMaterializationH2Debug.class.getClassLoader().getResourceAsStream("mat/mat-h2-vdb-debug.xml"));
        
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


    public static void main(String[] args) throws Exception {
        
        startTimer();
        
        startup();
        
        execute(conn, "execute SYSADMIN.loadMatView('Stocks','MatView')", false);
        
        teardown();
    }

    private static void startTimer() throws ResourceException, SQLException {

        DataSource ds = EmbeddedHelper.newDataSource(H2_JDBC_DRIVER, H2_JDBC_URL, H2_JDBC_USER, H2_JDBC_PASS);
        Timer time = new Timer("Query", true);
        TimerTask task = new QueryJob(ds.getConnection());   
        time.schedule(task, 2000, 5000);
    }
    
    private static class QueryJob extends TimerTask {
        
        Connection conn;
        
        QueryJob(Connection conn) {
            this.conn = conn;
        }

        @Override
        public void run() {
            try {
                execute(conn, "select * from Product", false);
                execute(conn, "select * from h2_test_mat", false);
                execute(conn, "select * from mat_test_staging", false);
                execute(conn, "select * from status", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
