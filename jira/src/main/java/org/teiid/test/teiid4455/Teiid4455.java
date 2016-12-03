package org.teiid.test.teiid4455;

import static org.teiid.test.utils.JDBCUtils.*;

import java.sql.Connection;
import java.util.logging.Level;

import javax.sql.DataSource;

import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.test.utils.EmbeddedHelper;
import org.teiid.translator.jdbc.mysql.MySQL5ExecutionFactory;

public class Teiid4455 {
    
    static final String SQL_MYSQL_TIMESTAMPDIFF = "SELECT mysql.timestampdiff('SECOND','2016-05-18','2016-10-11'), mysql.timestampdiff('MINUTE','2016-05-18','2016-10-11'), mysql.timestampdiff('HOUR','2016-05-18','2016-10-11'), mysql.timestampdiff('DAY','2016-05-18','2016-10-11'), mysql.timestampdiff('WEEK','2016-05-18','2016-10-11'), mysql.timestampdiff('MONTH','2016-05-18','2016-10-11'), mysql.timestampdiff('QUARTER','2016-05-18','2016-10-11'), mysql.timestampdiff('YEAR','2016-05-18','2016-10-11')";
    static final String SQL_MYSQL_TIMESTAMPDIFF_M = "SELECT mysql.timestampdiff('MONTH','2016-05-18','2016-10-11')";
    
    static final String SQL_DATOFWEEK = "SELECT DAYOFWEEK(DATECLOSED) FROM ACCOUNT";
    
    public static void main(String[] args) throws Exception {

        EmbeddedHelper.enableLogger(Level.ALL);
        
        DataSource ds = EmbeddedHelper.newDataSource("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/test", "test_user", "test_pass");
        
        EmbeddedServer server = new EmbeddedServer();
        
        MySQL5ExecutionFactory executionFactory = new MySQL5ExecutionFactory();
        executionFactory.setSupportsDirectQueryProcedure(true);
        executionFactory.start();
        server.addTranslator("translator-mysql", executionFactory);
        
        server.addConnectionFactory("java:/accounts-ds", ds);
        
        EmbeddedConfiguration config = new EmbeddedConfiguration();
        config.setTimeSliceInMilli(Integer.MAX_VALUE);
        server.start(config);
        
        server.deployVDB(Teiid4455.class.getClassLoader().getResourceAsStream("teiid4455-vdb.xml"));
        
        Connection conn = server.getDriver().connect("jdbc:teiid:Portfolio", null);
        
//        execute(conn, "SELECT * FROM ACCOUNT", false);
        
//        execute(conn, "SELECT TIMESTAMPDIFF(SQL_TSI_HOUR,'2016-05-18','2016-10-11'), TIMESTAMPDIFF(SQL_TSI_DAY,'2016-05-18','2016-10-11'), TIMESTAMPDIFF(SQL_TSI_WEEK,'2016-05-18','2016-10-11')", false);
//        execute(conn, "SELECT TIMESTAMPDIFF(SQL_TSI_FRAC_SECOND,'2000-01-02 00:00:00.0','2016-10-10 23:59:59.999999')", false);
//        execute(conn, "SELECT TIMESTAMPDIFF(SQL_TSI_SECOND,'2000-01-02 00:00:00','2016-10-10 23:59:59')", false);
//        execute(conn, "SELECT TIMESTAMPDIFF(SQL_TSI_MONTH,'2000-01-02','2016-10-10')", false);
        
//        execute(conn, "SELECT TIMESTAMPADD(SQL_TSI_MONTH, 12,'2016-10-10')", false);
//        execute(conn, "SELECT TIMESTAMPADD(SQL_TSI_SECOND, 12,'2016-10-10 23:59:59')", false);
        
        execute(conn, "SELECT from_unixtime(a) FROM smallints", false); 
//        execute(conn, "SELECT FROM_UNIXTIME(1900000000, '%Y %D %M %h:%i:%s')", false);
//        execute(conn, "SELECT FROM_UNIXTIME(1900000000, 'MM/dd/yyyy')", false);
        
//        execute(conn, "SELECT Name FROM SYS.Schemas WHERE VDBName = 'Portfolio'", false); 
        
//        execute(conn, "SELECT Name FROM SYS.Tables WHERE SchemaName = 'Accounts' AND VDBName = 'Portfolio'", false); 
        
//        execute(conn, "SELECT TypeName, JavaClass, RuntimeType FROM SYS.DataTypes", false);
        
//        execute(conn, SQL_MYSQL_TIMESTAMPDIFF, false);
//        execute(conn, SQL_MYSQL_TIMESTAMPDIFF_M, false);
        
//        execute(conn, SQL_DATOFWEEK, false);
        
//        execute(conn, "SELECT * FROM TESTVIEW", false);
        
        close(conn);
    }

}
