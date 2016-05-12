package org.teiid.test.teiid4201;

import java.sql.Connection;

import org.teiid.test.util.JDBCUtils;


public class TEIID4201ReproduceClient {
    
    public static void main(String[] args) throws Exception {
        
        Connection conn = JDBCUtils.getDriverConnection("org.teiid.jdbc.TeiidDriver", "jdbc:teiid:apm_public@mm://localhost:54321", "user", "pass");
    
//        System.out.println(conn);
        
        JDBCUtils.execute(conn, "select * from public.share_market_data where frequency=5000 and ts between {ts '2016-04-08 01:00:00.0'} and {ts '2016-04-09 13:00:00.0'}", false);
        
        JDBCUtils.close(conn);
    }

}
