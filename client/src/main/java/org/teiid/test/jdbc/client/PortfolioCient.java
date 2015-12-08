package org.teiid.test.jdbc.client;

import static org.teiid.example.util.JDBCUtils.getDriverConnection;
import static org.teiid.example.util.JDBCUtils.execute;

import java.sql.Connection;


public class PortfolioCient {
	
	private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:Portfolio@mm://localhost:31000;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";
    
    static String sql_sheet1 = "SELECT * FROM Sheet1";
    static String sql_StockPrices = "SELECT * FROM StockPrices";
    static String sql_PRODUCT = "SELECT * FROM PRODUCT";
    static String sql_Stock = "SELECT * FROM Stock";
    
	public static void main(String[] args) throws Exception {

		Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
		
//		execute(conn, sql_sheet1, false);
//		execute(conn, sql_StockPrices, false);
//		execute(conn, sql_PRODUCT, false);
		execute(conn, sql_Stock, true);
		
	}

}
