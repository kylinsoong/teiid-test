package com.simba.schema.map.nodes;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class SMSchemaMap extends SMObject {
    
    private String m_version;
    private String m_standard;
    private String m_delimiter;
    private SMDefinition m_definition;

    private Set<SMCatalog> m_catalogs = new HashSet<>();
    private Set<SMSchema> m_schemas = new HashSet<>();
    private Set<SMTable> m_tables = new HashSet<>();
    private Map<String, SMObject> m_lookupTable = new TreeMap<>();

    public SMSchemaMap(String version, String standard, String delimiter, SMDefinition definition) {
        super("ROOT", definition, null);
        this.m_version = version;
        this.m_delimiter = delimiter;
        this.m_definition = definition;
        this.m_standard = standard;
    }
    
    public void addCatalog(SMCatalog catalog) {
        this.m_catalogs.add(catalog);
        this.m_lookupTable.put(getLookupName(catalog), catalog);
    }
    
    public void addSchema(SMSchema schema) {
        this.m_schemas.add(schema);
        this.m_lookupTable.put(getLookupName(schema), schema);
    }
    
    public void addTable(SMTable table) {
        this.m_tables.add(table);
        this.m_lookupTable.put(getLookupName(table), table);
    }
    
    public SMObject get(String key) {
        return (SMObject)this.m_lookupTable.get(key);
    }
    
    public SMDefinition getDefinition() {
        return this.m_definition;
    }
    
    public Set<SMCatalog> getCatalogs() {
        return this.m_catalogs;
    }
    
    public Set<SMSchema> getSchemas() {
        return this.m_schemas;
    }
    
    public Set<SMTable> getTables() {
        return this.m_tables;
    }
    
    public String getVersion() {
        return this.m_version;
    }
    
    public String getDelimiter() {
        return this.m_delimiter;
    }
    
    public String getStandard() {
        return this.m_standard;
    }
    
    private String getLookupName(SMObject object) {
        String result = object.getName();
        SMObject parent = object;
        while ((parent = parent.getParent()) != null) {
            if (!parent.getName().equals("ROOT")) {
                result = parent.getName() + "." + result;    
            }    
        }
        
        return result;
    }
    
    public void print() {
        
        for (Map.Entry<String, SMAttribute> a : this.m_definition.getColumnDefinition().entrySet()) {
            System.out.print(a.getValue());    
        }
        
        for (SMObject o : this.m_catalogs) {
            o.print(0);    
        }
        
        for (SMObject o : this.m_schemas) {
            o.print(1);    
        }
        
        for (SMObject o : this.m_tables) {
            o.print(2);
            SMTable smtable = (SMTable)o;
            for (SMColumn c : smtable.getColumns()) {
                c.print(3);    
            }
        }
        
        for (Map.Entry<String, SMObject> e : this.m_lookupTable.entrySet()) {
            System.out.println((String)e.getKey());
        }
    }
    
    public String serialize() {
        
        StringBuilder sb = new StringBuilder();
        
        sb.append("{\n\"");
        sb.append("SchemaMapVersion");
        sb.append("\":\"");
        sb.append(this.m_version);
        sb.append("\",\n\"");
        sb.append("Standard");
        sb.append("\":\"");
        sb.append(this.m_standard);
        sb.append("\",\n\"");
        sb.append("Delimiter");sb.append("\":\"");
        sb.append(this.m_delimiter);
        sb.append("\",");
        sb.append(this.m_definition.serialize());
        
        if (this.m_definition.supportsCatalogs()) {
            sb.append("\n\"");
            sb.append("Catalogs");
            sb.append("\":\n[");
            int j = 0;
            for (SMCatalog currentCatalog : this.m_catalogs) {
                sb.append(currentCatalog.serialize());
                if (j++ < this.m_catalogs.size() - 1) {
                    sb.append(",");
                }
            }
            sb.append("\n]");
        } else if (this.m_definition.supportsSchemas()) {
            sb.append("\n\"");
            sb.append("Schemas");
            sb.append("\":\n[");
            int j = 0;
            for (SMSchema currentSchema : this.m_schemas) {
                sb.append(currentSchema.serialize());
                if (j++ < this.m_schemas.size() - 1) {
                    sb.append(",");    
                }    
            }
            sb.append("\n]");
        } else {
            sb.append("\n\"");
            sb.append("Tables");
            sb.append("\":\n[");
            int j = 0; 
            for (SMTable currentTable : this.m_tables) {
                sb.append(currentTable.serialize());
                if (j++ < this.m_tables.size() - 1) {
                    sb.append(",");
                }
            }
            sb.append("\n]");
        }
        sb.append("\n}");
        return sb.toString();
    }
}