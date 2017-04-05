package com.simba.sqlengine.aeprocessor.aetree.bool;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.ColumnMetadata;
import com.simba.sqlengine.aeprocessor.aetree.IAEBinaryNode;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;
import com.simba.sqlengine.aeprocessor.metadatautil.AECoercionColumnInfo;
import com.simba.sqlengine.dsiext.dataengine.ICoercionHandler;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngineContext;
import com.simba.support.exceptions.ErrorException;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;

public class AELikePredicate
  extends AEBooleanExpr
  implements IAEBinaryNode<AEValueExpr, AEValueExpr>
{
  private final ICoercionHandler m_coercionHandler;
  private AEValueExpr m_leftExpr;
  private AEValueExpr m_rightExpr;
  private AEValueExpr m_escape;
  private IAENode m_parent = null;
  private IColumn m_coercedMeta;
  
  public AELikePredicate(SqlDataEngineContext paramSqlDataEngineContext, AEValueExpr paramAEValueExpr1, AEValueExpr paramAEValueExpr2, AEValueExpr paramAEValueExpr3)
    throws ErrorException
  {
    if ((null == paramSqlDataEngineContext) || (null == paramAEValueExpr1) || (null == paramAEValueExpr2)) {
      throw new NullPointerException("Arguments cannot be null.");
    }
    this.m_coercionHandler = paramSqlDataEngineContext.getCoercionHandler();
    this.m_leftExpr = paramAEValueExpr1;
    this.m_leftExpr.setParent(this);
    this.m_rightExpr = paramAEValueExpr2;
    this.m_rightExpr.setParent(this);
    this.m_escape = paramAEValueExpr3;
    if (this.m_escape != null) {
      this.m_escape.setParent(this);
    }
    initalizeMetadata(this.m_coercionHandler);
  }
  
  private AELikePredicate(AELikePredicate paramAELikePredicate)
  {
    setIsOptimized(paramAELikePredicate.isOptimized());
    this.m_coercionHandler = paramAELikePredicate.m_coercionHandler;
    this.m_leftExpr = paramAELikePredicate.m_leftExpr.copy();
    this.m_leftExpr.setParent(this);
    this.m_rightExpr = paramAELikePredicate.m_rightExpr.copy();
    this.m_rightExpr.setParent(this);
    if (null != paramAELikePredicate.m_escape)
    {
      this.m_escape = paramAELikePredicate.m_escape.copy();
      this.m_escape.setParent(this);
    }
    this.m_coercedMeta = ColumnMetadata.copyOf(paramAELikePredicate.m_coercedMeta);
  }
  
  private void initalizeMetadata(ICoercionHandler paramICoercionHandler)
    throws ErrorException
  {
    this.m_coercedMeta = paramICoercionHandler.coerceLikeColumns(new AECoercionColumnInfo(this.m_leftExpr), new AECoercionColumnInfo(this.m_rightExpr));
    assert (null != this.m_coercedMeta);
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
    return this.m_coercedMeta;
  }
  
  public String getLogString()
  {
    return getClass().getSimpleName();
  }
  
  public String toString()
  {
    return getLogString();
  }
  
  public int getNumChildren()
  {
    return asList().size();
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
    return AEBooleanExpr.AEBooleanType.LIKE_PRED;
  }
  
  public boolean isEquivalent(IAENode paramIAENode)
  {
    if (this == paramIAENode) {
      return true;
    }
    if (!(paramIAENode instanceof AELikePredicate)) {
      return false;
    }
    AELikePredicate localAELikePredicate = (AELikePredicate)paramIAENode;
    if ((!this.m_leftExpr.isEquivalent(localAELikePredicate.m_leftExpr)) || (!this.m_rightExpr.isEquivalent(localAELikePredicate.m_rightExpr))) {
      return false;
    }
    if (null == this.m_escape) {
      return null == localAELikePredicate.m_escape;
    }
    return this.m_escape.isEquivalent(localAELikePredicate.m_escape);
  }
  
  public AEBooleanExpr copy()
  {
    return new AELikePredicate(this);
  }
  
  public AEValueExpr getLeftOperand()
  {
    return this.m_leftExpr;
  }
  
  public AEValueExpr getRightOperand()
  {
    return this.m_rightExpr;
  }
  
  public AEValueExpr getEscapeChar()
  {
    return this.m_escape;
  }
  
  public boolean hasEscapeChar()
  {
    return null != this.m_escape;
  }
  
  public void setLeftOperand(AEValueExpr paramAEValueExpr)
  {
    if (paramAEValueExpr == null) {
      throw new NullPointerException("can not set null to like operand.");
    }
    this.m_leftExpr = paramAEValueExpr;
    this.m_leftExpr.setParent(this);
  }
  
  public void setRightOperand(AEValueExpr paramAEValueExpr)
  {
    if (paramAEValueExpr == null) {
      throw new NullPointerException("can not set null to like operand.");
    }
    this.m_rightExpr = paramAEValueExpr;
    this.m_rightExpr.setParent(this);
  }
  
  public void setEscape(AEValueExpr paramAEValueExpr)
  {
    this.m_escape = paramAEValueExpr;
    this.m_escape.setParent(this);
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
          return AELikePredicate.this.m_leftExpr;
        case 1: 
          return AELikePredicate.this.m_rightExpr;
        case 2: 
          if (null != AELikePredicate.this.m_escape) {
            return AELikePredicate.this.m_escape;
          }
          break;
        }
        throw new IndexOutOfBoundsException("Index: " + paramAnonymousInt);
      }
      
      public int size()
      {
        if (null != AELikePredicate.this.m_escape) {
          return 3;
        }
        return 2;
      }
    };
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/bool/AELikePredicate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */