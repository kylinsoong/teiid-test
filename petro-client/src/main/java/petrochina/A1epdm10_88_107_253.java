package petrochina;

import static org.teiid.test.jdbc.client.JDBCUtils.close;
import static org.teiid.test.jdbc.client.JDBCUtils.getDriverConnection;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

import org.teiid.test.jdbc.client.util.ResultSetToCSVRender;

public class A1epdm10_88_107_253 {
    
    private static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String JDBC_URL = "jdbc:oracle:thin:@10.88.107.253:1521:a1epdm";
    private static final String JDBC_USER = "a1zjk";
    private static final String JDBC_PASS = "a1zjk135";

    public static void main(String[] args) throws Exception {
        
        args = new String[]{"table"};

        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        
        System.out.println(A1epdm10_88_107_253.class + " Connection is Created");
        
        DatabaseMetaData metadata = conn.getMetaData();

        if(args.length > 0 && args[0].equals("console")) {
            TablesMeta meta = new TablesMeta("A1epdm10_88_107_253");
            try(ResultSet tables = metadata.getTables(null, null, null, null);) {
                while(tables.next()) {
                    String schema = tables.getString("TABLE_SCHEM");
                    String name = tables.getString("TABLE_NAME");
                    String type = tables.getString("TABLE_TYPE");
                    meta.add(schema, name, type);
                }
                System.out.println(meta);
            }
        } else if(args.length > 0 && args[0].equals("table")) {
            TablesMeta meta = new TablesMeta("A1epdm10_88_107_253");
            try(ResultSet tables = metadata.getTables(null, "A1%", null, new String[] {"TABLE"});) {
                while(tables.next()) {
                    String schema = tables.getString("TABLE_SCHEM");
                    String name = tables.getString("TABLE_NAME");
                    String type = tables.getString("TABLE_TYPE");
                    meta.add(schema, name, type);
                }
                System.out.println(meta);
            }
        } else {
            try(
                    ResultSet tables = metadata.getTables(null, null, null, null);
                    ResultSetToCSVRender renderer = new ResultSetToCSVRender(tables, "a1epdm-10.88.107.253.csv");
                    ) {
                renderer.renderer();
            }
        }
        
        
        close(conn);
        
        System.out.println(A1epdm10_88_107_253.class + " exit");
    }

}
