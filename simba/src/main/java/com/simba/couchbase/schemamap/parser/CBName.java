package com.simba.couchbase.schemamap.parser;

public class CBName {
    
    private String m_alias;
    
    private CBName m_parent;
    
    public CBName(CBName parentNode) {
        this.m_parent = parentNode;
    }
    
    public CBName findNode(CBName inputNode) {
        if (isEqual(inputNode)) {
            return this;
        }
        
        if (null != this.m_parent) {
            return this.m_parent.findNode(inputNode);
        }
        
        return null;
    }

    public boolean isAttribute() {
        return false;
    }
    
    public boolean isArrayIndexedElement() {
        return false;
    }
    
    public boolean isArrayDimension() {
        return false;
    }
    
    public boolean isEqual(CBName inputNode) {
        return false;
    }
    
    public boolean isSpecialIdentifier() {
        return false;
    }
    
    public String getAlias() {
        return this.m_alias;
    }
    
    public CBArrayDimension getAsArrayDimension() {
        return null;
    }
    
    public CBArrayIndexedElement getAsArrayIndexedElement() {
        return null;
    }
    
    public CBAttribute getAsAttribute() {
        return null;
    }
    
    public CBSpecialIdentifier getAsSpecialIdentifier() {
        return null;
    }
    
    public CBName getParentName() {
        return this.m_parent;
    }
    
    public CBName rebaseName(CBName originalNode, CBName inputBaseNode) {
        
        CBName baseNameNode = inputBaseNode;
        CBName nameNode = null;
        CBName prevNameNode = null;
        boolean found = false;
        
        while (null != baseNameNode) {
            nameNode = originalNode;
            prevNameNode = null;
            while (null != nameNode) {
                if (nameNode.isEqual(baseNameNode)) {
                    found = true;
                    break;
                }
                
                prevNameNode = nameNode;
                nameNode = nameNode.getParentName();
            }
            
            if (found) {
                break;
            }
            
            baseNameNode = baseNameNode.getParentName();
        }
        
        if (((null == nameNode) || (null != baseNameNode)) || (null == prevNameNode)) {
            
            originalNode = inputBaseNode;
            return originalNode;
        }
        
        prevNameNode.setParentName(baseNameNode);
        return originalNode;
    }
    
    public void setAlias(String inAlias) {
        this.m_alias = inAlias;
    }
    
    public void setParentName(CBName inputParentNode) {
        this.m_parent = inputParentNode;
    }
}