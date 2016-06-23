package org.teiid.test.teiid3617;

import static org.teiid.test.util.JDBCUtils.getDriverConnection;

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
    static String sql_PersonalHoldings = "SELECT * FROM PersonalHoldings";
    
	public static void main(String[] args) throws Exception {

		Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);

		
	}

}
