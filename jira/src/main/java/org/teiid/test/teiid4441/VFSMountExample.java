package org.teiid.test.teiid4441;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;

import org.jboss.vfs.TempFileProvider;
import org.jboss.vfs.VFS;
import org.jboss.vfs.VirtualFile;

public class VFSMountExample {

    public static void main(String[] args) throws Exception{
        
//        example_1();
        
        example_2();
    }

    static void example_2() throws IOException {
        VirtualFile testDir = VFS.getChild("/home/kylin/tmp/4441");
        VirtualFile tmpDir = testDir.getChild("t"); 
        TempFileProvider provider = TempFileProvider.create("t", Executors.newScheduledThreadPool(2));  
        VFS.mountTemp(testDir, provider);
        VirtualFile tmpFile = tmpDir.getChild("tmp.txt");  
        System.out.println(tmpFile.exists());
        System.out.println(tmpFile.getPhysicalFile());
        tmpFile.getPhysicalFile().createNewFile();
        System.out.println(tmpFile.isFile());
        System.out.println(tmpFile.isDirectory());
        
    }

    /**
     * Mount Existing File in an Alternate Location
     * @throws IOException 
     */
    static void example_1() throws IOException {
        File homeDir = new File("/home/kylin"); 
        VirtualFile testDir = VFS.getChild("/home/kylin/tmp/4441");
        VFS.mountReal(homeDir, testDir);
        System.out.println(new File(homeDir, ".jboss-cli-history").length());
        System.out.println(testDir.getChild(".jboss-cli-history").getSize());
        System.out.println(testDir.getChild(".jboss-cli-history").getPhysicalFile());
        System.out.println(testDir.getChild(".jboss-cli-history").getPathName());
        System.out.println(testDir.getChild(".jboss-cli-history").toURL());
    }

}
