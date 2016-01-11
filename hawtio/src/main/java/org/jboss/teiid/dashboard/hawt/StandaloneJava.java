package org.jboss.teiid.dashboard.hawt;

import io.hawt.embedded.Main;

public class StandaloneJava {

    public static void main(String[] args) throws Exception {

        Main main = new Main();
        main.setWar("hawtio-web.war");
        main.run();
    }

}
