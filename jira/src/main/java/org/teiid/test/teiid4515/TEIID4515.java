package org.teiid.test.teiid4515;

import static org.teiid.test.utils.JDBCUtils.close;
import static org.teiid.test.utils.JDBCUtils.execute;

import java.sql.Connection;
import java.util.logging.Level;

import javax.sql.DataSource;

import org.teiid.logging.LogManager;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.test.utils.EmbeddedHelper;
import org.teiid.translator.hbase.HBaseExecutionFactory;
//import org.teiid.translator.hbase.PhoenixExecutionFactory;

public class TEIID4515 {
    
    public static final String URL =  "jdbc:phoenix:127.0.0.1:2181";
    public static final String DRIVER = "org.apache.phoenix.jdbc.PhoenixDriver";
    public static final String USER = "sa";
    public static final String PASS = "sa";
    
    public static final String SQL_QUERY_CUSTOMERS = "SELECT * FROM Customers"; 
    public static final String SQL_QUERY_CUSTOMERS_LIMIT_1 = "SELECT * FROM Customers LIMIT 2";
    public static final String SQL_QUERY_CUSTOMERS_LIMIT_2 = "SELECT * FROM Customers LIMIT 2, 2";
    public static final String SQL_QUERY_CUSTOMERS_LIMIT_3 = "SELECT * FROM Customers OFFSET 2 ROWS";
    public static final String SQL_QUERY_CUSTOMERS_LIMIT_4 = "SELECT * FROM Customers FETCH FIRST 2 ROWS ONLY";
    public static final String SQL_QUERY_CUSTOMERS_LIMIT_5 = "SELECT * FROM Customers OFFSET 2 ROWS FETCH NEXT 2 ROWS ONLY";
    
    public static void main(String[] args) throws Exception {
        
        EmbeddedHelper.enableLogger(Level.ALL);
        
        LogManager.logDetail("org.teiid.example", "TEIID-4515 Test");
        
        DataSource ds = EmbeddedHelper.newDataSource(DRIVER, URL, USER, PASS, "phoenix.connection.autoCommit=true");
        
        EmbeddedServer server = new EmbeddedServer();
        
//        PhoenixExecutionFactory factory = new PhoenixExecutionFactory();
//        factory.start();
//        server.addTranslator("translator-hbase", factory);

        server.addConnectionFactory("java:/hbaseDS", ds);
        
        EmbeddedConfiguration config = new EmbeddedConfiguration();
        config.setTimeSliceInMilli(Integer.MAX_VALUE);
        server.start(config);
        
        server.deployVDB(TEIID4515.class.getClassLoader().getResourceAsStream("teiid4515-vdb.xml"));
        
        Connection conn = server.getDriver().connect("jdbc:teiid:Portfolio", null);
        
        execute(conn, SQL_QUERY_CUSTOMERS, false); 
//        execute(conn, SQL_QUERY_CUSTOMERS_LIMIT_1, false); 
//        execute(conn, SQL_QUERY_CUSTOMERS_LIMIT_2, false); 
//        execute(conn, SQL_QUERY_CUSTOMERS_LIMIT_3, false); 
//        execute(conn, SQL_QUERY_CUSTOMERS_LIMIT_4, false); 
//        execute(conn, SQL_QUERY_CUSTOMERS_LIMIT_5, false); 
        
        functions(conn);
        
        close(conn);
        
//        LogManager.logTrace("org.teiid.example", "TEIID-4515 Test Exist");
    }

    static void functions(Connection conn) throws Exception {
        
//        execute(conn, "SELECT phoenix.TIMEZONE_OFFSET('Asia/Shanghai', c1) FROM TimesTest WHERE c0 = '101'", false);
//        execute(conn, "SELECT phoenix.TIMEZONE_OFFSET('Asia/Shanghai', c2) FROM TimesTest WHERE c0 = '101'", false);
//        execute(conn, "SELECT phoenix.TO_DATE('1970-01-01', 'yyyy-MM-dd', 'GMT+1') FROM TimesTest WHERE c0 = '101'", false);
        
        execute(conn, "SELECT phoenix.CONVERT_TZ(c1, 'Asia/Shanghai', 'UTC') FROM TimesTest WHERE c0 = '101'", false);
        execute(conn, "SELECT phoenix.CONVERT_TZ(c2, 'Asia/Shanghai', 'UTC') FROM TimesTest WHERE c0 = '101'", false);
        
        
//        execute(conn, "SELECT SIGN(1.1), SIGN(0), SIGN(-1.1), ABS(1.1), ABS(0), ABS(-1.1)", false);
//        execute(conn, "SELECT SQRT(1.1), SQRT(0), SQRT(4)", false);
        
//        execute(conn, "SELECT EXP(2), EXP(0), EXP(-1), POWER(2, 4)", false);
        
//        execute(conn, "SELECT LOG(2), LOG10(1000)", false);
        
//        execute(conn, "SELECT RAND(), ROUND(2.1, 2)", false);
        
//        execute(conn, "SELECT CURTIME() FROM Customers", false);
        
//        execute(conn, "SELECT phoenix.CURRENT_TIME() FROM Customers", false);
        
//        execute(conn, "SELECT phoenix.REVERSE(CUSTOMERNAME) FROM Customers WHERE CUSTOMERID = 'C005'", false);
//        execute(conn, "SELECT phoenix.TO_DATE('Sat, 3 Feb 2001 03:05:06 GMT', 'EEE, d MMM yyyy HH:mm:ss z', 'GMT+1') FROM Customers", false);
        
        
        
//        execute(conn, "SELECT UCASE(COUNTRY) FROM Customers WHERE CUSTOMERID = 'C005'", false);
//        execute(conn, "SELECT LOCATE(CUSTOMERNAME, 'Appliances') FROM Customers WHERE CUSTOMERID = 'C005'", false);
        
//        execute(conn, "SELECT regexp_replace('Goodbye World', '[g-o].', 'x', 'gi')", false);
        
//        execute(conn, "SELECT phoenix.REVERSE(CUSTOMERNAME) FROM Customers WHERE CUSTOMERID = 'C005'", false);
//        execute(conn, "SELECT phoenix.REGEXP_SUBSTR('na1-appsrv35-sj35','\"[^-]+'\")", false);
//        execute(conn, "SELECT phoenix.TO_CHAR(O.DATE, 'yyyy-MM-dd HH:mm:ss') FROM Orders AS O WHERE O.ORDERID = '1630781'", false);
//        execute(conn, "SELECT SUBSTR(CUSTOMERNAME, 15) FROM Customers WHERE CUSTOMERID = 'C005'", false);
//        execute(conn, "SELECT SUBSTRING(CUSTOMERNAME, 15) FROM Customers WHERE CUSTOMERID = 'C005'", false);
//        execute(conn, "SELECT SUBSTRING(CUSTOMERNAME, 15, 9) FROM Customers WHERE CUSTOMERID = 'C005'", false);
//        execute(conn, "SELECT LPAD(COUNTRY, 15) FROM Customers WHERE CUSTOMERID = 'C005'", false);
//        execute(conn, "SELECT LPAD(COUNTRY, 15, '*') FROM Customers WHERE CUSTOMERID = 'C005'", false);

//        execute(conn, "SELECT AVG(QUANTITY) AVG_QUANTITY FROM ORDERS", false);
//        execute(conn, "SELECT ITEMID, AVG(QUANTITY) AVG_QUANTITY FROM ORDERS GROUP BY ITEMID ORDER BY ITEMID DESC", false);
//        execute(conn, "SELECT COUNT(*), COUNT(ITEMID), COUNT(DISTINCT ITEMID) FROM ORDERS", false);
//        execute(conn, "SELECT MAX(QUANTITY), MIN(QUANTITY), SUM(QUANTITY) FROM ORDERS", false);
//        execute(conn, "SELECT ITEMID, MAX(QUANTITY), MIN(QUANTITY), SUM(QUANTITY) FROM ORDERS GROUP BY ITEMID", false);
//        
        
    }

}
