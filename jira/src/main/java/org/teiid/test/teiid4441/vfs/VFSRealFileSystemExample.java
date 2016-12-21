package org.teiid.test.teiid4441.vfs;

import java.io.IOException;

import org.jboss.vfs.VFS;
import org.jboss.vfs.VirtualFile;

public class VFSRealFileSystemExample {

    public static void main(String[] args) throws IOException {
        
        VirtualFile marketdata = VFS.getChild("/home/kylin/tmp/4441/marketdata-price.txt");
        System.out.println(marketdata.exists());
        System.out.println(marketdata.getPhysicalFile());
        System.out.println(marketdata.toURL());
        System.out.println();
        
    }

}
