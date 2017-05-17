package org.teiid.test.facebook;

import org.springframework.social.facebook.connect.FacebookConnectionFactory;

public class Example_FacebookConnectionFactory {

    public static void main(String[] args) {

        FacebookConnectionFactory factory = new FacebookConnectionFactory("862558810566543", "577440b169b4ad0c5c7a497e83feae26");
        
        System.out.println(factory);
    }

}
