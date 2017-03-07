package org.teiid.test.logging;

import static org.teiid.test.utils.JDBCUtils.close;
import static org.teiid.test.utils.JDBCUtils.execute;

import java.sql.Connection;
import java.util.logging.Level;

import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.test.utils.EmbeddedHelper;

public class TestLogging {

    static String SQL_TABLE_CREATE = "CREATE LOCAL TEMPORARY TABLE TEMP (a integer, b string)";
    static String SQL_INSERT = "INSERT INTO TEMP VALUES(100, 'Teiid')";
    static String SQL_SELECT = "SELECT * FROM TEMP";

    public static void main(String[] args) throws Exception {

        EmbeddedHelper.enableLogger(Level.ALL);
                
        EmbeddedServer server = new EmbeddedServer();
        
        EmbeddedConfiguration config = new EmbeddedConfiguration();
        config.setTransactionManager(EmbeddedHelper.getTransactionManager());   
        server.start(config);
        
        server.deployVDB(TestLogging.class.getClassLoader().getResourceAsStream("empty-vdb.xml"));
        
        Connection conn = server.getDriver().connect("jdbc:teiid:Portfolio", null);
        
        execute(conn, SQL_TABLE_CREATE);
        
        execute(conn, SQL_INSERT);
        
        execute(conn, SQL_SELECT);
        
        close(conn);
    }

}
