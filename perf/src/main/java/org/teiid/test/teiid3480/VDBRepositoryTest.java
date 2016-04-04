package org.teiid.test.teiid3480;

import java.util.logging.Level;

import org.teiid.deployers.VDBRepository;
import org.teiid.query.metadata.SystemMetadata;
import org.teiid.test.util.EmbeddedHelper;

public class VDBRepositoryTest {

    public static void main(String[] args) {
        
        EmbeddedHelper.enableLogger(Level.ALL);
        
        SystemMetadata.getInstance().getSystemStore();

        new VDBRepository();
    }

}
