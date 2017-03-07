package org.teiid.test.teiid4682;

import static org.teiid.test.utils.JDBCUtils.close;
import static org.teiid.test.utils.JDBCUtils.execute;

import java.sql.Connection;
import java.util.logging.Level;

import javax.sql.DataSource;

import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.test.utils.EmbeddedHelper;
import org.teiid.translator.prestodb.PrestoDBExecutionFactory;

public class TEIID4682PrestoLab {

    public static void main(String[] args) throws Exception {

        EmbeddedHelper.enableLogger(Level.ALL);

        DataSource ds = EmbeddedHelper.newDataSource("com.facebook.presto.jdbc.PrestoDriver", "jdbc:presto://dvqe07.mw.lab.eng.bos.redhat.com:8888/mysql/bqt2", "root", "root");
        
        EmbeddedServer server = new EmbeddedServer();
        
        PrestoDBExecutionFactory executionFactory = new PrestoDBExecutionFactory() ;
        executionFactory.setSupportsDirectQueryProcedure(true);
        executionFactory.start();
        server.addTranslator("prestodb", executionFactory);
        
        server.addConnectionFactory("java:/prestoDS", ds);
    
    
        EmbeddedConfiguration config = new EmbeddedConfiguration();
        config.setTransactionManager(EmbeddedHelper.getTransactionManager());   
        server.start(config);
        
        server.deployVDB(TEIID4682PrestoLab.class.getClassLoader().getResourceAsStream("presto-vdb.xml"));
        
        Connection conn = server.getDriver().connect("jdbc:teiid:PrestoDB", null);
  
//        System.out.println(conn.getMetaData().getDatabaseProductName() + " - " + conn.getMetaData().getDatabaseProductVersion());
        
        execute(conn, "INSERT INTO #t SELECT IntKey FROM Bqt1.Smalla");
        
//        execute(conn, "SELECT * FROM #t", false);
        
//        // Implicit create temp table
//        execute(conn, "INSERT INTO #t SELECT IntKey FROM Bqt1.Smalla", false);
//        execute(conn, "INSERT INTO #t VALUES(2)", false);
//        execute(conn, "SELECT * FROM #t", false);
         
        close(conn);
    }

}
