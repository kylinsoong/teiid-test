package org.jboss.teiid.test.mat;

import static org.teiid.test.util.JDBCUtils.close;
import static org.teiid.test.util.JDBCUtils.executeQuery;
import static org.teiid.test.util.JDBCUtils.executeUpdate;
import static org.teiid.test.util.JDBCUtils.getDriverConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class MatVDBClient {
    
    private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:MatVDB@mm://localhost:31000;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";

    @SuppressWarnings("static-access")
    public static void main(String[] args) throws Exception {
        
        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        
        query(conn);
        
        executeUpdate(conn, "INSERT INTO PRODUCT (ID,SYMBOL,COMPANY_NAME) VALUES(2000,'RHT','Red Hat Inc')");
        Thread.currentThread().sleep(30000);
        query(conn);
        
        executeUpdate(conn, "DELETE FROM PRODUCT  WHERE ID = 2000");
        Thread.currentThread().sleep(30000);
        query(conn);
        
        close(conn);
    }

    private static void query(Connection conn) throws SQLException {

        executeQuery(conn, "select * from MatView");
        
        executeQuery(conn, "select * from h2_test_mat");
        
        executeQuery(conn, "select * from mat_test_staging");
        
        executeQuery(conn, "select * from status");
    }

}
