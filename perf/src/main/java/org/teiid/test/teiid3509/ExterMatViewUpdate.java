package org.teiid.test.teiid3509;

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

public class ExterMatViewUpdate {

    static EmbeddedServer server = null;
    static Connection conn = null;
    
    static final String SQL_MAT_QUERY = "SELECT * FROM SAMPLEEXTERMATVIEW";
    
    static final String SQL_MAT_UPDATE = "UPDATE SAMPLEEXTERMATVIEW SET c = '" + timetoken() + "' WHERE id = '102'"; 
    
    static final String SQL_MAT_QUERY_SOURCE = "SELECT * FROM SampleTable";
    
    static final String SQL_MAT_updateMatView = "EXEC SYSADMIN.updateMatView('TestExterMat', 'SAMPLEEXTERMATVIEW', 'id = ''102''')";
    
    static String timetoken() {
        return "" + System.nanoTime();
    }
    
    public static void main(String[] args) throws Exception {

        EmbeddedHelper.enableLogger(Level.WARNING);
        
        DataSource ds = EmbeddedHelper.newDataSource(H2_JDBC_DRIVER, H2_JDBC_URL, H2_JDBC_USER, H2_JDBC_PASS);
        RunScript.execute(ds.getConnection(), new InputStreamReader(ExterMatViewUpdate.class.getClassLoader().getResourceAsStream("teiid-3509/h2-schema.sql")));
        
        
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
                
        server.deployVDB(TEIID3509ReproduceExterMatView.class.getClassLoader().getResourceAsStream("teiid-3509/teiid3509-h2-exter-mat-vdb.xml"));
        
        Properties info = new Properties();
        conn = server.getDriver().connect("jdbc:teiid:ExternalMatViewH2VDB", info);
        
        Thread.sleep(3000);

        execute(conn, SQL_MAT_QUERY, false);
        
        execute(conn, SQL_MAT_UPDATE, false);
        
        execute(conn, SQL_MAT_QUERY_SOURCE, false);
        
        execute(conn, SQL_MAT_updateMatView, false);
        
        execute(conn, SQL_MAT_QUERY, false);
        
        execute(conn, "SELECT * FROM SYSADMIN.MatViews", false);
        execute(conn, "SELECT * FROM Accounts.SAMPLETABLE_MAT", false);
        
        conn.close();
    }

}
