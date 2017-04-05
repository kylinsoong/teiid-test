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
        "SELECT * FROM Oder_Items",
        "SELECT * FROM Customer INNER JOIN Oder ON Customer.ID = Oder.CustomerID"
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
        
        execute(conn, "SELECT Name, type  FROM Customer WHERE Name = 'John Doe'");
//        execute(conn, queries[2]);

        close(conn);
    }

}
