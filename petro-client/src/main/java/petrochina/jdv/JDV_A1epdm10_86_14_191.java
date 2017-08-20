package petrochina.jdv;

import static org.teiid.test.jdbc.client.JDBCUtils.*;

import java.sql.Connection;

public class JDV_A1epdm10_86_14_191 {
    
    private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:a1epdm10-86-14-191@mm://11.11.208.135:31100;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";

    public static void main(String[] args) throws Exception {


        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, JDBC_USER, JDBC_PASS);
        
        execute(conn, "SELECT COUNT(*) FROM A1ZJK.A1_DICTIONARY");
        execute(conn, "SELECT * FROM A1ZJK.A1_DICTIONARY");
        
        close(conn);
    }

}
