package com.simba.couchbase.schemamap.parser;

public class CBSpecialIdentifier extends CBName {

    private String m_name;

    public CBSpecialIdentifier(String name, CBName parentNode) {
        super(parentNode);
        this.m_name = name;
    }
    
    public CBSpecialIdentifier getAsSpecialIdentifier() {
        return this;
    }
    
    public String getIdentifierName() {
        return this.m_name;
    }
    
    public boolean isSpecialIdentifier() {
        return true;
    }
  
    public boolean isEqual(CBName inputNode) {
        CBName otherParent = inputNode.getParentName();
        CBName parent = getParentName();
        return (inputNode.isSpecialIdentifier()) && (this.m_name.equals(inputNode.getAsSpecialIdentifier().getIdentifierName())) && (((null == parent) && (null == otherParent)) || ((null != parent) && (null != otherParent) && (parent.isEqual(otherParent))));
    }
}