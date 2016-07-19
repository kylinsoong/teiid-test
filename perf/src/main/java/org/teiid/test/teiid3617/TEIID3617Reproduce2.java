package org.teiid.test.teiid3617;

import static org.teiid.test.Constants.H2_JDBC_DRIVER;
import static org.teiid.test.Constants.H2_JDBC_PASS;
import static org.teiid.test.Constants.H2_JDBC_URL;
import static org.teiid.test.Constants.H2_JDBC_USER;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;

import javax.resource.ResourceException;
import javax.sql.DataSource;

import org.h2.tools.RunScript;
import org.teiid.deployers.VirtualDatabaseException;
import org.teiid.dqp.internal.datamgr.ConnectorManagerRepository.ConnectorManagerException;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.test.util.EmbeddedHelper;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;

public class TEIID3617Reproduce2 {

    public static void main(String[] args) throws ResourceException, SQLException, TranslatorException, VirtualDatabaseException, ConnectorManagerException, IOException {

        EmbeddedHelper.enableLogger(Level.INFO);
        
        DataSource ds = EmbeddedHelper.newDataSource(H2_JDBC_DRIVER, H2_JDBC_URL, H2_JDBC_USER, H2_JDBC_PASS);
        RunScript.execute(ds.getConnection(), new InputStreamReader(TEIID3617Reproduce.class.getClassLoader().getResourceAsStream("teiid-3617/h2-schema.sql")));
        
        
        EmbeddedServer server = new EmbeddedServer();
        
        H2ExecutionFactory factory = new H2ExecutionFactory();
        factory.start();
        factory.setSupportsDirectQueryProcedure(true);
        server.addTranslator("translator-h2", factory);
        
        server.addConnectionFactory("java:/accounts-ds", ds);
        
        EmbeddedConfiguration config = new EmbeddedConfiguration();
        config.setTimeSliceInMilli(Integer.MAX_VALUE);
        server.start(config);
                
        server.deployVDB(TEIID3617Reproduce2.class.getClassLoader().getResourceAsStream("teiid-3617/teiid3617-h2-vdb.xml"));
        server.deployVDB(TEIID3617Reproduce2.class.getClassLoader().getResourceAsStream("teiid-3617/teiid3617-h2-vdb.1.xml"));
        server.deployVDB(TEIID3617Reproduce2.class.getClassLoader().getResourceAsStream("teiid-3617/teiid3617-h2-vdb.2.xml"));
        
        server.undeployVDB("TEIID3617");
        server.undeployVDB("TEIID3617_1");
        server.undeployVDB("TEIID3617_2");
        
//        Connection conn = server.getDriver().connect("jdbc:teiid:TEIID3617", null);
//        
//        System.out.println(conn);
        
    }

}
