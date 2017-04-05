package com.simba.sqlengine.parser;

import com.simba.sqlengine.parser.parsetree.IPTNode;
import com.simba.sqlengine.parser.parsetree.PTDefaultParameterNode;
import com.simba.sqlengine.parser.parsetree.PTDynamicParameterNode;
import com.simba.sqlengine.parser.parsetree.PTEmptyNode;
import com.simba.sqlengine.parser.parsetree.PTFlagNode;
import com.simba.sqlengine.parser.parsetree.PTIdentifierNode;
import com.simba.sqlengine.parser.parsetree.PTListNode;
import com.simba.sqlengine.parser.parsetree.PTLiteralNode;
import com.simba.sqlengine.parser.parsetree.PTNonterminalNode;
import com.simba.sqlengine.parser.type.PTFlagType;
import com.simba.sqlengine.parser.type.PTListType;
import com.simba.sqlengine.parser.type.PTLiteralType;
import com.simba.sqlengine.parser.type.PTNonterminalType;
import com.simba.sqlengine.parser.type.PTPositionalType;
import com.simba.support.exceptions.ErrorException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class PTNodeFactory
{
  private static final PTEmptyNode EMPTY_NODE;
  private static final PTDefaultParameterNode DEFAULT_PARAM_NODE;
  private static final PTFlagNode[] FLAG_CACHE;
  private int m_parameterCount = 0;
  
  public PTDefaultParameterNode buildDefaultParameterNode()
  {
    return DEFAULT_PARAM_NODE;
  }
  
  public PTDynamicParameterNode buildDynamicParameterNode()
  {
    return new PTDynamicParameterNode(nextParameterIndex());
  }
  
  public PTEmptyNode buildEmptyNode()
  {
    return EMPTY_NODE;
  }
  
  public PTFlagNode buildFlagNode(PTFlagType paramPTFlagType)
  {
    assert (null != paramPTFlagType);
    assert (paramPTFlagType.ordinal() < FLAG_CACHE.length);
    return FLAG_CACHE[paramPTFlagType.ordinal()];
  }
  
  public PTIdentifierNode buildIdentifierNode(String paramString)
  {
    return new PTIdentifierNode(paramString);
  }
  
  public PTListNode buildListNode(PTListType paramPTListType, IPTNode... paramVarArgs)
  {
    return buildListNode(paramPTListType, Arrays.asList(paramVarArgs));
  }
  
  public PTListNode buildListNode(PTListType paramPTListType, List<IPTNode> paramList)
  {
    if (null == paramPTListType) {
      throw new NullPointerException("listType must not be null");
    }
    if (null == paramList) {
      throw new NullPointerException("children must not be null");
    }
    PTListNode localPTListNode = new PTListNode(paramPTListType);
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      IPTNode localIPTNode = (IPTNode)localIterator.next();
      if (null == localIPTNode) {
        throw new NullPointerException("Child node must not be null");
      }
      localPTListNode.addChild(localIPTNode);
    }
    return localPTListNode;
  }
  
  public PTLiteralNode buildLiteralNode(PTLiteralType paramPTLiteralType, String paramString)
  {
    if ((null == paramPTLiteralType) || (null == paramString)) {
      throw new NullPointerException();
    }
    return new PTLiteralNode(paramPTLiteralType, paramString);
  }
  
  public PTNonterminalNode buildNonterminalNode(PTNonterminalType paramPTNonterminalType)
  {
    if (null == paramPTNonterminalType) {
      throw new NullPointerException("ntType must not be null");
    }
    return new PTNonterminalNode(paramPTNonterminalType);
  }
  
  public PTNonterminalNode buildNonterminalNode(PTNonterminalType paramPTNonterminalType, Object... paramVarArgs)
  {
    if (0 != paramVarArgs.length % 2) {
      throw new IllegalArgumentException("Missing value for key");
    }
    PTNonterminalNode localPTNonterminalNode = new PTNonterminalNode(paramPTNonterminalType);
    for (int i = 0; i < paramVarArgs.length; i += 2)
    {
      Object localObject1 = paramVarArgs[i];
      Object localObject2 = paramVarArgs[(i + 1)];
      if ((!(localObject1 instanceof PTPositionalType)) || (!(localObject2 instanceof IPTNode))) {
        throw new IllegalArgumentException("Invalid key or value: " + localObject1 + ", " + localObject2);
      }
      try
      {
        localPTNonterminalNode.addChild((PTPositionalType)localObject1, (IPTNode)localObject2);
      }
      catch (ErrorException localErrorException)
      {
        throw new RuntimeException("Logic error", localErrorException);
      }
    }
    return localPTNonterminalNode;
  }
  
  private int nextParameterIndex()
  {
    return ++this.m_parameterCount;
  }
  
  static
  {
    EMPTY_NODE = new PTEmptyNode();
    DEFAULT_PARAM_NODE = new PTDefaultParameterNode();
    PTFlagType[] arrayOfPTFlagType = PTFlagType.values();
    FLAG_CACHE = new PTFlagNode[arrayOfPTFlagType.length];
    for (int i = 0; i < arrayOfPTFlagType.length; i++) {
      FLAG_CACHE[i] = new PTFlagNode(arrayOfPTFlagType[i]);
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/parser/PTNodeFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */