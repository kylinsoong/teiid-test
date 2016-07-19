package org.teiid.test.teiid3617;

import static org.teiid.test.util.JDBCUtils.execute;
import static org.teiid.test.util.JDBCUtils.getDriverConnection;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.teiid.jdbc.TeiidSQLException;

public class TestCient {
    
    private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:TestVDB@mm://localhost:31000;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";

    public static void main(String[] args) throws Exception {

//        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);     
//        execute(conn, "SELECT * FROM SYSADMIN.MatViews", true);
        
        List<Connection> list = new ArrayList<>(); 
        list.add(getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS));
        list.add(getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS));
//        list.add(getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS));
        
//        try {
//            list.add(getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS));
//        } catch (TeiidSQLException e) {
//            System.out.println(e.getMessage());
//        }
//        
//        for(Connection conn : list) {
//            conn.close();
//        }
//        
//        list.add(getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS));
//        list.add(getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS));
//        list.add(getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS));
        
        Thread.sleep(Long.MAX_VALUE);
        
        for(Connection conn : list) {
            if(!conn.isClosed()){
                conn.close();
            }
        }
    }

}
