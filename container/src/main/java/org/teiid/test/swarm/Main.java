package org.teiid.test.swarm;

import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.spi.api.SocketBindingGroup;

public class Main {

    public static void main(String[] args) throws Exception {    	
        
        Container container = new Container();
       
        container.start();
        
        System.out.println(container.socketBindingGroups());
        System.out.println(container.socketBindings());
  
    }

    
}
