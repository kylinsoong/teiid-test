package org.teiid.test.teiid3480;

import static org.teiid.test.Constants.H2_JDBC_DRIVER;
import static org.teiid.test.Constants.H2_JDBC_PASS;
import static org.teiid.test.Constants.H2_JDBC_URL;
import static org.teiid.test.Constants.H2_JDBC_USER;
import static org.teiid.test.util.JDBCUtils.execute;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.Properties;
import java.util.logging.Level;

import javax.sql.DataSource;

import org.h2.tools.RunScript;
import org.teiid.resource.adapter.file.FileManagedConnectionFactory;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.test.perf.ResultsCachingMysql;
import org.teiid.test.util.EmbeddedHelper;
import org.teiid.translator.file.FileExecutionFactory;
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;

public class ExternalMaterialization {
    
    static EmbeddedServer server = null;
    static Connection conn = null;
    
    static final String SQL_matViewStatus = "EXEC SYSADMIN.matViewStatus('StocksMatModel', 'stockPricesMatView')";
    static final String SQL_matViewsStatus = "EXEC SYSADMIN.matViewsStatus()";
 
    static final String SQL_matViewStatus_ = "EXEC matViewStatus_('StocksMatModel', 'stockPricesMatView')";
    
    static final String SQL_GET_UID = "SELECT UID FROM Sys.Tables WHERE VDBName = 'PortfolioMaterialize' AND SchemaName = 'StocksMatModel' AND Name = 'stockPricesMatView'";
    static final String SQL_MAT_TABLE = "SELECT \"Value\" from SYS.Properties WHERE UID = 'tid:5cb132cf1822-0f07a905-00000001' AND Name = '{http://www.teiid.org/ext/relational/2012}MATVIEW_STATUS_TABLE'";
    
    static final String SQL_SYS_Properties = "SELECT * FROM SYS.Properties";
    static final String SQL_SYS_Tables = "SELECT * FROM SYS.Tables";
    
    static final String SQL_SYS_StoredProcedures = "SELECT * FROM SYS.Procedures";
    
    public static void main(String[] args) throws Exception {

        EmbeddedHelper.enableLogger(Level.INFO);
        
        DataSource ds = EmbeddedHelper.newDataSource(H2_JDBC_DRIVER, H2_JDBC_URL, H2_JDBC_USER, H2_JDBC_PASS);
        RunScript.execute(ds.getConnection(), new InputStreamReader(InternalMaterialization.class.getClassLoader().getResourceAsStream("teiid-3840/customer-schema.sql")));
        
        server = new EmbeddedServer();
        
        H2ExecutionFactory factory = new H2ExecutionFactory();
        factory.start();
        factory.setSupportsDirectQueryProcedure(true);
        server.addTranslator("translator-h2", factory);
        
        server.addConnectionFactory("java:/accounts-ds", ds);
        
        FileExecutionFactory fileExecutionFactory = new FileExecutionFactory();
        fileExecutionFactory.start();
        server.addTranslator("file", fileExecutionFactory);
        
        FileManagedConnectionFactory managedconnectionFactory = new FileManagedConnectionFactory();
        managedconnectionFactory.setParentDirectory("src/main/resources/teiid-3840/data");
        server.addConnectionFactory("java:/marketdata-file", managedconnectionFactory.createConnectionFactory());
    
        
        EmbeddedConfiguration config = new EmbeddedConfiguration();
        config.setTransactionManager(EmbeddedHelper.getTransactionManager());
        server.start(config);
                
        server.deployVDB(ResultsCachingMysql.class.getClassLoader().getResourceAsStream("teiid-3840/portfolio-mat-vdb.xml"));
        
        Properties info = new Properties();
        conn = server.getDriver().connect("jdbc:teiid:PortfolioMaterialize", info);
        
//        execute(conn, "select * from Stock", false);
//        execute(conn, "select * from stockPricesMatView", false);
        
//        execute(conn, SQL_GET_UID, false);
//        execute(conn, SQL_MAT_TABLE, false);
//        
//        execute(conn, "SELECT \"Value\" from SYS.Properties WHERE Name = '{http://www.teiid.org/ext/relational/2012}MATVIEW_STATUS_TABLE' LIMIT 1", false);
//        execute(conn, "SELECT * FROM Sys.Tables WHERE isMaterialized = true AND IsSystem = false", false);
//        execute(conn, SQL_SYS_Tables, false);
        
//        execute(conn, "EXEC matViewsStatus()", false);
        
        Thread.sleep(2000);
        
//        unitTest();
//        
//        execute(conn, SQL_matViewStatus, false);
        
//        execute(conn, "select * from Accounts.status", false);
        
//        execute(conn, "EXEC matViewsStatus()", false);
        
//        execute(conn, SQL_matViewStatus_, false);
        
        execute(conn, SQL_matViewsStatus, false);

        
//        for(;;){
//            execute(conn, "select * from sysadmin.matviews WHERE Name = 'stockPricesMatView'", false);
//            execute(conn, "select * from Accounts.status", false);
//            Thread.sleep(60000);
//            
//        }
        conn.close();
    }

    static void unitTest() throws Exception {

        execute(conn, SQL_SYS_StoredProcedures, false);
    }

}
