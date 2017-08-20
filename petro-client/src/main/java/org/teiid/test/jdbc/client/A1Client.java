package org.teiid.test.jdbc.client;

import static org.teiid.test.jdbc.client.JDBCUtils.*;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

import org.teiid.test.jdbc.client.util.ResultSetToCSVRender;

public class A1Client {
    
    private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:a1epdm10-88-107-253@mm://localhost:31000;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";

    public static void main(String[] args) throws Exception {


        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
//        DatabaseMetaData metadata = conn.getMetaData();
//        try(
//                ResultSet tables = metadata.getTables(null, null, null, null);
//                ResultSetToCSVRender renderer = new ResultSetToCSVRender(tables, "a1epdm-10.88.107.253-teiid.csv");
//                ) {
//            renderer.renderer();
//        }
        
        execute(conn, "SELECT count(*) FROM a1epdm_10_88_107_253.A1ZJK.CD_ORGANIZATION");
        close(conn);
    }

}
