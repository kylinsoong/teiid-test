package org.teiid.test.jdbc.client.mysql;

import static org.teiid.test.jdbc.client.JDBCUtils.*;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;

public class MysqlCLient {
    
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/accounts";
    private static final String JDBC_USER = "jdv_user";
    private static final String JDBC_PASS = "jdv_pass";

    public static void main(String[] args) throws Exception {

        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        DatabaseMetaData metadata = conn.getMetaData();
        
        String table[] = { "TABLE" };
        ResultSet rs = null;
        ArrayList<String> tables = null;
        rs = metadata.getTables(null, null, null, table);
        tables = new ArrayList<>();
        while (rs.next()) {
            tables.add(rs.getString("TABLE_NAME"));
        }
        System.out.println(tables);
        
        close(conn);
    }

}
