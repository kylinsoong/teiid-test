package com.simba.sqlengine.executor.materializer;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.aeprocessor.aebuilder.AEQueryScope;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEAggregate;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEProject;
import com.simba.sqlengine.aeprocessor.aetree.relation.AERelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEAggrFn;
import com.simba.sqlengine.aeprocessor.aetree.value.AECountStarAggrFn;
import com.simba.sqlengine.aeprocessor.aetree.value.AEGeneralAggrFn;
import com.simba.sqlengine.aeprocessor.aetree.value.AEProxyColumn;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExprList;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngineContext;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.sqlengine.executor.etree.hash.HashPartitionProperties;
import com.simba.sqlengine.executor.etree.hash.HashPartitionProperties.Builder;
import com.simba.sqlengine.executor.etree.hash.IRowBinaryPredicate;
import com.simba.sqlengine.executor.etree.hash.RowCmpEquals;
import com.simba.sqlengine.executor.etree.relation.ETAggregate;
import com.simba.sqlengine.executor.etree.relation.ETDistinctMove;
import com.simba.sqlengine.executor.etree.relation.ETHashAggregate;
import com.simba.sqlengine.executor.etree.relation.ETHashAggregate.AggregateProjectionInfo;
import com.simba.sqlengine.executor.etree.relation.ETHashAggregate.OperandProjectionInfo;
import com.simba.sqlengine.executor.etree.relation.ETProject;
import com.simba.sqlengine.executor.etree.relation.ETRelationalExpr;
import com.simba.sqlengine.executor.etree.relation.ETSort;
import com.simba.sqlengine.executor.etree.relation.ETStreamAggregate;
import com.simba.sqlengine.executor.etree.temptable.RowComparator;
import com.simba.sqlengine.executor.etree.temptable.SortedTemporaryTable;
import com.simba.sqlengine.executor.etree.temptable.TemporaryTableBuilder;
import com.simba.sqlengine.executor.etree.value.ETValueExprList;
import com.simba.sqlengine.executor.etree.value.aggregatefn.IAggregatorFactory;
import com.simba.sqlengine.executor.queryplan.IQueryPlan;
import com.simba.sqlengine.utilities.ExternalAlgorithmUtil.ExternalAlgorithmProperties;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class ETAggregateMaterializer
  extends MaterializerBase<ETRelationalExpr>
{
  private ETRelationalExprMaterializer m_parent;
  
  ETAggregateMaterializer(ETRelationalExprMaterializer paramETRelationalExprMaterializer, IQueryPlan paramIQueryPlan, MaterializerContext paramMaterializerContext)
  {
    super(paramIQueryPlan, paramMaterializerContext);
    this.m_parent = paramETRelationalExprMaterializer;
  }
  
  public ETRelationalExpr visit(AEAggregate paramAEAggregate)
    throws ErrorException
  {
    if (!paramAEAggregate.hasGroupingList()) {
      return materializeScalarAggregate(paramAEAggregate);
    }
    return materializeSortAggregate(paramAEAggregate);
  }
  
  private ETAggregate materializeHashAggregate(AEAggregate paramAEAggregate)
    throws ErrorException
  {
    AEValueExprList localAEValueExprList1 = paramAEAggregate.getAggregationList();
    int i = localAEValueExprList1.getNumChildren();
    AEValueExprList localAEValueExprList2 = new AEValueExprList();
    ArrayList localArrayList1 = new ArrayList();
    ArrayList localArrayList2 = new ArrayList();
    ArrayList localArrayList3 = new ArrayList();
    ArrayList localArrayList4 = new ArrayList();
    ArrayList localArrayList5 = new ArrayList();
    int[] arrayOfInt1 = new int[i];
    for (int j = 0; j < i; j++)
    {
      localObject1 = (AEValueExpr)localAEValueExprList1.getChild(j);
      localArrayList1.add(((AEValueExpr)localObject1).getColumn());
      if ((localObject1 instanceof AEAggrFn))
      {
        localArrayList2.add(Integer.valueOf(j));
        AEAggrFn localAEAggrFn = (AEAggrFn)localObject1;
        int m = localAEAggrFn.getNumChildren();
        localObject3 = new int[m];
        IColumn[] arrayOfIColumn = new IColumn[m];
        localObject4 = localAEAggrFn.getChildItr();
        for (i1 = 0; i1 < m; i1++)
        {
          localObject5 = (AEValueExpr)((Iterator)localObject4).next();
          localObject3[i1] = findOrAdd((AEValueExpr)localObject5, localAEValueExprList2);
          arrayOfIColumn[i1] = ((AEValueExpr)localObject5).getColumn();
        }
        localArrayList3.add(localObject3);
        localArrayList4.add(arrayOfIColumn);
        localArrayList5.add(ETAggregateFnFactory.makeNewAggregatorFactory(localAEAggrFn, getContext().getExternalAlgorithmProperties().getCellMemoryLimit()));
        arrayOfInt1[j] = -1;
      }
      else
      {
        arrayOfInt1[j] = findOrAdd((AEValueExpr)localObject1, localAEValueExprList2);
      }
    }
    ETHashAggregate.AggregateProjectionInfo localAggregateProjectionInfo = new ETHashAggregate.AggregateProjectionInfo(i, (IColumn[])localArrayList1.toArray(new IColumn[0]), arrayOfInt1, createIntArray(localArrayList2), localArrayList3, localArrayList4, (IAggregatorFactory[])localArrayList5.toArray(new IAggregatorFactory[0]));
    Object localObject1 = new boolean[paramAEAggregate.getOperand().getColumnCount()];
    for (int k = 0; k < localObject1.length; k++) {
      if (paramAEAggregate.getOperand().getDataNeeded(k)) {
        localObject1[k] = 1;
      }
    }
    Object localObject2 = (ETRelationalExpr)paramAEAggregate.getOperand().acceptVisitor(this.m_parent);
    ETValueExprMaterializer localETValueExprMaterializer = new ETValueExprMaterializer(getQueryPlan(), getContext());
    Object localObject3 = new ETValueExprList();
    int n = localAEValueExprList2.getNumChildren();
    Object localObject4 = new IColumn[n];
    for (int i1 = 0; i1 < n; i1++)
    {
      localObject5 = (AEValueExpr)localAEValueExprList2.getChild(i1);
      localObject4[i1] = ((AEValueExpr)localObject5).getColumn();
      ((ETValueExprList)localObject3).addNode((IETNode)((AEValueExpr)localObject5).acceptVisitor(localETValueExprMaterializer));
    }
    localObject2 = new ETProject((ETRelationalExpr)localObject2, (ETValueExprList)localObject3, Arrays.asList((Object[])localObject4), (boolean[])localObject1);
    int[] arrayOfInt2;
    if (paramAEAggregate.hasGroupingList())
    {
      localObject5 = paramAEAggregate.getGroupingList();
      i2 = ((AEValueExprList)localObject5).getNumChildren();
      arrayOfInt2 = new int[i2];
      for (int i3 = 0; i3 < i2; i3++)
      {
        localObject6 = (AEProxyColumn)((AEValueExprList)localObject5).getChild(i3);
        arrayOfInt2[i3] = localAggregateProjectionInfo.mapToOperandColumn(((AEProxyColumn)localObject6).getColumnNumber());
      }
    }
    else
    {
      arrayOfInt2 = new int[0];
    }
    Object localObject5 = new HashSet();
    for (int i2 = 0; i2 < localAggregateProjectionInfo.getNumColumns(); i2++) {
      if (!localAggregateProjectionInfo.isAggregateFnColumn(i2)) {
        ((HashSet)localObject5).add(Integer.valueOf(localAggregateProjectionInfo.mapToOperandColumn(i2)));
      }
    }
    ArrayList localArrayList6 = new ArrayList((Collection)localObject5);
    Collections.sort(localArrayList6);
    ETHashAggregate.OperandProjectionInfo localOperandProjectionInfo = new ETHashAggregate.OperandProjectionInfo((IColumn[])localObject4, createIntArray(localArrayList6), arrayOfInt2);
    Object localObject6 = getContext();
    HashPartitionProperties localHashPartitionProperties = HashPartitionProperties.builder().equalityPredicate(createPredicate(arrayOfInt2, (IColumn[])localObject4)).extAlgorithmProperties(((MaterializerContext)localObject6).getExternalAlgorithmProperties()).aggregateProjection(localAggregateProjectionInfo).operandProjection(localOperandProjectionInfo).logger(getContext().getLog()).build();
    ETHashAggregate localETHashAggregate = new ETHashAggregate((ETRelationalExpr)localObject2, localHashPartitionProperties);
    getContext().setMaterializedRelation(paramAEAggregate, localETHashAggregate);
    return localETHashAggregate;
  }
  
  private ETAggregate materializeScalarAggregate(AEAggregate paramAEAggregate)
    throws ErrorException
  {
    assert (!paramAEAggregate.hasGroupingList());
    ETRelationalExpr localETRelationalExpr = (ETRelationalExpr)paramAEAggregate.getOperand().acceptVisitor(this.m_parent);
    ETValueExprList localETValueExprList = materializeAggrList(paramAEAggregate);
    boolean[] arrayOfBoolean = buildDataNeeded(paramAEAggregate);
    ETStreamAggregate localETStreamAggregate = new ETStreamAggregate(localETRelationalExpr, localETValueExprList, paramAEAggregate.createResultSetColumns(), false, arrayOfBoolean);
    getContext().setMaterializedRelation(paramAEAggregate, localETStreamAggregate);
    return localETStreamAggregate;
  }
  
  private ETAggregate materializeSortAggregate(AEAggregate paramAEAggregate)
    throws ErrorException
  {
    assert (paramAEAggregate.hasGroupingList());
    ETDistinctMove localETDistinctMove = materializeSortedOperand(paramAEAggregate);
    boolean[] arrayOfBoolean = buildDataNeeded(paramAEAggregate);
    ETValueExprList localETValueExprList = materializeAggrList(paramAEAggregate);
    ETStreamAggregate localETStreamAggregate = new ETStreamAggregate(localETDistinctMove, localETValueExprList, paramAEAggregate.createResultSetColumns(), true, arrayOfBoolean);
    localETDistinctMove.registerRowLister(localETStreamAggregate.getRowListener());
    getContext().setMaterializedRelation(paramAEAggregate, localETStreamAggregate);
    return localETStreamAggregate;
  }
  
  private ETDistinctMove materializeSortedOperand(AEAggregate paramAEAggregate)
    throws ErrorException
  {
    Object localObject1 = (ETRelationalExpr)paramAEAggregate.getOperand().acceptVisitor(this.m_parent);
    AEValueExprList localAEValueExprList1 = paramAEAggregate.getAggregationList();
    AEValueExprList localAEValueExprList2 = new AEValueExprList();
    List localList = proxyAggregateList(localAEValueExprList1, localAEValueExprList2, paramAEAggregate.getQueryScope());
    AEProject localAEProject = new AEProject(localAEValueExprList2, paramAEAggregate.getOperand());
    Object localObject2 = localList.iterator();
    while (((Iterator)localObject2).hasNext())
    {
      localObject3 = (AEProxyColumn)((Iterator)localObject2).next();
      ((AEProxyColumn)localObject3).setRelationalExpr(localAEProject);
    }
    localObject2 = new ArrayList();
    Object localObject3 = paramAEAggregate.getGroupingList().iterator();
    while (((Iterator)localObject3).hasNext())
    {
      localObject4 = (AEValueExpr)((Iterator)localObject3).next();
      int i = ((AEProxyColumn)localObject4).getColumnNumber();
      localObject5 = (AEProxyColumn)localAEValueExprList1.getChild(i);
      ((List)localObject2).add(RowComparator.createDefaultSortSpec(((AEProxyColumn)localObject5).getColumnNumber()));
    }
    localObject3 = new boolean[localAEProject.getColumnCount()];
    Arrays.fill((boolean[])localObject3, true);
    Object localObject4 = new ETValueExprMaterializer(getQueryPlan(), getContext());
    ETValueExprList localETValueExprList = new ETValueExprList();
    Object localObject5 = localAEValueExprList2.iterator();
    while (((Iterator)localObject5).hasNext())
    {
      localObject6 = (AEValueExpr)((Iterator)localObject5).next();
      localETValueExprList.addNode((IETNode)((AEValueExpr)localObject6).acceptVisitor((IAENodeVisitor)localObject4));
    }
    localObject1 = new ETProject((ETRelationalExpr)localObject1, localETValueExprList, localAEProject.createResultSetColumns(), (boolean[])localObject3);
    localObject5 = new TemporaryTableBuilder(localAEProject.createResultSetColumns(), getContext().getDataEngineContext().getDataEngine(), getContext().getExternalAlgorithmProperties(), getContext().getCancelState(), (boolean[])localObject3);
    Object localObject6 = ((TemporaryTableBuilder)localObject5).sortSpec((List)localObject2).buildSorted();
    localObject1 = new ETSort((ETRelationalExpr)localObject1, localAEProject.createResultSetColumns(), (SortedTemporaryTable)localObject6, (boolean[])localObject3);
    ETDistinctMove localETDistinctMove = new ETDistinctMove((ETRelationalExpr)localObject1, localAEProject.createResultSetColumns(), ((SortedTemporaryTable)localObject6).getRowComparator(), getContext().getExternalAlgorithmProperties(), (boolean[])localObject3);
    getContext().setMaterializedRelation(localAEProject, localETDistinctMove);
    return localETDistinctMove;
  }
  
  private ETValueExprList materializeAggrList(AEAggregate paramAEAggregate)
    throws ErrorException
  {
    ETValueExprList localETValueExprList = new ETValueExprList();
    ETValueExprMaterializer localETValueExprMaterializer = new ETValueExprMaterializer(getQueryPlan(), getContext());
    Iterator localIterator = paramAEAggregate.getAggregationList().iterator();
    while (localIterator.hasNext())
    {
      AEValueExpr localAEValueExpr = (AEValueExpr)localIterator.next();
      localETValueExprList.addNode((IETNode)localAEValueExpr.acceptVisitor(localETValueExprMaterializer));
    }
    return localETValueExprList;
  }
  
  private boolean[] buildDataNeeded(AEAggregate paramAEAggregate)
  {
    boolean[] arrayOfBoolean = new boolean[paramAEAggregate.getColumnCount()];
    for (int i = 0; i < arrayOfBoolean.length; i++) {
      arrayOfBoolean[i] = paramAEAggregate.getDataNeeded(i);
    }
    return arrayOfBoolean;
  }
  
  private List<AEProxyColumn> proxyAggregateList(AEValueExprList paramAEValueExprList1, AEValueExprList paramAEValueExprList2, AEQueryScope paramAEQueryScope)
    throws ErrorException
  {
    ArrayList localArrayList = new ArrayList();
    for (int i = 0; i < paramAEValueExprList1.getNumChildren(); i++)
    {
      AEValueExpr localAEValueExpr = (AEValueExpr)paramAEValueExprList1.getChild(i);
      if ((localAEValueExpr instanceof AEAggrFn))
      {
        if ((localAEValueExpr instanceof AEGeneralAggrFn))
        {
          AEGeneralAggrFn localAEGeneralAggrFn = (AEGeneralAggrFn)localAEValueExpr;
          int k = findOrAdd(localAEGeneralAggrFn.getOperand(), paramAEValueExprList2);
          paramAEValueExprList2.addNode(localAEGeneralAggrFn.getOperand());
          AEProxyColumn localAEProxyColumn2 = new AEProxyColumn(localAEGeneralAggrFn.getOperand(), paramAEQueryScope, k);
          localArrayList.add(localAEProxyColumn2);
          localAEGeneralAggrFn.setOperand(localAEProxyColumn2);
        }
        else if (!(localAEValueExpr instanceof AECountStarAggrFn))
        {
          throw SQLEngineExceptionFactory.featureNotImplementedException(localAEValueExpr.getLogString());
        }
      }
      else
      {
        int j = findOrAdd(localAEValueExpr, paramAEValueExprList2);
        AEProxyColumn localAEProxyColumn1 = new AEProxyColumn(localAEValueExpr, paramAEQueryScope, j);
        localArrayList.add(localAEProxyColumn1);
        paramAEValueExprList1.replaceNode(localAEProxyColumn1, i);
      }
    }
    return localArrayList;
  }
  
  private static int[] createIntArray(List<Integer> paramList)
  {
    int i = paramList.size();
    int[] arrayOfInt = new int[i];
    for (int j = 0; j < i; j++) {
      arrayOfInt[j] = ((Integer)paramList.get(j)).intValue();
    }
    return arrayOfInt;
  }
  
  private static int findOrAdd(AEValueExpr paramAEValueExpr, AEValueExprList paramAEValueExprList)
  {
    int i = paramAEValueExprList.findNode(paramAEValueExpr);
    if (0 > i)
    {
      i = paramAEValueExprList.getNumChildren();
      paramAEValueExprList.addNode(paramAEValueExpr);
    }
    return i;
  }
  
  private static IRowBinaryPredicate createPredicate(int[] paramArrayOfInt, IColumn[] paramArrayOfIColumn)
    throws ErrorException
  {
    int i = paramArrayOfInt.length;
    ArrayList localArrayList1 = new ArrayList(i);
    ArrayList localArrayList2 = new ArrayList(i);
    for (int m : paramArrayOfInt)
    {
      localArrayList1.add(RowComparator.createDefaultSortSpec(m));
      localArrayList2.add(paramArrayOfIColumn[m]);
    }
    ??? = RowComparator.createComparator((IColumn[])localArrayList2.toArray(new IColumn[0]), localArrayList1, RowComparator.getDefaultNullCollation());
    return new RowCmpEquals((Comparator)???);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/materializer/ETAggregateMaterializer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */