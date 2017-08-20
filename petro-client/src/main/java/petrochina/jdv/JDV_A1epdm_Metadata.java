package petrochina.jdv;

import static org.teiid.test.jdbc.client.JDBCUtils.*;

import java.sql.Connection;

public class JDV_A1epdm_Metadata {
    
    private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:a1epdmvdb@mm://11.11.208.135:31100;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";

    public static void main(String[] args) throws Exception {


        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        
        execute(conn, "SELECT * FROM a1epdm_10_76_32_26.A1ZJK.CD_ACTIVITY");
        
        close(conn);
    }
    
    

}
