package com.simba.sqlengine.executor.materializer;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.aeprocessor.aemanipulator.CNFIterator;
import com.simba.sqlengine.aeprocessor.aetree.AEComparisonType;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanTrue;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEComparison;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEBinaryRelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AECrossJoin;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEJoin;
import com.simba.sqlengine.aeprocessor.aetree.relation.AEJoin.AEJoinType;
import com.simba.sqlengine.aeprocessor.aetree.relation.AENamedRelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AERelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AESelect;
import com.simba.sqlengine.aeprocessor.aetree.value.AEColumnReference;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExprList;
import com.simba.sqlengine.executor.conversions.ConversionUtil;
import com.simba.sqlengine.executor.conversions.ISqlConverter;
import com.simba.sqlengine.executor.etree.bool.ETBooleanExpr;
import com.simba.sqlengine.executor.etree.relation.ETRelationalExpr;
import com.simba.sqlengine.executor.etree.relation.join.ETConditionedJoin;
import com.simba.sqlengine.executor.etree.relation.join.ETConditionedJoin.Builder;
import com.simba.sqlengine.executor.etree.relation.join.HybridHashJoinAlgorithm;
import com.simba.sqlengine.executor.etree.relation.join.IJoinAlgorithmAdapter;
import com.simba.sqlengine.executor.etree.relation.join.NBJoinAlgorithm;
import com.simba.sqlengine.executor.etree.temptable.column.ColumnSizeCalculator;
import com.simba.sqlengine.executor.etree.value.ETColumnRef;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.sqlengine.executor.queryplan.IQueryPlan;
import com.simba.sqlengine.utilities.ExternalAlgorithmUtil.ExternalAlgorithmProperties;
import com.simba.support.Pair;
import com.simba.support.exceptions.ErrorException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ETJoinMaterializer
  extends MaterializerBase<ETRelationalExpr>
{
  private AERelationalExpr m_leftAeRelation;
  private AERelationalExpr m_rightAeRelation;
  private ETRelationalExpr m_leftEtRelation;
  private ETRelationalExpr m_rightEtRelation;
  private ETRelationalExpr m_leftRelationWrapper = null;
  private ETRelationalExpr m_rightRelationWrapper = null;
  private AEBooleanExpr m_joinCond;
  private AEJoin.AEJoinType m_joinType;
  private boolean[] m_joinDataNeeded;
  private AEBinaryRelationalExpr m_join;
  private AENamedRelationalExpr m_leftTable;
  private AENamedRelationalExpr m_rightTable;
  
  public ETJoinMaterializer(IQueryPlan paramIQueryPlan, MaterializerContext paramMaterializerContext, AEJoin paramAEJoin)
    throws ErrorException
  {
    super(paramIQueryPlan, paramMaterializerContext);
    this.m_join = paramAEJoin;
    this.m_leftAeRelation = paramAEJoin.getLeftOperand();
    this.m_rightAeRelation = paramAEJoin.getRightOperand();
    this.m_joinCond = paramAEJoin.getJoinCondition();
    this.m_joinType = paramAEJoin.getJoinType();
    setJoinTable();
    setDataNeeded();
  }
  
  public ETJoinMaterializer(IQueryPlan paramIQueryPlan, MaterializerContext paramMaterializerContext, AECrossJoin paramAECrossJoin)
  {
    super(paramIQueryPlan, paramMaterializerContext);
    this.m_join = paramAECrossJoin;
    this.m_leftAeRelation = paramAECrossJoin.getLeftOperand();
    this.m_rightAeRelation = paramAECrossJoin.getRightOperand();
    this.m_joinCond = new AEBooleanTrue();
    this.m_joinType = AEJoin.AEJoinType.INNER_JOIN;
    setJoinTable();
    setDataNeeded();
  }
  
  public ETRelationalExpr materialize()
    throws ErrorException
  {
    this.m_leftEtRelation = ((ETRelationalExpr)this.m_leftAeRelation.acceptVisitor(new ETRelationalExprMaterializer(getQueryPlan(), getContext())));
    this.m_rightEtRelation = ((ETRelationalExpr)this.m_rightAeRelation.acceptVisitor(new ETRelationalExprMaterializer(getQueryPlan(), getContext())));
    ETConditionedJoin.Builder localBuilder = new ETConditionedJoin.Builder(this.m_leftEtRelation, this.m_rightEtRelation, getContext().getCancelState(), this.m_joinDataNeeded);
    this.m_leftRelationWrapper = localBuilder.getLeftRelationWrapper();
    this.m_rightRelationWrapper = localBuilder.getRightRelationWrapper();
    ETJoinCondValueExprMaterializer localETJoinCondValueExprMaterializer = new ETJoinCondValueExprMaterializer(getQueryPlan(), getContext());
    ETJoinConditionMaterializer localETJoinConditionMaterializer = new ETJoinConditionMaterializer(getQueryPlan(), getContext(), localETJoinCondValueExprMaterializer);
    localBuilder.setJoinAlgorithm(buildJoinAlgorithmAdapter());
    localBuilder.setJoinCondition((ETBooleanExpr)this.m_joinCond.acceptVisitor(localETJoinConditionMaterializer));
    ETConditionedJoin localETConditionedJoin = localBuilder.build();
    getContext().setMaterializedRelation(this.m_join, localETConditionedJoin);
    if (this.m_leftTable != null)
    {
      getContext().setJoinMapping(this.m_leftTable, this.m_join, 0);
    }
    else
    {
      assert (((this.m_leftAeRelation instanceof AEJoin)) || ((this.m_leftAeRelation instanceof AECrossJoin)));
      getContext().updateMapping((AEBinaryRelationalExpr)this.m_leftAeRelation, this.m_join, 0);
    }
    if (this.m_rightTable != null)
    {
      getContext().setJoinMapping(this.m_rightTable, this.m_join, this.m_leftAeRelation.getColumnCount());
    }
    else
    {
      assert (((this.m_rightAeRelation instanceof AEJoin)) || ((this.m_rightAeRelation instanceof AECrossJoin)));
      getContext().updateMapping((AEBinaryRelationalExpr)this.m_rightAeRelation, this.m_join, this.m_leftAeRelation.getColumnCount());
    }
    return localETConditionedJoin;
  }
  
  private IJoinAlgorithmAdapter buildJoinAlgorithmAdapter()
    throws ErrorException
  {
    if ((this.m_joinCond instanceof AEBooleanTrue)) {
      return new NBJoinAlgorithm(this.m_leftEtRelation, this.m_rightEtRelation, AEJoin.AEJoinType.INNER_JOIN, getContext().getExternalAlgorithmProperties(), getContext().getCancelState(), getContext().getLog());
    }
    LinkedList localLinkedList1 = new LinkedList();
    LinkedList localLinkedList2 = new LinkedList();
    LinkedList localLinkedList3 = new LinkedList();
    LinkedList localLinkedList4 = new LinkedList();
    HashSet localHashSet = new HashSet();
    CNFIterator localCNFIterator = new CNFIterator(this.m_joinCond);
    int i = getContext().getExternalAlgorithmProperties().getCellMemoryLimit();
    while (localCNFIterator.hasNext())
    {
      AEBooleanExpr localAEBooleanExpr = localCNFIterator.next();
      if (((localAEBooleanExpr instanceof AEComparison)) && (((AEComparison)localAEBooleanExpr).getComparisonOp() == AEComparisonType.EQUAL))
      {
        AEComparison localAEComparison = (AEComparison)localAEBooleanExpr;
        if (((localAEComparison.getLeftOperand().getChild(0) instanceof AEColumnReference)) && ((localAEComparison.getRightOperand().getChild(0) instanceof AEColumnReference)))
        {
          AEColumnReference localAEColumnReference1 = (AEColumnReference)localAEComparison.getLeftOperand().getChild(0);
          AEColumnReference localAEColumnReference2 = (AEColumnReference)localAEComparison.getRightOperand().getChild(0);
          Pair localPair1 = resolveColumn(localAEColumnReference1);
          Pair localPair2 = resolveColumn(localAEColumnReference2);
          if (localPair1.key() != localPair2.key())
          {
            IColumn localIColumn1 = localAEComparison.getCoercedColumnMetadata();
            IColumn localIColumn2;
            IColumn localIColumn3;
            Pair localPair3;
            if (((Boolean)localPair1.key()).booleanValue())
            {
              localIColumn2 = localAEColumnReference1.getColumn();
              localIColumn3 = localAEColumnReference2.getColumn();
              localPair3 = new Pair(localPair1.value(), localPair2.value());
            }
            else
            {
              localIColumn2 = localAEColumnReference2.getColumn();
              localIColumn3 = localAEColumnReference1.getColumn();
              localPair3 = new Pair(localPair2.value(), localPair1.value());
            }
            if ((!ColumnSizeCalculator.isLongData(localIColumn2, i)) && (!ColumnSizeCalculator.isLongData(localIColumn3, i)))
            {
              localLinkedList1.add(localPair3);
              localLinkedList2.add(localIColumn1);
              localLinkedList3.add(ConversionUtil.createConverter(getContext().getSqlConverterGenerator(), localIColumn2, localIColumn1));
              localLinkedList4.add(ConversionUtil.createConverter(getContext().getSqlConverterGenerator(), localIColumn3, localIColumn1));
              localHashSet.add(localAEBooleanExpr);
            }
          }
        }
      }
    }
    if (localLinkedList1.size() == 0) {
      return new NBJoinAlgorithm(this.m_leftEtRelation, this.m_rightEtRelation, this.m_joinType, getContext().getExternalAlgorithmProperties(), getContext().getCancelState(), getContext().getLog());
    }
    localCNFIterator = new CNFIterator(this.m_joinCond);
    while (localCNFIterator.hasNext()) {
      if (localHashSet.contains(localCNFIterator.next())) {
        localCNFIterator.remove();
      }
    }
    this.m_joinCond = localCNFIterator.getExpr();
    if (this.m_joinCond == null) {
      this.m_joinCond = new AEBooleanTrue();
    }
    assert ((this.m_join instanceof AEJoin));
    ((AEJoin)this.m_join).setJoinCondition(this.m_joinCond);
    return getHHJoinAlgorithmAdapter(localLinkedList1, localLinkedList2, localLinkedList3, localLinkedList4);
  }
  
  private IJoinAlgorithmAdapter getHHJoinAlgorithmAdapter(List<Pair<Integer, Integer>> paramList, List<IColumn> paramList1, List<ISqlConverter> paramList2, List<ISqlConverter> paramList3)
    throws ErrorException
  {
    int[] arrayOfInt1 = new int[paramList.size()];
    int[] arrayOfInt2 = new int[paramList.size()];
    int i = 0;
    Object localObject = paramList.iterator();
    while (((Iterator)localObject).hasNext())
    {
      Pair localPair = (Pair)((Iterator)localObject).next();
      arrayOfInt1[i] = ((Integer)localPair.key()).intValue();
      arrayOfInt2[i] = ((Integer)localPair.value()).intValue();
      i++;
    }
    localObject = getContext().getExternalAlgorithmProperties();
    return new HybridHashJoinAlgorithm(this.m_leftEtRelation, this.m_rightEtRelation, arrayOfInt1, arrayOfInt2, this.m_joinType, (ExternalAlgorithmUtil.ExternalAlgorithmProperties)localObject, paramList1, paramList2, paramList3, getContext().getCancelState(), this.m_joinDataNeeded);
  }
  
  private void setDataNeeded()
  {
    this.m_joinDataNeeded = new boolean[this.m_join.getColumnCount()];
    for (int i = 0; i < this.m_joinDataNeeded.length; i++) {
      if (this.m_join.getDataNeeded(i)) {
        this.m_joinDataNeeded[i] = true;
      }
    }
  }
  
  private void setJoinTable()
  {
    if ((!(this.m_leftAeRelation instanceof AEJoin)) && (!(this.m_leftAeRelation instanceof AECrossJoin))) {
      this.m_leftTable = getNamedRelation(this.m_leftAeRelation);
    }
    if ((!(this.m_rightAeRelation instanceof AEJoin)) && (!(this.m_rightAeRelation instanceof AECrossJoin))) {
      this.m_rightTable = getNamedRelation(this.m_rightAeRelation);
    }
  }
  
  private AENamedRelationalExpr getNamedRelation(AERelationalExpr paramAERelationalExpr)
  {
    while (!(paramAERelationalExpr instanceof AENamedRelationalExpr)) {
      if ((paramAERelationalExpr instanceof AESelect)) {
        paramAERelationalExpr = ((AESelect)paramAERelationalExpr).getOperand();
      } else {
        throw new IllegalStateException("Invalid AETree structure");
      }
    }
    return (AENamedRelationalExpr)paramAERelationalExpr;
  }
  
  private Pair<Boolean, Integer> resolveColumn(AEColumnReference paramAEColumnReference)
  {
    AENamedRelationalExpr localAENamedRelationalExpr = paramAEColumnReference.getNamedRelationalExpr();
    if (localAENamedRelationalExpr == this.m_leftTable) {
      return new Pair(Boolean.valueOf(true), Integer.valueOf(paramAEColumnReference.getColumnNum()));
    }
    if (localAENamedRelationalExpr == this.m_rightTable) {
      return new Pair(Boolean.valueOf(false), Integer.valueOf(paramAEColumnReference.getColumnNum()));
    }
    Pair localPair = getContext().resolveJoinRelation(paramAEColumnReference.getNamedRelationalExpr());
    return new Pair(Boolean.valueOf(localPair.key() == this.m_leftAeRelation), Integer.valueOf(((Integer)localPair.value()).intValue() + paramAEColumnReference.getColumnNum()));
  }
  
  private class ETJoinCondValueExprMaterializer
    extends ETValueExprMaterializer
  {
    public ETJoinCondValueExprMaterializer(IQueryPlan paramIQueryPlan, MaterializerContext paramMaterializerContext)
    {
      super(paramMaterializerContext);
    }
    
    public ETValueExpr visit(AEColumnReference paramAEColumnReference)
      throws ErrorException
    {
      if (paramAEColumnReference.isOuterReference()) {
        return super.visit(paramAEColumnReference);
      }
      Pair localPair = ETJoinMaterializer.this.resolveColumn(paramAEColumnReference);
      return new ETColumnRef(((Boolean)localPair.key()).booleanValue() ? ETJoinMaterializer.this.m_leftRelationWrapper : ETJoinMaterializer.this.m_rightRelationWrapper, ((Integer)localPair.value()).intValue(), false);
    }
  }
  
  private static class ETJoinConditionMaterializer
    extends ETBoolExprMaterializer
  {
    private ETValueExprMaterializer m_valueExprMaterializer;
    
    public ETJoinConditionMaterializer(IQueryPlan paramIQueryPlan, MaterializerContext paramMaterializerContext, ETJoinMaterializer.ETJoinCondValueExprMaterializer paramETJoinCondValueExprMaterializer)
    {
      super(paramMaterializerContext);
      this.m_valueExprMaterializer = paramETJoinCondValueExprMaterializer;
    }
    
    protected ETValueExprMaterializer createValueExprMaterializer()
    {
      return this.m_valueExprMaterializer;
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/materializer/ETJoinMaterializer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */