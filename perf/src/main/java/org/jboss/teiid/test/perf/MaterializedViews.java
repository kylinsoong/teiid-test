package org.jboss.teiid.test.perf;

import static org.jboss.teiid.test.perf.Constants.H2_JDBC_DRIVER;
import static org.jboss.teiid.test.perf.Constants.H2_JDBC_PASS;
import static org.jboss.teiid.test.perf.Constants.H2_JDBC_URL;
import static org.jboss.teiid.test.perf.Constants.H2_JDBC_USER;
import static org.jboss.teiid.test.perf.Util.prompt;
import static org.jboss.teiid.test.perf.Util.dumpResult;

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
import org.teiid.test.util.JDBCUtils;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;

public class MaterializedViews {
    
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
        
        server.deployVDB(ResultsCachingMysql.class.getClassLoader().getResourceAsStream("matView-h2-vdb.xml"));
        
        Properties info = new Properties();
        conn = server.getDriver().connect("jdbc:teiid:MatViewH2VDB", info);
    }
    
    static void teardown() throws SQLException {
        JDBCUtils.close(conn);
        server.stop();
    }
    
    public static void query(String sql) throws Exception {
        
        startup();
        
        long[] array = new long[10];         
        
        prompt(sql);
       
        for(int i = 0 ; i < 10 ; i ++) {
            PerfEntity entity = Util.executeQueryCount(conn, sql);
            array[i] = entity.getQueryTime();
        }
        
        dumpResult(array, sql);
                
        teardown();
    }

    public static void main(String[] args) throws Exception {

        query("SELECT * FROM PERFTESTEXTER_MATVIEW");
        
        query("/*+ cache */ SELECT * FROM PERFTESTINTER_MATVIEW");
        
    }

}
