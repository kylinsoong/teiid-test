package org.teiid.test.perf;

import static org.teiid.test.util.JDBCUtils.executeQuery;
import static org.teiid.test.util.JDBCUtils.execute;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.test.util.EmbeddedHelper;
import org.teiid.test.util.JDBCUtils;
import org.teiid.translator.jdbc.mysql.MySQL5ExecutionFactory;

public class ExternalMaterializationMysqlDebug {
    static {
        EmbeddedHelper.enableLogger(Level.INFO);
    }
    
    static EmbeddedServer server = null;
    static Connection conn = null;
    
    static Logger logger = Logger.getLogger(ExternalMaterializationMysql.class.getName());
    
    static void startup() throws Exception {
              
        logger.info("Start");
        
        server = new EmbeddedServer();
        
        MySQL5ExecutionFactory factory = new MySQL5ExecutionFactory();
        factory.start();
        factory.setSupportsDirectQueryProcedure(true);
        server.addTranslator("translator-mysql", factory);
        
        DataSource ds = EmbeddedHelper.newDataSource("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/test", "jdv_user", "jdv_pass");
        server.addConnectionFactory("java:/accounts-ds", ds);
        
        EmbeddedConfiguration config = new EmbeddedConfiguration();
        config.setTransactionManager(EmbeddedHelper.getTransactionManager());
        server.start(config);
        
        server.deployVDB(ResultsCachingMysql.class.getClassLoader().getResourceAsStream("mat/mat-mysql-vdb-debug.xml"));
        
        Properties info = new Properties();
        conn = server.getDriver().connect("jdbc:teiid:MatVDB", info);
    }
    
    static void teardown() throws SQLException {
        JDBCUtils.close(conn);
        server.stop();
    }
  

    public static void main(String[] args) throws Exception {
        
        startup();
        
//        executeMatViewStatus();
        
        executeLoadMatView();
        
        teardown();
    }

    static void executeLoadMatView() throws Exception {
        
        String vdbName = "MatVDB";
        Integer vdbVersion = 1;
        String uid = "tid:6021235fbeea-95513c05-00000001";
        String status = "CHECK";
        Integer rowsUpdated = 0;
        String crit;
        Integer lineCount = 0;
        Integer index = 0;
        
        boolean isMaterialized = true;
        
        String statusTable = "status";
        String beforeLoadScript = "execute accounts.native('truncate table mat_test_staging')";
        String loadScript = "";
        String afterLoadScript = "execute accounts.native('ALTER TABLE h2_test_mat RENAME TO h2_test_mat_temp');execute accounts.native('ALTER TABLE mat_test_staging RENAME TO h2_test_mat');execute accounts.native('ALTER TABLE h2_test_mat_temp RENAME TO mat_test_staging');";
        Integer integer = -1;
        String matViewStageTable = "Accounts.mat_test_staging";
        String scope = "NONE";
        String matViewTable = "h2_test_mat";
        String action = "THROW_EXCEPTION";
        
        crit = "WHERE VDBName = 'MatVDB' AND VDBVersion = 1 AND SchemaName = 'Stocks' AND Name = 'MatView'";
        
        execute(conn, "execute SYSADMIN.loadMatView('Stocks','MatView')", false);
        
        
//        executeQuery(conn, "SELECT Name AS vdbName FROM VirtualDatabases");
//        executeQuery(conn, "SELECT convert(Version, integer) AS vdbVersion FROM VirtualDatabases");
//        executeQuery(conn, "SELECT UID FROM Sys.Tables WHERE VDBName = 'MatVDB' AND SchemaName = 'Stocks' AND Name = 'MatView'");
//        execute(conn, "SELECT IsMaterialized FROM SYS.Tables WHERE UID = 'tid:6021235fbeea-95513c05-00000001'", false);
        
//        executeQuery(conn, "SELECT \"value\" from SYS.Properties WHERE UID = 'tid:6021235fbeea-95513c05-00000001' AND Name = '{http://www.teiid.org/ext/relational/2012}MATVIEW_STATUS_TABLE'");
//        executeQuery(conn, "SELECT \"value\" from SYS.Properties WHERE UID = 'tid:6021235fbeea-95513c05-00000001' AND Name = '{http://www.teiid.org/ext/relational/2012}MATVIEW_BEFORE_LOAD_SCRIPT'");
//        executeQuery(conn, "SELECT \"value\" from SYS.Properties WHERE UID = 'tid:6021235fbeea-95513c05-00000001' AND Name = '{http://www.teiid.org/ext/relational/2012}MATVIEW_LOAD_SCRIPT'");
//        execute(conn, "SELECT \"value\" from SYS.Properties WHERE UID = 'tid:6021235fbeea-95513c05-00000001' AND Name = '{http://www.teiid.org/ext/relational/2012}MATVIEW_AFTER_LOAD_SCRIPT'", false);
//        executeQuery(conn, "SELECT \"value\" from SYS.Properties WHERE UID = 'tid:6021235fbeea-95513c05-00000001' AND Name = '{http://www.teiid.org/ext/relational/2012}MATVIEW_TTL'");
//        executeQuery(conn, "SELECT \"value\" from SYS.Properties WHERE UID = 'tid:6021235fbeea-95513c05-00000001' AND Name = '{http://www.teiid.org/ext/relational/2012}MATERIALIZED_STAGE_TABLE'");
//        executeQuery(conn, "SELECT \"value\" from SYS.Properties WHERE UID = 'tid:6021235fbeea-95513c05-00000001' AND Name = '{http://www.teiid.org/ext/relational/2012}MATVIEW_SHARE_SCOPE'");
//        executeQuery(conn, "SELECT TargetName from SYSADMIN.MatViews WHERE VDBName = 'MatVDB' AND SchemaName = 'Stocks' AND Name = 'MatView'");
//        executeQuery(conn, "SELECT \"value\" from SYS.Properties WHERE UID = 'tid:6021235fbeea-95513c05-00000001' AND Name = '{http://www.teiid.org/ext/relational/2012}MATVIEW_ONERROR_ACTION'");
    }

    static void executeMatViewStatus() throws Exception {
        
        executeQuery(conn, "SELECT Name AS vdbName FROM VirtualDatabases");
        executeQuery(conn, "SELECT convert(Version, integer) AS vdbVersion FROM VirtualDatabases");
//        executeQuery(conn, "SELECT * FROM Sys.Tables");
        executeQuery(conn, "SELECT UID FROM Sys.Tables WHERE VDBName = 'MatVDB' AND SchemaName = 'Stocks' AND Name = 'MatView'");
        execute(conn, "SELECT IsMaterialized FROM SYS.Tables WHERE UID = 'tid:6021235fbeea-95513c05-00000001'", false);
//        execute(conn, "SELECT * from SYS.Properties", false);
        execute(conn, "SELECT \"Value\" AS statusTable from SYS.Properties WHERE UID = 'tid:6021235fbeea-95513c05-00000001' AND Name = '{http://www.teiid.org/ext/relational/2012}MATVIEW_STATUS_TABLE'", false);
        execute(conn, "SELECT \"Value\" AS action from SYS.Properties WHERE UID = 'tid:6021235fbeea-95513c05-00000001' AND Name = '{http://www.teiid.org/ext/relational/2012}MATVIEW_ONERROR_ACTION'", false);
        execute(conn, "SELECT * FROM status WHERE VDBName = 'MatVDB' AND VDBVersion = 1 AND SchemaName = 'Stocks' AND Name = 'MatView'", false);
//        execute(conn, "SELECT * FROM status WHERE VDBName = 'MatVDB' AND VDBVersion = 1 AND SchemaName = 'Stocks' AND Name = 'MatView' AS TargetSchemaName string, TargetName string, Valid boolean, LoadState string, Updated timestamp, Cardinality long, LoadNumber long, OnErrorAction varchar(25) USING vdbName = 'MatVDB', vdbVersion = 1, schemaName = 'Stocks', viewName = 'MatView'", false);
            
        JDBCUtils.execute(conn, "execute SYSADMIN.matViewStatus('Stocks', 'MatView')", false);
    }

}
