package org.teiid.test.couchbase.simba;

import static org.teiid.test.couchbase.simba.util.JDBCUtils.*;
import static org.teiid.test.couchbase.simba.SampleProperties.JDBC4_DRIVER;
import static org.teiid.test.couchbase.simba.SampleProperties.JDBC_URL;

import java.sql.Connection;

public class Sample_1 {

    public static void main(String[] args) throws Exception {

        Connection conn = getDriverConnection(JDBC4_DRIVER, JDBC_URL);
        
        System.out.println(conn);
        
        close(conn);
    }

}
