package com.simba.sqlengine.aeprocessor.aetree.bool;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.ColumnMetadata;
import com.simba.sqlengine.aeprocessor.aetree.AEComparisonType;
import com.simba.sqlengine.aeprocessor.aetree.IAEBinaryNode;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.aeprocessor.aetree.relation.AERelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExprList;
import com.simba.sqlengine.aeprocessor.metadatautil.AECoercionColumnInfo;
import com.simba.sqlengine.aeprocessor.metadatautil.MetadataColumnInfo;
import com.simba.sqlengine.dsiext.dataengine.ICoercionHandler;
import com.simba.sqlengine.dsiext.dataengine.IColumnInfo.ColumnType;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngineContext;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.support.exceptions.ErrorException;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;

public class AEQuantifiedComparison
  extends AEBooleanExpr
  implements IAEBinaryNode<AEValueExprList, IAENode>
{
  private static final int NUM_CHILDREN = 2;
  private final ICoercionHandler m_coercionHandler;
  private AEValueExprList m_leftOperand;
  private AERelationalExpr m_rightOperand;
  private IColumn m_coercionColumnMeta;
  private IAENode m_parent = null;
  private AEComparisonType m_compType;
  private QuantifierType m_qtyType;
  
  public AEQuantifiedComparison(SqlDataEngineContext paramSqlDataEngineContext, AEValueExprList paramAEValueExprList, AERelationalExpr paramAERelationalExpr, AEComparisonType paramAEComparisonType, QuantifierType paramQuantifierType)
    throws ErrorException
  {
    if ((null == paramSqlDataEngineContext) || (null == paramAEValueExprList) || (null == paramAERelationalExpr) || (0 == paramAEValueExprList.getNumChildren()) || (0 == paramAERelationalExpr.getNumChildren())) {
      throw new IllegalArgumentException("Illegal parameters for AEInPredicate. Null parameter or invalid operand");
    }
    if ((1 != paramAEValueExprList.getNumChildren()) || (1 != paramAERelationalExpr.getColumnCount())) {
      throw SQLEngineExceptionFactory.featureNotImplementedException("Multiple expresions for quantified comparison is not supported.");
    }
    this.m_coercionHandler = paramSqlDataEngineContext.getCoercionHandler();
    this.m_leftOperand = paramAEValueExprList;
    this.m_leftOperand.setParent(this);
    this.m_rightOperand = paramAERelationalExpr;
    this.m_rightOperand.setParent(this);
    this.m_compType = paramAEComparisonType;
    this.m_qtyType = paramQuantifierType;
    initalizeMetadata(this.m_coercionHandler);
  }
  
  private AEQuantifiedComparison(AEQuantifiedComparison paramAEQuantifiedComparison)
  {
    setIsOptimized(paramAEQuantifiedComparison.isOptimized());
    this.m_coercionHandler = paramAEQuantifiedComparison.m_coercionHandler;
    this.m_leftOperand = paramAEQuantifiedComparison.m_leftOperand.copy();
    this.m_leftOperand.setParent(this);
    this.m_rightOperand = paramAEQuantifiedComparison.m_rightOperand.copy();
    this.m_rightOperand.setParent(this);
    this.m_coercionColumnMeta = ColumnMetadata.copyOf(paramAEQuantifiedComparison.m_coercionColumnMeta);
    this.m_qtyType = paramAEQuantifiedComparison.m_qtyType;
    this.m_compType = paramAEQuantifiedComparison.m_compType;
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public QuantifierType getQuantifierType()
  {
    return this.m_qtyType;
  }
  
  public AEComparisonType getComparisonOp()
  {
    return this.m_compType;
  }
  
  public Iterator<? extends IAENode> getChildItr()
  {
    return asList().iterator();
  }
  
  public String getLogString()
  {
    return getClass().getSimpleName() + " : " + this.m_compType.name() + " " + this.m_qtyType.name();
  }
  
  public int getNumChildren()
  {
    return 2;
  }
  
  public IAENode getParent()
  {
    return this.m_parent;
  }
  
  public void setParent(IAENode paramIAENode)
  {
    this.m_parent = paramIAENode;
  }
  
  public AEBooleanExpr.AEBooleanType getType()
  {
    return AEBooleanExpr.AEBooleanType.QUANITIFIED_COMPARISON;
  }
  
  public boolean isEquivalent(IAENode paramIAENode)
  {
    if (this == paramIAENode) {
      return true;
    }
    if (!(paramIAENode instanceof AEQuantifiedComparison)) {
      return false;
    }
    AEQuantifiedComparison localAEQuantifiedComparison = (AEQuantifiedComparison)paramIAENode;
    return (this.m_leftOperand.isEquivalent(localAEQuantifiedComparison.m_leftOperand)) && (this.m_rightOperand.isEquivalent(localAEQuantifiedComparison.m_rightOperand)) && (this.m_compType == localAEQuantifiedComparison.m_compType) && (this.m_qtyType == localAEQuantifiedComparison.m_qtyType);
  }
  
  public AEQuantifiedComparison copy()
  {
    return new AEQuantifiedComparison(this);
  }
  
  public AEValueExprList getLeftOperand()
  {
    return this.m_leftOperand;
  }
  
  public AERelationalExpr getRightOperand()
  {
    return this.m_rightOperand;
  }
  
  public IColumn getCoercedColumnMetadata()
  {
    return this.m_coercionColumnMeta;
  }
  
  public void updateCoercion()
    throws ErrorException
  {
    initalizeMetadata(this.m_coercionHandler);
  }
  
  private void initalizeMetadata(ICoercionHandler paramICoercionHandler)
    throws ErrorException
  {
    assert (this.m_leftOperand.getNumChildren() == 1);
    AECoercionColumnInfo localAECoercionColumnInfo = new AECoercionColumnInfo((AEValueExpr)this.m_leftOperand.getChild(0));
    MetadataColumnInfo localMetadataColumnInfo = new MetadataColumnInfo(this.m_rightOperand.getColumn(0), IColumnInfo.ColumnType.COLUMN);
    this.m_coercionColumnMeta = paramICoercionHandler.coerceComparisonColumns(localAECoercionColumnInfo, localMetadataColumnInfo);
  }
  
  private List<IAENode> asList()
  {
    new AbstractList()
    {
      public IAENode get(int paramAnonymousInt)
      {
        switch (paramAnonymousInt)
        {
        case 0: 
          return AEQuantifiedComparison.this.m_leftOperand;
        case 1: 
          return AEQuantifiedComparison.this.m_rightOperand;
        }
        throw new IndexOutOfBoundsException("Index: " + paramAnonymousInt);
      }
      
      public int size()
      {
        return 2;
      }
    };
  }
  
  public static enum QuantifierType
  {
    ANY,  ALL;
    
    private QuantifierType() {}
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/bool/AEQuantifiedComparison.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */