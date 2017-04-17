package org.teiid.test.couchbase.simba;

import static org.teiid.test.couchbase.simba.SampleProperties.JDBC4_DRIVER;
import static org.teiid.test.couchbase.simba.SampleProperties.JDBC_URL_TEST;
import static org.teiid.test.couchbase.simba.util.JDBCUtils.*;

import java.sql.Connection;
import java.util.Arrays;

public class Sample_4_Query {
    
    static String[] queries = new String[] {
        "SELECT * FROM beer",
        "SELECT * FROM brewery",
        "SELECT * FROM brewery_address",
        "SELECT * FROM default",
        "SELECT * FROM default_A",
        "SELECT * FROM default_A_dim2",
        "SELECT * FROM default_A_dim2_dim3",
        "SELECT * FROM default_Items",
        "SELECT * FROM default_SavedAddresses",
        "SELECT * FROM default_attr_jsonArray",
        "SELECT * FROM default_attr_jsonArray_attr_jsonArray_dim2",
        "SELECT * FROM test",
        "SELECT * FROM test_Items",
        "SELECT * FROM test_SavedAddresses",
        "SELECT * FROM airline",
        "SELECT * FROM airport",
        "SELECT * FROM hotel",
        "SELECT * FROM hotel_public_likes",
        "SELECT * FROM hotel_reviews",
        "SELECT * FROM landmark",
        "SELECT * FROM route",
        "SELECT * FROM route_schedule"
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
        
//        execute(conn, "SELECT * FROM default_nestedArray_dim2_dim3_dim4");
        execute(conn, "INSERT INTO default_nestedArray_dim2_dim3_dim4 (PK, default_nestedArray_idx, default_nestedArray_dim2_idx, default_nestedArray_dim2_dim3_idx, default_nestedArray_dim2_dim3_dim4_idx, default_nestedArray_dim2_dim3_dim4) VALUES ('nestedArray', 1, 1, 1, 1, 'Hello World')");
        
        close(conn);
    }

}
