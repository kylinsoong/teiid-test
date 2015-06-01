package org.teiid.test.perf;

import static org.teiid.test.Constants.H2_JDBC_DRIVER;
import static org.teiid.test.Constants.H2_JDBC_PASS;
import static org.teiid.test.Constants.H2_JDBC_URL;
import static org.teiid.test.Constants.H2_JDBC_USER;
import static org.teiid.test.perf.Util.dumpResult;
import static org.teiid.test.perf.Util.prompt;
import static org.teiid.test.util.JDBCUtils.execute;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

import javax.resource.ResourceException;
import javax.sql.DataSource;

import org.teiid.deployers.VirtualDatabaseException;
import org.teiid.dqp.internal.datamgr.ConnectorManagerRepository.ConnectorManagerException;
import org.teiid.example.EmbeddedHelper;
import org.teiid.query.test.TestHelper;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.test.util.JDBCUtils;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;

public class MaterializedViews {
    
    static EmbeddedServer server = null;
    static Connection conn = null;
    
    static void startup() throws Exception {
        
        TestHelper.enableLogger(Level.INFO);
        
        server = new EmbeddedServer();
        
        H2ExecutionFactory factory = new H2ExecutionFactory();
        factory.start();
        factory.setSupportsDirectQueryProcedure(true);
        server.addTranslator("translator-h2", factory);
        
        DataSource ds = EmbeddedHelper.newDataSource(H2_JDBC_DRIVER, H2_JDBC_URL, H2_JDBC_USER, H2_JDBC_PASS);
        server.addConnectionFactory("java:/accounts-ds", ds);
        
        EmbeddedConfiguration config = new EmbeddedConfiguration();
        config.setTransactionManager(EmbeddedHelper.getTransactionManager());
        server.start(config);
        
        server.deployVDB(ResultsCachingMysql.class.getClassLoader().getResourceAsStream("matView-h2-vdb.xml"));
        
        Properties info = new Properties();
        conn = server.getDriver().connect("jdbc:teiid:MatViewH2VDB", info);
    }
    
    static void teardown() throws SQLException {
        JDBCUtils.close(conn);
        server.stop();
    }
    
    public static void externalMaterialization() throws Exception {
        
        startup();
        
        Thread.currentThread().sleep(1000 * 30);
        
        long[] array = new long[10];  
        String sql = "SELECT * FROM PERFTESTEXTER_MATVIEW";
        
        prompt(sql);
       
        for(int i = 0 ; i < 10 ; i ++) {
            PerfEntity entity = Util.executeQueryCount(conn, sql);
            array[i] = entity.getQueryTime();
        }
        
        JDBCUtils.executeQuery(conn, "SELECT COUNT(*) AS total_rows FROM PERFTESTEXTER_MATVIEW");
        
        dumpResult(array, sql);
                
        teardown();
    }
    
    public static void internalMaterialization() throws Exception {
        
        startup();
        
        
        
        long[] array = new long[10];    
        String sql = "/*+ cache */ SELECT * FROM PERFTESTINTER_MATVIEW";
        
        
        prompt(sql);
       
        for(int i = 0 ; i < 10 ; i ++) {
            PerfEntity entity = Util.executeQueryCount(conn, sql);
            array[i] = entity.getQueryTime();
        }
        
        JDBCUtils.executeQuery(conn, "SELECT COUNT(*) AS total_rows FROM PERFTESTINTER_MATVIEW");
        
        dumpResult(array, sql);
                
        teardown();
    }

    public static void main(String[] args) throws Exception {
        
        startTimer();
        
        startup();
        
        execute(conn, "execute SYSADMIN.loadMatView('TestMat','PERFTESTEXTERMATVIEW')", false);
        
        execute(conn, "select count(*) from PERFTESTEXTERMATVIEW", false);
        
        teardown();
        

//        externalMaterialization();
        
//        internalMaterialization();
        
    }
    
    private static void startTimer() throws ResourceException, SQLException {

        DataSource ds = EmbeddedHelper.newDataSource(H2_JDBC_DRIVER, H2_JDBC_URL, H2_JDBC_USER, H2_JDBC_PASS);
        Timer time = new Timer("Query", true);
        TimerTask task = new QueryJob(ds.getConnection());   
        time.schedule(task, 2000, 5000);
    }
    
    private static class QueryJob extends TimerTask {
        
        Connection conn;
        
        QueryJob(Connection conn) {
            this.conn = conn;
        }

        @Override
        public void run() {
            try {
                execute(conn, "select count(*) from PERFTEST", false);
                execute(conn, "select count(*) from PERFTEST_MAT", false);
                execute(conn, "select count(*) from PERFTEST_STAGING", false);
                execute(conn, "select * from status", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
