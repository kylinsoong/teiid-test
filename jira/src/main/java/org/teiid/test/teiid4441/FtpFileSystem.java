package org.teiid.test.teiid4441;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.CodeSigner;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.jboss.vfs.VirtualFile;
import org.jboss.vfs.spi.FileSystem;

public class FtpFileSystem implements FileSystem {
    
    private FTPClient client;
    
    public FtpFileSystem(FTPClient ftpClient) {
        this.client = ftpClient;
    }

    @Override
    public File getFile(VirtualFile mountPoint, VirtualFile target) throws IOException {
        System.out.println(target);
        return null;
    }

    @Override
    public InputStream openInputStream(VirtualFile mountPoint, VirtualFile target) throws IOException {
        return this.client.retrieveFileStream(target.getPathName());
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public boolean delete(VirtualFile mountPoint, VirtualFile target) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public long getSize(VirtualFile mountPoint, VirtualFile target) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getLastModified(VirtualFile mountPoint, VirtualFile target) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean exists(VirtualFile mountPoint, VirtualFile target) {
        String name = target.getName();
        try {
            String[] names = this.client.listNames();
            boolean exists = false;
            if (names != null && names.length > 0){
                for(String n : names) {
                    if(n.equals(name)) {
                        exists = true;
                        break;
                    }
                }
            }
            return exists;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean isFile(VirtualFile mountPoint, VirtualFile target) {
        //TODO-- depend on getFile
        return false;
    }

    @Override
    public boolean isDirectory(VirtualFile mountPoint, VirtualFile target) {
        try {
            if(this.client.cwd(target.getPathName()) == 250) {
                return true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } 
        return false;
    }

    @Override
    public List<String> getDirectoryEntries(VirtualFile mountPoint, VirtualFile target) {
        try {
            return Arrays.asList(this.client.listNames(target.getPathName()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CodeSigner[] getCodeSigners(VirtualFile mountPoint, VirtualFile target) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void close() throws IOException {
        this.client.disconnect();
    }

    @Override
    public File getMountSource() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public URI getRootURI() throws URISyntaxException {
        try {
            return new URI("ftp", this.client.printWorkingDirectory() + "!/", null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
