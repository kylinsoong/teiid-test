package org.teiid.test.teiid4121;

import static org.teiid.test.Constants.H2_JDBC_DRIVER;
import static org.teiid.test.Constants.H2_JDBC_PASS;
import static org.teiid.test.Constants.H2_JDBC_URL;
import static org.teiid.test.Constants.H2_JDBC_USER;
import static org.teiid.test.util.JDBCUtils.execute;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.Properties;
import java.util.logging.Level;

import javax.sql.DataSource;

import org.h2.tools.RunScript;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.test.util.EmbeddedHelper;
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;

public class TeiidMaterializationExample {

    static EmbeddedServer server = null;
    static Connection conn = null;

    public static void main(String[] args) throws Exception {

        EmbeddedHelper.enableLogger(Level.WARNING);
        
        DataSource ds = EmbeddedHelper.newDataSource(H2_JDBC_DRIVER, H2_JDBC_URL, H2_JDBC_USER, H2_JDBC_PASS);
        RunScript.execute(ds.getConnection(), new InputStreamReader(TeiidMaterializationExample.class.getClassLoader().getResourceAsStream("teiid-4121/h2-schema.sql")));
        
        
        server = new EmbeddedServer();
        
        H2ExecutionFactory factory = new H2ExecutionFactory();
        factory.start();
        factory.setSupportsDirectQueryProcedure(true);
        server.addTranslator("translator-h2", factory);
        
        server.addConnectionFactory("java:/accounts-ds", ds);
        
        EmbeddedConfiguration config = new EmbeddedConfiguration();
        config.setTransactionManager(EmbeddedHelper.getTransactionManager());
        config.setTimeSliceInMilli(Integer.MAX_VALUE);
        server.start(config);
                
        server.deployVDB(TEIID4121.class.getClassLoader().getResourceAsStream("teiid-4121/teiid4121-example-vdb.xml"));
        
        Properties info = new Properties();
        conn = server.getDriver().connect("jdbc:teiid:TEIID4121H2VDB", info);
        
        Thread.sleep(3000);
        
        execute(conn, "SELECT * FROM VIEW_A", false);
        execute(conn, "SELECT * FROM VIEW_B", false);
        execute(conn, "SELECT * FROM VIEW_C", false);
        execute(conn, "SELECT * FROM VIEW_D", false);
        execute(conn, "exec SYSADMIN.loadMatView('Model_E', 'VIEW_E')", false);
        execute(conn, "SELECT * FROM VIEW_E", false);
        execute(conn, "SELECT * FROM VIEW_F", false);
        
        execute(conn, "SELECT * FROM status", false);
        
        conn.close();
    }
}
