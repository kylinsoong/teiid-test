package org.teiid.test.teiid4441;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Executors;

import org.jboss.vfs.TempFileProvider;
import org.jboss.vfs.VFS;
import org.jboss.vfs.VirtualFile;

public class VFSCreateTmpExample {

    
    static final Random rng = new Random();
    
    static String createTempName(String prefix, String suffix) {
        return prefix + Long.toHexString(rng.nextLong()) + suffix;
    }
    
    public static void main(String[] args) throws IOException {

        VirtualFile mountPoint = VFS.getChild("/home/kylin/tmp/4441");
        TempFileProvider provider = TempFileProvider.create("tmp", Executors.newScheduledThreadPool(2));
        VFS.mountTemp(mountPoint, provider);
        
        VirtualFile target = mountPoint.getChild("market-data.txt");
        target.getPhysicalFile().createNewFile();
        target.getPhysicalFile().deleteOnExit();
        
        System.out.println(target.getName());
    }

}
