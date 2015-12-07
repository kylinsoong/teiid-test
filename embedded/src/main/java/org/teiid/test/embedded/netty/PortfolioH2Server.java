package org.teiid.test.embedded.netty;

import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.logging.Level;

import javax.sql.DataSource;

import org.h2.tools.RunScript;
import org.teiid.example.EmbeddedHelper;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;
import org.teiid.transport.SocketConfiguration;
import org.teiid.transport.WireProtocol;

public class PortfolioH2Server {

    public static void main(String[] args) throws Exception {
        
        EmbeddedHelper.enableLogger(Level.ALL);
        
        DataSource ds = EmbeddedHelper.newDataSource("org.h2.Driver", "jdbc:h2:mem://localhost/~/account", "sa", "sa");
        RunScript.execute(ds.getConnection(), new InputStreamReader(PortfolioH2Server.class.getClassLoader().getResourceAsStream("data/customer-schema.sql")));
        
        EmbeddedServer server = new EmbeddedServer();
        
        H2ExecutionFactory executionFactory = new H2ExecutionFactory() ;
        executionFactory.setSupportsDirectQueryProcedure(true);
        executionFactory.start();
        server.addTranslator("translator-h2", executionFactory);
        
        server.addConnectionFactory("java:/accounts-ds", ds);
        
        EmbeddedConfiguration config = new EmbeddedConfiguration();
        config.setTransactionManager(EmbeddedHelper.getTransactionManager());  
        
        config.setTimeSliceInMilli(Integer.MAX_VALUE);
        SocketConfiguration s = new SocketConfiguration();
        InetSocketAddress addr = new InetSocketAddress("localhost", 31000);
        s.setBindAddress(addr.getHostName());
        s.setPortNumber(addr.getPort());
        s.setProtocol(WireProtocol.teiid);
        config.addTransport(s);
        
        config.setSecurityDomain("teiid-security-file");
        config.setSecurityHelper(EmbeddedHelper.getSecurityHelper());
        
        server.start(config);
        
        server.deployVDB(PortfolioH2Server.class.getClassLoader().getResourceAsStream("portfolio-h2-vdb.xml"));
        
        Thread.sleep(Long.MAX_VALUE);
    }

}
