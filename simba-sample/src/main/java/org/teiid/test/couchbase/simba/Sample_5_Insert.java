package org.teiid.test.couchbase.simba;

import static org.teiid.test.couchbase.simba.SampleProperties.JDBC4_DRIVER;
import static org.teiid.test.couchbase.simba.SampleProperties.JDBC_URL_TEST;
import static org.teiid.test.couchbase.simba.util.JDBCUtils.close;
import static org.teiid.test.couchbase.simba.util.JDBCUtils.execute;
import static org.teiid.test.couchbase.simba.util.JDBCUtils.getDriverConnection;

import java.sql.Connection;

public class Sample_5_Insert {

    public static void main(String[] args) throws Exception {

        Connection conn = getDriverConnection(JDBC4_DRIVER, JDBC_URL_TEST);

//        execute(conn, "INSERT INTO Customer (PK, ID, Name, type) values ('customer-3', 'Customer_12346', 'Kylin Soong', 'Customer')");
        execute(conn, "INSERT INTO Customer_SavedAddresses(PK, Customer_SavedAddresses_idx, Customer_SavedAddresses) VALUES ('customer-3', 2, 'Beijing')");
        
        close(conn);
    }

}
