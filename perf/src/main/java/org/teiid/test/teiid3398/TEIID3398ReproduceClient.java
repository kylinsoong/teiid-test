package org.teiid.test.teiid3398;

import static org.teiid.test.util.JDBCUtils.execute;
import static org.teiid.test.util.JDBCUtils.getDriverConnection;
import static org.teiid.test.util.JDBCUtils.close;

import java.sql.Connection;
import java.util.Properties;
import java.util.logging.Level;

import org.teiid.test.util.EmbeddedHelper;

public class TEIID3398ReproduceClient {
    
    private static final String JDBC_DRIVER = "org.teiid.jdbc.TeiidDriver";
    private static final String JDBC_URL = "jdbc:teiid:TEIID-3398@mm://localhost:31000;version=1";
    private static final String JDBC_USER = "teiidUser";
    private static final String JDBC_PASS = "password1!";

    public static void main(String[] args) throws Exception {
        
        EmbeddedHelper.enableLogger(Level.INFO);
        
        Properties info = new Properties();
        info.setProperty("FetchSize", "2");
        info.put("user", JDBC_USER);
        info.put("password", JDBC_PASS);
        
        Connection conn = getDriverConnection(JDBC_DRIVER, JDBC_URL, info);
        
        execute(conn, "SELECT * FROM share_market_data", false);

        
        close(conn);
    }

}
