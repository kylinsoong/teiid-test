package org.teiid.test.teiid4441.vfs;

import java.io.IOException;

import javax.resource.ResourceException;

import org.apache.commons.net.ftp.FTPClient;
import org.jboss.vfs.VFS;
import org.jboss.vfs.VirtualFile;
import org.teiid.test.teiid4441.FTPClientFactory;
import org.teiid.test.teiid4441.FtpFileSystem;

public class VFSFtpFileSystemExample {

    public static void main(String[] args) throws IOException, ResourceException {
        
        FTPClientFactory factory = new FTPClientFactory();
        factory.setParentDirectory("/home/kylin/vsftpd"); //$NON-NLS-1$
        factory.setHost("10.66.192.120"); //$NON-NLS-1$
        factory.setPort(21); 
        factory.setUsername("kylin"); //$NON-NLS-1$
        factory.setPassword("redhat"); //$NON-NLS-1$
        
        FTPClient ftpClient = factory.createClient();
        
        FtpFileSystem ftpFileSystem = new FtpFileSystem(ftpClient);
        VirtualFile mountPoint = VFS.getChild("/home/kylin/vsftpd");
        VFS.mount(mountPoint, ftpFileSystem);
        
        VirtualFile marketdata = VFS.getChild("/home/kylin/vsftpd/marketdata-price.txt");
        System.out.println(marketdata.exists());
        System.out.println(marketdata.getPhysicalFile());
        System.out.println(marketdata.toURL());
        System.out.println();
    }

}
