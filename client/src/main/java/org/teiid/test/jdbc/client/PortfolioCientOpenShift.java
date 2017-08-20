package org.teiid.test.jdbc.client;

import static org.teiid.test.jdbc.client.JDBCUtils.execute;
import static org.teiid.test.jdbc.client.JDBCUtils.getDriverConnection;

import java.sql.Connection;

public class PortfolioCientOpenShift {
    
    private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:Portfolio@mm://localhost:31000;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";
    
    private static String SQL_PRODUCT = "SELECT * FROM Accounts.PRODUCT";
    private static String SQL_MARKETDATA = "SELECT SP.symbol, SP.price FROM (EXEC MarketData.getTextFiles('*.txt')) AS f, TEXTTABLE(f.file COLUMNS symbol string, price bigdecimal HEADER) AS SP";
    private static String SQL_STOCK = "SELECT * FROM Stock";

    public static void main(String[] args) throws Exception {

        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        
        execute(conn, SQL_PRODUCT, false);
        execute(conn, SQL_MARKETDATA, false);
        execute(conn, SQL_STOCK, true);
    }

}
