package org.teiid.test.teiid4441.vfs;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.resource.ResourceException;

public class Example {

    public static void main(String[] args) throws IOException, URISyntaxException, ResourceException {

        VFSRealFileSystemExample.main(args);
        VFSClassPathFileExample.main(args);
        VFSFtpFileSystemExample.main(args);
    }

}
