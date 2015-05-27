package org.jboss.teiid.test.perf;

import static org.jboss.teiid.test.perf.Util.dumpResult;
import static org.jboss.teiid.test.perf.Util.prompt;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

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
import org.teiid.translator.jdbc.mysql.MySQL5ExecutionFactory;

public class ExternalMaterializationMysql {
    
    static EmbeddedServer server = null;
    static Connection conn = null;
    
    static void startup() throws TranslatorException, VirtualDatabaseException, ConnectorManagerException, IOException, SQLException, ResourceException {
        
        server = new EmbeddedServer();
        
        MySQL5ExecutionFactory factory = new MySQL5ExecutionFactory();
        factory.start();
        factory.setSupportsDirectQueryProcedure(true);
        server.addTranslator("translator-mysql", factory);
        
        DataSource ds = EmbeddedHelper.newDataSource("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/test", "jdv_user", "jdv_pass");
        server.addConnectionFactory("java:/accounts-ds", ds);
        
        EmbeddedConfiguration config = new EmbeddedConfiguration();
        config.setUseDisk(true);
        config.setBufferDirectory("/home/kylin/tmp/buffer");
        server.start(config);
        
        server.deployVDB(ResultsCachingMysql.class.getClassLoader().getResourceAsStream("extMatView-mysql-vdb.xml"));
        
        Properties info = new Properties();
        conn = server.getDriver().connect("jdbc:teiid:MatViewMySQLVDB", info);
    }
    
    static void teardown() throws SQLException {
        JDBCUtils.close(conn);
        server.stop();
    }
    
    public static void externalMaterialization() throws Exception {
        
        startup();
        
        long[] array = new long[10];  
        String sql = "SELECT * FROM PERFTESTEXTER_MATVIEW";
        
        prompt(sql);
       
        for(int i = 0 ; i < 10 ; i ++) {
            PerfEntity entity = Util.executeQueryCount(conn, sql);
            array[i] = entity.getQueryTime();
        }
        
        JDBCUtils.executeQuery(conn, "SELECT COUNT(*) FROM PERFTESTEXTER_MATVIEW");
        
        dumpResult(array, sql);
                
        teardown();
    }

    public static void main(String[] args) throws Exception {
        
//        externalMaterialization();
        
        debug();
    }

    static void debug() throws VirtualDatabaseException, TranslatorException, ConnectorManagerException, IOException, SQLException, ResourceException {
        
        TestHelper.enableLogger();
        
        startup();
        
    }

}
