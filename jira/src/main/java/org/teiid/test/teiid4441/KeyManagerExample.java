package org.teiid.test.teiid4441;

import java.security.NoSuchAlgorithmException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;

public class KeyManagerExample {

    public static void main(String[] args) throws NoSuchAlgorithmException {

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        KeyManager[] keyManager = kmf.getKeyManagers();
        System.out.println(keyManager.length);
    }

}
