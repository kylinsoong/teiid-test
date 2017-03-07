package org.teiid.test.temp;

import static org.teiid.test.utils.JDBCUtils.*;

import java.sql.Connection;
import java.util.logging.Level;

import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.test.utils.EmbeddedHelper;

public class LocalTempTable {
    
    static String SQL_TABLE_CREATE = "CREATE LOCAL TEMPORARY TABLE TEMP_A (a integer, b string)";
    static String SQL_INSERT = "INSERT INTO TEMP_A VALUES(100, 'Teiid')";
    static String SQL_SELECT = "SELECT * FROM TEMP_A";

    public static void main(String[] args) throws Exception {

        EmbeddedHelper.enableLogger(Level.INFO);
        
        
        EmbeddedServer server = new EmbeddedServer();
        
        EmbeddedConfiguration config = new EmbeddedConfiguration();
//        config.setTransactionManager(EmbeddedHelper.getTransactionManager());   
        server.start(config);
        
        server.deployVDB(LocalTempTable.class.getClassLoader().getResourceAsStream("empty-vdb.xml"));
        
        Connection conn = server.getDriver().connect("jdbc:teiid:Portfolio", null);
        
        execute(conn, SQL_TABLE_CREATE);
        
//        execute(conn, SQL_INSERT);
        
//        execute(conn, SQL_SELECT);
        
        close(conn);
    }

}
