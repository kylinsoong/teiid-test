package org.teiid.test.teiid3398;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.teiid.client.security.ILogon;
import org.teiid.net.CommunicationException;
import org.teiid.net.socket.SocketServerInstance;
import org.teiid.net.socket.SocketServerInstanceImpl;

public class ProxyTest {

    public static void main(String[] args) {
        
        ClassLoader loader = ProxyTest.class.getClassLoader();

        Class<ILogon> iface  = ILogon.class;
        Class<?>[] interfaces = new Class[] {iface};
        InvocationHandler h = new SocketServerInstanceImpl.RemoteInvocationHandler(iface, false){

            @Override
            protected SocketServerInstance getInstance()
                    throws CommunicationException {
                // TODO Auto-generated method stub
                return null;
            }};
        Object proxy = Proxy.newProxyInstance(loader, interfaces, h);
        ILogon logon = iface.cast(proxy);
    }

}
