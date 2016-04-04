package org.jboss.teiid.test.rsCaching;

import static org.teiid.test.Constants.H2_JDBC_DRIVER;
import static org.teiid.test.Constants.H2_JDBC_PASS;
import static org.teiid.test.Constants.H2_JDBC_URL;
import static org.teiid.test.Constants.H2_JDBC_USER;
import static org.teiid.test.Constants.MB;
import static org.teiid.test.util.Util.promptSQL;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import javax.resource.ResourceException;
import javax.sql.DataSource;

import org.teiid.deployers.VirtualDatabaseException;
import org.teiid.dqp.internal.datamgr.ConnectorManagerRepository.ConnectorManagerException;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.test.PerfEntity;
import org.teiid.test.perf.H2PERFTESTClient;
import org.teiid.test.util.EmbeddedHelper;
import org.teiid.test.util.JDBCUtils;
import org.teiid.test.util.TableRenderer;
import org.teiid.test.util.Util;
import org.teiid.test.util.TableRenderer.Column;
import org.teiid.test.util.TableRenderer.ColumnMetaData;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;

public class ResultsCaching {
    
    static EmbeddedServer server = null;
    static Connection conn = null;
    
    static void startup() throws TranslatorException, VirtualDatabaseException, ConnectorManagerException, IOException, SQLException, ResourceException {
        
        EmbeddedHelper.enableLogger(Level.INFO);
        
        server = new EmbeddedServer();
        
        H2ExecutionFactory factory = new H2ExecutionFactory();
        factory.start();
        factory.setSupportsDirectQueryProcedure(true);
        server.addTranslator("translator-h2", factory);
        
        DataSource ds = EmbeddedHelper.newDataSource(H2_JDBC_DRIVER, H2_JDBC_URL, H2_JDBC_USER, H2_JDBC_PASS);
        server.addConnectionFactory("java:/accounts-ds", ds);
        
        server.start(new EmbeddedConfiguration());
        
        server.deployVDB(ResultsCaching.class.getClassLoader().getResourceAsStream("rs/rsCaching-h2-vdb.xml"));
        
        Properties info = new Properties();
//      info.setProperty("ResultSetCacheMode", "true");
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
        
        H2PERFTESTClient.insert(MB);
        
        String sql = "SELECT * FROM PERFTESTVIEW";
        String sqlPerf = "/*+ cache */ SELECT * FROM PERFTESTVIEW";
        String sqlDirect = "SELECT * FROM PERFTEST";
        
        long[] array1 = query(sqlDirect);
        long[] array2 = query(sql);
        long[] array3 = query(sqlPerf);
        
        dumpResult(array1, array2, array3, sqlDirect, sql, sqlPerf);
    }
    
    static void dumpResult(long[] array1, long[] array2, long[] array3,  String sqlDirect, String sql, String sqlPerf) {
        
        Util.prompt("Dump Comparison Results");
        
        TableRenderer render = new TableRenderer(ColumnMetaData.Factory.create(sqlDirect, sql, sqlPerf));
        for(int i = 0 ; i < 10 ; i ++) {
            render.addRow(Column.Factory.create(array1[i], array2[i], array3[i]));
        }
        
        render.renderer();
        
        Util.print("\n");
    }

}
