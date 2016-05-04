package org.teiid.test.teiidwebcn25;

import java.util.logging.Level;

import org.teiid.query.metadata.SystemMetadata;
import org.teiid.test.util.EmbeddedHelper;

public class SystemMetadataLoadTest {

    public static void main(String[] args) {

        EmbeddedHelper.enableLogger(Level.ALL);
        
        SystemMetadata.getInstance().getSystemStore();
    }

}
