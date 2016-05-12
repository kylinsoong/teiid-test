package org.teiid.test.swarm;

import java.sql.Connection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.datasources.DatasourcesFraction;
import org.wildfly.swarm.spi.api.Fraction;

public class Main {

    public static void main(String[] args) throws Exception {    	
        
        Container container = new Container();
        
        container.fraction(datasourceWithH2());
        
        container.start();
        
        Context ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("jboss/datasources/ExampleDS");
        Connection conn = ds.getConnection();
    	
        System.out.println(conn);
    }

    private static Fraction datasourceWithH2() {
        return new DatasourcesFraction()
            .jdbcDriver("h2", (d) -> {
                d.driverClassName("org.h2.Driver");
                d.xaDatasourceClass("org.h2.jdbcx.JdbcDataSource");
                d.driverModuleName("com.h2database.h2");
            })
            .dataSource("ExampleDS", (ds) -> {
                ds.driverName("h2");
                ds.connectionUrl("jdbc:h2:mem:test-swarm;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
                ds.userName("sa");
                ds.password("sa");
            });
    }
    
}
