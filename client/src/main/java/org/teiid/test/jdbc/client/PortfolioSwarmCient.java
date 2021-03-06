package org.teiid.test.jdbc.client;

import static org.teiid.test.jdbc.client.JDBCUtils.*;

import java.sql.Connection;

public class PortfolioSwarmCient {
    
    private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:Portfolio@mm://localhost:31000;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";
    

    public static void main(String[] args) throws Exception {
        
        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        
//        execute(conn, "SELECT * FROM SYSADMIN.MatViews", false);
        
//        execute(conn, "SELECT * FROM SampleTable", false);
        
//        execute(conn, "SELECT * FROM PRODUCT", false); 
//        execute(conn, "SELECT * FROM StockPrices", false); 
        execute(conn, "SELECT * FROM Accounts.PRODUCT", false); 
        
        execute(conn, "SELECT SP.symbol, SP.price FROM (EXEC MarketData.getTextFiles('*.txt')) AS f, TEXTTABLE(f.file COLUMNS symbol string, price bigdecimal HEADER) AS SP", false); 
        
        execute(conn, "SELECT * FROM Stock", false); 
        
        close(conn);

    }

}
