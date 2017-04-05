package com.simba.sqlengine.aeprocessor.aetree.value;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.ColumnMetadata;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.dataengine.utilities.TypeUtilities;
import com.simba.dsi.exceptions.NumericOverflowException;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.IAENodeVisitor;
import com.simba.sqlengine.aeprocessor.aetree.IAEUnaryNode;
import com.simba.sqlengine.aeprocessor.metadatautil.MetadataColumnInfo;
import com.simba.sqlengine.dsiext.dataengine.ICoercionHandler;
import com.simba.sqlengine.dsiext.dataengine.IColumnInfo.ColumnType;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.support.exceptions.ErrorException;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;

public class AEGeneralAggrFn
  extends AEAggrFn
  implements IAEUnaryNode<AEValueExpr>
{
  private static final MetadataColumnInfo INT_COLUMN = new MetadataColumnInfo(new ColumnMetadata(new TypeMetadata((short)-5, "SQL_BIGINT", (short)19, (short)0, 0, false)), IColumnInfo.ColumnType.COLUMN);
  private AEAggrFn.AggrFnQuantifier m_setQuantifier;
  private AEValueExpr m_operand;
  private IColumn m_metadata;
  private final ICoercionHandler m_coercionHandler;
  
  public AEGeneralAggrFn(AEAggrFn.AggrFnId paramAggrFnId, AEAggrFn.AggrFnQuantifier paramAggrFnQuantifier, AEValueExpr paramAEValueExpr, ICoercionHandler paramICoercionHandler)
    throws ErrorException
  {
    super(paramAggrFnId);
    this.m_setQuantifier = paramAggrFnQuantifier;
    this.m_operand = paramAEValueExpr;
    validate();
    this.m_coercionHandler = paramICoercionHandler;
    initializeMetadata();
    this.m_operand.setParent(this);
  }
  
  public AEGeneralAggrFn(AEGeneralAggrFn paramAEGeneralAggrFn)
  {
    super(paramAEGeneralAggrFn.getAggrFnId());
    this.m_setQuantifier = paramAEGeneralAggrFn.m_setQuantifier;
    this.m_operand = paramAEGeneralAggrFn.m_operand.copy();
    this.m_metadata = createColumnMetadata(paramAEGeneralAggrFn.m_metadata);
    this.m_coercionHandler = paramAEGeneralAggrFn.m_coercionHandler;
    this.m_operand.setParent(this);
  }
  
  public <T> T acceptVisitor(IAENodeVisitor<T> paramIAENodeVisitor)
    throws ErrorException
  {
    return (T)paramIAENodeVisitor.visit(this);
  }
  
  public Iterator<AEValueExpr> getChildItr()
  {
    return asList().iterator();
  }
  
  public int getNumChildren()
  {
    return 1;
  }
  
  public boolean isEquivalent(IAENode paramIAENode)
  {
    if (this == paramIAENode) {
      return true;
    }
    if (!(paramIAENode instanceof AEGeneralAggrFn)) {
      return false;
    }
    AEGeneralAggrFn localAEGeneralAggrFn = (AEGeneralAggrFn)paramIAENode;
    return (getAggrFnId() == localAEGeneralAggrFn.getAggrFnId()) && (this.m_operand.isEquivalent(localAEGeneralAggrFn.m_operand)) && (this.m_setQuantifier == localAEGeneralAggrFn.m_setQuantifier);
  }
  
  public IColumn getColumn()
  {
    return this.m_metadata;
  }
  
  public AEValueExpr copy()
  {
    return new AEGeneralAggrFn(this);
  }
  
  public String getLogString()
  {
    return "AEGeneralAggrFn: " + getAggrFnId().name() + " " + this.m_setQuantifier.name();
  }
  
  public AEValueExpr getOperand()
  {
    return this.m_operand;
  }
  
  public void setOperand(AEValueExpr paramAEValueExpr)
  {
    this.m_operand.setParent(null);
    this.m_operand = paramAEValueExpr;
    paramAEValueExpr.setParent(this);
  }
  
  private void initializeMetadata()
    throws ErrorException
  {
    TypeMetadata localTypeMetadata;
    switch (getAggrFnId())
    {
    case COUNT: 
      this.m_metadata = new ColumnMetadata(TypeMetadata.createTypeMetadata(-5, false));
      break;
    case MAX: 
    case MIN: 
      this.m_metadata = copyOperandMetadata();
      break;
    case SUM: 
      localTypeMetadata = this.m_operand.getColumn().getTypeMetadata();
      if (localTypeMetadata.isExactNumericType()) {
        this.m_metadata = copyOperandMetadata();
      } else if ((TypeUtilities.isNumberType(localTypeMetadata.getType())) || (localTypeMetadata.getType() == -7)) {
        this.m_metadata = new ColumnMetadata(TypeMetadata.createTypeMetadata(8));
      }
      break;
    case AVG: 
      localTypeMetadata = this.m_operand.getColumn().getTypeMetadata();
      if (localTypeMetadata.isExactNumericType())
      {
        ColumnMetadata localColumnMetadata = new ColumnMetadata(new TypeMetadata((short)3, "SQL_DECIMAL", (short)38, localTypeMetadata.getScale(), 0));
        this.m_metadata = this.m_coercionHandler.coerceDivisionColumns(new MetadataColumnInfo(localColumnMetadata, IColumnInfo.ColumnType.COLUMN), INT_COLUMN);
      }
      else if ((TypeUtilities.isNumberType(localTypeMetadata.getType())) || (localTypeMetadata.getType() == -7))
      {
        this.m_metadata = new ColumnMetadata(TypeMetadata.createTypeMetadata(8));
      }
      else
      {
        this.m_metadata = new ColumnMetadata(TypeMetadata.createTypeMetadata(localTypeMetadata.getType()));
      }
      break;
    default: 
      throw new IllegalStateException("Unknown SET function name: " + getAggrFnId().name());
    }
    if ((this.m_metadata instanceof ColumnMetadata)) {
      ((ColumnMetadata)this.m_metadata).setName(null);
    }
  }
  
  private void validate()
    throws ErrorException
  {
    int i = this.m_operand.getTypeMetadata().getType();
    switch (getAggrFnId())
    {
    case COUNT: 
      return;
    case SUM: 
    case AVG: 
      if ((!TypeUtilities.isNumberType(i)) && (i != -7)) {
        throw SQLEngineExceptionFactory.invalidSetArgTypeException(getAggrFnId().name(), i);
      }
      return;
    case MAX: 
    case MIN: 
      if (i == -4) {
        throw SQLEngineExceptionFactory.invalidSetArgTypeException(getAggrFnId().name(), i);
      }
      return;
    }
    throw new IllegalStateException("Unknown SET function name: " + getAggrFnId().name());
  }
  
  public AEAggrFn.AggrFnQuantifier getSetQuantifier()
  {
    return this.m_setQuantifier;
  }
  
  private List<AEValueExpr> asList()
  {
    new AbstractList()
    {
      public AEValueExpr get(int paramAnonymousInt)
      {
        if (0 == paramAnonymousInt) {
          return AEGeneralAggrFn.this.m_operand;
        }
        throw new IndexOutOfBoundsException("Index: " + paramAnonymousInt);
      }
      
      public int size()
      {
        return AEGeneralAggrFn.this.getNumChildren();
      }
    };
  }
  
  private ColumnMetadata copyOperandMetadata()
  {
    IColumn localIColumn = this.m_operand.getColumn();
    TypeMetadata localTypeMetadata = localIColumn.getTypeMetadata();
    ColumnMetadata localColumnMetadata = new ColumnMetadata(TypeMetadata.copyOf(localTypeMetadata));
    localColumnMetadata.setCaseSensitive(localIColumn.isCaseSensitive());
    try
    {
      localColumnMetadata.setColumnLength(localIColumn.getColumnLength());
    }
    catch (NumericOverflowException localNumericOverflowException)
    {
      throw new RuntimeException(localNumericOverflowException);
    }
    return localColumnMetadata;
  }
  
  public void updateColumn()
    throws ErrorException
  {
    initializeMetadata();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/value/AEGeneralAggrFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */