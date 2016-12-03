package org.teiid.test.teiid4441;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.jboss.vfs.VFS;
import org.jboss.vfs.VirtualFile;

public class VFSBasicExample {

    public static void main(String[] args) throws Exception {

        example_1();
        
        example_2();
        
        example_3();
        
        example_4();
    }

    static void example_4() {
        VirtualFile testDir = VFS.getChild("/home/kylin/tmp/4441");
        VirtualFile someFile = testDir.getChild("marketdata-price.txt");
        if(someFile.exists() && someFile.isFile()) {
            System.out.println(someFile.getName());
            System.out.println(someFile.getPathName());
            System.out.println(someFile.getSize());
            System.out.println(someFile.getLastModified());
        }
    }

    static void example_3() {
        VirtualFile testDir = VFS.getChild("/home/kylin/tmp/4441");
        VirtualFile documents = testDir.getChild("documents");
        if(documents.exists() && documents.isDirectory()) {
            List<VirtualFile> documentList = documents.getChildren();
            documentList.forEach(f -> System.out.println(f));
        }
    }

    static void example_2() {

        VirtualFile testDir = VFS.getChild("/home/kylin/tmp/4441");
        VirtualFile documents = testDir.getChild("documents");
        if(documents.exists()) {
            VirtualFile parent = documents.getParent();
            while(parent != null && !parent.equals(testDir)) {
                parent = parent.getParent();
            }
            if(parent == null) {
                System.out.println("File is not found under" + testDir); 
            }
        }
        
    }

    static void example_1() throws IOException, URISyntaxException {
        
        VirtualFile testDir = VFS.getChild("/home/kylin/tmp/4441");
        System.out.println(testDir);
        System.out.println(testDir.isDirectory());
        System.out.println(testDir.asDirectoryURI());
        System.out.println(testDir.asDirectoryURL());
        System.out.println(testDir.asFileURI());
        System.out.println(testDir.asFileURL());
        System.out.println(testDir.toURI());
        System.out.println(testDir.toURL());
        System.out.println(testDir.getName());
        System.out.println(testDir.getPhysicalFile());
        
        
        VirtualFile documents = testDir.getChild("documents");
        
        if(documents.exists() && documents.isDirectory()) {
            VirtualFile someFile = documents.getChild("marketdata-price.txt");  
            if(someFile.exists()){
                System.out.println(someFile.getPhysicalFile());
            }
        }
    }

}
