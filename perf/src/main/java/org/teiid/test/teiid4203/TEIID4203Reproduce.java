package org.teiid.test.teiid4203;

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

public class TEIID4203Reproduce {

    static EmbeddedServer server = null;
    static Connection conn = null;

    public static void main(String[] args) throws Exception {

        EmbeddedHelper.enableLogger(Level.INFO);
        
        DataSource ds = EmbeddedHelper.newDataSource(H2_JDBC_DRIVER, H2_JDBC_URL, H2_JDBC_USER, H2_JDBC_PASS);
        RunScript.execute(ds.getConnection(), new InputStreamReader(TEIID4203Reproduce.class.getClassLoader().getResourceAsStream("teiid-4203/h2-schema.sql")));
                
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
                
        server.deployVDB(TEIID4203Reproduce.class.getClassLoader().getResourceAsStream("teiid-4203/teiid4203-vdb.xml"));
        
        Properties info = new Properties();
        conn = server.getDriver().connect("jdbc:teiid:MatViewH2VDB", info);
        
        
//        execute(conn, "EXEC testProc('TestExterMat', 'SAMPLEEXTERMATVIEW')", false);
        execute(conn, "EXEC testProcLoad('TestExterMat', 'SAMPLEEXTERMATVIEW', true)", false);
        
//        execute(conn, "SELECT * FROM status", false);
                
        conn.close();
    }
}
