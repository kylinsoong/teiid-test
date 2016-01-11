package org.teiid.test.embedded.ssl;

import javax.net.ssl.KeyManagerFactory;

public class Tmp {

    public static void main(String[] args) {

        System.out.println(KeyManagerFactory.getDefaultAlgorithm());
    }

}
