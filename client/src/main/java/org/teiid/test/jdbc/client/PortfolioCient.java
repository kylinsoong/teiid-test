package org.teiid.test.jdbc.client;

import static org.teiid.test.jdbc.client.JDBCUtils.getDriverConnection;
import static org.teiid.test.jdbc.client.JDBCUtils.execute;

import java.sql.Connection;


public class PortfolioCient {
	
	private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:Portfolio@mm://localhost:31000;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";
    
    static String SQL_QUERY_1 = "SELECT * FROM product";
    static String SQL_QUERY_2 = "SELECT stock.* from (call MarketData.getTextFiles('*.txt')) f, TEXTTABLE(f.file COLUMNS symbol string, price bigdecimal HEADER) stock";
    static String SQL_QUERY_3 = "SELECT * FROM StockPrices";
    static String SQL_QUERY_4 = "SELECT product.symbol, stock.price, company_name from product, (call MarketData.getTextFiles('*.txt')) f, TEXTTABLE(f.file COLUMNS symbol string, price bigdecimal HEADER) stock where product.symbol=stock.symbol";
    static String SQL_QUERY_5 = "SELECT * FROM Stock";
    static String SQL_QUERY_6 = "SELECT x.* FROM (call native('select Shares_Count, MONTHNAME(Purchase_Date) from Holdings')) w, ARRAYTABLE(w.tuple COLUMNS \"Shares_Count\" integer, \"MonthPurchased\" string ) AS x";
    
	public static void main(String[] args) throws Exception {

		Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
		
		execute(conn, SQL_QUERY_1, false);
		execute(conn, SQL_QUERY_2, false);
		execute(conn, SQL_QUERY_3, false);
		execute(conn, SQL_QUERY_4, false);
		execute(conn, SQL_QUERY_5, false);
		execute(conn, SQL_QUERY_6, true);
		
	}

}
