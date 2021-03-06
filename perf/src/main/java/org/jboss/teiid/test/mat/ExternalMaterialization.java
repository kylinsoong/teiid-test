package org.jboss.teiid.test.mat;

import static org.teiid.test.Constants.H2_JDBC_DRIVER;
import static org.teiid.test.Constants.H2_JDBC_PASS;
import static org.teiid.test.Constants.H2_JDBC_URL;
import static org.teiid.test.Constants.H2_JDBC_USER;
import static org.teiid.test.util.JDBCUtils.execute;
import static org.teiid.test.util.Util.sleep;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import javax.sql.DataSource;

import org.h2.tools.RunScript;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.test.perf.ResultsCachingMysql;
import org.teiid.test.util.EmbeddedHelper;
import org.teiid.test.util.JDBCUtils;
import org.teiid.test.util.Util;
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;

public class ExternalMaterialization {
    
    static EmbeddedServer server = null;
    static Connection conn = null;
    
    static final String MSG = "wait for loadMatView() finish refresh external materialized table";
    
    static void startup() throws Exception {
        
        EmbeddedHelper.enableLogger(Level.INFO);
        
        DataSource ds = EmbeddedHelper.newDataSource(H2_JDBC_DRIVER, H2_JDBC_URL, H2_JDBC_USER, H2_JDBC_PASS);
        insertSampleData(ds.getConnection());
        
        server = new EmbeddedServer();
        
        H2ExecutionFactory factory = new H2ExecutionFactory();
        factory.start();
        factory.setSupportsDirectQueryProcedure(true);
        server.addTranslator("translator-h2", factory);
        
        server.addConnectionFactory("java:/accounts-ds", ds);
        
        EmbeddedConfiguration config = new EmbeddedConfiguration();
        config.setTransactionManager(EmbeddedHelper.getTransactionManager());
        server.start(config);
                
        server.deployVDB(ResultsCachingMysql.class.getClassLoader().getResourceAsStream("mat/mat-h2-vdb.xml"));
        
        Properties info = new Properties();
        conn = server.getDriver().connect("jdbc:teiid:MatVDB", info);
        
    }
    
    private static void insertSampleData(Connection connection) throws SQLException, FileNotFoundException {
        RunScript.execute(connection, new InputStreamReader(ResultsCachingMysql.class.getClassLoader().getResourceAsStream("mat/schema.sql")));
    }
    
    static void teardown() throws SQLException {
        JDBCUtils.close(conn);
        server.stop();
    }

    public static void main(String[] args) throws Exception {

        startup();
        
        sleep(MSG, 5000L);
        
        execute(conn, "select * from MatView", false);
        
        execute(conn, "INSERT INTO PRODUCT (ID,SYMBOL,COMPANY_NAME) VALUES(2000,'RHT','Red Hat Inc')", false);
        
        sleep(MSG, 20000L);
        
        execute(conn, "select * from MatView", false);
        
        execute(conn, "DELETE FROM PRODUCT  WHERE ID = 2000", false);
        
        sleep(MSG, 20000L);
        
        execute(conn, "select * from MatView", false);
        
        teardown();
        
        Util.print("\n");
    }

}
