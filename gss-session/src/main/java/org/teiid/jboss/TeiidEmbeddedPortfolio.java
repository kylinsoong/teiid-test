package org.teiid.jboss;

import java.sql.Connection;

import org.teiid.resource.adapter.file.FileManagedConnectionFactory;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.file.FileExecutionFactory;

public class TeiidEmbeddedPortfolio {

    static String SQL_1 = "SELECT * from StockPrices";

    public static void main(String[] args) throws Exception {

       
        EmbeddedServer server = new EmbeddedServer();
        
        FileExecutionFactory fileExecutionFactory = new FileExecutionFactory();
        fileExecutionFactory.start();
        server.addTranslator("file", fileExecutionFactory);
        
        FileManagedConnectionFactory managedconnectionFactory = new FileManagedConnectionFactory();
        managedconnectionFactory.setParentDirectory("src/main/resources/data");
        server.addConnectionFactory("java:/marketdata-file", managedconnectionFactory.createConnectionFactory());

        server.start(new EmbeddedConfiguration());
        
//        server.deployVDB(TeiidEmbeddedPortfolio.class.getClassLoader().getResourceAsStream("portfolio-vdb.xml"));
        server.deployVDB(TeiidEmbeddedPortfolio.class.getClassLoader().getResourceAsStream("portfolio.vdb"));
        
        Connection conn = server.getDriver().connect("jdbc:teiid:Portfolio", null);
        for(int i = 0 ; i < 1 ; i ++)
            JDBCUtils.execute(conn, SQL_1, true);
    }

}
