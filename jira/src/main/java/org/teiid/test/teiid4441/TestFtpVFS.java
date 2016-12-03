package org.teiid.test.teiid4441;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.resource.ResourceException;

import org.apache.commons.net.ftp.FTPClient;
import org.jboss.vfs.VFS;
import org.jboss.vfs.VirtualFile;

public class TestFtpVFS {

    public static void main(String[] args) throws IOException, ResourceException, URISyntaxException {

        FTPClientFactory factory = new FTPClientFactory();
        factory.setParentDirectory("/home/kylin/vsftpd"); //$NON-NLS-1$
        factory.setHost("10.66.192.120"); //$NON-NLS-1$
        factory.setPort(21); 
        factory.setUsername("kylin"); //$NON-NLS-1$
        factory.setPassword("redhat"); //$NON-NLS-1$
        
        FTPClient client = factory.createClient();
        
        FtpFileSystem ftpFileSystem = new FtpFileSystem(client);
        VirtualFile remoteDir = VFS.getChild("/home/kylin/vsftpd");
        VFS.mount(remoteDir, ftpFileSystem);
        VirtualFile test = remoteDir.getChild("marketdata-price.txt");
        
        System.out.println(test.asFileURI());
    }

}
