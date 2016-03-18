package org.teiid.jboss;

import java.sql.Connection;

import org.teiid.resource.adapter.file.FileManagedConnectionFactory;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.runtime.EmbeddedServer;
import org.teiid.translator.file.FileExecutionFactory;

public class Case01593403 {

    public static void main(String[] args) throws Exception {
        
        EmbeddedServer server = new EmbeddedServer();

        FileExecutionFactory fileExecutionFactory = new FileExecutionFactory();
        fileExecutionFactory.start();
        server.addTranslator("file", fileExecutionFactory);

        FileManagedConnectionFactory mcf1 = new FileManagedConnectionFactory();
        mcf1.setParentDirectory("/home/kylin/tmp/highcpu/long-running-queries");
        server.addConnectionFactory("java:/csv1-file", mcf1.createConnectionFactory());
        
        FileManagedConnectionFactory mcf2 = new FileManagedConnectionFactory();
        mcf2.setParentDirectory("/home/kylin/tmp/highcpu/long-running-queries");
        server.addConnectionFactory("java:/csv2-file", mcf2.createConnectionFactory());

        server.start(new EmbeddedConfiguration());

        server.deployVDB(Case01593403.class.getClassLoader().getResourceAsStream("highcpu-vdb.xml"));

        Connection conn = server.getDriver().connect("jdbc:teiid:cancelLongRunningQueries", null);
        
//        JDBCUtils.execute(conn, "SELECT COUNT(*) AS count FROM ViewModel.csv1 AS a, ViewModel.csv2 AS b", true);


        JDBCUtils.execute(conn, "SELECT COUNT(*) AS count FROM combinedcsv", true);
        
    }

}
