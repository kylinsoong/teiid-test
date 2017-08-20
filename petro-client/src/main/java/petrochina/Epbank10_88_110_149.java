package petrochina;

import static org.teiid.test.jdbc.client.JDBCUtils.close;
import static org.teiid.test.jdbc.client.JDBCUtils.getDriverConnection;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

import org.teiid.test.jdbc.client.util.ResultSetToCSVRender;

public class Epbank10_88_110_149 {
    
    private static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String JDBC_URL = "jdbc:oracle:thin:@10.88.110.149:1521:orcl";
    private static final String JDBC_USER = "meta_dm";
    private static final String JDBC_PASS = "meta_dm";

    public static void main(String[] args) throws Exception {

        args = new String[]{"console"};

        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        
        DatabaseMetaData metadata = conn.getMetaData();
        
        if(args.length > 0 && args[0].equals("console")) {
            TablesMeta meta = new TablesMeta("Epbank10_88_110_149");
            try(ResultSet tables = metadata.getTables(null, null, null, null);) {
                while(tables.next()) {
                    String schema = tables.getString("TABLE_SCHEM");
                    String name = tables.getString("TABLE_NAME");
                    String type = tables.getString("TABLE_TYPE");
                    meta.add(schema, name, type);
                }
                System.out.println(meta);
            }
        } else if(args.length > 0 && args[0].equals("console")) {
            TablesMeta meta = new TablesMeta("Epbank10_88_110_149");
            try(ResultSet tables = metadata.getTables(null, null, null, null);) {
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
                    ResultSetToCSVRender renderer = new ResultSetToCSVRender(tables, "epbank-10.88.110.149.csv");
                    ) {
                renderer.renderer();
            }
        }
        
        close(conn);
        
        System.out.println(Epbank10_88_110_149.class + " exit");
    }

}
