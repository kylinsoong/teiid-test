package org.teiid.test.teiid4441;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.resource.ResourceException;

import org.apache.commons.net.ftp.FTPClient;
import org.jboss.vfs.VFS;
import org.jboss.vfs.VirtualFile;
import org.jboss.vfs.VirtualFileFilter;

public class TestFtpVFS {

    public static void main(String[] args) throws IOException, ResourceException, URISyntaxException {

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
//        VirtualFile test = mountPoint.getChild("marketdata-price.txt");
//        
//        System.out.println(test.getPhysicalFile().getAbsolutePath());
//        System.out.println(test.getPhysicalFile());
        
//        VirtualFile marketdata = VFS.getChild("/home/kylin/vsftpd/marketdata-price.txt");
//        marketdata.isDirectory();
//        marketdata.isFile();
//        marketdata.getSize();
//        marketdata.getLastModified();
//        marketdata.openStream();
        
//        VirtualFile marketdata = VFS.getChild("/home/kylin/vsftpd/marketdata-price.csv");
        ftpClient.completePendingCommand();
        System.out.println(VFS.getChild("/home/kylin/vsftpd/marketdata-price.txt").openStream());
        System.out.println(VFS.getChild("/home/kylin/vsftpd/marketdata-price1.txt").openStream());
//        
//        marketdata = VFS.getChild("/home/kylin/vsftpd/marketdata-price.csv");
//        System.out.println(marketdata.delete());
//        
//        System.out.println(mountPoint.getChildrenRecursively(new VirtualFileFilter(){
//
//            @Override
//            public boolean accepts(VirtualFile file) {
//                return file.getName().endsWith(".txt");
//            }}));
    }

}
