package org.teiid.test.swarm;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.transactions.TransactionsFraction;

/**
 * http://localhost:8080/
 * @author kylin
 *
 */
public class Main {

    public static void main(String[] args) throws Exception {    	
        
        Container container = new Container();
        
        container.fraction(TransactionsFraction.createDefaultFraction().port(4712).statusPort(4713));
        
        container.start();

        // add rest source
        JAXRSArchive appDeployment = ShrinkWrap.create(JAXRSArchive.class);
        appDeployment.addResource(MyResource.class);
        container.deploy(appDeployment);
    }

    
}
