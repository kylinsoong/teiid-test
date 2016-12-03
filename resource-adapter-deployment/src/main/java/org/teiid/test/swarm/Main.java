package org.teiid.test.swarm;

import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.resource.adapters.ResourceAdapterFraction;

public class Main {

    public static void main(String[] args) throws Exception {    	
        
        Swarm swarm = new Swarm();
                
        swarm.fraction(new ResourceAdapterFraction()
                .resourceAdapter("fileQS", rac -> rac.module("org.jboss.teiid.resource-adapter.file")
                        .connectionDefinitions("fileQS", cdc -> cdc.className("org.teiid.resource.adapter.file.FileManagedConnectionFactory")
                                .jndiName("java:/marketdata-file")
                                .configProperties("ParentDirectory", cpc -> cpc.value("/home/kylin/work"))
                                .configProperties("AllowParentPaths", cpc -> cpc.value("true")))));
        
//        swarm.fraction(new ResourceAdapterFraction().resourceAdapter("file", rac -> rac.module("org.jboss.teiid.resource-adapter.file")));
        swarm.start();
        
//        JAXRSArchive appDeployment = ShrinkWrap.create(JAXRSArchive.class);
//        appDeployment.addResource(MyResource.class);
//        container.deploy(appDeployment);
    }



    
}
