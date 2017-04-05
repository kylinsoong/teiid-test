package com.simba.sqlengine.aeprocessor.aebuilder.statement;

import com.simba.dsi.core.utilities.Variant;
import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.Nullable;
import com.simba.dsi.exceptions.IncorrectTypeException;
import com.simba.dsi.exceptions.NumericOverflowException;
import com.simba.sqlengine.aeprocessor.AEQTableName;
import com.simba.sqlengine.aeprocessor.AEUtils;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderBase;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderCheck;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderCheck.NonterminalMatcher;
import com.simba.sqlengine.aeprocessor.aebuilder.AEBuilderCheck.NonterminalTypeMatcher;
import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope;
import com.simba.sqlengine.aeprocessor.aetree.statement.AECreateTable;
import com.simba.sqlengine.aeprocessor.aetree.statement.AEDropTable;
import com.simba.sqlengine.aeprocessor.aetree.statement.IAEStatement;
import com.simba.sqlengine.dsiext.dataengine.IColumnFactory;
import com.simba.sqlengine.dsiext.dataengine.SqlCustomBehaviourProvider;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngine;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngineContext;
import com.simba.sqlengine.dsiext.dataengine.ddl.PrimaryKeyConstraint;
import com.simba.sqlengine.dsiext.dataengine.ddl.TableConstraint;
import com.simba.sqlengine.dsiext.dataengine.ddl.TableSpecification;
import com.simba.sqlengine.dsiext.dataengine.ddl.UniqueConstraint;
import com.simba.sqlengine.exceptions.SQLEngineException;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.parser.parsetree.IPTNode;
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
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.Pair;
import com.simba.support.exceptions.DiagState;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class AEDdlStatementBuilder
  extends AEBuilderBase<IAEStatement>
{
  public AEDdlStatementBuilder(AEQueryScope paramAEQueryScope)
  {
    super(paramAEQueryScope);
  }
  
  public IAEStatement visit(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    switch (paramPTNonterminalNode.getNonterminalType())
    {
    case CREATE_TABLE_STATEMENT: 
      return buildCreateTableStmt(paramPTNonterminalNode);
    case DROP_TABLE_STATEMENT: 
      return buildDropTableStmt(paramPTNonterminalNode);
    }
    return (IAEStatement)super.visit(paramPTNonterminalNode);
  }
  
  private AEDropTable buildDropTableStmt(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    AEQueryScope localAEQueryScope = getQueryScope();
    long l;
    try
    {
      l = localAEQueryScope.getDataEngine().getContext().getConnProperty(52).getLong();
    }
    catch (IncorrectTypeException localIncorrectTypeException)
    {
      throw new RuntimeException(localIncorrectTypeException);
    }
    catch (NumericOverflowException localNumericOverflowException)
    {
      throw new RuntimeException(localNumericOverflowException);
    }
    if (0L == (l & 1L)) {
      throw new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.DROP_TABLE_NOT_SUPPORTED.name());
    }
    AEUtils.checkReadOnly(localAEQueryScope.getDataEngine().getContext(), "DROP TABLE");
    AEBuilderCheck.checkThat(paramPTNonterminalNode, AEBuilderCheck.is(AEBuilderCheck.nonTerminal(PTNonterminalType.DROP_TABLE_STATEMENT).withExactChildren(PTPositionalType.TABLE_NAME, AEBuilderCheck.nonTerminal(PTNonterminalType.TABLE_NAME))));
    AEQTableName localAEQTableName = AEQTableName.fromPTNode((PTNonterminalNode)paramPTNonterminalNode.getChild(PTPositionalType.TABLE_NAME));
    if (!localAEQueryScope.getDataEngine().doesTableExist(localAEQTableName.getCatalogName(), localAEQTableName.getSchemaName(), localAEQTableName.getTableName())) {
      throw new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.TABLE_OR_VIEW_NOT_EXISTS.name());
    }
    Variant localVariant = getQueryScope().getDataEngine().getProperty(15);
    String str = localVariant.getString();
    if (str.equalsIgnoreCase("Y")) {
      localAEQTableName = AEUtils.adjustCatalogAndSchema(localAEQTableName, localAEQueryScope.getDataEngine().getContext(), true);
    } else {
      localAEQTableName = AEUtils.adjustCatalogAndSchema(localAEQTableName, localAEQueryScope.getDataEngine().getContext(), false);
    }
    boolean bool = localAEQueryScope.isCaseSensitive();
    return new AEDropTable(localAEQTableName.getCatalogName(), localAEQTableName.getSchemaName(), localAEQTableName.getTableName(), bool);
  }
  
  private AECreateTable buildCreateTableStmt(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    AEQueryScope localAEQueryScope = getQueryScope();
    long l;
    try
    {
      l = localAEQueryScope.getDataEngine().getContext().getConnProperty(34).getLong();
    }
    catch (IncorrectTypeException localIncorrectTypeException)
    {
      throw new RuntimeException(localIncorrectTypeException);
    }
    catch (NumericOverflowException localNumericOverflowException)
    {
      throw new RuntimeException(localNumericOverflowException);
    }
    if (0L == (l & 1L)) {
      throw new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.CREATE_TABLE_NOT_SUPPORTED.name());
    }
    AEUtils.checkReadOnly(localAEQueryScope.getDataEngine().getContext(), "CREATE TABLE");
    AEBuilderCheck.checkThat(paramPTNonterminalNode, AEBuilderCheck.is(AEBuilderCheck.nonTerminal(PTNonterminalType.CREATE_TABLE_STATEMENT).withExactChildren(PTPositionalType.TABLE_NAME, AEBuilderCheck.nonTerminal(PTNonterminalType.TABLE_NAME), PTPositionalType.COLUMN_DEFINITION_LIST, AEBuilderCheck.list(PTListType.COLUMN_DEFINITION_LIST), PTPositionalType.TABLE_CONSTRAINT_DEFINITION_LIST, AEBuilderCheck.optionalList(PTListType.TABLE_CONSTRAINT_DEFINITION_LIST))));
    AEQTableName localAEQTableName = AEQTableName.fromPTNode((PTNonterminalNode)paramPTNonterminalNode.getChild(PTPositionalType.TABLE_NAME));
    if (localAEQueryScope.getDataEngine().doesTableExist(localAEQTableName.getCatalogName(), localAEQTableName.getSchemaName(), localAEQTableName.getTableName())) {
      throw new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.TABLE_OR_VIEW_ALREADY_EXISTS.name());
    }
    Variant localVariant = getQueryScope().getDataEngine().getProperty(15);
    String str = localVariant.getString();
    if (str.equalsIgnoreCase("Y")) {
      localAEQTableName = AEUtils.adjustCatalogAndSchema(localAEQTableName, localAEQueryScope.getDataEngine().getContext(), true);
    } else {
      localAEQTableName = AEUtils.adjustCatalogAndSchema(localAEQTableName, localAEQueryScope.getDataEngine().getContext(), false);
    }
    boolean bool = localAEQueryScope.isCaseSensitive();
    PTListNode localPTListNode = (PTListNode)paramPTNonterminalNode.getChild(PTPositionalType.COLUMN_DEFINITION_LIST);
    ArrayList localArrayList = new ArrayList();
    Object localObject1 = null;
    if (bool) {
      localObject1 = new HashSet();
    } else {
      localObject1 = new TreeSet(String.CASE_INSENSITIVE_ORDER);
    }
    IColumnFactory localIColumnFactory = localAEQueryScope.getDataEngine().getContext().getCustomBehaviourProvider().getColumnFactory();
    Object localObject2 = localPTListNode.getImmutableChildList().iterator();
    while (((Iterator)localObject2).hasNext())
    {
      localObject3 = (IPTNode)((Iterator)localObject2).next();
      IColumn localIColumn = createColumnDefinition(localAEQTableName, (IPTNode)localObject3, localIColumnFactory);
      if (((Set)localObject1).contains(localIColumn.getName())) {
        throw new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.COLUMN_NAME_DUPLICATED.name(), new String[] { localIColumn.getName(), localAEQTableName.getTableName() });
      }
      ((Set)localObject1).add(localIColumn.getName());
      localArrayList.add(localIColumn);
    }
    localObject2 = paramPTNonterminalNode.getChild(PTPositionalType.TABLE_CONSTRAINT_DEFINITION_LIST);
    Object localObject3 = getTableConstraints((Set)localObject1, (IPTNode)localObject2, (l & 0x1000) != 0L);
    return new AECreateTable(new TableSpecification(localAEQTableName.getCatalogName(), localAEQTableName.getSchemaName(), localAEQTableName.getTableName(), localArrayList, (List)localObject3), bool);
  }
  
  private IColumn createColumnDefinition(AEQTableName paramAEQTableName, IPTNode paramIPTNode, IColumnFactory paramIColumnFactory)
    throws ErrorException
  {
    AEBuilderCheck.checkThat(paramIPTNode, AEBuilderCheck.is(AEBuilderCheck.nonTerminal(PTNonterminalType.COLUMN_DEFINITION).withExactChildren(PTPositionalType.COLUMN_NAME, AEBuilderCheck.identifier(), PTPositionalType.COLUMN_TYPE, AEBuilderCheck.nonTerminal(PTNonterminalType.DATA_TYPE), PTPositionalType.COLUMN_CONSTRAINT, AEBuilderCheck.optional(AEBuilderCheck.nonTerminal(PTNonterminalType.COLUMN_CONSTRAINT_DEFINITION)))));
    PTNonterminalNode localPTNonterminalNode1 = (PTNonterminalNode)paramIPTNode;
    PTIdentifierNode localPTIdentifierNode = (PTIdentifierNode)localPTNonterminalNode1.getChild(PTPositionalType.COLUMN_NAME);
    String str = localPTIdentifierNode.getIdentifier();
    PTNonterminalNode localPTNonterminalNode2 = (PTNonterminalNode)localPTNonterminalNode1.getChild(PTPositionalType.COLUMN_TYPE);
    Pair localPair = getTypeInfo(localPTNonterminalNode2);
    IPTNode localIPTNode = localPTNonterminalNode1.getChild(PTPositionalType.COLUMN_CONSTRAINT);
    Nullable localNullable = getNullable(localIPTNode);
    return paramIColumnFactory.createColumn(paramAEQTableName.getCatalogName(), paramAEQTableName.getSchemaName(), paramAEQTableName.getTableName(), str, (String)localPair.key(), (List)localPair.value(), localNullable);
  }
  
  private Pair<String, List<String>> getTypeInfo(PTNonterminalNode paramPTNonterminalNode)
    throws ErrorException
  {
    AEBuilderCheck.checkThat(paramPTNonterminalNode, AEBuilderCheck.is(AEBuilderCheck.nonTerminal(PTNonterminalType.DATA_TYPE).withExactChildren(PTPositionalType.DATA_TYPE_IDENTIFIER, AEBuilderCheck.identifier(), PTPositionalType.DATA_TYPE_ATTRIBUTE_LIST, AEBuilderCheck.optionalList(PTListType.DATA_TYPE_ATTRIBUTE_LIST))));
    PTIdentifierNode localPTIdentifierNode = (PTIdentifierNode)paramPTNonterminalNode.getChild(PTPositionalType.DATA_TYPE_IDENTIFIER);
    String str = localPTIdentifierNode.getIdentifier();
    List localList = getTypeParam(paramPTNonterminalNode.getChild(PTPositionalType.DATA_TYPE_ATTRIBUTE_LIST));
    return new Pair(str, localList);
  }
  
  private List<String> getTypeParam(IPTNode paramIPTNode)
    throws ErrorException
  {
    ArrayList localArrayList = new ArrayList();
    if (paramIPTNode.isEmptyNode()) {
      return localArrayList;
    }
    PTListNode localPTListNode = (PTListNode)paramIPTNode;
    Iterator localIterator = localPTListNode.getImmutableChildList().iterator();
    while (localIterator.hasNext())
    {
      IPTNode localIPTNode = (IPTNode)localIterator.next();
      if ((localIPTNode instanceof PTIdentifierNode))
      {
        localArrayList.add(((PTIdentifierNode)localIPTNode).getIdentifier());
      }
      else if ((localIPTNode instanceof PTLiteralNode))
      {
        PTLiteralNode localPTLiteralNode = (PTLiteralNode)localIPTNode;
        if (PTLiteralType.CHARSTR == localPTLiteralNode.getLiteralType()) {
          localArrayList.add("'" + localPTLiteralNode.getStringValue() + "'");
        } else {
          localArrayList.add(localPTLiteralNode.getStringValue());
        }
      }
      else
      {
        throw SQLEngineExceptionFactory.invalidParseTreeException();
      }
    }
    return localArrayList;
  }
  
  private Nullable getNullable(IPTNode paramIPTNode)
    throws ErrorException
  {
    if (paramIPTNode.isEmptyNode()) {
      return Nullable.UNKNOWN;
    }
    if ((paramIPTNode instanceof PTNonterminalNode))
    {
      AEBuilderCheck.checkThat(paramIPTNode, AEBuilderCheck.is(AEBuilderCheck.nonTerminal().withExactChildren(PTPositionalType.COLUMN_CONSTRAINT_FLAG, AEBuilderCheck.flagNode())));
      PTFlagNode localPTFlagNode = (PTFlagNode)((PTNonterminalNode)paramIPTNode).getChild(PTPositionalType.COLUMN_CONSTRAINT_FLAG);
      if (PTFlagType.NULL == localPTFlagNode.getFlagType()) {
        return Nullable.NULLABLE;
      }
      if (PTFlagType.NOT_NULL == localPTFlagNode.getFlagType()) {
        return Nullable.NO_NULLS;
      }
    }
    throw SQLEngineExceptionFactory.invalidParseTreeException();
  }
  
  private List<TableConstraint> getTableConstraints(Set<String> paramSet, IPTNode paramIPTNode, boolean paramBoolean)
    throws ErrorException
  {
    assert (paramSet != null);
    assert (paramIPTNode != null);
    ArrayList localArrayList = new ArrayList();
    if (paramIPTNode.isEmptyNode()) {
      return localArrayList;
    }
    if (!paramBoolean) {
      throw new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.TABLE_CONSTRAINTS_NOT_SUPPORTED.name());
    }
    AEBuilderCheck.checkThat(paramIPTNode, AEBuilderCheck.optionalList(PTListType.TABLE_CONSTRAINT_DEFINITION_LIST));
    PTListNode localPTListNode1 = (PTListNode)paramIPTNode;
    Iterator localIterator1 = localPTListNode1.getImmutableChildList().iterator();
    while (localIterator1.hasNext())
    {
      IPTNode localIPTNode1 = (IPTNode)localIterator1.next();
      AEBuilderCheck.checkThat(localIPTNode1, AEBuilderCheck.is(AEBuilderCheck.nonTerminal(PTNonterminalType.TABLE_CONSTRAINT_DEFINITION).withExactChildren(PTPositionalType.UNIQUE_SPECIFICATION, AEBuilderCheck.flagNode(PTFlagType.UNIQUE, new PTFlagType[] { PTFlagType.PRIMARY_KEY }), PTPositionalType.COLUMN_LIST, AEBuilderCheck.list(PTListType.COLUMN_NAME_LIST))));
      PTNonterminalNode localPTNonterminalNode = (PTNonterminalNode)localIPTNode1;
      PTFlagType localPTFlagType = ((PTFlagNode)localPTNonterminalNode.getChild(PTPositionalType.UNIQUE_SPECIFICATION)).getFlagType();
      PTListNode localPTListNode2 = (PTListNode)localPTNonterminalNode.getChild(PTPositionalType.COLUMN_LIST);
      HashSet localHashSet = new HashSet();
      Iterator localIterator2 = localPTListNode2.getImmutableChildList().iterator();
      while (localIterator2.hasNext())
      {
        IPTNode localIPTNode2 = (IPTNode)localIterator2.next();
        if ((localIPTNode2 instanceof PTIdentifierNode))
        {
          String str = ((PTIdentifierNode)localIPTNode2).getIdentifier();
          if (!paramSet.contains(str)) {
            throw new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.INVALID_TABLE_CONSTRAINT_SPECIFICATION.name(), new String[] { "column name " + str + " is not defined" });
          }
          localHashSet.add(str);
        }
      }
      if (localPTFlagType == PTFlagType.UNIQUE) {
        localArrayList.add(new UniqueConstraint(null, localHashSet));
      } else {
        localArrayList.add(new PrimaryKeyConstraint(null, localHashSet));
      }
    }
    return localArrayList;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aebuilder/statement/AEDdlStatementBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */