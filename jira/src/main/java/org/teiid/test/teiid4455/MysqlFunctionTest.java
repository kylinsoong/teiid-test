package org.teiid.test.teiid4455;

import static org.teiid.test.utils.JDBCUtils.*;

import java.sql.Connection;

public class MysqlFunctionTest {
    
    static final String SQL_TIMESTAMPDIFF = "SELECT TIMESTAMPDIFF(SECOND,'2016-05-18','2016-10-11') AS expr1, TIMESTAMPDIFF(MINUTE,'2016-05-18','2016-10-11') AS expr2, TIMESTAMPDIFF(HOUR,'2016-05-18','2016-10-11') AS expr3, TIMESTAMPDIFF(DAY,'2016-05-18','2016-10-11') AS expr4, TIMESTAMPDIFF(WEEK,'2016-05-18','2016-10-11') AS expr5, TIMESTAMPDIFF(MONTH,'2016-05-18','2016-10-11') AS expr6, TIMESTAMPDIFF(QUARTER,'2016-05-18','2016-10-11') AS expr7, TIMESTAMPDIFF(YEAR,'2016-05-18','2016-10-11') AS expr8";
    static final String SQL_TIMESTAMPDIFF_MONTH = "SELECT TIMESTAMPDIFF(MONTH,'2016-05-18','2016-10-11') AS expr1";
    static final String SQL_TIMESTAMPDIFF_1 = "SELECT TIMESTAMPDIFF(SECOND,'2016-05-18 11:45:42','2016-10-11 15:16:39') AS expr1, TIMESTAMPDIFF(MINUTE,'2016-05-18 11:45:42','2016-10-11 15:16:39') AS expr2, TIMESTAMPDIFF(HOUR,'2016-05-18 11:45:42','2016-10-11 15:16:39') AS expr3, TIMESTAMPDIFF(DAY,'2016-05-18 11:45:42','2016-10-11 15:16:39') AS expr4, TIMESTAMPDIFF(WEEK,'2016-05-18 11:45:42','2016-10-11 15:16:39') AS expr5, TIMESTAMPDIFF(MONTH,'2016-05-18 11:45:42','2016-10-11 15:16:39') AS expr6, TIMESTAMPDIFF(QUARTER,'2016-05-18 11:45:42','2016-10-11 15:16:39') AS expr7, TIMESTAMPDIFF(YEAR,'2016-05-18 11:45:42','2016-10-11 15:16:39') AS expr8";
    static final String SQL_TIMESTAMPDIFF_MONTH_1 = "SELECT TIMESTAMPDIFF(MONTH,'2016-05-18 11:45:42','2016-10-11 15:16:39') AS expr1";

    
    static final String SQL_DATOFWEEK = "SELECT DAYOFWEEK('2016-10-17')";
    
    // TIMESTAMPDIFF(FRAC_SECOND,'2016-05-18','2016-10-11'), TIMESTAMPDIFF(SECOND,'2016-05-18','2016-10-11'), TIMESTAMPDIFF(MINUTE,'2016-05-18','2016-10-11'), 

    public static void main(String[] args) throws Exception {

        Connection conn = getDriverConnection("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/test", "test_user", "test_pass");
        
//        execute(conn, "SELECT FROM_UNIXTIME(1900000000)", false);      
//        execute(conn, "SELECT FROM_UNIXTIME(1900000000, '%Y %D %M %h:%i:%s')", false);
        
        execute(conn, SQL_TIMESTAMPDIFF, false);
        execute(conn, SQL_TIMESTAMPDIFF_MONTH, false);
        execute(conn, SQL_TIMESTAMPDIFF_1, false);
        execute(conn, SQL_TIMESTAMPDIFF_MONTH_1, false);
        execute(conn, SQL_DATOFWEEK, false);
       
        close(conn);
        
    }

}
