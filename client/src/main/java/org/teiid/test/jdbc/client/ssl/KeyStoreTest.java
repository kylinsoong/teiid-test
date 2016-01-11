package org.teiid.test.jdbc.client.ssl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import org.teiid.jdbc.JDBCPlugin;
import org.teiid.net.socket.SocketUtil;

public class KeyStoreTest {

	public static void main(String[] args) throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException {

		String name = "ssl-example.keystore";
		String type = "JKS";
		String password = "redhat";
		
		InputStream stream = SocketUtil.class.getClassLoader().getResourceAsStream(name);
        if (stream == null) {
            try {
                stream = new FileInputStream(name);
            } catch (FileNotFoundException e) {
                IOException exception = new IOException(JDBCPlugin.Util.getString("SocketHelper.keystore_not_found", name)); //$NON-NLS-1$
                exception.initCause(e);
                throw exception;
            } 
        }
                
        KeyStore ks = KeyStore.getInstance(type);        
        try {
        	ks.load(stream, password != null ? password.toCharArray() : null);
        } finally {
    		stream.close();
        }
        
        System.out.println(ks);
	}

}
