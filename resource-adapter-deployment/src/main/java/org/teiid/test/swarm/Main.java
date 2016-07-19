package org.teiid.test.swarm;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Paths;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.resource.adapters.RARArchive;

/**
 * http://localhost:8080/
 * @author kylin
 *
 */
public class Main {

    public static void main(String[] args) throws Exception {    	
        
        Container container = new Container();
                
        container.start();
        
        RARArchive raArchive = ShrinkWrap.create(RARArchive.class);
        raArchive.resourceAdapter(new ClassLoaderAsset("ironjacamar.xml", Main.class.getClassLoader()));
        container.deploy(raArchive);
        
//        JAXRSArchive appDeployment = ShrinkWrap.create(JAXRSArchive.class);
//        appDeployment.addResource(MyResource.class);
//        container.deploy(appDeployment);
    }



    
}
