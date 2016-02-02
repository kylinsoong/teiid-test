package org.teiid.test.embedded.metadata;

import org.teiid.metadata.MetadataStore;
import org.teiid.metadata.Schema;
import org.teiid.metadata.Table;

public class ExampleTable {

    public static void main(String[] args) {

        MetadataStore metadataStore = new MetadataStore();
        Schema schema = new Schema();
        schema.setName("name");
        metadataStore.addSchema(schema);
        
        Table table = new Table();
        table.setName("name");
        table.setSupportsUpdate(true);
        table.setTableType(org.teiid.metadata.Table.Type.Table);
        schema.addTable(table);
        
    }

}
