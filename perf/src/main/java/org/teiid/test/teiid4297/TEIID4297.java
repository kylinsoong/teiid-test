package org.teiid.test.teiid4297;

import static org.teiid.test.util.JDBCUtils.*;

import java.sql.Connection;
import java.util.logging.Level;

import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.test.util.EmbeddedHelper;

public class TEIID4297 {

    public static void main(String[] args) throws Exception {

        EmbeddedHelper.enableLogger(Level.INFO);
        
        EmbeddedServer server = new EmbeddedServer();
        
        EmbeddedConfiguration config = new EmbeddedConfiguration();
        config.setTimeSliceInMilli(Integer.MAX_VALUE);
        server.start(config);
        
        server.deployVDB(TEIID4297.class.getClassLoader().getResourceAsStream("empty-vdb.xml"));
        
        Connection conn = server.getDriver().connect("jdbc:teiid:EmptyVDB", null);
        
        execute(conn, "SELECT random('123456789') AS random, hash('123456789') AS hash, digit('123ads4sdf56sfg7fr8~!@9') AS digit", false);
        
        close(conn);
    }

}
