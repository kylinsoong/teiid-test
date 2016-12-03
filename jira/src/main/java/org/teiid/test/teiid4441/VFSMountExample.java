package org.teiid.test.teiid4441;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.concurrent.Executors;

import org.jboss.vfs.TempFileProvider;
import org.jboss.vfs.VFS;
import org.jboss.vfs.VirtualFile;

import com.google.common.io.Files;

public class VFSMountExample {

    public static void main(String[] args) throws Exception{
        
//        example_1();
        
        example_2();
    }

    static void example_2() throws IOException {
        VirtualFile testDir = VFS.getChild("/home/kylin/tmp/4441");
        VirtualFile tmpDir = testDir.getChild("tmp"); 
        TempFileProvider provider = TempFileProvider.create("tmp", Executors.newScheduledThreadPool(2));  
        VFS.mountTemp(testDir, provider);
        VirtualFile tmpFile = tmpDir.getChild("tmp.txt");  
        System.out.println(tmpFile.getPhysicalFile());
        FileOutputStream fos = new FileOutputStream(tmpFile.getPhysicalFile());  
        fos.write("Hello World".getBytes());
        fos.flush();
        fos.close();
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
