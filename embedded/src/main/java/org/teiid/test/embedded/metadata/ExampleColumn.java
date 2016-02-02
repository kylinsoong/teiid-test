package org.teiid.test.embedded.metadata;

import org.teiid.core.types.DataTypeManager;
import org.teiid.metadata.Column;
import org.teiid.metadata.MetadataStore;
import org.teiid.metadata.Schema;
import org.teiid.metadata.Table;
import org.teiid.metadata.BaseColumn.NullType;
import org.teiid.metadata.Column.SearchType;

public class ExampleColumn {

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
        
        Column column = new Column();
        column.setName("name");
        column.setRuntimeType(DataTypeManager.DefaultDataTypes.STRING);
        column.setSearchType(SearchType.Searchable); 
        column.setNullType(NullType.Nullable);
        column.setPosition(1);
        column.setUpdatable(true);
        column.setLength(100);
        table.addColumn(column);
    }
}
