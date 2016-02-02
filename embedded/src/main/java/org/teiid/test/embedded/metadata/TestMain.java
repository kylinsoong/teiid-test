package org.teiid.test.embedded.metadata;

import java.util.logging.Level;

import org.teiid.example.EmbeddedHelper;
import org.teiid.query.metadata.QueryMetadataInterface;
import org.teiid.query.metadata.SystemMetadata;

public class TestMain {

    public static void main(String[] args) throws Exception {

        EmbeddedHelper.enableLogger(Level.ALL);
        
        QueryMetadataInterface qmi = MetadataUtils.queryMetadataInterface();
        
        System.out.println(SystemMetadata.getInstance());
    }

}
