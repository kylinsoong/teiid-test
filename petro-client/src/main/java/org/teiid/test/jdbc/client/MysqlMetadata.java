package org.teiid.test.jdbc.client;

import static org.teiid.test.jdbc.client.JDBCUtils.*;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

import org.teiid.test.jdbc.client.util.ResultSetToCSVRender;

public class MysqlMetadata {

    
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/accounts";
    private static final String JDBC_USER = "jdv_user";
    private static final String JDBC_PASS = "jdv_pass";
    
    public static void main(String[] args) throws Exception {
        
        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        DatabaseMetaData metadata = conn.getMetaData();
        
        try(
                ResultSet tables = metadata.getTables(null, null, null, null);
                ResultSetToCSVRender renderer = new ResultSetToCSVRender(tables, "sample.csv");
                ) {
            renderer.renderer();
        }
        
        close(conn);
        
    }

}
