package org.teiid.test.teiid4441;

import static org.teiid.test.utils.JDBCUtils.close;
import static org.teiid.test.utils.JDBCUtils.execute;

import java.sql.Connection;
import java.util.logging.Level;

import org.teiid.resource.adapter.file.FileManagedConnectionFactory;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.test.utils.EmbeddedHelper;
import org.teiid.translator.file.FileExecutionFactory;

public class TEIID4441 {

    public static void main(String[] args) throws Exception {
        
        EmbeddedHelper.enableLogger(Level.ALL);
        
        EmbeddedServer server = new EmbeddedServer();

        FileExecutionFactory fileExecutionFactory = new FileExecutionFactory();
        fileExecutionFactory.start();
        server.addTranslator("file", fileExecutionFactory);

        FileManagedConnectionFactory managedconnectionFactory = new FileManagedConnectionFactory();
        managedconnectionFactory.setParentDirectory("/home/kylin/tmp/4441");
        server.addConnectionFactory("java:/marketdata-file", managedconnectionFactory.createConnectionFactory());
        
        server.start(new EmbeddedConfiguration());
        
        server.deployVDB(TEIID4441.class.getClassLoader().getResourceAsStream("teiid4441-vdb.xml"));

        Connection conn = server.getDriver().connect("jdbc:teiid:Portfolio", null);
        
        execute(conn, "EXEC getTextFiles('*.txt')", false);
//        execute(conn, "EXEC getFiles('*.txt')", false); 
        
        close(conn);
    }

}
