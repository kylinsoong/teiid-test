package org.teiid.test.jdbc.client;

import static org.teiid.test.jdbc.client.JDBCUtils.execute;
import static org.teiid.test.jdbc.client.JDBCUtils.getDriverConnection;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class Case01731853 {
    
    private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:Portfolio@mm://localhost:31000;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";

    @SuppressWarnings("static-access")
    public static void main(String[] args) throws Exception {

        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        
        List<Connection> list = new ArrayList<>();
        list.add(conn);
        
        
//        int index = 0;
//        while(index <= 14) {
//            Thread.currentThread().sleep(1000 * 60);
//            System.out.println(index ++);
//        }
        
        execute(conn, "SELECT * FROM SYSADMIN.MatViews", true);
    }

}
