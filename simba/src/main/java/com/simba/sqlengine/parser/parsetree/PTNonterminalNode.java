package com.simba.sqlengine.parser.parsetree;

import com.simba.sqlengine.SQLEngineGenericContext;
import com.simba.sqlengine.parser.type.PTNonterminalType;
import com.simba.sqlengine.parser.type.PTPositionalType;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.exceptions.ExceptionBuilder;
import com.simba.support.exceptions.ExceptionType;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Set;

public class PTNonterminalNode
  extends AbstractPTNonterminalNode
{
  private EnumMap<PTPositionalType, IPTNode> m_children;
  private final PTNonterminalType m_nonterminalType;
  
  public PTNonterminalNode(PTNonterminalType paramPTNonterminalType)
  {
    if (null == paramPTNonterminalType) {
      throw new NullPointerException("Type cannot be null.");
    }
    this.m_children = new EnumMap(PTPositionalType.class);
    this.m_nonterminalType = paramPTNonterminalType;
  }
  
  public <T> T acceptVisitor(IPTVisitor<T> paramIPTVisitor)
    throws ErrorException
  {
    if (null == paramIPTVisitor) {
      throw new NullPointerException("Visitor cannot be null.");
    }
    return (T)paramIPTVisitor.visit(this);
  }
  
  public PTNonterminalNode addChild(PTPositionalType paramPTPositionalType, IPTNode paramIPTNode)
    throws ErrorException
  {
    if ((null == paramPTPositionalType) || (null == paramIPTNode)) {
      throw new NullPointerException("Invalid arguments.");
    }
    if (this.m_children.containsKey(paramPTPositionalType)) {
      throw SQLEngineGenericContext.s_SQLEngineMessages.createGeneralException(SQLEngineMessageKey.INVALID_ARGUMENT.name(), ExceptionType.DEFAULT);
    }
    this.m_children.put(paramPTPositionalType, paramIPTNode);
    return this;
  }
  
  public Set<PTPositionalType> getAllPositionalTypes()
  {
    return Collections.unmodifiableSet(this.m_children.keySet());
  }
  
  public IPTNode getChild(PTPositionalType paramPTPositionalType)
  {
    return (IPTNode)this.m_children.get(paramPTPositionalType);
  }
  
  public Iterator<IPTNode> getChildItr()
  {
    return this.m_children.values().iterator();
  }
  
  public PTNonterminalType getNonterminalType()
  {
    return this.m_nonterminalType;
  }
  
  public int numChildren()
  {
    return this.m_children.size();
  }
  
  public IPTNode removeChild(PTPositionalType paramPTPositionalType)
  {
    return (IPTNode)this.m_children.remove(paramPTPositionalType);
  }
  
  public IPTNode replaceChild(PTPositionalType paramPTPositionalType, IPTNode paramIPTNode)
  {
    if (null == paramIPTNode) {
      throw new NullPointerException("Child cannot be null.");
    }
    return (IPTNode)this.m_children.put(paramPTPositionalType, paramIPTNode);
  }
  
  public String toString()
  {
    return this.m_nonterminalType.name();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/parser/parsetree/PTNonterminalNode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */