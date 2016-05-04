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

public class ExternalMaterializationMore {
    
    static EmbeddedServer server = null;
    static Connection conn = null;
    
    static final String SQL_SYS_Properties = "SELECT DISTINCT \"Value\" FROM SYS.Properties WHERE Name = '{http://www.teiid.org/ext/relational/2012}MATVIEW_STATUS_TABLE'";

    static final String SQL_MAT = "SELECT M.VDBName, M.SchemaName, M.Name, S.TargetSchemaName, S.TargetName, S.Valid, S.LoadState, S.Updated, S.Cardinality FROM SYSADMIN.MatViews AS M, TABLE(CALL SYSADMIN.matViewStatus(M.SchemaName, M.Name)) AS S";
    
    public static void main(String[] args) throws Exception {

        EmbeddedHelper.enableLogger(Level.OFF);
        
        DataSource ds = EmbeddedHelper.newDataSource(H2_JDBC_DRIVER, H2_JDBC_URL, H2_JDBC_USER, H2_JDBC_PASS);
        RunScript.execute(ds.getConnection(), new InputStreamReader(ExternalMaterializationMore.class.getClassLoader().getResourceAsStream("teiid-3840/customer-schema.sql")));
        
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
                
        server.deployVDB(ResultsCachingMysql.class.getClassLoader().getResourceAsStream("teiid-3840/portfolio-mat-more-vdb.xml"));
        
        Properties info = new Properties();
        conn = server.getDriver().connect("jdbc:teiid:PortfolioMaterialize", info);
        
        execute(conn, SQL_SYS_Properties, false);
        
        Thread.sleep(3000);
        
//        execute(conn, "DELETE FROM SYSADMIN.MatViews WHERE SchemaName = 'StocksMatModel_1' AND Name = 'stockPricesMatView_1'", false);
        
        execute(conn, "SELECT VDBName, SchemaName, Name, TargetSchemaName, TargetName, Valid, LoadState, Updated, Cardinality FROM Accounts.status", false);
        
        execute(conn, "SELECT VDBName, SchemaName, Name, TargetSchemaName, TargetName, Valid, LoadState, Updated, Cardinality FROM Accounts.status_1", false);
        
        execute(conn, "SELECT VDBName, SchemaName, Name, TargetSchemaName, TargetName, Valid, LoadState, Updated, Cardinality FROM SYSADMIN.MatViews", false);
        
        String sql = formQuerySQL("Accounts.status", "Accounts.status_1");
                
        execute(conn, sql, false);
        
        execute(conn, "EXEC SYSADMIN.matViewStatus('StocksMatModel', 'stockPricesMatView')", false);
        
        execute(conn, "SELECT M.VDBName, M.SchemaName, M.Name, S.TargetSchemaName, S.TargetName, S.Valid, S.LoadState, S.Updated, S.Cardinality FROM SYSADMIN.MatViews AS M, TABLE(CALL SYSADMIN.matViewStatus(M.SchemaName, M.Name)) AS S", false);
        
        execute(conn, SQL_MAT, false);
        
        conn.close();
        
    }

    private static String formQuerySQL(String... tables) {
        
        String COLUMNS = "SELECT VDBName, SchemaName, Name, TargetSchemaName, TargetName, Valid, LoadState, Updated, Cardinality FROM";
        String UNION = "UNION";
        String BLANK = " ";
        
        StringBuffer sb = new StringBuffer();
        sb.append(COLUMNS).append(BLANK);
        sb.append("SYSADMIN.MatViews").append(BLANK);
        for(String table : tables){
            sb.append(UNION).append(BLANK);
            sb.append(COLUMNS).append(BLANK);
            sb.append(table).append(BLANK);
        }
        
        return sb.toString();

    }

}
