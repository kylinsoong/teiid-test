package org.teiid.test.swarm;

import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/")
public class MyResource {

    @GET
    @Produces("text/plain")
    public String get() throws NamingException {
        Context ctx = new InitialContext();
        ctx.lookup("java:jboss/datasources/ExampleDS");
//        Connection conn = ds.getConnection();
        List<?> list = new ArrayList<>();
//        try {
//            for(int i = 0 ; i < 10 ; i ++){
//                list.add(ds.getConnection());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        StringBuffer sb = new StringBuffer();
        sb.append("Queued Connection size: " + list.size());
//        for(Connection conn : list){
//            sb.append("\n    " + conn);
//        }
        return sb.toString();
    }
}
