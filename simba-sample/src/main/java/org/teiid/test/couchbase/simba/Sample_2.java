package org.teiid.test.couchbase.simba;

import static org.teiid.test.couchbase.simba.SampleProperties.JDBC4_DRIVER;
import static org.teiid.test.couchbase.simba.SampleProperties.JDBC_URL_TEST;
import static org.teiid.test.couchbase.simba.util.JDBCUtils.close;
import static org.teiid.test.couchbase.simba.util.JDBCUtils.getDriverConnection;

import java.sql.Connection;

public class Sample_2 {

    public static void main(String[] args) throws Exception {

        Connection conn = getDriverConnection(JDBC4_DRIVER, JDBC_URL_TEST);
        
        System.out.println(conn);
        
        close(conn);
    }

}
