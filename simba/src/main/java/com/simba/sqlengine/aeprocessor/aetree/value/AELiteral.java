package com.simba.sqlengine.aeprocessor.aetree.value;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.ColumnMetadata;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.dsiext.dataengine.ICoercionHandler;
import com.simba.sqlengine.dsiext.dataengine.ICoercionHandler.LiteralType;
import com.simba.sqlengine.dsiext.dataengine.SqlDataEngineContext;
import com.simba.sqlengine.parser.type.PTLiteralType;
import com.simba.support.exceptions.ErrorException;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;

public class AELiteral
  extends AEValueExpr
{
  private String m_literalStr;
  private PTLiteralType m_literalType;
  private IColumn m_metaData;
  private boolean m_isSigned;
  
  public AELiteral(String paramString, PTLiteralType paramPTLiteralType, SqlDataEngineContext paramSqlDataEngineContext)
    throws ErrorException
  {
    this(paramString, paramPTLiteralType, false, paramSqlDataEngineContext);
  }
  
  public AELiteral(String paramString, PTLiteralType paramPTLiteralType, boolean paramBoolean, SqlDataEngineContext paramSqlDataEngineContext)
    throws ErrorException
  {
    this.m_literalStr = (paramPTLiteralType == PTLiteralType.CHARSTR ? paramString : paramString.trim());
    this.m_literalType = paramPTLiteralType;
    this.m_isSigned = paramBoolean;
    Object localObject = getLiteralMetadata(this.m_literalStr, paramPTLiteralType, paramBoolean, paramSqlDataEngineContext);
    if ("".equals(((IColumn)localObject).getName()))
    {
      ColumnMetadata localColumnMetadata = ColumnMetadata.copyOf((IColumn)localObject);
      localColumnMetadata.setName(null);
      localObject = localColumnMetadata;
    }
    this.m_metaData = ((IColumn)localObject);
  }
  
  public AELiteral(AELiteral paramAELiteral)
  {
    super(paramAELiteral);
    this.m_literalStr = paramAELiteral.m_literalStr;
    this.m_literalType = paramAELiteral.m_literalType;
    this.m_isSigned = paramAELiteral.m_isSigned;
    this.m_metaData = ColumnMetadata.copyOf(paramAELiteral.m_metaData);
  }
  
  public String getStringValue()
  {
    return this.m_literalStr;
  }
  
  public PTLiteralType getLiteralType()
  {
    return this.m_literalType;
  }
  
  public IColumn getColumn()
  {
    return this.m_metaData;
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public AELiteral copy()
  {
    return new AELiteral(this);
  }
  
  public Iterator<IAENode> getChildItr()
  {
    AbstractList local1 = new AbstractList()
    {
      public IAENode get(int paramAnonymousInt)
      {
        throw new IndexOutOfBoundsException("Child index out of bound for AELiteral");
      }
      
      public int size()
      {
        return 0;
      }
    };
    return local1.iterator();
  }
  
  public String getLogString()
  {
    StringBuilder localStringBuilder = new StringBuilder(100);
    localStringBuilder.append(getClass().getSimpleName() + ": " + this.m_literalStr + ": ");
    switch (this.m_literalType)
    {
    case TIMESTAMP: 
      localStringBuilder.append("Date Time Literal");
      break;
    case DATE: 
      localStringBuilder.append("Date Literal");
      break;
    case TIME: 
      localStringBuilder.append("Time Literal");
      break;
    case APPROXNUM: 
      localStringBuilder.append("Approximate Numeric Literal");
      break;
    case DECIMAL: 
      localStringBuilder.append("Decimal Numeric Literal");
      break;
    case CHARSTR: 
      localStringBuilder.append("Character String Literal");
      break;
    case USINT: 
      if (this.m_isSigned) {
        localStringBuilder.append("Signed Integer Literal");
      } else {
        localStringBuilder.append("Unsigned Integer Literal");
      }
      break;
    case DATATYPE: 
      localStringBuilder.append("SQL Data Type Keyword Literal");
      break;
    default: 
      localStringBuilder.append(this.m_literalType.name());
    }
    return localStringBuilder.toString();
  }
  
  public int getNumChildren()
  {
    return 0;
  }
  
  public boolean isEquivalent(IAENode paramIAENode)
  {
    if (paramIAENode == this) {
      return true;
    }
    if (!(paramIAENode instanceof AELiteral)) {
      return false;
    }
    AELiteral localAELiteral = (AELiteral)paramIAENode;
    return (this.m_isSigned == localAELiteral.m_isSigned) && (this.m_literalStr.equals(localAELiteral.m_literalStr)) && (this.m_literalType == localAELiteral.m_literalType) && (this.m_metaData.equals(localAELiteral.m_metaData));
  }
  
  private IColumn getLiteralMetadata(String paramString, PTLiteralType paramPTLiteralType, boolean paramBoolean, SqlDataEngineContext paramSqlDataEngineContext)
    throws ErrorException
  {
    ICoercionHandler localICoercionHandler = paramSqlDataEngineContext.getCoercionHandler();
    switch (paramPTLiteralType)
    {
    case DATATYPE: 
      return localICoercionHandler.determineLiteralColumn(paramString, ICoercionHandler.LiteralType.DATATYPE);
    case CHARSTR: 
      return localICoercionHandler.determineLiteralColumn(paramString, ICoercionHandler.LiteralType.CHAR);
    case USINT: 
      return localICoercionHandler.determineLiteralColumn(paramString, ICoercionHandler.LiteralType.UNSIGNED_INT);
    case SINT: 
      return localICoercionHandler.determineLiteralColumn(paramString, ICoercionHandler.LiteralType.SIGNED_INT);
    case APPROXNUM: 
      return localICoercionHandler.determineLiteralColumn(paramString, ICoercionHandler.LiteralType.APROX_NUM);
    case DECIMAL: 
      return localICoercionHandler.determineLiteralColumn(paramString, ICoercionHandler.LiteralType.EXACT_NUM);
    case DATE: 
      return localICoercionHandler.determineLiteralColumn(paramString, ICoercionHandler.LiteralType.DATE);
    case TIME: 
      return localICoercionHandler.determineLiteralColumn(paramString, ICoercionHandler.LiteralType.TIME);
    case TIMESTAMP: 
      return localICoercionHandler.determineLiteralColumn(paramString, ICoercionHandler.LiteralType.TIMESTAMP);
    }
    throw new IllegalArgumentException("Unrecognized literal type.");
  }
  
  public void updateColumn()
    throws ErrorException
  {}
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/value/AELiteral.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */