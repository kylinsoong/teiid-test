package org.teiid.test.teiid4441.ftps;

import java.io.IOException;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;

import javax.net.ssl.KeyManager;

import org.apache.commons.net.util.KeyManagerUtils;

public class KeyManagerExample {

    public static void main(String[] args) throws IOException, GeneralSecurityException {

        KeyManager km = KeyManagerUtils.createClientKeyManager(Paths.get("src/main/resources/key.pem").toFile(), "");
    }

}
