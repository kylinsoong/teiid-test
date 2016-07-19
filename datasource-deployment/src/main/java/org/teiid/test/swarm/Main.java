package org.teiid.test.swarm;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.datasources.DatasourceArchive;

public class Main {

    public static void main(String[] args) throws Exception {    	
        
        Container container = new Container();
        
        container.start();
        
        // add jdbc driver
        container.deploy(Swarm.artifact("com.h2database:h2", "h2"));
        
        // add datasource
        DatasourceArchive dsArchive = ShrinkWrap.create(DatasourceArchive.class);
        dsArchive.dataSource("ExampleDS", (ds) -> {
            ds.connectionUrl("jdbc:h2:mem:test-swarm;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
            ds.driverName("h2");
            ds.userName("sa");
            ds.password("sa");
        });
        
//        DataSource<?> ds = new DataSource("ExampleDS");
//        ds.driverName("h2");
//        ds.connectionUrl("jdbc:h2:mem:test-swarm;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
//        ds.userName("sa");
//        ds.password("sa");
//        dsArchive.dataSource(ds);
        
        container.deploy(dsArchive);
        
//        System.out.println("----------");
//        
//        new Thread(() -> {
//            while(true){
//                try {
//                    Thread.currentThread().sleep(1000);
//                    Context ctx = new InitialContext();
//                    DataSource ds = (DataSource) ctx.lookup("jboss/datasources/ExampleDS");
//                    Connection conn = ds.getConnection();
//                    System.out.println("Howdy using connection: " + conn);
//                    conn.close();
//                } catch (Exception e) {
//                }
//            }
//        }).start();
    }


    
}
