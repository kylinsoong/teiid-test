package org.teiid.test.embedded;

import java.util.logging.Level;

import org.teiid.example.EmbeddedHelper;
import org.teiid.metadata.MetadataStore;
import org.teiid.query.metadata.SystemMetadata;

public class ResolverTest {

	public static void main(String[] args) {

		EmbeddedHelper.enableLogger(Level.ALL);
		
		MetadataStore systemStore = SystemMetadata.getInstance().getSystemStore();
		
		System.out.println(systemStore);
	}

}
