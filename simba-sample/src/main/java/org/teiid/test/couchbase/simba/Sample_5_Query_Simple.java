package org.teiid.test.couchbase.simba;

import static org.teiid.test.couchbase.simba.SampleProperties.JDBC4_DRIVER;
import static org.teiid.test.couchbase.simba.SampleProperties.JDBC_URL_TEST;
import static org.teiid.test.couchbase.simba.util.JDBCUtils.close;
import static org.teiid.test.couchbase.simba.util.JDBCUtils.execute;
import static org.teiid.test.couchbase.simba.util.JDBCUtils.getDriverConnection;

import java.sql.Connection;
import java.util.Arrays;

public class Sample_5_Query_Simple {
    
    static String[] queries = new String[] {
 
        "SELECT * FROM default",
        "SELECT * FROM default_nestedArray",
        "SELECT * FROM default_nestedArray_dim2",
        "SELECT * FROM default_nestedArray_dim2_dim3",
        "SELECT * FROM default_nestedArray_dim2_dim3_dim4",
        
//        "SELECT * FROM Customer",
//        "SELECT * FROM Customer_SavedAddresses",
//        "SELECT * FROM Oder",
//        "SELECT * FROM Oder_Items"
    };

    public static void main(String[] args) throws Exception {
        
        Connection conn = getDriverConnection(JDBC4_DRIVER, JDBC_URL_TEST);
        
//        Arrays.asList(queries).forEach(sql -> {
//            try {
//                execute(conn, sql);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
        
        execute(conn, queries[4]);
        
        close(conn);
    }

}
