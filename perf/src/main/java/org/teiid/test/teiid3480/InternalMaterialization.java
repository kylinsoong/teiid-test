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
import org.teiid.test.util.EmbeddedHelper;
import org.teiid.translator.file.FileExecutionFactory;
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;

public class InternalMaterialization {
    
    static EmbeddedServer server = null;
    static Connection conn = null;
    
    static final String SQL_matViewStatus = "EXEC SYSADMIN.matViewStatus('StocksMatModel', 'stockPricesInterMatView')";
    
    static final String SQL_refreshMatView = "EXEC SYSADMIN.refreshMatView('StocksMatModel.stockPricesInterMatView', true)";
    
    static final String SQL_GET_UID = "SELECT UID FROM Sys.Tables WHERE VDBName = 'PortfolioInterMaterialize' AND SchemaName = 'StocksMatModel' AND Name = 'stockPricesInterMatView'";
    static final String SQL_MAT_STATUS_TABLE = "SELECT \"Value\" from SYS.Properties WHERE UID = 'tid:455a9f178c8e-5c10eec9-00000001' AND Name = '{http://www.teiid.org/ext/relational/2012}MATVIEW_STATUS_TABLE'";
    
    static final String SQL_matViewStatus_ = "EXEC matViewStatus_('StocksMatModel', 'stockPricesInterMatView')";
    
    public static void main(String[] args) throws Exception {
        
        EmbeddedHelper.enableLogger(Level.OFF);
        
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
                
        server.deployVDB(InternalMaterialization.class.getClassLoader().getResourceAsStream("teiid-3840/portfolio-intermat-vdb.xml"));
        
        Properties info = new Properties();
        conn = server.getDriver().connect("jdbc:teiid:PortfolioInterMaterialize", info);
        
//        execute(conn, "select * from sysadmin.matviews WHERE SchemaName = 'StocksMatModel'", false);
        
//        statusUpdateTimeline();
        
//        execute(conn, SQL_GET_UID, false);
//        execute(conn, SQL_MAT_STATUS_TABLE, false);
        
        Thread.sleep(5000);
        
        execute(conn, SQL_matViewStatus, false);
        
        conn.close();
        
//        execute(conn, "select * from stockPricesInterMatView", false);
//        
//        
//        for(;;){
//            execute(conn, "select * from sysadmin.matviews WHERE Name = 'stockPricesInterMatView'", false);
//            Thread.sleep(60000);
//            execute(conn, "select * from stockPricesInterMatView", false);
//        }
        
//        Thread.sleep(Long.MAX_VALUE);
    }

    /**
     * Test how status be updated in Internal Mat View
     * @throws Exception
     */
    static void statusUpdateTimeline() throws Exception {

      //wait some seconds to make sure Internal Mat View be updated
        Thread.sleep(5 * 1000);
        execute(conn, "select * from sysadmin.matviews WHERE SchemaName = 'StocksMatModel'", false);
        
        //wait 30 seconds to make sure Internal Mat View be updated
        Thread.sleep(30 * 1000);
        execute(conn, "select * from sysadmin.matviews WHERE SchemaName = 'StocksMatModel'", false);
        
        //use refreshMatView to force update the status table
        execute(conn, SQL_refreshMatView, false);
        execute(conn, "select * from sysadmin.matviews WHERE SchemaName = 'StocksMatModel'", false);
        
        //force update another time
        execute(conn, SQL_refreshMatView, false);
        execute(conn, "select * from sysadmin.matviews WHERE SchemaName = 'StocksMatModel'", false);
        
        //wait some seconds to another round of update
        Thread.sleep(30 * 1000);
        execute(conn, "select * from sysadmin.matviews WHERE SchemaName = 'StocksMatModel'", false);
    }

}
