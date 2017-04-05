package com.simba.couchbase.schemamap.parser;

public class CBArrayIndexedElement extends CBName {

    private int m_index;

    public CBArrayIndexedElement(int index, CBName parentNode) {
        super(parentNode);
        this.m_index = index;
    }
    
    public CBArrayIndexedElement getAsArrayIndexedElement() {
        return this;
    }
    
    public int getIndex() {
        return this.m_index;
    }
    
    public boolean isArrayIndexedElement() {
        return true;
    }
    
    public boolean isEqual(CBName inputNode) {
        CBName otherParent = inputNode.getParentName();
        CBName parent = getParentName();
        return (inputNode.isArrayIndexedElement()) && (this.m_index == inputNode.getAsArrayIndexedElement().getIndex()) && (((null == parent) && (null == otherParent)) || ((null != parent) && (null != otherParent) && (parent.isEqual(otherParent))));
    }
}