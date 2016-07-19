package org.teiid.test.swarm;

import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.UserTransaction;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/")
public class MyResource {

    @GET
    @Produces("text/plain")
    public String get() throws NamingException, SQLException {
        
        StringBuffer sb = new StringBuffer();
        Context ctx = new InitialContext();
        sb.append("Active").append("\n");
        
        UserTransaction txn = (UserTransaction) ctx.lookup("java:comp/UserTransaction");
        sb.append(txn).append("\n");

        return sb.toString();
    }
}
