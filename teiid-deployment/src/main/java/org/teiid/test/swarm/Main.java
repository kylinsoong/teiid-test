package org.teiid.test.swarm;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.datasources.DatasourceArchive;
import org.wildfly.swarm.teiid.VDBArchive;

public class Main {

    public static void main(String[] args) throws Exception {
        
        // 1. start container, configure translator
        Container container = new Container();
        container.start();
        
        // 2. deploy data sources/resource adapter
        container.deploy(Swarm.artifact("com.h2database:h2", "h2"));
        DatasourceArchive dsArchive = ShrinkWrap.create(DatasourceArchive.class);
        dsArchive.dataSource("ExampleDS", (ds) -> {
            ds.connectionUrl("jdbc:h2:mem:test-swarm;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
            ds.driverName("h2");
            ds.userName("sa");
            ds.password("sa");
        });
        container.deploy(dsArchive);
        
        // 3. deploy VDB
        VDBArchive vdb = ShrinkWrap.create(VDBArchive.class);
        vdb.vdb("portfolio-vdb.xml");
        container.deploy(vdb);
        
    }


    
}
