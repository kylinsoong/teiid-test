package org.teiid.test.teiid4441;

import java.io.File;

import javax.resource.ResourceException;

import org.jboss.vfs.VirtualFile;

public interface ITest {
    
    File getFile(String path) throws ResourceException;
    
    void add(VirtualFile file) throws ResourceException;
    VirtualFile getVFSFile(String path) throws ResourceException;
    void remote(String path) throws ResourceException;
    void delete(VirtualFile file) throws ResourceException;

}
