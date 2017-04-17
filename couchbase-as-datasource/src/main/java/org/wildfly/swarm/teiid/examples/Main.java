package org.wildfly.swarm.teiid.examples;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.teiid.TeiidFraction;
import org.wildfly.swarm.teiid.VDBArchive;
import org.wildfly.swarm.resource.adapters.ResourceAdapterFraction;

public class Main {
    
    
    public static void main(String[] args) throws Exception {
                
        Swarm swarm = new Swarm();        
        swarm.fraction(new TeiidFraction().translator("couchbase", t -> t.module("org.jboss.teiid.translator.couchbase")))
             .fraction(new ResourceAdapterFraction()
                     .resourceAdapter("couchbaseQS", rac -> rac.module("org.jboss.teiid.resource-adapter.couchbase")
                             .connectionDefinitions("couchbaseDS", cdc -> cdc.className("org.teiid.resource.adapter.couchbase.CouchbaseManagedConnectionFactory")
                                     .jndiName("java:/couchbaseDS")
                                     .enabled(true)
                                     .useJavaContext(true)
                                     .configProperties("ConnectionString", cpc -> cpc.value("10.66.192.120"))
                                     .configProperties("Keyspace", cpc -> cpc.value("default"))
                                     .configProperties("Namespace", cpc -> cpc.value("default")))));
        swarm.start();

        VDBArchive vdb = ShrinkWrap.create(VDBArchive.class);
        vdb.vdb(Main.class.getClassLoader().getResourceAsStream("couchbase-vdb.xml"));
        swarm.deploy(vdb);     
    }

 
    
}
