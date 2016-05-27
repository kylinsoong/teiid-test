package org.teiid.test.teiid3509;

import static org.teiid.test.util.JDBCUtils.execute;

import java.sql.Connection;
import java.util.Properties;
import java.util.logging.Level;

import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.test.util.EmbeddedHelper;

public class ReservedWordTest {
    
    static EmbeddedServer server = null;
    static Connection conn = null;

    public static void main(String[] args) throws Exception {

        EmbeddedHelper.enableLogger(Level.WARNING);      
        
        server = new EmbeddedServer();

        server.start(new EmbeddedConfiguration());
                
        server.deployVDB(ReservedWordTest.class.getClassLoader().getResourceAsStream("teiid-3509/teiid3509-h2-vdb.xml"));
        
        Properties info = new Properties();
        conn = server.getDriver().connect("jdbc:teiid:ReservedWordH2VDB", info);
        
        execute(conn, "SELECT * FROM v1", true);
    }

}
