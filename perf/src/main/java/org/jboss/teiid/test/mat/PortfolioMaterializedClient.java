package org.jboss.teiid.test.mat;

import java.sql.Connection;

import static org.teiid.test.util.JDBCUtils.getDriverConnection;
import static org.teiid.test.util.JDBCUtils.close;
import static org.teiid.test.util.JDBCUtils.executeQuery;
import static org.teiid.test.util.JDBCUtils.executeUpdate;

public class PortfolioMaterializedClient {
    
    private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:PortfolioMaterialize@mm://localhost:31000;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";

    @SuppressWarnings("static-access")
    public static void main(String[] args) throws Exception {
        
        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        
        executeQuery(conn, "select * from stockPricesMatView");
        
        executeQuery(conn, "select * from h2_stock_mat");
        
        executeQuery(conn, "select * from mat_stock_staging");
        
        executeQuery(conn, "select * from status");
        
        executeUpdate(conn, "INSERT INTO PRODUCT (ID,SYMBOL,COMPANY_NAME) VALUES(2000,'RHT','Red Hat Inc')");
        
        Thread.currentThread().sleep(70000);
        
        executeQuery(conn, "select * from stockPricesMatView");
        
        executeQuery(conn, "select * from h2_stock_mat");
        
        executeQuery(conn, "select * from mat_stock_staging");
        
        executeQuery(conn, "select * from status");
        
        executeUpdate(conn, "DELETE FROM PRODUCT  WHERE ID = 2000");
        
        Thread.currentThread().sleep(70000);
        
        executeQuery(conn, "select * from stockPricesMatView");
        
        executeQuery(conn, "select * from h2_stock_mat");
        
        executeQuery(conn, "select * from mat_stock_staging");
        
        executeQuery(conn, "select * from status");
        
        
        close(conn);
    }

}
