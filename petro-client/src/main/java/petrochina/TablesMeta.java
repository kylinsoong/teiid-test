package petrochina;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TablesMeta {
    
    public static class Meta {
        
        private String schema;
        
        private String name;
        
        private String type;

        public Meta(String schema, String name, String type) {
            super();
            this.schema = schema;
            this.name = name;
            this.type = type;
        }

        @Override
        public String toString() {
            return name;
        }

        public String getSchema() {
            return schema;
        }

        public void setSchema(String schema) {
            this.schema = schema;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

    }
    
    private String id;
    
    public TablesMeta(String id)  {
        this.id = id;
    }

    private Map<String, List<Meta>> schemaMap = new HashMap<>();
    
    private Map<String, List<Meta>> typeMap = new HashMap<>();
    
    public void add(String schema, String name, String type) {
        
        Meta m = new Meta(schema, name, type);
        
        if(schema == null || name == null || type == null) {
            return;
        }
        
        if(schemaMap.get(schema) == null) {
            schemaMap.put(schema, new ArrayList<Meta>());
        }
        schemaMap.get(schema).add(m);
        
        if(typeMap.get(type) == null) {
            typeMap.put(type, new ArrayList<Meta>());
        }
        typeMap.get(type).add(m);
    }

    
    private static final String LN = "\n";
    private static final String TAB = "   ";
    private static final String COLON = ": ";
    private static final String COMMA = ", ";
    private static final String SIZE = "size = ";
    
    @Override
    public String toString() {
        
        final StringBuffer sb = new StringBuffer();
        
        sb.append(id + ", Metadata Tables Schemas").append(LN);
        schemaMap.keySet().stream().forEach(schema -> {
            if(schemaMap.get(schema) != null) {
                sb.append(TAB).append(schema).append(COLON).append(SIZE).append(schemaMap.get(schema).size()).append(LN);
            }
        });
        
        if(typeMap.size() >0) {
            sb.append("Metadata Tables Types").append(LN);
            typeMap.keySet().stream().forEach(type -> {
                if(typeMap.get(type) != null) {
                    sb.append(TAB).append(type).append(COLON).append(SIZE).append(typeMap.get(type).size()).append(LN);
                }
            });
        }
        
        sb.append(LN);
        
        
        return sb.toString();
    }
}
