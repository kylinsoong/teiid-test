package org.teiid.test.jdbc.client.mysql;

import static org.teiid.test.jdbc.client.JDBCUtils.close;
import static org.teiid.test.jdbc.client.JDBCUtils.getDriverConnection;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;

public class MetaDataTestClient {
    
    private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:MetadataFilterVDB@mm://localhost:31000;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";
    

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
