package org.teiid.test.perf;

import static org.teiid.test.Constants.H2_JDBC_DRIVER;
import static org.teiid.test.Constants.H2_JDBC_PASS;
import static org.teiid.test.Constants.H2_JDBC_URL;
import static org.teiid.test.Constants.H2_JDBC_USER;
import static org.teiid.test.Util.dumpResult;
import static org.teiid.test.Util.promptSQL;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import javax.sql.DataSource;

import org.teiid.example.EmbeddedHelper;
import org.teiid.query.test.TestHelper;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.test.PerfEntity;
import org.teiid.test.Util;
import org.teiid.test.util.JDBCUtils;
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
    
    @SuppressWarnings("static-access")
    public static void externalMaterialization() throws Exception {
        
        startup();
        
        Thread.currentThread().sleep(1000 * 10); // wait for loadMatView finish execute
        
        long[] array = new long[10];  
        String sql = "SELECT * FROM PERFTESTEXTERMATVIEW";
        
        promptSQL(sql);
       
        for(int i = 0 ; i < 10 ; i ++) {
            PerfEntity entity = Util.executeQueryCount(conn, sql);
            array[i] = entity.getQueryTime();
        }
                
        dumpResult(array, sql);
                
        teardown();
    }
    
    public static void internalMaterialization() throws Exception {
        
        startup();
        
     
        long[] array = new long[10];    
        String sql = "SELECT * FROM PERFTESTINTERMATVIEW";
        
        
        promptSQL(sql);
       
        for(int i = 0 ; i < 10 ; i ++) {
            PerfEntity entity = Util.executeQueryCount(conn, sql);
            array[i] = entity.getQueryTime();
        }
                
        dumpResult(array, sql);
                
        teardown();
    }

    public static void main(String[] args) throws Exception {
        
        externalMaterialization();
        
        internalMaterialization();
        
    }
    
   
}
