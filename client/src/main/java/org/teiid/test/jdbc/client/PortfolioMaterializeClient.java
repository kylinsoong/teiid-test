package org.teiid.test.jdbc.client;

import static org.teiid.example.util.JDBCUtils.execute;
import static org.teiid.example.util.JDBCUtils.getDriverConnection;

import java.sql.Connection;

public class PortfolioMaterializeClient {
	
	private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:PortfolioMaterialize@mm://localhost:31000;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";
    
    static String sql_mat = "SELECT * FROM stockPricesMatView";

	public static void main(String[] args) throws Exception {

		Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
		execute(conn, sql_mat, true);
	}

}
