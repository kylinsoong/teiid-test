package org.teiid.test.jdbc.client;

import static org.teiid.test.jdbc.client.JDBCUtils.execute;
import static org.teiid.test.jdbc.client.JDBCUtils.getDriverConnection;

import java.sql.Connection;


public class PortfolioMaterializeClient {
	
	private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:PortfolioMaterialize@mm://localhost:31000;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";
    
    static String SQL_MATVIEW = "SELECT * FROM stockPricesMatView";
    
    static String SQL_MATVIEW_STATUS = "EXEC SYSADMIN.matViewStatus('StocksMatModel', 'stockPricesMatView')";
    
    static String SQL_MATVIEW_REFRESH = "EXEC SYSADMIN.loadMatView('StocksMatModel', 'stockPricesMatView', true)";
    
	public static void main(String[] args) throws Exception {

		Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
		
		// Query Mat View
		execute(conn, SQL_MATVIEW, false);
		
		// Query Mat View Status
		execute(conn, SQL_MATVIEW_STATUS, false);
		
		// Refresh Mat View
		execute(conn, SQL_MATVIEW_REFRESH, false);
		
		conn.close();
	}
	


}
