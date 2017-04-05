package com.simba.sqlengine.parser.parsetree;

import com.simba.sqlengine.SQLEngineGenericContext;
import com.simba.sqlengine.parser.type.PTListType;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.ExceptionBuilder;
import com.simba.support.exceptions.ExceptionType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class PTListNode
  extends AbstractPTNonterminalNode
{
  private final PTListType m_listType;
  private ArrayList<IPTNode> m_children;
  
  public PTListNode(PTListType paramPTListType)
  {
    this.m_listType = paramPTListType;
    this.m_children = new ArrayList();
  }
  
  public <T> T acceptVisitor(IPTVisitor<T> paramIPTVisitor)
    throws ErrorException
  {
    if (null == paramIPTVisitor) {
      throw new NullPointerException("Visitor cannot be null");
    }
    return (T)paramIPTVisitor.visit(this);
  }
  
  public PTListNode addChild(IPTNode paramIPTNode)
  {
    if (null == paramIPTNode) {
      throw new NullPointerException("Child cannot be null");
    }
    this.m_children.add(paramIPTNode);
    return this;
  }
  
  public IPTNode getChild(int paramInt)
    throws ErrorException
  {
    try
    {
      return (IPTNode)this.m_children.get(paramInt);
    }
    catch (IndexOutOfBoundsException localIndexOutOfBoundsException)
    {
      throw SQLEngineGenericContext.s_SQLEngineMessages.createGeneralException(SQLEngineMessageKey.INVALID_ARGUMENT.name(), localIndexOutOfBoundsException, ExceptionType.DEFAULT);
    }
  }
  
  public Iterator<IPTNode> getChildItr()
  {
    return this.m_children.iterator();
  }
  
  public PTListType getListType()
  {
    return this.m_listType;
  }
  
  public List<IPTNode> getImmutableChildList()
  {
    return Collections.unmodifiableList(this.m_children);
  }
  
  public int numChildren()
  {
    return this.m_children.size();
  }
  
  public IPTNode removeChild(int paramInt)
    throws ErrorException
  {
    try
    {
      return (IPTNode)this.m_children.remove(paramInt);
    }
    catch (IndexOutOfBoundsException localIndexOutOfBoundsException)
    {
      throw SQLEngineGenericContext.s_SQLEngineMessages.createGeneralException(SQLEngineMessageKey.INVALID_ARGUMENT.name(), localIndexOutOfBoundsException, ExceptionType.DEFAULT);
    }
  }
  
  public IPTNode setChild(int paramInt, IPTNode paramIPTNode)
    throws ErrorException
  {
    if (null == paramIPTNode) {
      throw new NullPointerException("The child cannot be null.");
    }
    try
    {
      return (IPTNode)this.m_children.set(paramInt, paramIPTNode);
    }
    catch (IndexOutOfBoundsException localIndexOutOfBoundsException)
    {
      throw SQLEngineGenericContext.s_SQLEngineMessages.createGeneralException(SQLEngineMessageKey.INVALID_ARGUMENT.name(), localIndexOutOfBoundsException, ExceptionType.DEFAULT);
    }
  }
  
  public String toString()
  {
    return this.m_listType.name();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/parser/parsetree/PTListNode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */