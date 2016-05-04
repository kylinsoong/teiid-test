package org.teiid.test.jdbc.client;

import static org.teiid.test.jdbc.client.JDBCUtils.execute;
import static org.teiid.test.jdbc.client.JDBCUtils.getDriverConnection;

import java.sql.Connection;

public class PortfolioInterMaterializeClient {

    private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:PortfolioInterMaterialize@mm://localhost:31000;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";
    
    static String sql_mat = "SELECT * FROM stockPricesInterMatView";
    static String sql_status = "select * from sysadmin.matviews";
    static String sql_viewStatus = "EXEC SYSADMIN.matViewStatus('StocksMatModel', 'stockPricesInterMatView')";

    public static void main(String[] args) throws Exception {

        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        
//        basicQuery(conn);
        
        refreshMatView(conn);
        
        conn.close();
    }

    static void refreshMatView(Connection conn) throws Exception {
        execute(conn, "", false);
    }

    static void basicQuery(Connection conn) throws Exception {

        execute(conn, sql_mat, false);
        execute(conn, sql_status, false);
        execute(conn, sql_viewStatus, false);
    }

}
