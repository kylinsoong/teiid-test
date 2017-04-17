package org.teiid.test.couchbase.simba;

import static org.teiid.test.couchbase.simba.SampleProperties.JDBC4_DRIVER;
import static org.teiid.test.couchbase.simba.SampleProperties.JDBC_URL_TEST;
import static org.teiid.test.couchbase.simba.util.JDBCUtils.close;
import static org.teiid.test.couchbase.simba.util.JDBCUtils.execute;
import static org.teiid.test.couchbase.simba.util.JDBCUtils.getDriverConnection;

import java.sql.Connection;
import java.util.Arrays;

public class Sample_5_Query_CustomerOrder {
    
    static String[] queries = new String[] {
        "SELECT * FROM Customer",
        "SELECT * FROM Customer_SavedAddresses",
        "SELECT * FROM Oder",
        "SELECT * FROM Oder_Items"
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
        
//        execute(conn, "UPDATE Customer SET Name = 'John Doe' WHERE PK = 'customer-5'");
        
//        execute(conn, "UPDATE Oder SET CreditCard_CVN = 786 WHERE PK = 'order-2'");

        execute(conn, "UPDATE default_nestedArray_dim2_dim3_dim4 SET default_nestedArray_dim2_dim3_dim4 = 'Hello Teiid' WHERE documentID = 'nestedArray' AND default_nestedArray_idx = 1 AND default_nestedArray_dim2_idx = 1 AND default_nestedArray_dim2_dim3_idx = 1 AND default_nestedArray_dim2_dim3_dim4_idx = 3");
        
        close(conn);
    }

}
