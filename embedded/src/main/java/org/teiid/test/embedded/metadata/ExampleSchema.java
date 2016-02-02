package org.teiid.test.embedded.metadata;

import org.teiid.metadata.MetadataStore;
import org.teiid.metadata.Schema;

public class ExampleSchema {

    public static void main(String[] args) {

        MetadataStore metadataStore = new MetadataStore();
        Schema schema = new Schema();
        schema.setName("name");
        metadataStore.addSchema(schema);
        
        System.out.println(schema.isPhysical());
    }

}
