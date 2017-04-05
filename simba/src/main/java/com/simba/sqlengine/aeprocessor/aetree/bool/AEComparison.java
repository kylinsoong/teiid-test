package com.simba.sqlengine.aeprocessor.aetree.bool;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.ColumnMetadata;
import com.simba.sqlengine.aeprocessor.aetree.AEComparisonType;
import com.simba.sqlengine.aeprocessor.aetree.IAEBinaryNode;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExprList;
import com.simba.sqlengine.aeprocessor.metadatautil.AECoercionColumnInfo;
import com.simba.sqlengine.dsiext.dataengine.ICoercionHandler;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngineContext;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.support.exceptions.ErrorException;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;

public class AEComparison
  extends AEBooleanExpr
  implements IAEBinaryNode<AEValueExprList, AEValueExprList>
{
  private static final int NUM_CHILDREN = 2;
  private final ICoercionHandler m_coercionHandler;
  private AEValueExprList m_leftOperand;
  private AEValueExprList m_rightOperand;
  private AEComparisonType m_compOp;
  private IColumn m_coercionColumnMeta;
  private IAENode m_parent = null;
  
  public AEComparison(SqlDataEngineContext paramSqlDataEngineContext, AEComparisonType paramAEComparisonType, AEValueExprList paramAEValueExprList1, AEValueExprList paramAEValueExprList2)
    throws ErrorException
  {
    if ((null == paramSqlDataEngineContext) || (null == paramAEComparisonType) || (null == paramAEValueExprList1) || (null == paramAEValueExprList2)) {
      throw new IllegalArgumentException("AEComparison does not accept null parameters.");
    }
    this.m_coercionHandler = paramSqlDataEngineContext.getCoercionHandler();
    this.m_compOp = paramAEComparisonType;
    this.m_leftOperand = paramAEValueExprList1;
    this.m_leftOperand.setParent(this);
    this.m_rightOperand = paramAEValueExprList2;
    this.m_rightOperand.setParent(this);
    initalizeMetadata(this.m_coercionHandler);
  }
  
  private AEComparison(AEComparison paramAEComparison)
  {
    this.m_compOp = paramAEComparison.m_compOp;
    this.m_coercionHandler = paramAEComparison.m_coercionHandler;
    this.m_leftOperand = paramAEComparison.m_leftOperand.copy();
    this.m_leftOperand.setParent(this);
    this.m_rightOperand = paramAEComparison.m_rightOperand.copy();
    this.m_rightOperand.setParent(this);
    setIsOptimized(paramAEComparison.isOptimized());
    this.m_coercionColumnMeta = ColumnMetadata.copyOf(paramAEComparison.m_coercionColumnMeta);
  }
  
  private void initalizeMetadata(ICoercionHandler paramICoercionHandler)
    throws ErrorException
  {
    if ((this.m_leftOperand.getNumChildren() != 1) && (this.m_rightOperand.getNumChildren() != 1)) {
      throw SQLEngineExceptionFactory.featureNotImplementedException("Comparison lists are not supported.");
    }
    AEValueExpr localAEValueExpr1 = (AEValueExpr)this.m_leftOperand.getChild(0);
    AEValueExpr localAEValueExpr2 = (AEValueExpr)this.m_rightOperand.getChild(0);
    this.m_coercionColumnMeta = paramICoercionHandler.coerceComparisonColumns(new AECoercionColumnInfo(localAEValueExpr1), new AECoercionColumnInfo(localAEValueExpr2));
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public Iterator<IAENode> getChildItr()
  {
    return asList().iterator();
  }
  
  public IColumn getCoercedColumnMetadata()
  {
    return this.m_coercionColumnMeta;
  }
  
  public AEComparisonType getComparisonOp()
  {
    return this.m_compOp;
  }
  
  public String getLogString()
  {
    String str = "AEComparison: ";
    str = str + this.m_compOp.toString();
    return str;
  }
  
  public String toString()
  {
    return getLogString();
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
  
  public boolean isEquivalent(IAENode paramIAENode)
  {
    if (this == paramIAENode) {
      return true;
    }
    if (!(paramIAENode instanceof AEComparison)) {
      return false;
    }
    AEComparison localAEComparison = (AEComparison)paramIAENode;
    return ((this.m_compOp == localAEComparison.m_compOp) && (this.m_leftOperand.isEquivalent(localAEComparison.m_leftOperand)) && (this.m_rightOperand.isEquivalent(localAEComparison.m_rightOperand))) || ((this.m_compOp == localAEComparison.m_compOp.flip()) && (this.m_leftOperand.isEquivalent(localAEComparison.m_rightOperand)) && (this.m_rightOperand.isEquivalent(localAEComparison.m_leftOperand)));
  }
  
  public AEBooleanExpr.AEBooleanType getType()
  {
    return AEBooleanExpr.AEBooleanType.COMPARISON;
  }
  
  public AEComparison copy()
  {
    return new AEComparison(this);
  }
  
  public AEValueExprList getLeftOperand()
  {
    return this.m_leftOperand;
  }
  
  public AEValueExprList getRightOperand()
  {
    return this.m_rightOperand;
  }
  
  public void updateCoercion()
    throws ErrorException
  {
    initalizeMetadata(this.m_coercionHandler);
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
          return AEComparison.this.m_leftOperand;
        case 1: 
          return AEComparison.this.m_rightOperand;
        }
        throw new IndexOutOfBoundsException("Index: " + paramAnonymousInt);
      }
      
      public int size()
      {
        return 2;
      }
    };
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/bool/AEComparison.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */