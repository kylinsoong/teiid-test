package org.teiid.test.teiid3999;

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

public class TEIID3999internalMat {

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
        RunScript.execute(ds.getConnection(), new InputStreamReader(TEIID3999internalMat.class.getClassLoader().getResourceAsStream("teiid-3999/h2-schema.sql")));
        
        
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
                
        server.deployVDB(TEIID3999internalMat.class.getClassLoader().getResourceAsStream("teiid-3999/teiid3999-h2-inter-mat-vdb.xml"));
        
        Properties info = new Properties();
        conn = server.getDriver().connect("jdbc:teiid:InternalMatViewH2VDB", info);
        
        Thread.sleep(3000);

        execute(conn, SQL_MAT_QUERY, false);
        
        conn.close();
    }

}
