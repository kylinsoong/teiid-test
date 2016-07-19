package org.teiid.test.teiid3398;

import static org.teiid.test.Constants.H2_JDBC_DRIVER;
import static org.teiid.test.Constants.H2_JDBC_PASS;
import static org.teiid.test.Constants.H2_JDBC_URL;
import static org.teiid.test.Constants.H2_JDBC_USER;

import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.logging.Level;

import javax.sql.DataSource;

import org.h2.tools.RunScript;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.test.util.EmbeddedHelper;
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;
import org.teiid.transport.SocketConfiguration;
import org.teiid.transport.WireProtocol;

public class TEIID3398ReproduceServer {

    public static void main(String[] args) throws Exception {

        EmbeddedHelper.enableLogger(Level.ALL);
        
        DataSource ds = EmbeddedHelper.newDataSource(H2_JDBC_DRIVER, H2_JDBC_URL, H2_JDBC_USER, H2_JDBC_PASS);
        RunScript.execute(ds.getConnection(), new InputStreamReader(TEIID3398ReproduceServer.class.getClassLoader().getResourceAsStream("teiid-3398/h2-schema.sql")));
        
        
        EmbeddedServer server = new EmbeddedServer();
        
        H2ExecutionFactory factory = new H2ExecutionFactory();
        factory.start();
        factory.setSupportsDirectQueryProcedure(true);
        server.addTranslator("translator-h2", factory);
        
        server.addConnectionFactory("java:/accounts-ds", ds);
        
        EmbeddedConfiguration config = new EmbeddedConfiguration();
        config.setTimeSliceInMilli(Integer.MAX_VALUE);
        SocketConfiguration s = new SocketConfiguration();
        InetSocketAddress addr = new InetSocketAddress("localhost", 31000);
        s.setBindAddress(addr.getHostName());
        s.setPortNumber(addr.getPort());
        s.setProtocol(WireProtocol.teiid);
        config.addTransport(s);
        server.start(config);
                
        server.deployVDB(TEIID3398ReproduceServer.class.getClassLoader().getResourceAsStream("teiid-3398/teiid3398-h2-vdb.xml"));
        
        Thread.sleep(Long.MAX_VALUE);
       
    }

}
