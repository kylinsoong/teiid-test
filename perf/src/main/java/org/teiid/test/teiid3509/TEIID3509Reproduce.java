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

public class TEIID3509Reproduce {

    static EmbeddedServer server = null;
    static Connection conn = null;
    
    public static void main(String[] args) throws Exception {

        EmbeddedHelper.enableLogger(Level.WARNING);
        
        DataSource ds = EmbeddedHelper.newDataSource(H2_JDBC_DRIVER, H2_JDBC_URL, H2_JDBC_USER, H2_JDBC_PASS);
        RunScript.execute(ds.getConnection(), new InputStreamReader(TEIID3509Reproduce.class.getClassLoader().getResourceAsStream("teiid-3509/h2-schema.sql")));
        
        
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
                
        server.deployVDB(TEIID3509Reproduce.class.getClassLoader().getResourceAsStream("teiid-3509/teiid3509-h2-mat-vdb.xml"));
        
        Properties info = new Properties();
        conn = server.getDriver().connect("jdbc:teiid:MatViewH2VDB", info);
        
        Thread.sleep(3000);
        
        execute(conn, "SELECT * FROM TestExterMat.SAMPLEEXTERMATVIEW", false);
        execute(conn, "SELECT * FROM TestInterMat.SAMPLEMATVIEW", false);
        
//        execute(conn, "SELECT (id, a, b) AS pkArray FROM TestInterMat.SAMPLEMATVIEW WHERE id = '102'", false);
//        execute(conn, "SELECT (id, a, b) AS pkArray FROM TestExterMat.SAMPLEEXTERMATVIEW WHERE id = '102'", false);
        
//        execute(conn, "EXEC TestMatProc.testupdateMatView('TestExterMat', 'SAMPLEEXTERMATVIEW', 'id = ''102''')", false);
        execute(conn, "EXEC TestMatProc.testupdateMatView('TestInterMat', 'SAMPLEMATVIEW', 'id = ''102''')", false);
        
//        status_refresh();
        
        conn.close();
    }

    static void status_refresh() throws Exception {

        // refresh internal mat view
        execute(conn, "EXEC SYSADMIN.loadMatView('TestInterMat', 'SAMPLEMATVIEW', true)", false);
        
        // refresh external mat view
        execute(conn, "EXEC SYSADMIN.loadMatView('TestExterMat', 'SAMPLEEXTERMATVIEW', true)", false);
        
        // query internal Mat view status
        execute(conn, "EXEC SYSADMIN.matViewStatus('TestInterMat', 'SAMPLEMATVIEW')", false);
        
        // query external Mat view status
        execute(conn, "EXEC SYSADMIN.matViewStatus('TestExterMat', 'SAMPLEEXTERMATVIEW')", false);
    }

}
