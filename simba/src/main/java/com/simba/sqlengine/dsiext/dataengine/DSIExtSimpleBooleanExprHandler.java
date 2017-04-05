package com.simba.sqlengine.dsiext.dataengine;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.DataWrapper;
import com.simba.sqlengine.aeprocessor.aetree.AEComparisonType;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEComparison;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEInPredicate;
import com.simba.sqlengine.aeprocessor.aetree.bool.AENot;
import com.simba.sqlengine.aeprocessor.aetree.bool.AENullPredicate;
import com.simba.sqlengine.aeprocessor.aetree.value.AEColumnReference;
import com.simba.sqlengine.aeprocessor.aetree.value.AELiteral;
import com.simba.sqlengine.aeprocessor.aetree.value.AENegate;
import com.simba.sqlengine.aeprocessor.aetree.value.AEParameter;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExprList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class DSIExtSimpleBooleanExprHandler
  extends DSIExtAbstractBooleanExprHandler
{
  private int m_paramSetCount;
  
  public DSIExtSimpleBooleanExprHandler(int paramInt)
  {
    this.m_paramSetCount = paramInt;
  }
  
  protected abstract boolean passdownSimpleComparison(AEColumnReference paramAEColumnReference, DSIExtComparisonValue paramDSIExtComparisonValue, AEComparisonType paramAEComparisonType);
  
  protected abstract boolean passdownSimpleComparison(AEColumnReference paramAEColumnReference1, AEColumnReference paramAEColumnReference2, AEComparisonType paramAEComparisonType);
  
  protected abstract boolean passdownSimpleNullPredicate(AEColumnReference paramAEColumnReference, boolean paramBoolean);
  
  protected abstract boolean passdownSimpleInPredicate(AEColumnReference paramAEColumnReference, List<DSIExtComparisonValue> paramList, boolean paramBoolean);
  
  protected boolean passdownComparison(AEComparison paramAEComparison)
  {
    assert (null != paramAEComparison);
    if ((paramAEComparison.getLeftOperand().getNumChildren() == 1) && (paramAEComparison.getRightOperand().getNumChildren() == 1))
    {
      AEValueExpr localAEValueExpr1 = (AEValueExpr)paramAEComparison.getLeftOperand().getChild(0);
      AEValueExpr localAEValueExpr2 = (AEValueExpr)paramAEComparison.getRightOperand().getChild(0);
      if (((localAEValueExpr2 instanceof AEColumnReference)) && ((localAEValueExpr1 instanceof AEColumnReference))) {
        return passdownSimpleComparison((AEColumnReference)localAEValueExpr1, (AEColumnReference)localAEValueExpr2, paramAEComparison.getComparisonOp());
      }
      AEColumnReference localAEColumnReference = null;
      DSIExtComparisonValue localDSIExtComparisonValue = null;
      AEComparisonType localAEComparisonType = null;
      if ((localAEValueExpr1 instanceof AEColumnReference))
      {
        localAEColumnReference = (AEColumnReference)localAEValueExpr1;
        localDSIExtComparisonValue = convertToCompValue(localAEValueExpr2);
        localAEComparisonType = paramAEComparison.getComparisonOp();
      }
      else if ((localAEValueExpr2 instanceof AEColumnReference))
      {
        localAEColumnReference = (AEColumnReference)localAEValueExpr2;
        localDSIExtComparisonValue = convertToCompValue(localAEValueExpr1);
        localAEComparisonType = paramAEComparison.getComparisonOp().flip();
      }
      if ((localAEColumnReference == null) || (localDSIExtComparisonValue == null) || (localAEComparisonType == null)) {
        return false;
      }
      return passdownSimpleComparison(localAEColumnReference, localDSIExtComparisonValue, localAEComparisonType);
    }
    return false;
  }
  
  protected boolean passdownInPredicate(AEInPredicate paramAEInPredicate)
  {
    return passdownInPredicate(paramAEInPredicate, true);
  }
  
  protected boolean passdownNot(AENot paramAENot)
  {
    assert (null != paramAENot);
    AEBooleanExpr localAEBooleanExpr = paramAENot.getOperand();
    Object localObject;
    if ((localAEBooleanExpr instanceof AENullPredicate))
    {
      localObject = (AENullPredicate)localAEBooleanExpr;
      if (1 == ((AENullPredicate)localObject).getOperand().getNumChildren())
      {
        IAENode localIAENode = ((AENullPredicate)localObject).getOperand().getChild(0);
        if ((localIAENode instanceof AEColumnReference))
        {
          AEColumnReference localAEColumnReference = (AEColumnReference)localIAENode;
          return passdownSimpleNullPredicate(localAEColumnReference, false);
        }
      }
    }
    else if ((localAEBooleanExpr instanceof AEInPredicate))
    {
      localObject = (AEInPredicate)localAEBooleanExpr;
      return passdownInPredicate((AEInPredicate)localObject, false);
    }
    return false;
  }
  
  protected boolean passdownNullPredicate(AENullPredicate paramAENullPredicate)
  {
    assert (null != paramAENullPredicate);
    if (1 == paramAENullPredicate.getOperand().getNumChildren())
    {
      IAENode localIAENode = paramAENullPredicate.getOperand().getChild(0);
      if ((localIAENode instanceof AEColumnReference))
      {
        AEColumnReference localAEColumnReference = (AEColumnReference)localIAENode;
        return passdownSimpleNullPredicate(localAEColumnReference, true);
      }
    }
    return false;
  }
  
  private DSIExtComparisonValue convertToCompValue(AEValueExpr paramAEValueExpr)
  {
    assert (null != paramAEValueExpr);
    Object localObject1;
    if ((paramAEValueExpr instanceof AELiteral))
    {
      localObject1 = new DataWrapper();
      ((DataWrapper)localObject1).setChar(((AELiteral)paramAEValueExpr).getStringValue());
      return new DSIExtComparisonValue(((AELiteral)paramAEValueExpr).getColumn(), (DataWrapper)localObject1, false, null);
    }
    if ((paramAEValueExpr instanceof AENegate))
    {
      localObject1 = (AENegate)paramAEValueExpr;
      Object localObject2;
      if ((((AENegate)localObject1).getOperand() instanceof AELiteral))
      {
        localObject2 = (AELiteral)((AENegate)localObject1).getOperand();
        DataWrapper localDataWrapper = new DataWrapper();
        localDataWrapper.setChar(((AELiteral)localObject2).getStringValue());
        return new DSIExtComparisonValue(((AELiteral)localObject2).getColumn(), localDataWrapper, true, null);
      }
      if ((((AENegate)localObject1).getOperand() instanceof AEParameter))
      {
        if (this.m_paramSetCount == 1)
        {
          localObject2 = (AEParameter)((AENegate)localObject1).getOperand();
          return new DSIExtComparisonValue(((AEParameter)localObject2).getColumn(), ((AEParameter)localObject2).getInputData(), true, null);
        }
        if (this.m_paramSetCount == 0) {
          throw new IllegalStateException(" The parameter set count is not correct. It shouldn't be zero.");
        }
      }
      return null;
    }
    if ((paramAEValueExpr instanceof AEParameter))
    {
      localObject1 = (AEParameter)paramAEValueExpr;
      if (this.m_paramSetCount == 1)
      {
        if (null != ((AEParameter)localObject1).getInputData()) {
          return new DSIExtComparisonValue(((AEParameter)localObject1).getColumn(), ((AEParameter)localObject1).getInputData(), false, null);
        }
      }
      else if (this.m_paramSetCount == 0) {
        throw new IllegalStateException(" The parameter set count is not correct. It shouldn't be zero.");
      }
      return null;
    }
    return null;
  }
  
  private boolean passdownInPredicate(AEInPredicate paramAEInPredicate, boolean paramBoolean)
  {
    assert (null != paramAEInPredicate);
    AEValueExprList localAEValueExprList1 = paramAEInPredicate.getLeftOperand();
    IAENode localIAENode = paramAEInPredicate.getRightOperand();
    if ((1 == localAEValueExprList1.getNumChildren()) && ((localAEValueExprList1.getChild(0) instanceof AEColumnReference)) && ((localIAENode instanceof AEValueExprList)))
    {
      AEColumnReference localAEColumnReference = (AEColumnReference)localAEValueExprList1.getChild(0);
      ArrayList localArrayList = new ArrayList();
      AEValueExprList localAEValueExprList2 = (AEValueExprList)localIAENode;
      Iterator localIterator = localAEValueExprList2.getChildItr();
      AEValueExpr localAEValueExpr = null;
      DSIExtComparisonValue localDSIExtComparisonValue = null;
      while (localIterator.hasNext())
      {
        localAEValueExpr = (AEValueExpr)localIterator.next();
        localDSIExtComparisonValue = convertToCompValue(localAEValueExpr);
        if (null != localDSIExtComparisonValue) {
          localArrayList.add(localDSIExtComparisonValue);
        } else {
          return false;
        }
      }
      return passdownSimpleInPredicate(localAEColumnReference, localArrayList, paramBoolean);
    }
    return false;
  }
  
  public static class DSIExtComparisonValue
  {
    private IColumn m_compMetaData;
    private DataWrapper m_compData;
    private boolean m_isNegate;
    
    private DSIExtComparisonValue(IColumn paramIColumn, DataWrapper paramDataWrapper, boolean paramBoolean)
    {
      this.m_compMetaData = paramIColumn;
      this.m_compData = paramDataWrapper;
      this.m_isNegate = paramBoolean;
    }
    
    public IColumn getCompMetaData()
    {
      return this.m_compMetaData;
    }
    
    public DataWrapper getData()
    {
      return this.m_compData;
    }
    
    public boolean isNegate()
    {
      return this.m_isNegate;
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/dsiext/dataengine/DSIExtSimpleBooleanExprHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */