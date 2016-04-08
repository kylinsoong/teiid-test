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

public class TEIID3952Reproduce {

    static EmbeddedServer server = null;
    static Connection conn = null;
    
    public static void main(String[] args) throws Exception {

        EmbeddedHelper.enableLogger(Level.OFF);
        
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
        server.start(config);
                
        server.deployVDB(TEIID3952Reproduce.class.getClassLoader().getResourceAsStream("teiid-3952/teiid3952-h2-vdb.xml"));
        
        Properties info = new Properties();
        conn = server.getDriver().connect("jdbc:teiid:MatViewH2VDB", info);
        
//        reproduce_1(ds.getConnection());
        
        reproduce_2(ds.getConnection());
        
        conn.close();
    }

    /**
     * 
     * 1) select a row from the materialized table
     * 2) update a field in a row in the materialized view
     * 3) issue EXEC SYSADMIN.refreshMatViewRow(...) using primary key for the row that has been changed
     * 4) select that row (value unchanged) - now I can see the changed data
     * 
     */
    static void reproduce_2(Connection dsConn) throws Exception {

        execute(dsConn, "SELECT * FROM SampleTable WHERE id = '100'", false);
        
        execute(conn, "SELECT * FROM SAMPLEMATVIEW WHERE id = '100'", false);
        
        execute(conn, "UPDATE SAMPLEMATVIEW SET a = 'aaa' WHERE id = '100'", false);
        
        execute(conn, "EXEC SYSADMIN.refreshMatViewRow('TestMat.SAMPLEMATVIEW', '100')", false);
        
        execute(dsConn, "SELECT * FROM SampleTable WHERE id = '100'", false);
        
        execute(conn, "SELECT * FROM SAMPLEMATVIEW WHERE id = '100'", false);
    }

    /**
     * 1) select a row from the materialized table
     * 2) update a field in a row in the materialized view
     * 3) select that row (value unchanged ie, same as int 1) - tried multiple times
     * 
     */
    static void reproduce_1(Connection dsConn) throws Exception {
        
        execute(dsConn, "SELECT * FROM SampleTable WHERE id = '100'", false);

        execute(conn, "SELECT * FROM SAMPLEMATVIEW WHERE id = '100'", false);
        
        execute(conn, "UPDATE SAMPLEMATVIEW SET a = 'aaa' WHERE id = '100'", false);
        
        for(int i = 0 ; i < 10 ; i++) {
            
            System.out.println("\n The " + (i + 1)  +" times query:\n");
            
            Thread.sleep(5000);
            
            execute(conn, "SELECT * FROM SAMPLEMATVIEW WHERE id = '100'", false);
            
            execute(dsConn, "SELECT * FROM SampleTable WHERE id = '100'", false);
        }
    }

}
