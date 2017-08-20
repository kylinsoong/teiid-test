package tomcat.example;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

public class TomcatEmbedded {

    public static void main(String[] args) throws LifecycleException {

        new TomcatEmbedded().start();
    }

    private void start() throws LifecycleException {

        Tomcat tomcat = new Tomcat();
//        tomcat.start();
        
        startDaemonAwaitThread(tomcat);
    }

    private void startDaemonAwaitThread(final Tomcat tomcat) {

        Thread awaitThread = new Thread("container-1") {

            @Override
            public void run() {
                tomcat.getServer().await();
            }

        };
        awaitThread.setContextClassLoader(getClass().getClassLoader());
        awaitThread.setDaemon(false);
        awaitThread.start();
    }

}
