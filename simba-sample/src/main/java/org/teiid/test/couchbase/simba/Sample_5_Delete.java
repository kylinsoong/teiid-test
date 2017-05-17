package org.teiid.test.couchbase.simba;

import static org.teiid.test.couchbase.simba.SampleProperties.JDBC4_DRIVER;
import static org.teiid.test.couchbase.simba.SampleProperties.JDBC_URL_TEST;
import static org.teiid.test.couchbase.simba.util.JDBCUtils.close;
import static org.teiid.test.couchbase.simba.util.JDBCUtils.execute;
import static org.teiid.test.couchbase.simba.util.JDBCUtils.getDriverConnection;

import java.sql.Connection;

public class Sample_5_Delete {

    public static void main(String[] args) throws Exception {

        Connection conn = getDriverConnection(JDBC4_DRIVER, JDBC_URL_TEST);

//      execute(conn, "DELETE FROM Customer WHERE PK = 'customer-5'");
//      execute(conn, "DELETE FROM Customer WHERE ID = 'Customer_10000'");
        
//        execute(conn, "DELETE FROM default_nestedArray_dim2_dim3_dim4 WHERE PK = 'nestedArray' AND default_nestedArray_idx = 1 AND default_nestedArray_dim2_idx = 1 AND default_nestedArray_dim2_dim3_idx = 1 AND default_nestedArray_dim2_dim3_dim4_idx = 3 AND default_nestedArray_dim2_dim3_dim4 = 'Hello World'");
      
//        execute(conn, "DELETE FROM default_nestedArray_dim2_dim3_dim4 WHERE PK = 'nestedArray' AND default_nestedArray_idx = 2");
        
        execute(conn, "DELETE FROM Customer WHERE PK = 'customer-5' AND ID = 'Customer_10000' AND type = 'Customer'");
        
        
      close(conn);
    }

}
