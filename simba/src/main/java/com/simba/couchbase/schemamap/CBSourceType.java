package com.simba.couchbase.schemamap;

public enum CBSourceType {
    
    BOOLEAN, 
    NUMBER, 
    STRING,  
    ARRAY,  
    OBJECTS,  
    NULL,  
    MISSING;
    
    private CBSourceType() {
        
    }
}
