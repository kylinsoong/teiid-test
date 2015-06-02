package org.teiid.test.perf;

import static org.teiid.test.Constants.H2_JDBC_DRIVER;
import static org.teiid.test.Constants.H2_JDBC_PASS;
import static org.teiid.test.Constants.H2_JDBC_URL;
import static org.teiid.test.Constants.H2_JDBC_USER;
import static org.teiid.test.Util.dumpResult;
import static org.teiid.test.Util.promptSQL;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import javax.resource.ResourceException;
import javax.sql.DataSource;

import org.teiid.deployers.VirtualDatabaseException;
import org.teiid.dqp.internal.datamgr.ConnectorManagerRepository.ConnectorManagerException;
import org.teiid.example.EmbeddedHelper;
import org.teiid.query.test.TestHelper;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.test.PerfEntity;
import org.teiid.test.Util;
import org.teiid.test.util.JDBCUtils;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;

public class ResultsCachingDebug {
    
    static EmbeddedServer server = null;
    static Connection conn = null;
    
    static void startup() throws TranslatorException, VirtualDatabaseException, ConnectorManagerException, IOException, SQLException, ResourceException {
        
        TestHelper.enableLogger(Level.INFO);
        
        server = new EmbeddedServer();
        
        H2ExecutionFactory factory = new H2ExecutionFactory();
        factory.start();
        factory.setSupportsDirectQueryProcedure(true);
        server.addTranslator("translator-h2", factory);
        
        DataSource ds = EmbeddedHelper.newDataSource(H2_JDBC_DRIVER, H2_JDBC_URL, H2_JDBC_USER, H2_JDBC_PASS);
        server.addConnectionFactory("java:/accounts-ds", ds);
        
        server.start(new EmbeddedConfiguration());
        
        server.deployVDB(ResultsCachingMysql.class.getClassLoader().getResourceAsStream("resultsCaching-h2-vdb.xml"));
        
        Properties info = new Properties();
//      info.setProperty("ResultSetCacheMode", "true");
        conn = server.getDriver().connect("jdbc:teiid:ResultsCachingH2VDB", info);
    }
    
    static void teardown() throws SQLException {
        JDBCUtils.close(conn);
        server.stop();
    }
    
    static long[] array = new long[10]; 
    static long[] arrayPerf = new long[10];
    
    static String sql = "SELECT * FROM PERFTESTVIEW";
    static String sqlPerf = "/*+ cache */ SELECT * FROM PERFTESTVIEW";
    
    public static void query() throws Exception {
        
        startup();
        
        promptSQL(sql);
       
        for(int i = 0 ; i < 10 ; i ++) {
            PerfEntity entity = Util.executeQueryCount(conn, sql);
            array[i] = entity.getQueryTime();
        }
                
        teardown();
    }
    
    public static void perfQuery() throws Exception {
        
        startup();
        
        promptSQL(sqlPerf);
       
        for(int i = 0 ; i < 10 ; i ++) {
            PerfEntity entity = Util.executeQueryCount(conn, sqlPerf);
            arrayPerf[i] = entity.getQueryTime();
        }
                
        teardown();
    }
    
    static long[] arrayProc1 = new long[10]; 
    static long[] arrayProc2 = new long[10];
    
    static String sqlProc1 = "call PERFTPROCE1()";
    static String sqlProc2 = "call PERFTPROCE2()";
    
    public static void callProcedure() throws VirtualDatabaseException, TranslatorException, ConnectorManagerException, IOException, SQLException, ResourceException {

        startup();
        
        promptSQL(sqlProc1);
        
        for(int i = 0 ; i < 10 ; i ++) {
            PerfEntity entity = Util.executeProcedureCount(conn, sqlProc1);
            arrayProc1[i] = entity.getQueryTime();
        }
                
        teardown();
    }
    
    public static void callProcedurePerf() throws VirtualDatabaseException, TranslatorException, ConnectorManagerException, IOException, SQLException, ResourceException {

        startup();
        
        promptSQL(sqlProc2);
        
        for(int i = 0 ; i < 10 ; i ++) {
            PerfEntity entity = Util.executeProcedureCount(conn, sqlProc2);
            arrayProc2[i] = entity.getQueryTime();
        }
                
        teardown();
    }

    public static void main(String[] args) throws Exception {
        
        query();
        perfQuery();
        dumpResult(array, arrayPerf, sql, sqlPerf);
        
        callProcedure();
        callProcedurePerf();
        dumpResult(arrayProc1, arrayProc2, sqlProc1, sqlProc2);
    }

}
