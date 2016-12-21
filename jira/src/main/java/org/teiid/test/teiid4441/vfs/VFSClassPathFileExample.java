package org.teiid.test.teiid4441.vfs;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.jboss.vfs.VFS;
import org.jboss.vfs.VirtualFile;

public class VFSClassPathFileExample {

    public static void main(String[] args) throws URISyntaxException, IOException {
        
        ClassLoader classloader = VFSClassPathFileExample.class.getClassLoader();

        URL url = classloader.getResource("teiidfiles/data/marketdata-price.txt");

        VirtualFile marketdata = VFS.getChild(url.toURI());
        
        System.out.println(url.toURI());
        System.out.println(marketdata.exists());
        System.out.println(marketdata.getPhysicalFile());
        System.out.println(marketdata.getPhysicalFile().exists());
        System.out.println(marketdata.toURL());
        System.out.println();
        
    }

}
