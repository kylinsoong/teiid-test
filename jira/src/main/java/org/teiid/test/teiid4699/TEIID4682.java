package org.teiid.test.teiid4699;

import static org.teiid.test.utils.JDBCUtils.close;
import static org.teiid.test.utils.JDBCUtils.execute;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.logging.Level;

import javax.sql.DataSource;

import org.h2.tools.RunScript;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.test.utils.EmbeddedHelper;
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;

public class TEIID4682 {

    public static void main(String[] args) throws Exception {
        
        EmbeddedHelper.enableLogger(Level.OFF);

        DataSource ds = EmbeddedHelper.newDataSource("org.h2.Driver", "jdbc:h2:mem://localhost/~/account", "sa", "sa");
        RunScript.execute(ds.getConnection(), new InputStreamReader(TEIID4686.class.getClassLoader().getResourceAsStream("data/teiid4486.sql")));
        
        EmbeddedServer server = new EmbeddedServer();
        
        H2ExecutionFactory executionFactory = new H2ExecutionFactory() ;
        executionFactory.setSupportsDirectQueryProcedure(true);
        executionFactory.start();
        server.addTranslator("translator-h2", executionFactory);
        
        server.addConnectionFactory("java:/accounts-ds", ds);
    
    
        EmbeddedConfiguration config = new EmbeddedConfiguration();
        config.setTransactionManager(EmbeddedHelper.getTransactionManager());   
        server.start(config);
        
        server.deployVDB(TEIID4682.class.getClassLoader().getResourceAsStream("teiid4482-vdb.xml"));
        
        Connection conn = server.getDriver().connect("jdbc:teiid:Portfolio", null);
  
        execute(conn, "SELECT IntKey FROM Bqt1.Smalla", false);
        
        // Implicit create temp table
        execute(conn, "INSERT INTO #t SELECT IntKey FROM Bqt1.Smalla", false);
        execute(conn, "INSERT INTO #t VALUES(2)", false);
        execute(conn, "SELECT * FROM #t", false);
        
        // Explicit Implicit create temp table
        execute(conn, "CREATE LOCAL TEMPORARY TABLE TEMP (a integer)", false);
        execute(conn, "SELECT IntKey INTO TEMP FROM Bqt1.Smalla", false);
        execute(conn, "INSERT INTO TEMP VALUES(2)", false);
        execute(conn, "SELECT * FROM TEMP", false);
        
        execute(conn, "INSERT INTO GTEMP (name) VALUES ('teiid')", false);
        execute(conn, "INSERT INTO GTEMP (name) VALUES ('jboss')", false);
        execute(conn, "SELECT * FROM GTEMP", false);
        execute(conn, "UPDATE GTEMP SET name = 'teiid to jdv' WHERE id =1", false);
        execute(conn, "UPDATE GTEMP SET name = 'jboss by redhat' WHERE id =2", false);
        execute(conn, "SELECT * FROM GTEMP", false);
        
        close(conn);

    }

}
