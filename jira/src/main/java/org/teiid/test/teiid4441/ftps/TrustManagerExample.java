package org.teiid.test.teiid4441.ftps;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

import org.apache.commons.net.util.TrustManagerUtils;

public class TrustManagerExample {

    public static void main(String[] args) throws IOException, GeneralSecurityException {

//        ByteArrayInputStream derInputStream = new ByteArrayInputStream(TrustManagerExample.class.getClassLoader().getResourceAsStream(""));
        
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Certificate cert = certificateFactory.generateCertificate(TrustManagerExample.class.getClassLoader().getResourceAsStream("cert.pem"));
       
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null);
        keyStore.setCertificateEntry("alias", cert);
        
        X509TrustManager trustManager = TrustManagerUtils.getDefaultTrustManager(keyStore);
        System.out.println(trustManager);
    }

}
