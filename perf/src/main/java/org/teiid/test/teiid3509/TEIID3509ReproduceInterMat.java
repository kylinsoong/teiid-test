package org.teiid.test.teiid3509;

import static org.teiid.test.Constants.H2_JDBC_DRIVER;
import static org.teiid.test.Constants.H2_JDBC_PASS;
import static org.teiid.test.Constants.H2_JDBC_URL;
import static org.teiid.test.Constants.H2_JDBC_USER;
import static org.teiid.test.util.JDBCUtils.execute;
import static org.teiid.test.util.JDBCUtils.executeQuery;

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

public class TEIID3509ReproduceInterMat {
    
    static EmbeddedServer server = null;
    static Connection conn = null;
    
    static final String SQL_MAT_QUERY = "SELECT * FROM SAMPLEMATVIEW";
    
    static final String SQL_MAT_STATUS = "EXEC SYSADMIN.matViewStatus('TestInterMat', 'SAMPLEMATVIEW')";
    
    static final String SQL_MAT_REFRESH = "EXEC SYSADMIN.loadMatView('TestInterMat', 'SAMPLEMATVIEW', true)";
    
    static final String SQL_MAT_refreshMatView = "EXEC SYSADMIN.refreshMatView('TestInterMat.SAMPLEMATVIEW', true)";
    
    static final String SQL_SampleTable_UPDATE_100 = "UPDATE SampleTable SET c = '" + timetoken() + "' WHERE id = '100'";
    static final String SQL_SampleTable_UPDATE_101 = "UPDATE SampleTable SET c = '" + timetoken() + "' WHERE id = '101'";
    static final String SQL_SampleTable_UPDATE_102 = "UPDATE SampleTable SET c = '" + timetoken() + "' WHERE id = '102'";
    
    static final String SQL_MAT_refreshMatViewRow = "EXEC SYSADMIN.refreshMatViewRow('TestInterMat.SAMPLEMATVIEW', '100')";
    static final String SQL_MAT_refreshMatViewRow_morerows = "EXEC SYSADMIN.refreshMatViewRows('TestInterMat.SAMPLEMATVIEW', ('100',), ('101',), ('102',))";
    
    
    static final String SQL_MAT_refreshMatViewRows = "EXEC SYSADMIN.refreshMatViewRows('TestInterMat.SAMPLEMATVIEW', ('101', 'a1'))";
    static final String SQL_MAT_refreshMatViewRows_morerows = "EXEC SYSADMIN.refreshMatViewRows('TestInterMat.SAMPLEMATVIEW', ('100', 'a0'), ('101', 'a1'), ('102', 'a2'))";
    
    static String timetoken() {
        return "" + System.currentTimeMillis();
    }
    
    public static void main(String[] args) throws Exception {

        EmbeddedHelper.enableLogger(Level.WARNING);
        
        DataSource ds = EmbeddedHelper.newDataSource(H2_JDBC_DRIVER, H2_JDBC_URL, H2_JDBC_USER, H2_JDBC_PASS);
        RunScript.execute(ds.getConnection(), new InputStreamReader(TEIID3509ReproduceInterMat.class.getClassLoader().getResourceAsStream("teiid-3509/h2-schema.sql")));
        
        
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
                
        server.deployVDB(TEIID3509Reproduce.class.getClassLoader().getResourceAsStream("teiid-3509/teiid3509-h2-inter-mat-vdb.xml"));
        
        Properties info = new Properties();
        conn = server.getDriver().connect("jdbc:teiid:InternalMatViewH2VDB", info);
        
//        Thread.sleep(3000);

//        execute(conn, SQL_MAT_STATUS, false);
//        
//        execute(conn, SQL_MAT_REFRESH, false);
        
//        execute(conn, SQL_MAT_refreshMatView, false);
        
//        execute(conn, SQL_MAT_QUERY, false);
        
        
        
        
        
//        pk_one_update_one_row();
        
//        pk_one_update_more_rows();
        
//        pk_more_update_one_row();     
//        pk_more_update_more_rows();
        
//        systemTablesQuery();
        
//        execute(conn, "SELECT (id,) FROM TestInterMat.SAMPLEMATVIEW WHERE id = '102'", false);
//        execute(conn, "SELECT (id, a, b) FROM TestInterMat.SAMPLEMATVIEW WHERE id = '102'", false);
        
//        execute(conn, "EXEC testupdateMatView('TestInterMat', 'SAMPLEMATVIEW', '(''100'', ''a0'', ''b0''),(''101'', ''a1'', ''b1''),(''102'', ''a2'', ''b2'')')", false);
        
//        execute(conn, "EXEC testStringFunction('Hello', 'World', 'REDHAT')", false);
        
//        execute(conn, SQL_SampleTable_UPDATE_102, false);
        executeQuery(conn, "select * from (call SYSADMIN.updateMatView('TestInterMat', 'SAMPLEMATVIEW', 'id = ''102''')) p");
//        executeQuery(conn, "call testupdateMatView('TestInterMat', 'SAMPLEMATVIEW', 'id = ''102''' OR 'id = ''101''')");
//        execute(conn, "call testupdateMatView('TestInterMat', 'SAMPLEMATVIEW', 'id = ''102''')", false);
        
//        execute(conn, "SELECT * FROM v1", false);
//        
        conn.close();
    }

    static void pk_one_update_more_rows() throws Exception {

        execute(conn, SQL_SampleTable_UPDATE_100, false);
        execute(conn, SQL_SampleTable_UPDATE_101, false);
        execute(conn, SQL_SampleTable_UPDATE_102, false);
        execute(conn, SQL_MAT_QUERY, false);
        execute(conn, SQL_MAT_refreshMatViewRow_morerows, false);
        execute(conn, SQL_MAT_QUERY, false);
    }

    static void pk_one_update_one_row() throws Exception {

        execute(conn, SQL_SampleTable_UPDATE_100, false);
        execute(conn, SQL_MAT_QUERY, false);
        execute(conn, SQL_MAT_refreshMatViewRow, false);
        execute(conn, SQL_MAT_QUERY, false);
    }

    static void pk_more_update_more_rows() throws Exception {
        
        execute(conn, SQL_SampleTable_UPDATE_100, false);
        execute(conn, SQL_SampleTable_UPDATE_101, false);
        execute(conn, SQL_SampleTable_UPDATE_102, false);
        execute(conn, SQL_MAT_QUERY, false);
        execute(conn, SQL_MAT_refreshMatViewRows_morerows, false);
        execute(conn, SQL_MAT_QUERY, false);
    }

    static void pk_more_update_one_row() throws Exception {

        execute(conn, SQL_SampleTable_UPDATE_101, false);
        execute(conn, SQL_MAT_QUERY, false);
        execute(conn, SQL_MAT_refreshMatViewRows, false);
        execute(conn, SQL_MAT_QUERY, false);
    }

    static void systemTablesQuery() throws Exception {
//        execute(conn, "SELECT * FROM SYS.Tables WHERE SchemaName = 'TestInterMat' AND Name = 'SAMPLEMATVIEW'", false);
//        execute(conn, "SELECT UID FROM SYS.Keys WHERE SchemaName = 'SYSADMIN' AND TableName = 'MatViews' AND Type = 'Primary'", false);
//        execute(conn, "SELECT Name FROM SYS.KeyColumns WHERE SchemaName = 'SYSADMIN' AND TableName = 'MatViews' AND UID = 'tid:60b87e792634-d67a9241-00000018'", false);
        
        execute(conn, "SELECT UID FROM SYS.Keys WHERE SchemaName = 'TestInterMat' AND TableName = 'SAMPLEMATVIEW' AND Type = 'Primary'", false);
        execute(conn, "SELECT Name FROM SYS.KeyColumns WHERE SchemaName = 'TestInterMat' AND TableName = 'SAMPLEMATVIEW' AND UID = 'tid:dad9e9814ad5-d6e7d3a6-00000006'", false);
    }
    

}
