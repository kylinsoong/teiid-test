package com.simba.sqlengine.executor.materializer;

import com.simba.dsi.core.utilities.Variant;
import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.exceptions.IncorrectTypeException;
import com.simba.dsi.exceptions.NumericOverflowException;
import com.simba.sqlengine.aeprocessor.aetree.AETreeWalker;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AERelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AETable;
import com.simba.sqlengine.aeprocessor.aetree.statement.AEInsert;
import com.simba.sqlengine.aeprocessor.aetree.statement.AEInsertDefaults;
import com.simba.sqlengine.aeprocessor.aetree.statement.AESetClause;
import com.simba.sqlengine.aeprocessor.aetree.statement.AESetClauseList;
import com.simba.sqlengine.aeprocessor.aetree.statement.AEUpdate;
import com.simba.sqlengine.aeprocessor.aetree.value.AEColumnReference;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExprList;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngineContext;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.etree.bool.ETBooleanExpr;
import com.simba.sqlengine.executor.etree.relation.ETRelationalCache;
import com.simba.sqlengine.executor.etree.relation.ETRelationalExpr;
import com.simba.sqlengine.executor.etree.relation.ETTable;
import com.simba.sqlengine.executor.etree.relation.ETTableConstructor;
import com.simba.sqlengine.executor.etree.statement.ETInsert;
import com.simba.sqlengine.executor.etree.statement.ETSearchedUpdate;
import com.simba.sqlengine.executor.etree.statement.ETSetClause;
import com.simba.sqlengine.executor.etree.statement.ETSetClauseList;
import com.simba.sqlengine.executor.etree.statement.RowCountStatement;
import com.simba.sqlengine.executor.etree.temptable.TemporaryTable;
import com.simba.sqlengine.executor.etree.temptable.TemporaryTableBuilder.TemporaryTableProperties;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.sqlengine.executor.etree.value.ETValueExprList;
import com.simba.sqlengine.executor.queryplan.IQueryPlan;
import com.simba.sqlengine.utilities.ExternalAlgorithmUtil;
import com.simba.sqlengine.utilities.ExternalAlgorithmUtil.ExternalAlgorithmProperties;
import com.simba.support.Pair;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ETDmlMaterializer
  extends MaterializerBase<RowCountStatement>
{
  public ETDmlMaterializer(IQueryPlan paramIQueryPlan, MaterializerContext paramMaterializerContext)
  {
    super(paramIQueryPlan, paramMaterializerContext);
  }
  
  public RowCountStatement visit(AEInsert paramAEInsert)
    throws ErrorException
  {
    AETable localAETable = paramAEInsert.getTable();
    ETTable localETTable = new ETTableMaterializer(getQueryPlan(), getContext()).visit(localAETable);
    AEValueExprList localAEValueExprList = paramAEInsert.getInsertColumns();
    ArrayList localArrayList1 = new ArrayList();
    int i;
    if (localAEValueExprList.getNumChildren() != 0) {
      for (i = 0; i < localAEValueExprList.getNumChildren(); i++)
      {
        AEColumnReference localAEColumnReference = (AEColumnReference)localAEValueExprList.getChild(i);
        localArrayList1.add(new Pair(Integer.valueOf(localAEColumnReference.getColumnNum()), localAEColumnReference.getColumn()));
      }
    } else {
      for (i = 0; i < localAETable.getColumnCount(); i++) {
        localArrayList1.add(new Pair(Integer.valueOf(i), localAETable.getBaseColumn(i)));
      }
    }
    boolean[] arrayOfBoolean = new boolean[paramAEInsert.getRelationalExpr().getColumnCount()];
    for (int j = 0; j < arrayOfBoolean.length; j++) {
      if (paramAEInsert.getRelationalExpr().getDataNeeded(j)) {
        arrayOfBoolean[j] = true;
      }
    }
    ETRelationalExprMaterializer localETRelationalExprMaterializer = new ETRelationalExprMaterializer(getQueryPlan(), getContext());
    localETRelationalExprMaterializer.setErrorOnTruncate(true);
    Object localObject = (ETRelationalExpr)paramAEInsert.getRelationalExpr().acceptVisitor(localETRelationalExprMaterializer);
    if ((paramAEInsert.isRecursive()) && (!isCursorInsensitive())) {
      localObject = cacheRelation((ETRelationalExpr)localObject, arrayOfBoolean, paramAEInsert.getRelationalExpr().getResultSetColumns(), "INSERT");
    }
    ArrayList localArrayList2 = new ArrayList(localArrayList1.size());
    Iterator localIterator = localArrayList1.iterator();
    while (localIterator.hasNext())
    {
      Pair localPair = (Pair)localIterator.next();
      localArrayList2.add(localPair.value());
    }
    localObject = ConvMaterializeUtil.makeNewRelationConvertNode(localArrayList2, (ETRelationalExpr)localObject, arrayOfBoolean, true, getContext());
    return new ETInsert(localETTable, localArrayList1, (ETRelationalExpr)localObject, getContext().getParameters());
  }
  
  public RowCountStatement visit(AEInsertDefaults paramAEInsertDefaults)
    throws ErrorException
  {
    List localList1 = Collections.emptyList();
    ETTableConstructor localETTableConstructor = new ETTableConstructor(new ETValueExprList(), localList1, new boolean[0]);
    ETTable localETTable = new ETTableMaterializer(getQueryPlan(), getContext()).visit(paramAEInsertDefaults.getTable());
    List localList2 = Collections.emptyList();
    return new ETInsert(localETTable, localList2, localETTableConstructor, getContext().getParameters());
  }
  
  public RowCountStatement visit(AEUpdate paramAEUpdate)
    throws ErrorException
  {
    ETRelationalExprMaterializer localETRelationalExprMaterializer = new ETRelationalExprMaterializer(getQueryPlan(), getContext());
    AETable localAETable = paramAEUpdate.getTable();
    AESetClauseList localAESetClauseList = paramAEUpdate.getSetClauses();
    ETTable localETTable = localETRelationalExprMaterializer.visit(localAETable);
    List localList = paramAEUpdate.getTable().getResultSetColumns();
    boolean bool = paramAEUpdate.doInsertOnRc0();
    if (bool)
    {
      localObject1 = localAESetClauseList.iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject2 = (AESetClause)((Iterator)localObject1).next();
        AETreeWalker localAETreeWalker = new AETreeWalker(((AESetClause)localObject2).getRightOperand());
        while (localAETreeWalker.hasNext())
        {
          IAENode localIAENode = localAETreeWalker.next();
          if ((localIAENode instanceof AEColumnReference))
          {
            AEColumnReference localAEColumnReference = (AEColumnReference)localIAENode;
            if (localAEColumnReference.getNamedRelationalExpr() == localAETable) {
              throw SQLEngineExceptionFactory.invalidUpsertQueryException("Table columns are referenced in the set clause");
            }
          }
        }
      }
    }
    Object localObject1 = materializeSetClauses(localAESetClauseList, localList);
    Object localObject2 = (ETBooleanExpr)paramAEUpdate.getUpdateCondition().acceptVisitor(new ETBoolExprMaterializer(getQueryPlan(), getContext()));
    return new ETSearchedUpdate(localETTable, (ETSetClauseList)localObject1, (ETBooleanExpr)localObject2, bool, getContext().getParameters());
  }
  
  private ETSetClauseList materializeSetClauses(AESetClauseList paramAESetClauseList, List<IColumn> paramList)
    throws ErrorException
  {
    ETValueExprMaterializer localETValueExprMaterializer = new ETValueExprMaterializer(getQueryPlan(), getContext());
    ETSetClauseList localETSetClauseList = new ETSetClauseList();
    Iterator localIterator = paramAESetClauseList.iterator();
    while (localIterator.hasNext())
    {
      AESetClause localAESetClause = (AESetClause)localIterator.next();
      int i = localAESetClause.getLeftOperand().getColumnNum();
      ETValueExpr localETValueExpr = (ETValueExpr)localAESetClause.getRightOperand().acceptVisitor(localETValueExprMaterializer);
      localETValueExpr = ConvMaterializeUtil.addConversionNodeWhenNeeded(localETValueExpr, localAESetClause.getRightOperand().getColumn(), (IColumn)paramList.get(i), true, getContext());
      localETSetClauseList.add(new ETSetClause(localETValueExpr, i));
    }
    return localETSetClauseList;
  }
  
  private ETRelationalExpr cacheRelation(ETRelationalExpr paramETRelationalExpr, boolean[] paramArrayOfBoolean, List<IColumn> paramList, String paramString)
    throws ErrorException
  {
    ExternalAlgorithmUtil.ExternalAlgorithmProperties localExternalAlgorithmProperties = getContext().getExternalAlgorithmProperties();
    TemporaryTableBuilder.TemporaryTableProperties localTemporaryTableProperties = new TemporaryTableBuilder.TemporaryTableProperties(localExternalAlgorithmProperties.getStorageDir(), localExternalAlgorithmProperties.getCellMemoryLimit(), localExternalAlgorithmProperties.getBlockSize(), ExternalAlgorithmUtil.calculateRowSize(paramList, paramArrayOfBoolean, localExternalAlgorithmProperties.getCellMemoryLimit()), localExternalAlgorithmProperties.getMaxNumOpenFiles(), localExternalAlgorithmProperties.getLogger(), paramString);
    return new ETRelationalCache(paramETRelationalExpr, new TemporaryTable(paramList, localTemporaryTableProperties, paramArrayOfBoolean));
  }
  
  private boolean isCursorInsensitive()
    throws ErrorException
  {
    Variant localVariant = getContext().getDataEngineContext().getConnProperty(39);
    try
    {
      return 1L == localVariant.getLong();
    }
    catch (IncorrectTypeException localIncorrectTypeException)
    {
      throw new AssertionError(localIncorrectTypeException);
    }
    catch (NumericOverflowException localNumericOverflowException)
    {
      throw new AssertionError(localNumericOverflowException);
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/materializer/ETDmlMaterializer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */