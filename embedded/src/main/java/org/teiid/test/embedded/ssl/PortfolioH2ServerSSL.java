package org.teiid.test.embedded.ssl;

import java.io.InputStreamReader;
import java.net.InetSocketAddress;

import javax.net.ssl.KeyManagerFactory;
import javax.sql.DataSource;

import org.h2.tools.RunScript;
import org.teiid.example.EmbeddedHelper;
import org.teiid.logging.LogManager;
import org.teiid.net.socket.SocketUtil;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.runtime.JBossLogger;
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;
import org.teiid.transport.SSLConfiguration;
import org.teiid.transport.SocketConfiguration;
import org.teiid.transport.WireProtocol;

public class PortfolioH2ServerSSL {
    
    static {
        LogManager.setLogListener(new JBossLogger());
        System.setProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager");
    }

    public static void main(String[] args) throws Exception {
        
//        LoggingHelper.enableLogger(Level.ALL);
        
        DataSource ds = EmbeddedHelper.newDataSource("org.h2.Driver", "jdbc:h2:mem://localhost/~/account", "sa", "sa");
        RunScript.execute(ds.getConnection(), new InputStreamReader(PortfolioH2ServerSSL.class.getClassLoader().getResourceAsStream("data/customer-schema.sql")));
        
        EmbeddedServer server = new EmbeddedServer();
        
        H2ExecutionFactory executionFactory = new H2ExecutionFactory() ;
        executionFactory.setSupportsDirectQueryProcedure(true);
        executionFactory.start();
        server.addTranslator("translator-h2", executionFactory);
        
        server.addConnectionFactory("java:/accounts-ds", ds);
        
        EmbeddedConfiguration config = new EmbeddedConfiguration();
        config.setTransactionManager(EmbeddedHelper.getTransactionManager());  
        
        config.setTimeSliceInMilli(Integer.MAX_VALUE);
 
        SocketConfiguration socketConfiguration = new SocketConfiguration();
 
        InetSocketAddress addr = new InetSocketAddress("localhost", 31000);
        socketConfiguration.setBindAddress(addr.getHostName());
        socketConfiguration.setPortNumber(addr.getPort());
        socketConfiguration.setProtocol(WireProtocol.teiid);
        
        SSLConfiguration sslConfiguration = new SSLConfiguration();
        sslConfiguration.setMode(SSLConfiguration.ENABLED);
        sslConfiguration.setAuthenticationMode(SSLConfiguration.ONEWAY);
        sslConfiguration.setSslProtocol(SocketUtil.DEFAULT_PROTOCOL);
        sslConfiguration.setKeymanagementAlgorithm(KeyManagerFactory.getDefaultAlgorithm());
        sslConfiguration.setEnabledCipherSuites("SSL_RSA_WITH_RC4_128_MD5,SSL_RSA_WITH_RC4_128_SHA");
        sslConfiguration.setKeystoreFilename("ssl-example.keystore");
        sslConfiguration.setKeystorePassword("redhat");
        sslConfiguration.setKeystoreType("JKS");
        sslConfiguration.setKeystoreKeyAlias("teiid");
        sslConfiguration.setKeystoreKeyPassword("redhat");
        sslConfiguration.setTruststoreFilename("ssl-example.truststore");
        sslConfiguration.setTruststorePassword("redhat");
        socketConfiguration.setSSLConfiguration(sslConfiguration);
        
        config.addTransport(socketConfiguration);
        
        config.setSecurityDomain("teiid-security-file");
        config.setSecurityHelper(EmbeddedHelper.getSecurityHelper());
        
        server.start(config);
        
        server.deployVDB(PortfolioH2ServerSSL.class.getClassLoader().getResourceAsStream("portfolio-h2-vdb.xml"));
        
        Thread.sleep(Long.MAX_VALUE);
    }

}
