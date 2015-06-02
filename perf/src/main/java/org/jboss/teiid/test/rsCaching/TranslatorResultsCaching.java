package org.jboss.teiid.test.rsCaching;

import static org.teiid.test.Constants.H2_JDBC_DRIVER;
import static org.teiid.test.Constants.H2_JDBC_PASS;
import static org.teiid.test.Constants.H2_JDBC_URL;
import static org.teiid.test.Constants.H2_JDBC_USER;
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
import org.teiid.language.Command;
import org.teiid.language.QueryExpression;
import org.teiid.metadata.RuntimeMetadata;
import org.teiid.query.test.TestHelper;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.test.PerfEntity;
import org.teiid.test.Util;
import org.teiid.test.util.JDBCUtils;
import org.teiid.test.util.TableRenderer;
import org.teiid.test.util.TableRenderer.Column;
import org.teiid.test.util.TableRenderer.ColumnMetaData;
import org.teiid.translator.CacheDirective;
import org.teiid.translator.ExecutionContext;
import org.teiid.translator.ResultSetExecution;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;

public class TranslatorResultsCaching {
    
    static EmbeddedServer server = null;
    static Connection conn = null;
    
    static class TestH2ExecutionFactory extends H2ExecutionFactory {

        @Override
        public ResultSetExecution createResultSetExecution(
                QueryExpression command, ExecutionContext executionContext,
                RuntimeMetadata metadata, Connection conn)
                throws TranslatorException {

            CacheDirective cacheDirective = this.getCacheDirective(command, executionContext, metadata);
            cacheDirective.setScope(CacheDirective.Scope.VDB);
            cacheDirective.setPrefersMemory(true);
            cacheDirective.setReadAll(true);
            cacheDirective.setTtl(120000L);
            cacheDirective.setUpdatable(true);
            
            return super.createResultSetExecution(command, executionContext, metadata, conn);
        }

        @Override
        public CacheDirective getCacheDirective(Command command,
                ExecutionContext executionContext, RuntimeMetadata metadata)
                throws TranslatorException {
            
            return new CacheDirective();
        }
        
    }
    
    static void startup() throws TranslatorException, VirtualDatabaseException, ConnectorManagerException, IOException, SQLException, ResourceException {
        
        TestHelper.enableLogger(Level.INFO);
        
        server = new EmbeddedServer();
        
        H2ExecutionFactory factory = new TestH2ExecutionFactory();
        factory.start();
        factory.setSupportsDirectQueryProcedure(true);
//        factory.g
        server.addTranslator("translator-h2", factory);
        
        DataSource ds = EmbeddedHelper.newDataSource(H2_JDBC_DRIVER, H2_JDBC_URL, H2_JDBC_USER, H2_JDBC_PASS);
        server.addConnectionFactory("java:/accounts-ds", ds);
        
        server.start(new EmbeddedConfiguration());
        
        server.deployVDB(ResultsCaching.class.getClassLoader().getResourceAsStream("rs/rsCaching-h2-vdb.xml"));
        
        Properties info = new Properties();

        conn = server.getDriver().connect("jdbc:teiid:ResultsCachingH2VDB", info);
    }
    
    static void teardown() throws SQLException {
        JDBCUtils.close(conn);
        server.stop();
    }
    
   
    public static long[] query(String sql) throws Exception {
        
        startup();
        
        promptSQL(sql);
       
        long[] array = new long[10]; 
        for(int i = 0 ; i < 10 ; i ++) {
            PerfEntity entity = Util.executeQueryCount(conn, sql);
            array[i] = entity.getQueryTime();
        }
                
        teardown();
        
        return array;
    }

    public static void main(String[] args) throws Exception {

        String sql = "SELECT * FROM PERFTESTVIEW";
        long[] array = query(sql);
        dumpResult(array, sql);
    }
    
    static void dumpResult(long[] array, String sql) {
        
        Util.prompt("Dump Results");
        
        TableRenderer render = new TableRenderer(ColumnMetaData.Factory.create( sql));
        for(int i = 0 ; i < 10 ; i ++) {
            render.addRow(Column.Factory.create(array[i]));
        }
        
        render.renderer();
        
        Util.print("\n");
    }

}
