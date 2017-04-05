package com.simba.sqlengine.aeprocessor.aebuilder.value;

import com.simba.sqlengine.aeprocessor.AEQColumnName;
import com.simba.sqlengine.aeprocessor.AEQTableName;
import com.simba.sqlengine.aeprocessor.AEUtils;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderBase;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderCheck;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderCheck.NonterminalTypeMatcher;
import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope;
import com.simba.sqlengine.aeprocessor.aetree.value.AEColumnReference;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngine;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngineContext;
import com.simba.sqlengine.parser.parsetree.IPTNode;
import com.simba.sqlengine.parser.parsetree.PTIdentifierNode;
import com.simba.sqlengine.parser.parsetree.PTNonterminalNode;
import com.simba.sqlengine.parser.type.PTNonterminalType;
import com.simba.sqlengine.parser.type.PTPositionalType;
import com.simba.support.exceptions.ErrorException;

public class AEColumnReferenceBuilder
  extends AEBuilderBase<AEColumnReference>
{
  public AEColumnReferenceBuilder(AEQueryScope paramAEQueryScope)
  {
    super(paramAEQueryScope);
  }
  
  public AEColumnReference visit(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    return buildColumnReference(paramPTNonterminalNode);
  }
  
  public AEColumnReference visit(PTIdentifierNode paramPTIdentifierNode)
    throws ErrorException
  {
    return buildColumnReference(paramPTIdentifierNode);
  }
  
  public static AEQColumnName buildQualifiedColumnName(SqlDataEngineContext paramSqlDataEngineContext, IPTNode paramIPTNode)
    throws ErrorException
  {
    AEBuilderCheck.checkThat(paramIPTNode, ((AEBuilderCheck.NonterminalTypeMatcher)AEBuilderCheck.is(AEBuilderCheck.nonTerminal(PTNonterminalType.COLUMN_REFERENCE))).withExactChildren(PTPositionalType.CATALOG_IDENT, AEBuilderCheck.optional(AEBuilderCheck.identifier()), PTPositionalType.SCHEMA_IDENT, AEBuilderCheck.optional(AEBuilderCheck.identifier()), PTPositionalType.TABLE_IDENT, AEBuilderCheck.optional(AEBuilderCheck.identifier()), PTPositionalType.COLUMN_NAME, AEBuilderCheck.identifier()));
    PTNonterminalNode localPTNonterminalNode = (PTNonterminalNode)paramIPTNode;
    IPTNode localIPTNode1 = localPTNonterminalNode.getChild(PTPositionalType.CATALOG_IDENT);
    IPTNode localIPTNode2 = localPTNonterminalNode.getChild(PTPositionalType.SCHEMA_IDENT);
    IPTNode localIPTNode3 = localPTNonterminalNode.getChild(PTPositionalType.TABLE_IDENT);
    IPTNode localIPTNode4 = localPTNonterminalNode.getChild(PTPositionalType.COLUMN_NAME);
    String str1 = ((PTIdentifierNode)localIPTNode4).getIdentifier();
    String str2 = AEUtils.getIdentifierString(localIPTNode1);
    String str3 = AEUtils.getIdentifierString(localIPTNode2);
    String str4 = AEUtils.getIdentifierString(localIPTNode3);
    AEQTableName localAEQTableName = AEUtils.adjustCatalogAndSchema(new AEQTableName(str2, str3, str4), paramSqlDataEngineContext, false);
    return new AEQColumnName(localAEQTableName, str1);
  }
  
  private AEColumnReference buildColumnReference(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    AEQColumnName localAEQColumnName = buildQualifiedColumnName(getQueryScope().getDataEngine().getContext(), paramPTNonterminalNode);
    return new AEColumnReference(getQueryScope().resolveColumn(localAEQColumnName));
  }
  
  private AEColumnReference buildColumnReference(PTIdentifierNode paramPTIdentifierNode)
    throws ErrorException
  {
    String str = paramPTIdentifierNode.getIdentifier();
    AEQColumnName localAEQColumnName = new AEQColumnName(AEQTableName.empty(), str);
    return new AEColumnReference(getQueryScope().resolveColumn(localAEQColumnName));
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aebuilder/value/AEColumnReferenceBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */