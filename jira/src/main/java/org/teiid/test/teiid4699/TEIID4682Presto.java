package org.teiid.test.teiid4699;

import static org.teiid.test.utils.JDBCUtils.close;

import java.sql.Connection;
import java.util.logging.Level;

import javax.sql.DataSource;

import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.test.utils.EmbeddedHelper;
import org.teiid.translator.prestodb.PrestoDBExecutionFactory;

public class TEIID4682Presto {

    public static void main(String[] args) throws Exception {

        EmbeddedHelper.enableLogger(Level.OFF);

        DataSource ds = EmbeddedHelper.newDataSource("com.facebook.presto.jdbc.PrestoDriver", "jdbc:presto://localhost:8080", "sa", "sa");
        
        EmbeddedServer server = new EmbeddedServer();
        
        PrestoDBExecutionFactory executionFactory = new PrestoDBExecutionFactory() ;
        executionFactory.setSupportsDirectQueryProcedure(true);
        executionFactory.start();
        server.addTranslator("translator-presto", executionFactory);
        
        server.addConnectionFactory("java:/accounts-ds", ds);
    
    
        EmbeddedConfiguration config = new EmbeddedConfiguration();
        config.setTransactionManager(EmbeddedHelper.getTransactionManager());   
        server.start(config);
        
        server.deployVDB(TEIID4682.class.getClassLoader().getResourceAsStream("teiid4682-vdb.xml"));
        
        Connection conn = server.getDriver().connect("jdbc:teiid:Portfolio", null);
  
        System.out.println(conn.getMetaData().getDatabaseProductName() + " - " + conn.getMetaData().getDatabaseProductVersion());
        
        
//        execute(conn, "SELECT 'hello world'", false);
        
//        // Implicit create temp table
//        execute(conn, "INSERT INTO #t SELECT IntKey FROM Bqt1.Smalla", false);
//        execute(conn, "INSERT INTO #t VALUES(2)", false);
//        execute(conn, "SELECT * FROM #t", false);
         
        close(conn);
    }

}
