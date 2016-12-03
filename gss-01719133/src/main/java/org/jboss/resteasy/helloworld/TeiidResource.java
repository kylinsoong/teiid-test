package org.jboss.resteasy.helloworld;

import javax.naming.InitialContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.teiid.events.EventDistributor;
import org.teiid.events.EventDistributorFactory;


@Path("/")
public class TeiidResource {
	
	@GET
	@Path("/test")
	@Produces({ "application/json" })
	public String getHelloWorldJSON(){
	    try {
            dolookup();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return "{\"result\":\"success\"}";
	}

    private void dolookup() throws Exception{

        InitialContext ic = new InitialContext();
        EventDistributorFactory edf = (EventDistributorFactory)ic.lookup("teiid/event-distributor-factory");
        System.out.println(edf);
        EventDistributor ed = ((EventDistributorFactory)ic.lookup("teiid/event-distributor-factory")).getEventDistributor();
        System.out.println(ed);     
    }

}
