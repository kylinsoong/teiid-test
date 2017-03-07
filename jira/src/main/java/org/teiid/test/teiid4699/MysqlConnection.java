package org.teiid.test.teiid4699;

import static org.teiid.test.utils.JDBCUtils.*;

import java.sql.Connection;

public class MysqlConnection {
    
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String JDBC_URL = "jdbc:mysql://10.66.192.120:3306/teiid";
    private static final String JDBC_USER = "teiid_user";
    private static final String JDBC_PASS = "teiid_pass";

    public static void main(String[] args) throws Exception {

        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        
        execute(conn, "SELECT * FROM skills");
        
        close(conn);
        
    }

}
