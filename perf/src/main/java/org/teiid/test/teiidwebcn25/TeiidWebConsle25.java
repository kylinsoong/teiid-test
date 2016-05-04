package org.teiid.test.teiidwebcn25;

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

public class TeiidWebConsle25 {
    
    static EmbeddedServer server = null;
    static Connection conn = null;
    
    static final String SQL_MAT = "SELECT * FROM stockPricesMatView";
    static final String SQL_INTER_MAT = "SELECT * FROM stockPricesInterMatView";
    
    static final String SQL_MAT_UPDATE = "EXEC SYSADMIN.loadMatView('StocksMatModel', 'stockPricesMatView', true)";
    static final String SQL_INTER_MAT_UPDATE = "EXEC SYSADMIN.refreshMatView('StocksInterMatModel.stockPricesInterMatView', true)";
        
    static void queryMatView() throws Exception {

//        execute(conn, SQL_MAT, false);
//        execute(conn, SQL_INTER_MAT, false);
        
//        execute(conn, SQL_MAT_UPDATE, false);
//        execute(conn, SQL_INTER_MAT_UPDATE, false);
    }
    
    static void loadMatViewProcedure() throws Exception {
        
//        execute(conn, "SELECT Name FROM SYS.VirtualDatabases", false);
//        
//        execute(conn, "SELECT convert(Version, integer) FROM SYS.VirtualDatabases", false);
//        
//        execute(conn, "SELECT UID FROM Sys.Tables WHERE VDBName = 'MatViewH2VDB' AND SchemaName = 'StocksMatModel' AND Name = 'stockPricesMatView'", false);
//        
//        execute(conn, "SELECT IsMaterialized FROM SYS.Tables WHERE UID = 'tid:438c26446762-0f07a905-00000001'", false);
//        
//        execute(conn, "SELECT \"value\" from SYS.Properties WHERE UID = 'tid:438c26446762-0f07a905-00000001' AND Name = '{http://www.teiid.org/ext/relational/2012}MATVIEW_STATUS_TABLE'", false);
//        execute(conn, "SELECT \"value\" from SYS.Properties WHERE UID = 'tid:438c26446762-0f07a905-00000001' AND Name = '{http://www.teiid.org/ext/relational/2012}MATVIEW_BEFORE_LOAD_SCRIPT'", false);
//        execute(conn, "SELECT \"value\" from SYS.Properties WHERE UID = 'tid:438c26446762-0f07a905-00000001' AND Name = '{http://www.teiid.org/ext/relational/2012}MATVIEW_LOAD_SCRIPT'", false);
//        execute(conn, "SELECT \"value\" from SYS.Properties WHERE UID = 'tid:438c26446762-0f07a905-00000001' AND Name = '{http://www.teiid.org/ext/relational/2012}MATVIEW_AFTER_LOAD_SCRIPT'", false);
//        execute(conn, "SELECT convert(\"value\", integer) from SYS.Properties WHERE UID = 'tid:438c26446762-0f07a905-00000001' AND Name = '{http://www.teiid.org/ext/relational/2012}MATVIEW_TTL'", false);
//        execute(conn, "SELECT \"value\" from SYS.Properties WHERE UID = 'tid:438c26446762-0f07a905-00000001' AND Name = '{http://www.teiid.org/ext/relational/2012}MATERIALIZED_STAGE_TABLE'", false);
//        execute(conn, "SELECT \"value\" from SYS.Properties WHERE UID = 'tid:438c26446762-0f07a905-00000001' AND Name = '{http://www.teiid.org/ext/relational/2012}MATVIEW_SHARE_SCOPE'", false);
//        
//        execute(conn, "SELECT (TargetName, TargetSchemaName) from SYSADMIN.MatViews WHERE VDBName = 'MatViewH2VDB' AND SchemaName = 'StocksMatModel' AND Name = 'stockPricesMatView'", false);
//        
//        execute(conn, "SELECT \"value\" from SYS.Properties WHERE UID = 'tid:438c26446762-0f07a905-00000001' AND Name = '{http://www.teiid.org/ext/relational/2012}MATVIEW_ONERROR_ACTION'", false);
//        
//        String crit = " VDBName = 'MatViewH2VDB' AND VDBVersion = 1 AND schemaName = 'StocksMatModel' AND Name = 'stockPricesMatView'"; 
//        String updateStmt = "UPDATE status";
        
//        execute(conn, "", false);
    }
    
    static void procedureTest() throws Exception{
//        execute(conn, "EXEC loadMatViewTest('StocksMatModel', 'stockPricesMatView', true)", false);
//        execute(conn, "EXEC loadMatViewTest('StocksInterMatModel', 'stockPricesInterMatView', true)", false);
//        execute(conn, "SELECT * FROM SYSADMIN.MatViews", false);
    }
    
    static void changedProcedureTest() throws Exception{ 
        execute(conn, "EXEC SYSADMIN.loadMatView('StocksMatModel', 'stockPricesMatView', true)", false);
        execute(conn, "EXEC SYSADMIN.loadMatView('StocksInterMatModel', 'stockPricesInterMatView', true)", false);
    }

    public static void main(String[] args) throws Exception{

        EmbeddedHelper.enableLogger(Level.INFO);
        
        DataSource ds = EmbeddedHelper.newDataSource(H2_JDBC_DRIVER, H2_JDBC_URL, H2_JDBC_USER, H2_JDBC_PASS);
        RunScript.execute(ds.getConnection(), new InputStreamReader(TeiidWebConsle25.class.getClassLoader().getResourceAsStream("teiidwebcn-25/customer-schema.sql")));
        
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
        managedconnectionFactory.setParentDirectory("src/main/resources/teiidwebcn-25/data");
        server.addConnectionFactory("java:/marketdata-file", managedconnectionFactory.createConnectionFactory());
    
        
        EmbeddedConfiguration config = new EmbeddedConfiguration();
        config.setTransactionManager(EmbeddedHelper.getTransactionManager());
        server.start(config);
                
        server.deployVDB(TeiidWebConsle25.class.getClassLoader().getResourceAsStream("teiidwebcn-25/teiidwebcn25-vdb.xml"));
        
        Properties info = new Properties();
        conn = server.getDriver().connect("jdbc:teiid:MatViewH2VDB", info);

        Thread.sleep(2000);
        
        queryMatView();
        
        loadMatViewProcedure();
        
        procedureTest();
        
        conn.close();
    }

}
