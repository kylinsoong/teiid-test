package com.simba.sqlengine.executor.materializer;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEAggregate;
import com.simba.sqlengine.aeprocessor.aetree.relation.AECrossJoin;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEDistinct;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEExcept;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEJoin;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEProject;
import com.simba.sqlengine.aeprocessor.aetree.relation.AERelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AESelect;
import com.simba.sqlengine.aeprocessor.aetree.relation.AESort;
import com.simba.sqlengine.aeprocessor.aetree.relation.AESubQuery;
import com.simba.sqlengine.aeprocessor.aetree.relation.AETable;
import com.simba.sqlengine.aeprocessor.aetree.relation.AETableConstructor;
import com.simba.sqlengine.aeprocessor.aetree.relation.AETop;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEUnion;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExprList;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngineContext;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.sqlengine.executor.etree.bool.ETBooleanExpr;
import com.simba.sqlengine.executor.etree.relation.ETProject;
import com.simba.sqlengine.executor.etree.relation.ETRelationalExpr;
import com.simba.sqlengine.executor.etree.relation.ETSelect;
import com.simba.sqlengine.executor.etree.relation.ETSort;
import com.simba.sqlengine.executor.etree.relation.ETTable;
import com.simba.sqlengine.executor.etree.relation.ETTableConstructor;
import com.simba.sqlengine.executor.etree.relation.ETTop;
import com.simba.sqlengine.executor.etree.relation.ETUnionAll;
import com.simba.sqlengine.executor.etree.temptable.SortedTemporaryTable;
import com.simba.sqlengine.executor.etree.temptable.TemporaryTableBuilder;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.sqlengine.executor.etree.value.ETValueExprList;
import com.simba.sqlengine.executor.queryplan.IQueryPlan;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ETRelationalExprMaterializer
  extends MaterializerBase<ETRelationalExpr>
{
  private boolean m_errorOnTruncate = false;
  
  public ETRelationalExprMaterializer(IQueryPlan paramIQueryPlan, MaterializerContext paramMaterializerContext)
  {
    super(paramIQueryPlan, paramMaterializerContext);
  }
  
  public ETRelationalExpr visit(AEAggregate paramAEAggregate)
    throws ErrorException
  {
    return new ETAggregateMaterializer(this, getQueryPlan(), getContext()).visit(paramAEAggregate);
  }
  
  public ETRelationalExpr visit(AECrossJoin paramAECrossJoin)
    throws ErrorException
  {
    return new ETJoinMaterializer(getQueryPlan(), getContext(), paramAECrossJoin).materialize();
  }
  
  public ETRelationalExpr visit(AEDistinct paramAEDistinct)
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.featureNotImplementedException("DISTINCT");
  }
  
  public ETRelationalExpr visit(AEJoin paramAEJoin)
    throws ErrorException
  {
    return new ETJoinMaterializer(getQueryPlan(), getContext(), paramAEJoin).materialize();
  }
  
  public ETProject visit(AEProject paramAEProject)
    throws ErrorException
  {
    boolean[] arrayOfBoolean = calculateDataNeeded(paramAEProject);
    ETRelationalExpr localETRelationalExpr = (ETRelationalExpr)paramAEProject.getOperand().acceptVisitor(new ETRelationalExprMaterializer(getQueryPlan(), getContext()));
    ETValueExprList localETValueExprList = new ETValueExprList();
    Iterator localIterator = paramAEProject.getProjectionList().getChildItr();
    ArrayList localArrayList = new ArrayList();
    while (localIterator.hasNext())
    {
      AEValueExpr localAEValueExpr = (AEValueExpr)localIterator.next();
      localArrayList.add(localAEValueExpr.getColumn());
      localETValueExprList.addNode((IETNode)localAEValueExpr.acceptVisitor(new ETValueExprMaterializer(getQueryPlan(), getContext())));
    }
    return new ETProject(localETRelationalExpr, localETValueExprList, localArrayList, arrayOfBoolean);
  }
  
  public ETRelationalExpr visit(AESelect paramAESelect)
    throws ErrorException
  {
    boolean[] arrayOfBoolean = calculateDataNeeded(paramAESelect);
    ETRelationalExpr localETRelationalExpr = (ETRelationalExpr)paramAESelect.getOperand().acceptVisitor(this);
    ETBooleanExpr localETBooleanExpr = (ETBooleanExpr)paramAESelect.getCondition().acceptVisitor(new ETBoolExprMaterializer(getQueryPlan(), getContext()));
    return new ETSelect(localETRelationalExpr, localETBooleanExpr, arrayOfBoolean);
  }
  
  public ETRelationalExpr visit(AESort paramAESort)
    throws ErrorException
  {
    boolean[] arrayOfBoolean = calculateDataNeeded(paramAESort.getOperand());
    ETRelationalExpr localETRelationalExpr = (ETRelationalExpr)paramAESort.getOperand().acceptVisitor(this);
    int i = localETRelationalExpr.getColumnCount();
    Object localObject = new ArrayList(i);
    for (int j = 0; j < i; j++) {
      ((List)localObject).add(localETRelationalExpr.getColumn(j));
    }
    MaterializerContext localMaterializerContext = getContext();
    TemporaryTableBuilder localTemporaryTableBuilder = new TemporaryTableBuilder((List)localObject, localMaterializerContext.getDataEngineContext().getDataEngine(), localMaterializerContext.getExternalAlgorithmProperties(), localMaterializerContext.getCancelState(), arrayOfBoolean);
    SortedTemporaryTable localSortedTemporaryTable = localTemporaryTableBuilder.sortSpec(paramAESort.getSortSpecs()).buildSorted();
    if (paramAESort.getColumnCount() < i) {
      localObject = ((List)localObject).subList(0, paramAESort.getColumnCount());
    }
    localObject = new ArrayList((Collection)localObject);
    return new ETSort(localETRelationalExpr, (List)localObject, localSortedTemporaryTable, arrayOfBoolean);
  }
  
  public ETRelationalExpr visit(AESubQuery paramAESubQuery)
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.featureNotImplementedException("subquery");
  }
  
  public ETTable visit(AETable paramAETable)
    throws ErrorException
  {
    ETTable localETTable = (ETTable)paramAETable.acceptVisitor(new ETTableMaterializer(getQueryPlan(), getContext()));
    return localETTable;
  }
  
  public ETRelationalExpr visit(AETableConstructor paramAETableConstructor)
    throws ErrorException
  {
    boolean[] arrayOfBoolean = calculateDataNeeded(paramAETableConstructor);
    ETValueExprMaterializer localETValueExprMaterializer = new ETValueExprMaterializer(getQueryPlan(), getContext());
    List localList = paramAETableConstructor.getResultSetColumns();
    Object localObject1 = null;
    for (int i = paramAETableConstructor.getNumChildren() - 1; i >= 0; i--)
    {
      ETValueExprList localETValueExprList = new ETValueExprList();
      int j = 0;
      Object localObject2 = paramAETableConstructor.getRow(i).iterator();
      while (((Iterator)localObject2).hasNext())
      {
        AEValueExpr localAEValueExpr = (AEValueExpr)((Iterator)localObject2).next();
        localETValueExprList.addNode(ConvMaterializeUtil.addConversionNodeWhenNeeded((ETValueExpr)localAEValueExpr.acceptVisitor(localETValueExprMaterializer), localAEValueExpr.getColumn(), (IColumn)localList.get(j++), isErrorOnTruncate(), getContext()));
      }
      localObject2 = new ETTableConstructor(localETValueExprList, paramAETableConstructor.getResultSetColumns(), arrayOfBoolean);
      if (null == localObject1) {
        localObject1 = localObject2;
      } else {
        localObject1 = new ETUnionAll((ETRelationalExpr)localObject2, (ETRelationalExpr)localObject1, arrayOfBoolean);
      }
    }
    return (ETRelationalExpr)localObject1;
  }
  
  public ETRelationalExpr visit(AETop paramAETop)
    throws ErrorException
  {
    boolean[] arrayOfBoolean = calculateDataNeeded(paramAETop);
    ETRelationalExpr localETRelationalExpr = (ETRelationalExpr)paramAETop.getOperand().acceptVisitor(this);
    ETValueExpr localETValueExpr = (ETValueExpr)paramAETop.getSelectLimitExpr().acceptVisitor(new ETValueExprMaterializer(getQueryPlan(), getContext()));
    return new ETTop(localETRelationalExpr, arrayOfBoolean, localETValueExpr, paramAETop.isPercent());
  }
  
  public ETRelationalExpr visit(AEUnion paramAEUnion)
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.featureNotImplementedException("UNION");
  }
  
  public ETRelationalExpr visit(AEExcept paramAEExcept)
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.featureNotImplementedException("EXCEPT");
  }
  
  public boolean isErrorOnTruncate()
  {
    return this.m_errorOnTruncate;
  }
  
  public void setErrorOnTruncate(boolean paramBoolean)
  {
    this.m_errorOnTruncate = paramBoolean;
  }
  
  private static boolean[] calculateDataNeeded(AERelationalExpr paramAERelationalExpr)
  {
    boolean[] arrayOfBoolean = new boolean[paramAERelationalExpr.getColumnCount()];
    for (int i = 0; i < arrayOfBoolean.length; i++) {
      if (paramAERelationalExpr.getDataNeeded(i)) {
        arrayOfBoolean[i] = true;
      }
    }
    return arrayOfBoolean;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/materializer/ETRelationalExprMaterializer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */