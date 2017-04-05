package com.simba.sqlengine.aeprocessor.metadatautil;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.aeprocessor.aetree.AEDefaultVisitor;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.value.AEAdd;
import com.simba.sqlengine.aeprocessor.aetree.value.AEColumnReference;
import com.simba.sqlengine.aeprocessor.aetree.value.AEConcat;
import com.simba.sqlengine.aeprocessor.aetree.value.AECountStarAggrFn;
import com.simba.sqlengine.aeprocessor.aetree.value.AECustomScalarFn;
import com.simba.sqlengine.aeprocessor.aetree.value.AEDivide;
import com.simba.sqlengine.aeprocessor.aetree.value.AEGeneralAggrFn;
import com.simba.sqlengine.aeprocessor.aetree.value.AELiteral;
import com.simba.sqlengine.aeprocessor.aetree.value.AEMultiply;
import com.simba.sqlengine.aeprocessor.aetree.value.AENegate;
import com.simba.sqlengine.aeprocessor.aetree.value.AENull;
import com.simba.sqlengine.aeprocessor.aetree.value.AEParameter;
import com.simba.sqlengine.aeprocessor.aetree.value.AEProxyColumn;
import com.simba.sqlengine.aeprocessor.aetree.value.AERename;
import com.simba.sqlengine.aeprocessor.aetree.value.AEScalarFn;
import com.simba.sqlengine.aeprocessor.aetree.value.AESearchedCase;
import com.simba.sqlengine.aeprocessor.aetree.value.AESimpleCase;
import com.simba.sqlengine.aeprocessor.aetree.value.AESubtract;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueSubQuery;
import com.simba.sqlengine.dsiext.dataengine.IColumnInfo;
import com.simba.sqlengine.dsiext.dataengine.IColumnInfo.ColumnType;
import com.simba.support.exceptions.ErrorException;

public class AECoercionColumnInfo
  extends AEAbstractColumnInfo
{
  private AEValueExpr m_node = null;
  private IColumnInfo.ColumnType m_columnType = null;
  private String m_literalString = null;
  private IColumn m_colMeta = null;
  
  public AECoercionColumnInfo(AEValueExpr paramAEValueExpr)
  {
    this.m_node = paramAEValueExpr;
    this.m_colMeta = paramAEValueExpr.getColumn();
    if ((paramAEValueExpr instanceof AEProxyColumn))
    {
      IColumnInfo localIColumnInfo = ((AEProxyColumn)paramAEValueExpr).getProxiedColumnInfo();
      if (localIColumnInfo == null) {
        throw new IllegalArgumentException("Invalid proxy column");
      }
      this.m_columnType = localIColumnInfo.getColumnType();
      this.m_literalString = localIColumnInfo.getLiteralString();
    }
  }
  
  public IColumnInfo.ColumnType getColumnType()
  {
    if (this.m_columnType != null) {
      return this.m_columnType;
    }
    try
    {
      ColumnTypeFinder localColumnTypeFinder = new ColumnTypeFinder();
      this.m_columnType = ((IColumnInfo.ColumnType)this.m_node.acceptVisitor(localColumnTypeFinder));
      this.m_literalString = localColumnTypeFinder.getLiterString();
      return this.m_columnType;
    }
    catch (ErrorException localErrorException)
    {
      throw new IllegalStateException("Impossible state.", localErrorException);
    }
  }
  
  public String getLiteralString()
  {
    if (this.m_columnType == null) {
      getColumnType();
    }
    return this.m_literalString;
  }
  
  protected IColumn getColumnMetadata()
  {
    return this.m_colMeta;
  }
  
  public static class ColumnTypeFinder
    extends AEDefaultVisitor<IColumnInfo.ColumnType>
  {
    private String m_literalString = null;
    
    public String getLiterString()
    {
      return this.m_literalString;
    }
    
    public IColumnInfo.ColumnType visit(AEColumnReference paramAEColumnReference)
    {
      return IColumnInfo.ColumnType.COLUMN;
    }
    
    public IColumnInfo.ColumnType visit(AESearchedCase paramAESearchedCase)
    {
      return IColumnInfo.ColumnType.CASE_EXPRESSION;
    }
    
    public IColumnInfo.ColumnType visit(AESimpleCase paramAESimpleCase)
    {
      return IColumnInfo.ColumnType.CASE_EXPRESSION;
    }
    
    public IColumnInfo.ColumnType visit(AECountStarAggrFn paramAECountStarAggrFn)
    {
      return IColumnInfo.ColumnType.SET_FUNCTION;
    }
    
    public IColumnInfo.ColumnType visit(AEGeneralAggrFn paramAEGeneralAggrFn)
    {
      return IColumnInfo.ColumnType.SET_FUNCTION;
    }
    
    public IColumnInfo.ColumnType visit(AERename paramAERename)
    {
      try
      {
        return (IColumnInfo.ColumnType)paramAERename.getOperand().acceptVisitor(this);
      }
      catch (ErrorException localErrorException)
      {
        throw new RuntimeException();
      }
    }
    
    public IColumnInfo.ColumnType visit(AEScalarFn paramAEScalarFn)
    {
      return IColumnInfo.ColumnType.SCALAR_FUNCTION;
    }
    
    public IColumnInfo.ColumnType visit(AECustomScalarFn paramAECustomScalarFn)
    {
      return IColumnInfo.ColumnType.SCALAR_FUNCTION;
    }
    
    public IColumnInfo.ColumnType visit(AELiteral paramAELiteral)
    {
      this.m_literalString = paramAELiteral.getStringValue();
      return IColumnInfo.ColumnType.LITERAL;
    }
    
    public IColumnInfo.ColumnType visit(AENull paramAENull)
    {
      return IColumnInfo.ColumnType.NULL;
    }
    
    public IColumnInfo.ColumnType visit(AENegate paramAENegate)
    {
      return IColumnInfo.ColumnType.NEGATE;
    }
    
    public IColumnInfo.ColumnType visit(AEAdd paramAEAdd)
    {
      return IColumnInfo.ColumnType.PLUS;
    }
    
    public IColumnInfo.ColumnType visit(AEConcat paramAEConcat)
    {
      return IColumnInfo.ColumnType.CONCATENATION;
    }
    
    public IColumnInfo.ColumnType visit(AEDivide paramAEDivide)
    {
      return IColumnInfo.ColumnType.DIVISION;
    }
    
    public IColumnInfo.ColumnType visit(AEMultiply paramAEMultiply)
    {
      return IColumnInfo.ColumnType.MULIPLICATION;
    }
    
    public IColumnInfo.ColumnType visit(AESubtract paramAESubtract)
    {
      return IColumnInfo.ColumnType.MINUS;
    }
    
    public IColumnInfo.ColumnType visit(AEParameter paramAEParameter)
    {
      if (paramAEParameter.hasBeenSet()) {
        return IColumnInfo.ColumnType.PARAMETER_SET;
      }
      return IColumnInfo.ColumnType.PARAMETER_UNSET;
    }
    
    public IColumnInfo.ColumnType visit(AEValueSubQuery paramAEValueSubQuery)
    {
      return IColumnInfo.ColumnType.SCALAR_SUBQUERY;
    }
    
    public IColumnInfo.ColumnType visit(AEProxyColumn paramAEProxyColumn)
    {
      IColumnInfo localIColumnInfo = paramAEProxyColumn.getProxiedColumnInfo();
      if (localIColumnInfo == null) {
        throw new IllegalStateException("Proxy column is not properly initialized.");
      }
      this.m_literalString = localIColumnInfo.getLiteralString();
      return localIColumnInfo.getColumnType();
    }
    
    public IColumnInfo.ColumnType defaultVisit(IAENode paramIAENode)
    {
      throw new IllegalStateException("Unexpected node visited. Node: " + paramIAENode.getLogString());
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/metadatautil/AECoercionColumnInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */