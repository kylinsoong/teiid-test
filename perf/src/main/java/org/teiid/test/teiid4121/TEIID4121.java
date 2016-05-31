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

public class TEIID4121 {

    static EmbeddedServer server = null;
    static Connection conn = null;

    static String timetoken() {
        return "" + System.nanoTime();
    }
    
    public static void main(String[] args) throws Exception {

        EmbeddedHelper.enableLogger(Level.WARNING);
        
        DataSource ds = EmbeddedHelper.newDataSource(H2_JDBC_DRIVER, H2_JDBC_URL, H2_JDBC_USER, H2_JDBC_PASS);
        RunScript.execute(ds.getConnection(), new InputStreamReader(TEIID4121.class.getClassLoader().getResourceAsStream("teiid-4121/h2-schema.sql")));
        
        
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
                
        server.deployVDB(TEIID4121.class.getClassLoader().getResourceAsStream("teiid-4121/teiid4121-h2-vdb.xml"));
        
        Properties info = new Properties();
        conn = server.getDriver().connect("jdbc:teiid:TEIID4121H2VDB", info);
        
        Thread.sleep(3000);
        
        execute(conn, "exec SYSADMIN.matViewStatus('SampleModel', 'SAMPLEMATVIEW')", false);
//        
//        execute(conn, "", false);
        
//        test_nocache();
//        test_nocache_2();
        
//        test_loadMatView();
        
//        test_loadMatView_2();
        
        conn.close();
    }

    static void test_loadMatView_2() throws Exception {

//        execute(conn, "SELECT * FROM MatViews", false);
//        execute(conn, "SELECT * FROM SAMPLEMATVIEW", false);
        
//        execute(conn, "SELECT * FROM SampleTable", false);
//        execute(conn, "SELECT * FROM SampleTable_mat", false);
//        execute(conn, "SELECT * FROM SampleTable_staging", false);
//        execute(conn, "SELECT * FROM status", false);
        
//        execute(conn, "SELECT * FROM SampleTable", false);
//        execute(conn, "execute accounts.native('truncate table SampleTable')", false);
//        execute(conn, "SELECT * FROM SampleTable", false);
        
//        execute(conn, "UPDATE SampleTable SET c = '" + timetoken() + "' WHERE id = '102'", false);
//        execute(conn, "SELECT * FROM SampleModel.SAMPLEMATVIEW OPTION NOCACHE", false);
//        execute(conn, "SELECT * FROM SampleModel.SAMPLEMATVIEW OPTION NOCACHE SampleModel.SAMPLEMATVIEW", false);
//        execute(conn, "INSERT INTO SampleTable_staging SELECT * FROM SampleModel.SAMPLEMATVIEW OPTION NOCACHE SampleModel.SAMPLEMATVIEW", false);
//        execute(conn, "SELECT * FROM SampleTable_staging", false);
        
        execute(conn, "EXEC testloadMatView('SampleModel', 'SAMPLEMATVIEW')", false);
        execute(conn, "SELECT * FROM status", false);
        Thread.sleep( 10 * 1000);
        execute(conn, "EXEC testloadMatView('SampleModel', 'SAMPLEMATVIEW', true)", false);
        
//        execute(conn, "SELECT * FROM SampleTable", false);
//        execute(conn, "SELECT * FROM SampleTable_mat", false);
//        execute(conn, "SELECT * FROM SampleTable_staging", false);
        execute(conn, "SELECT * FROM status", false);
    }

    static void test_nocache_2() throws Exception {

        Thread.sleep(3000);
        
        execute(conn, "UPDATE SampleTable SET c = '" + timetoken() + "' WHERE id = '102'", false);
        execute(conn, "SELECT * FROM SampleTable", false);
        execute(conn, "SELECT * FROM SampleTable_mat", false);
        execute(conn, "SELECT * FROM SAMPLEMATVIEW", false);
        execute(conn, "SELECT * FROM SAMPLEMATVIEW OPTION NOCACHE SAMPLEMATVIEW", false);
        execute(conn, "SELECT * FROM SAMPLEMATVIEW OPTION NOCACHE", false);
        
    }

    static void test_nocache() throws Exception {
        
        Thread.sleep(3000);
        
        execute(conn, "SELECT * FROM SAMPLEMATVIEW", false);
        execute(conn, "UPDATE SampleTable_mat SET c = '" + timetoken() + "' WHERE id = '102'", false);
        execute(conn, "SELECT * FROM SampleTable_mat", false);
        execute(conn, "SELECT * FROM SAMPLEMATVIEW", false);
        execute(conn, "SELECT * FROM SampleTable", false);
        execute(conn, "SELECT * FROM SAMPLEMATVIEW OPTION NOCACHE SAMPLEMATVIEW", false);
        execute(conn, "SELECT * FROM SAMPLEMATVIEW OPTION NOCACHE", false);
    }

    static void test_loadMatView() throws Exception {

        // 1. Check whether View is exist, if not exist throw example
        execute(conn, "SELECT UID FROM Sys.Tables WHERE VDBName = 'TEIID4121H2VDB' AND SchemaName = 'SampleModel' AND Name = 'SAMPLEMATVIEW'", false);
    
        // 2. Check whether View is Materialized, if not exist throw example
        execute(conn, "SELECT IsMaterialized FROM SYS.Tables WHERE UID = 'tid:563d8a97e0d9-c947fefb-00000001'", false);
        
        String statusTable = "status"; // MATVIEW_STATUS_TABLE
        String beforeLoadScript = "execute accounts.native('truncate table SampleTable_staging');"; // MATVIEW_BEFORE_LOAD_SCRIPT
        String loadScript = ""; //MATVIEW_LOAD_SCRIPT
        String afterLoadScript = "execute accounts.native('ALTER TABLE SampleTable_mat RENAME TO SampleTable_mat_temp');execute accounts.native('ALTER TABLE SampleTable_staging RENAME TO SampleTable_mat');execute accounts.native('ALTER TABLE SampleTable_mat_temp RENAME TO SampleTable_staging');";//MATVIEW_AFTER_LOAD_SCRIPT
        Integer ttl = 60000; //MATVIEW_TTL
        String matViewStageTable = "Accounts.SampleTable_staging";//MATERIALIZED_STAGE_TABLE
        String scope = "NONE";//MATVIEW_SHARE_SCOPE
        
        String matViewTable = "SAMPLETABLE_MAT";
        String targetSchemaName = "Accounts";
        
        String crit_none = "WHERE VDBName = 'TEIID4121H2VDB' AND VDBVersion = '1' AND SchemaName = 'SampleModel' AND Name = 'SAMPLEMATVIEW'";
        String crit_vdb = "WHERE VDBName = 'TEIID4121H2VDB' AND SchemaName = 'SampleModel' AND Name = 'SAMPLEMATVIEW'";
        String crit_schema = "WHERE SchemaName = 'SampleModel' AND Name = 'SAMPLEMATVIEW'";
        
        String updateStmt = "UPDATE status";
        
        String status = "CHECK";
        
        // 3. If status Table is empty, insert initial value to status table
        //     Valid -> false
        //     LoadState -> LOADING
        //     Updated -> now()
        //     Cardinality -> -1
        //     LoadNumber -> 1
        execute(conn, "INSERT INTO status (VDBName, VDBVersion, SchemaName, Name, TargetSchemaName, TargetName, Valid, LoadState, Updated, Cardinality, LoadNumber) values ('TEIID4121H2VDB', '1', 'SampleModel', 'SAMPLEMATVIEW', 'Accounts', 'SAMPLETABLE_MAT', false, 'LOADING', now(), -1, 1)", false);
        
        // 4. Insert View's content to stage table
        execute(conn, "INSERT INTO SampleTable_staging SELECT * FROM SampleModel.SAMPLEMATVIEW OPTION NOCACHE SampleModel.SAMPLEMATVIEW", false);
        
        execute(conn, "SELECT * FROM SampleTable_staging", false);
    }


}
