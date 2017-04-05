package com.simba.sqlengine.aeprocessor.aetree.bool;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.ColumnMetadata;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AEInPredicate
  extends AEBooleanExpr
  implements IAEBinaryNode<AEValueExprList, IAENode>
{
  private static final int NUM_CHILDREN = 2;
  private final ICoercionHandler m_coercionHandler;
  private AEValueExprList m_leftOperand;
  private IAENode m_rightOperand;
  private IColumn m_coercionColumnMeta;
  private IAENode m_parent = null;
  
  public AEInPredicate(SqlDataEngineContext paramSqlDataEngineContext, AEValueExprList paramAEValueExprList1, AEValueExprList paramAEValueExprList2)
    throws ErrorException
  {
    if ((null == paramSqlDataEngineContext) || (null == paramAEValueExprList1) || (null == paramAEValueExprList2) || (0 == paramAEValueExprList1.getNumChildren()) || (0 == paramAEValueExprList2.getNumChildren())) {
      throw new IllegalArgumentException("Illegal parameters for AEInPredicate. Null parameter or invalid operand");
    }
    if (1 < paramAEValueExprList1.getNumChildren()) {
      throw SQLEngineExceptionFactory.featureNotImplementedException("Multiple expresions for left operand of In predicate is not supported.");
    }
    this.m_coercionHandler = paramSqlDataEngineContext.getCoercionHandler();
    this.m_leftOperand = paramAEValueExprList1;
    this.m_leftOperand.setParent(this);
    this.m_rightOperand = paramAEValueExprList2;
    this.m_rightOperand.setParent(this);
    initalizeMetadata(this.m_coercionHandler);
  }
  
  public AEInPredicate(SqlDataEngineContext paramSqlDataEngineContext, AEValueExprList paramAEValueExprList, AERelationalExpr paramAERelationalExpr)
    throws ErrorException
  {
    if ((null == paramSqlDataEngineContext) || (null == paramAEValueExprList) || (null == paramAERelationalExpr) || (0 == paramAEValueExprList.getNumChildren()) || (0 == paramAERelationalExpr.getColumnCount())) {
      throw new IllegalArgumentException("Illegal parameters for AEInPredicate. Null parameter or invalid operand");
    }
    if ((1 != paramAEValueExprList.getNumChildren()) || (paramAERelationalExpr.getColumnCount() != 1)) {
      throw SQLEngineExceptionFactory.featureNotImplementedException("Multiple expresions for operands of In predicate is not supported.");
    }
    this.m_coercionHandler = paramSqlDataEngineContext.getCoercionHandler();
    this.m_leftOperand = paramAEValueExprList;
    this.m_leftOperand.setParent(this);
    this.m_rightOperand = paramAERelationalExpr;
    this.m_rightOperand.setParent(this);
    initalizeMetadata(this.m_coercionHandler);
  }
  
  private AEInPredicate(AEInPredicate paramAEInPredicate)
  {
    setIsOptimized(paramAEInPredicate.isOptimized());
    this.m_coercionHandler = paramAEInPredicate.m_coercionHandler;
    this.m_leftOperand = paramAEInPredicate.m_leftOperand.copy();
    this.m_leftOperand.setParent(this);
    this.m_rightOperand = paramAEInPredicate.m_rightOperand.copy();
    this.m_rightOperand.setParent(this);
    this.m_coercionColumnMeta = ColumnMetadata.copyOf(paramAEInPredicate.m_coercionColumnMeta);
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public Iterator<? extends IAENode> getChildItr()
  {
    return asList().iterator();
  }
  
  public String getLogString()
  {
    return getClass().getSimpleName();
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
    return AEBooleanExpr.AEBooleanType.IN_PRED;
  }
  
  public boolean isEquivalent(IAENode paramIAENode)
  {
    if (this == paramIAENode) {
      return true;
    }
    if (!(paramIAENode instanceof AEInPredicate)) {
      return false;
    }
    AEInPredicate localAEInPredicate = (AEInPredicate)paramIAENode;
    return (this.m_leftOperand.isEquivalent(localAEInPredicate.m_leftOperand)) && (this.m_rightOperand.isEquivalent(localAEInPredicate.m_rightOperand));
  }
  
  public AEInPredicate copy()
  {
    return new AEInPredicate(this);
  }
  
  public AEValueExprList getLeftOperand()
  {
    return this.m_leftOperand;
  }
  
  public IAENode getRightOperand()
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
    if (this.m_leftOperand.getNumChildren() != 1) {
      throw SQLEngineExceptionFactory.featureNotImplementedException("In Predicate only supports one value as the left operand.");
    }
    AECoercionColumnInfo localAECoercionColumnInfo = new AECoercionColumnInfo((AEValueExpr)this.m_leftOperand.getChild(0));
    ArrayList localArrayList = new ArrayList();
    Object localObject;
    if ((this.m_rightOperand instanceof AERelationalExpr))
    {
      localObject = (AERelationalExpr)this.m_rightOperand;
      localArrayList.add(new MetadataColumnInfo(((AERelationalExpr)localObject).getColumn(0), IColumnInfo.ColumnType.COLUMN));
    }
    else
    {
      localObject = (AEValueExprList)this.m_rightOperand;
      Iterator localIterator = ((AEValueExprList)localObject).getChildItr();
      while (localIterator.hasNext()) {
        localArrayList.add(new AECoercionColumnInfo((AEValueExpr)localIterator.next()));
      }
    }
    this.m_coercionColumnMeta = paramICoercionHandler.coerceInColumns(localAECoercionColumnInfo, localArrayList);
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
          return AEInPredicate.this.m_leftOperand;
        case 1: 
          return AEInPredicate.this.m_rightOperand;
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


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/bool/AEInPredicate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */