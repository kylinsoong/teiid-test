package org.teiid.test.teiid3952;

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

public class SystemSchemaTest {
    
    static EmbeddedServer server = null;
    static Connection conn = null;

    public static void main(String[] args) throws Exception {

        EmbeddedHelper.enableLogger(Level.ALL);
        
        DataSource ds = EmbeddedHelper.newDataSource(H2_JDBC_DRIVER, H2_JDBC_URL, H2_JDBC_USER, H2_JDBC_PASS);
        RunScript.execute(ds.getConnection(), new InputStreamReader(TEIID3952Reproduce.class.getClassLoader().getResourceAsStream("teiid-3952/h2-schema.sql")));
        
//        execute(ds.getConnection(), "SELECT * FROM SampleTable", false);
        
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
                
        server.deployVDB(TEIID3952Reproduce.class.getClassLoader().getResourceAsStream("teiid-3952/systemschema-vdb.xml"));
        
        Properties info = new Properties();
        conn = server.getDriver().connect("jdbc:teiid:MatViewH2VDB", info);
        
//        test_sysadmin_foreign_tables();
//        test_sysadmin_foreign_procedures();
//        test_sys_foreign_tables();
        
        execute(conn, "SELECT * FROM SYSADMIN.MatViews", false);
                
        conn.close();
    }

    static void test_sysadmin_foreign_procedures() throws Exception {
        
        execute(conn, "EXEC SYSADMIN.logMsg('ERROR', 'com.sample', 'Hello World SYSADMIN.logMsg')", false);
    }

    static void test_sys_foreign_tables() throws Exception {
        
        execute(conn, "SELECT * FROM SYS.Columns", false);
        execute(conn, "SELECT * FROM SYS.DataTypes", false);       
        execute(conn, "SELECT * FROM SYS.KeyColumns", false);
        execute(conn, "SELECT * FROM SYS.Keys", false);
        execute(conn, "SELECT * FROM SYS.ProcedureParams", false);
        
        execute(conn, "SELECT * FROM SYS.Procedures", false);
        execute(conn, "SELECT * FROM SYS.FunctionParams", false);
        execute(conn, "SELECT * FROM SYS.Functions", false);
        execute(conn, "SELECT * FROM SYS.Properties", false);
        execute(conn, "SELECT * FROM SYS.ReferenceKeyColumns", false);
        execute(conn, "SELECT * FROM SYS.Schemas", false);
        execute(conn, "SELECT * FROM SYS.Tables", false);
        execute(conn, "SELECT * FROM SYS.VirtualDatabases", false);
//        execute(conn, "SELECT * FROM ", false);
    }

    static void test_sysadmin_foreign_tables() throws Exception {

        execute(conn, "SELECT * FROM SYSADMIN.Usage", false);
        execute(conn, "SELECT * FROM SYSADMIN.MatViews", false);
        execute(conn, "SELECT * FROM SYSADMIN.VDBResources", false);
        execute(conn, "SELECT * FROM SYSADMIN.Triggers", false);
        execute(conn, "SELECT * FROM SYSADMIN.Views", false);
        execute(conn, "SELECT * FROM SYSADMIN.StoredProcedures", false);
    }

}
