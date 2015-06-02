package org.teiid.test.perf;

import static org.teiid.test.Constants.H2_JDBC_DRIVER;
import static org.teiid.test.Constants.H2_JDBC_PASS;
import static org.teiid.test.Constants.H2_JDBC_URL;
import static org.teiid.test.Constants.H2_JDBC_USER;
import static org.teiid.test.util.JDBCUtils.execute;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

import javax.sql.DataSource;

import org.h2.tools.RunScript;
import org.teiid.example.EmbeddedHelper;
import org.teiid.query.test.TestHelper;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.test.util.JDBCUtils;
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;

public class InternalMaterializationH2 {
    
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
                
        server.deployVDB(ResultsCachingMysql.class.getClassLoader().getResourceAsStream("mat/mat-inter-h2-vdb.xml"));
        
        Properties info = new Properties();
        conn = server.getDriver().connect("jdbc:teiid:MatVDB", info);
        
        
    }
    
    private static void insertSampleData(Connection connection) throws SQLException, FileNotFoundException {
        RunScript.execute(connection, new InputStreamReader(ResultsCachingMysql.class.getClassLoader().getResourceAsStream("mat/schema.sql")));
        
    }
    
    static void teardown() throws SQLException {
        JDBCUtils.close(conn);
        server.stop();
    }

    public static void main(String[] args) throws Exception {
                
        startup();
        
        execute(conn, "SELECT VDBName, SchemaName, Name, Type, NameInSource, IsPhysical, SupportsUpdates FROM Sys.Tables", false);
        execute(conn, "select * from MatView", false);
//        execute(conn, "select * from spatial_ref_sys", false);
        execute(conn, "SELECT VDBName, SchemaName, Name, Type, NameInSource, IsPhysical, SupportsUpdates FROM Sys.Tables", false);
        execute(conn, "select * from MatView", false);
//        execute(conn, "select * from spatial_ref_sys", false);
        
        teardown();
        
    }
}
