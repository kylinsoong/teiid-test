package com.simba.sqlengine.aeprocessor.aebuilder.value;

import com.simba.dsi.core.utilities.Variant;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderBase;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderCheck;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderCheck.NonterminalTypeMatcher;
import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.ScalarFunctionID;
import com.simba.sqlengine.aeprocessor.aetree.value.AECustomScalarFn;
import com.simba.sqlengine.aeprocessor.aetree.value.AELiteral;
import com.simba.sqlengine.aeprocessor.aetree.value.AEScalarFn;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExprList;
import com.simba.sqlengine.aeprocessor.metadatautil.AECoercionColumnInfo;
import com.simba.sqlengine.dsiext.dataengine.CustomScalarFunction;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngine;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngineContext;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.parser.parsetree.IPTNode;
import com.simba.sqlengine.parser.parsetree.PTIdentifierNode;
import com.simba.sqlengine.parser.parsetree.PTListNode;
import com.simba.sqlengine.parser.parsetree.PTLiteralNode;
import com.simba.sqlengine.parser.parsetree.PTNonterminalNode;
import com.simba.sqlengine.parser.type.PTListType;
import com.simba.sqlengine.parser.type.PTLiteralType;
import com.simba.sqlengine.parser.type.PTNonterminalType;
import com.simba.sqlengine.parser.type.PTPositionalType;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AEScalarFnBuilder
  extends AEBuilderBase<AEValueExpr>
{
  private static final Map<String, Map<Integer, ScalarFunctionID>> SCALAR_FN_LOOKUP;
  private SqlDataEngine m_dataEngine;
  
  protected AEScalarFnBuilder(AEQueryScope paramAEQueryScope)
  {
    super(paramAEQueryScope);
    this.m_dataEngine = paramAEQueryScope.getDataEngine();
  }
  
  public AEValueExpr visit(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    AEBuilderCheck.checkThat(paramPTNonterminalNode, ((AEBuilderCheck.NonterminalTypeMatcher)AEBuilderCheck.is(AEBuilderCheck.nonTerminal(PTNonterminalType.FUNC))).withExactChildren(PTPositionalType.NAME, AEBuilderCheck.identifier(), PTPositionalType.PARAM_LIST, AEBuilderCheck.optionalList(PTListType.PARAMETER_LIST)));
    String str = ((PTIdentifierNode)paramPTNonterminalNode.getChild(PTPositionalType.NAME)).getIdentifier();
    IPTNode localIPTNode = paramPTNonterminalNode.getChild(PTPositionalType.PARAM_LIST);
    PTListNode localPTListNode = localIPTNode.isEmptyNode() ? new PTListNode(PTListType.PARAMETER_LIST) : (PTListNode)localIPTNode;
    CustomScalarFunction localCustomScalarFunction = this.m_dataEngine.openScalarFunction(str, localPTListNode.numChildren());
    if (null != localCustomScalarFunction) {
      return buildCustomScalarFn(localPTListNode, localCustomScalarFunction);
    }
    Map localMap = (Map)SCALAR_FN_LOOKUP.get(str);
    if (null == localMap) {
      throw SQLEngineExceptionFactory.invalidScalarFnNameException(str);
    }
    ScalarFunctionID localScalarFunctionID = (ScalarFunctionID)localMap.get(Integer.valueOf(localPTListNode.numChildren()));
    if (null == localScalarFunctionID) {
      throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(str);
    }
    return buildScalarFn(str, localScalarFunctionID, localPTListNode);
  }
  
  private AECustomScalarFn buildCustomScalarFn(PTListNode paramPTListNode, CustomScalarFunction paramCustomScalarFunction)
    throws ErrorException
  {
    AEValueExprList localAEValueExprList = new AEValueExprList();
    if (0 < paramPTListNode.numChildren())
    {
      AEValueExprBuilder localAEValueExprBuilder = new AEValueExprBuilder(getQueryScope());
      Iterator localIterator = paramPTListNode.getImmutableChildList().iterator();
      while (localIterator.hasNext())
      {
        IPTNode localIPTNode = (IPTNode)localIterator.next();
        localAEValueExprList.addNode(localAEValueExprBuilder.build(localIPTNode));
      }
    }
    return new AECustomScalarFn(paramCustomScalarFunction, localAEValueExprList);
  }
  
  private AEScalarFn buildScalarFn(String paramString, ScalarFunctionID paramScalarFunctionID, PTListNode paramPTListNode)
    throws ErrorException
  {
    AEValueExprList localAEValueExprList = new AEValueExprList();
    ArrayList localArrayList = new ArrayList();
    if ((ScalarFunctionID.CONVERT == paramScalarFunctionID) || (ScalarFunctionID.CAST == paramScalarFunctionID))
    {
      if (2 != paramPTListNode.numChildren()) {
        throw SQLEngineExceptionFactory.invalidScalarFnArgumentCountException(paramString);
      }
      localObject1 = new AEValueExprBuilder(getQueryScope());
      localObject2 = (AEValueExpr)((AEValueExprBuilder)localObject1).build(paramPTListNode.getChild(0));
      localAEValueExprList.addNode((IAENode)localObject2);
      localArrayList.add(new AECoercionColumnInfo((AEValueExpr)localObject2));
      localObject3 = paramPTListNode.getChild(1);
      if ((localObject3 instanceof PTLiteralNode))
      {
        localObject2 = (AEValueExpr)((AEValueExprBuilder)localObject1).build((IPTNode)localObject3);
      }
      else if ((localObject3 instanceof PTIdentifierNode))
      {
        localObject2 = (AEValueExpr)((AEValueExprBuilder)localObject1).build(new PTLiteralNode(PTLiteralType.CHARSTR, ((PTIdentifierNode)localObject3).getIdentifier()));
      }
      else if (((localObject3 instanceof PTNonterminalNode)) && (PTNonterminalType.COLUMN_REFERENCE == ((PTNonterminalNode)localObject3).getNonterminalType()))
      {
        localObject4 = (PTNonterminalNode)localObject3;
        localObject2 = (AEValueExpr)((AEValueExprBuilder)localObject1).build(new PTLiteralNode(PTLiteralType.CHARSTR, ((PTIdentifierNode)((PTNonterminalNode)localObject4).getChild(PTPositionalType.COLUMN_NAME)).getIdentifier()));
      }
      else
      {
        throw (ScalarFunctionID.CONVERT == paramScalarFunctionID ? SQLEngineExceptionFactory.invalidSecondArgumentToConvertException() : SQLEngineExceptionFactory.invalidSecondArgumentToCastException());
      }
      localAEValueExprList.addNode((IAENode)localObject2);
      localArrayList.add(new AECoercionColumnInfo((AEValueExpr)localObject2));
    }
    else if ((ScalarFunctionID.USER == paramScalarFunctionID) || (ScalarFunctionID.DATABASE == paramScalarFunctionID))
    {
      assert (0 == paramPTListNode.numChildren());
      localObject1 = getQueryScope().getDataEngine().getContext();
      if (ScalarFunctionID.USER == paramScalarFunctionID) {
        localObject2 = ((SqlDataEngineContext)localObject1).getConnProperty(139).getString();
      } else {
        localObject2 = ((SqlDataEngineContext)localObject1).getConnProperty(22).getString();
      }
      localObject3 = new AELiteral((String)localObject2, PTLiteralType.CHARSTR, false, (SqlDataEngineContext)localObject1);
      ((AELiteral)localObject3).setParent(localAEValueExprList);
      localAEValueExprList.addNode((IAENode)localObject3);
      localArrayList.add(new AECoercionColumnInfo((AEValueExpr)localObject3));
    }
    else if (0 < paramPTListNode.numChildren())
    {
      localObject1 = new AEValueExprBuilder(getQueryScope());
      localObject2 = paramPTListNode.getImmutableChildList().iterator();
      while (((Iterator)localObject2).hasNext())
      {
        localObject3 = (IPTNode)((Iterator)localObject2).next();
        localObject4 = (AEValueExpr)((AEValueExprBuilder)localObject1).build((IPTNode)localObject3);
        localAEValueExprList.addNode((IAENode)localObject4);
        localArrayList.add(new AECoercionColumnInfo((AEValueExpr)localObject4));
      }
    }
    Object localObject1 = AEScalarFnMetadataFactory.getInstance();
    Object localObject2 = ((AEScalarFnMetadataFactory)localObject1).createMetadata(this.m_dataEngine.getContext(), paramScalarFunctionID, paramString, localArrayList);
    Object localObject3 = new AEScalarFn(paramString, paramScalarFunctionID, ((AEScalarFnMetadataFactory.ScalarFnMetadata)localObject2).getColumnMetadata(), localAEValueExprList, ((AEScalarFnMetadataFactory.ScalarFnMetadata)localObject2).getExpectedArgumentMetadata(), (AEScalarFnMetadataFactory)localObject1, this.m_dataEngine.getContext());
    localArrayList.clear();
    Object localObject4 = ((AEScalarFn)localObject3).getArguments().getChildItr();
    while (((Iterator)localObject4).hasNext()) {
      localArrayList.add(new AECoercionColumnInfo((AEValueExpr)((Iterator)localObject4).next()));
    }
    ((AEScalarFnMetadataFactory)localObject1).validateMetadata(this.m_dataEngine.getContext(), paramScalarFunctionID, paramString, localArrayList);
    return (AEScalarFn)localObject3;
  }
  
  static
  {
    TreeMap localTreeMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
    for (ScalarFunctionID localScalarFunctionID : ScalarFunctionID.values())
    {
      Object localObject = (Map)localTreeMap.get(localScalarFunctionID.getName());
      if (null == localObject)
      {
        localObject = new HashMap();
        localTreeMap.put(localScalarFunctionID.getName(), localObject);
      }
      ((Map)localObject).put(Integer.valueOf(localScalarFunctionID.getArguments().size()), localScalarFunctionID);
    }
    SCALAR_FN_LOOKUP = localTreeMap;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aebuilder/value/AEScalarFnBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */