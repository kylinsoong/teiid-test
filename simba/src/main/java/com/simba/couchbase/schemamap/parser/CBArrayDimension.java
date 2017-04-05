package com.simba.couchbase.schemamap.parser;

public class CBArrayDimension extends CBName {

    private int m_dimension;
    
    public CBArrayDimension(CBName parentNode) {
        super(parentNode);
        if (getParentName().isArrayDimension()) {
            this.m_dimension = (getParentName().getAsArrayDimension().getDimension() + 1);
        } else {
            this.m_dimension = 1;
        }
    }
    
    public CBArrayDimension getAsArrayDimension() {
        return this;
    }
    
    public int getDimension() {
        return this.m_dimension;
    }
    
    public boolean isArrayDimension() {
        return true;
    }
    
    public boolean isEqual(CBName inputNode) {
        CBName otherParent = inputNode.getParentName();
        CBName parent = getParentName();
        return (inputNode.isArrayDimension()) && (this.m_dimension == inputNode.getAsArrayDimension().getDimension()) && (((null == parent) && (null == otherParent)) || ((null != parent) && (null != otherParent) && (parent.isEqual(otherParent))));
    }
}