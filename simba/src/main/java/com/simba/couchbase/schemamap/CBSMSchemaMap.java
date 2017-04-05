package com.simba.couchbase.schemamap;

import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.simba.schema.map.nodes.SMAttribute;
import com.simba.schema.map.nodes.SMDefinition;
import com.simba.schema.map.nodes.SMSchemaMap;
import com.simba.support.Pair;
import java.util.HashMap;

public class CBSMSchemaMap extends SMSchemaMap {
    
    public static final String COLUMN_DIFF_KEY = "columnDiffName";
    private static final String DELIMITER = "~~~SchemaMap";
    private static final String STANDARD = "JDBC";
    private static final String VERSION = "Couchbase Schema Map Version 1.0";
    public static final String TYPECOLUMN_NAME_KEY = "typeName";
    public static final String TYPECOLUMN_VALUE_KEY = "typeValue";
    
    public static final Pair<String, String> COLUMN_DIFF_NAME = new Pair<>("columnDiffName", "String");
    public static final Pair<String, String> DSII_NAME = new Pair<>("dsiiName", "string");
    public static final Pair<String, String> DSII_TYPE = new Pair<>("dsiiType", "int");
    public static final Pair<String, String> SOURCE_NAME = new Pair<>("sourceName", "string");
    public static final Pair<String, String> SOURCE_TYPE = new Pair<>("sourceType", "int");
    public static final Pair<String, String> TYPECOLUMN_NAME = new Pair<>("typeName", "string");
    public static final Pair<String, String> TYPECOLUMN_VALUE = new Pair<>("typeValue", "string");
    public static HashMap<String, JsonNodeType> sourceTypeMap = new HashMap<>();
    
    static {
        initSourceTypeMap();
    }
    
    public CBSMSchemaMap() {
        super(VERSION, STANDARD, DELIMITER, createCBDefinition());    
    }
    
    public CBSMSchemaMap(String version, String standard, String delimiter, SMDefinition definition) {
        super(version, standard, delimiter, definition);
    }
    
    private static void addDsiiTypeEnum(SMAttribute dsiiType) {
        dsiiType.addEnum(Integer.toString(-5), "SQL_BIGINT");
        dsiiType.addEnum(Integer.toString(-7), "SQL_BIT");
        dsiiType.addEnum(Integer.toString(8), "SQL_DOUBLE");
        dsiiType.addEnum(Integer.toString(12), "SQL_VARCHAR");
    }
    
    private static SMDefinition createCBDefinition() {
        
        SMDefinition definition = new SMDefinition();
        definition.setSupportsCatalogs(true);
        definition.setSupportsSchemas(true);
        definition.addStandardTableAttributes();
        
        SMAttribute dsiiNameAttr = new SMAttribute("Type", (String)DSII_NAME.value());
        dsiiNameAttr.setRequired(false);
        definition.addSchemaAttribute((String)DSII_NAME.key(), dsiiNameAttr);
        definition.addTableAttribute((String)DSII_NAME.key(), dsiiNameAttr);
        definition.addColumnAttribute((String)DSII_NAME.key(), dsiiNameAttr);
        
        SMAttribute srcNameAttr = new SMAttribute("Type", (String)SOURCE_NAME.value());
        srcNameAttr.setRequired(false);
        definition.addColumnAttribute((String)SOURCE_NAME.key(), srcNameAttr);
        definition.addTableAttribute((String)SOURCE_NAME.key(), srcNameAttr);
        
        SMAttribute srcTypeAttr = new SMAttribute("Type", (String)SOURCE_TYPE.value());
        srcTypeAttr.setRequired(false);
        definition.addColumnAttribute((String)SOURCE_TYPE.key(), srcTypeAttr);

        SMAttribute dsiiTypeAttr = new SMAttribute("Type", (String)DSII_TYPE.value());
        dsiiTypeAttr.setRequired(false);
        addDsiiTypeEnum(dsiiTypeAttr);
        definition.addColumnAttribute((String)DSII_TYPE.key(), dsiiTypeAttr);
        
        SMAttribute typeIdentifier = new SMAttribute("Type", (String)COLUMN_DIFF_NAME.value());
        typeIdentifier.setRequired(false);
        definition.addSchemaAttribute((String)COLUMN_DIFF_NAME.key(), typeIdentifier);

        SMAttribute typeColumnName = new SMAttribute("Type", (String)TYPECOLUMN_NAME.value());
        typeColumnName.setRequired(false);
        definition.addTableAttribute((String)TYPECOLUMN_NAME.key(), typeColumnName);
        
        SMAttribute typeColumnValue = new SMAttribute("Type", (String)TYPECOLUMN_VALUE.value());
        typeColumnValue.setRequired(false);
        definition.addTableAttribute((String)TYPECOLUMN_VALUE.key(), typeColumnValue);
        
        return definition;
    }

    private static void initSourceTypeMap() {
        sourceTypeMap.put(CBSourceType.NULL.name(), JsonNodeType.NULL);
        sourceTypeMap.put(CBSourceType.BOOLEAN.name(), JsonNodeType.BOOLEAN);
        sourceTypeMap.put(CBSourceType.NUMBER.name(), JsonNodeType.NUMBER);
        sourceTypeMap.put(CBSourceType.STRING.name(), JsonNodeType.STRING);
        sourceTypeMap.put(CBSourceType.ARRAY.name(), JsonNodeType.ARRAY);
        sourceTypeMap.put(CBSourceType.OBJECTS.name(), JsonNodeType.OBJECT);
    }
}