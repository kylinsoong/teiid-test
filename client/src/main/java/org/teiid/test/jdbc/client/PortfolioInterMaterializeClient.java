package org.teiid.test.jdbc.client;

import static org.teiid.test.jdbc.client.JDBCUtils.execute;
import static org.teiid.test.jdbc.client.JDBCUtils.getDriverConnection;

import java.sql.Connection;

public class PortfolioInterMaterializeClient {

    private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:PortfolioInterMaterialize@mm://localhost:31000;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";
    
    static String SQL_MATVIEW = "SELECT * FROM stockPricesInterMatView";

    static String SQL_MATVIEW_STATUS = "EXEC SYSADMIN.matViewStatus('StocksMatModel', 'stockPricesInterMatView')";
    
    static String SQL_MATVIEW_REFRESH = "EXEC SYSADMIN.loadMatView('StocksMatModel', 'stockPricesInterMatView', true)";
    
    //An alternative ways to refresh, update, query status   
    static String SQL_MATVIEW_Query = "SELECT * FROM SYSADMIN.MatViews WHERE SchemaName = 'StocksMatModel' AND Name = 'stockPricesInterMatView'";
    static String SQL_MATVIEW_refreshMatView = "EXEC SYSADMIN.refreshMatView('StocksMatModel.stockPricesInterMatView', true)";
    
    public static void main(String[] args) throws Exception {

        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        
        // Query Mat View
        execute(conn, SQL_MATVIEW, false);
        
        // Query Mat View Status
        execute(conn, SQL_MATVIEW_STATUS, false);
        
        // Refresh Mat View
        execute(conn, SQL_MATVIEW_REFRESH, false);
        
        // Update Mat View
        
        //An alternative ways to refresh, update, query status
        execute(conn, SQL_MATVIEW_Query, false);
        execute(conn, SQL_MATVIEW_refreshMatView, false);
                
        conn.close();
    }



}
