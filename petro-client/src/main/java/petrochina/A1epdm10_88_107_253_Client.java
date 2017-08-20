package petrochina;

import static org.teiid.test.jdbc.client.JDBCUtils.*;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

import org.teiid.test.jdbc.client.util.ResultSetRenderer;

public class A1epdm10_88_107_253_Client {
    
    private static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String JDBC_URL = "jdbc:oracle:thin:@10.88.107.253:1521:a1epdm";
    private static final String JDBC_USER = "a1zjk";
    private static final String JDBC_PASS = "a1zjk135";

    public static void main(String[] args) throws Exception {

        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        
//        execute(conn, "SELECT * FROM A1ZJK.CJJSCG2013 where rownum < 10");
        
        DatabaseMetaData metadata = conn.getMetaData();
//        ResultSet indexInfo = metadata.getIndexInfo(null, "A1ZJK", "DH_OPS_JOB_DAILY", true, true);
        
//        try(
//                ResultSet tables = metadata.getTables(null, "APEX_030200", null, null);
//                ResultSetRenderer renderer = new ResultSetRenderer(tables);
//                ) {
//            renderer.renderer();
//        }
        
//        try(
//                ResultSet tables = metadata.getTables(null, "BI", null, null);
//                ResultSetRenderer renderer = new ResultSetRenderer(tables);
//                ) {
//            renderer.renderer();
//        }
        
        System.out.println("DONE");
//        
        close(conn);
    }

}
