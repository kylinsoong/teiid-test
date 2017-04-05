package com.simba.couchbase.schemamap.parser;

public class CBAttribute extends CBName {

    private String m_name;

    public CBAttribute(String attributeName, CBName parentNode) {
        super(parentNode);
        this.m_name = attributeName;
    }
    
    public CBAttribute(String attributeName) {
        super(null);
        this.m_name = attributeName;
    }
    
    public CBAttribute getAsAttribute() {
        return this;
    }
    
    public String getAttributeName() {
        return this.m_name;
    }
    
    public String getUnquotedName() {
        return this.m_name.substring(1, this.m_name.length() - 1);
    }
    
    public boolean isAttribute() {
        return true;
    }
    
    public boolean isEqual(CBName inputNode) {
        CBName otherParent = inputNode.getParentName();
        CBName parent = getParentName();
        return (inputNode.isAttribute()) && (this.m_name.equals(inputNode.getAsAttribute().getAttributeName())) && (((null == parent) && (null == otherParent)) || ((null != parent) && (null != otherParent) && (parent.isEqual(otherParent))));
    }
}