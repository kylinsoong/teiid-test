package com.simba.sqlengine.aeprocessor.metadatautil;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.ColumnMetadata;
import com.simba.dsi.dataengine.utilities.Nullable;
import com.simba.dsi.dataengine.utilities.Searchable;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.dsi.dataengine.utilities.Updatable;
import com.simba.dsi.exceptions.NumericOverflowException;
import com.simba.sqlengine.dsiext.dataengine.ICoercionHandler;
import com.simba.sqlengine.dsiext.dataengine.ICoercionHandler.LiteralType;
import com.simba.sqlengine.dsiext.dataengine.IColumnInfo;
import com.simba.sqlengine.dsiext.dataengine.IColumnInfo.ColumnType;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class AEMetadataCoercionHandler
  implements ICoercionHandler
{
  private final AECoercionProperties m_properties = new AECoercionProperties.Builder().build();
  private AETypeNormalizer m_typeNormalizer = new AETypeNormalizer(this.m_properties);
  private AESqlTypeCoercer m_typeCoercer = new AESqlTypeCoercer();
  private AEMetadataCoercer m_metadataCoercer = new AEMetadataCoercer(this.m_properties);
  private AELiteralMetadataFactory m_literalMetadataFactory = new AELiteralMetadataFactory(this.m_typeNormalizer, this.m_properties);
  
  public IColumn coerceComparisonColumns(IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2)
    throws ErrorException
  {
    return generalizedCoercion(CoercionOperation.COMPARISON, paramIColumnInfo1, paramIColumnInfo2);
  }
  
  public IColumn coerceConcatColumns(IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2)
    throws ErrorException
  {
    return generalizedCoercion(CoercionOperation.CONCAT, paramIColumnInfo1, paramIColumnInfo2);
  }
  
  public IColumn coerceInColumns(IColumnInfo paramIColumnInfo, List<? extends IColumnInfo> paramList)
    throws ErrorException
  {
    if ((paramList == null) || (paramList.size() == 0)) {
      throw new IllegalArgumentException("Can not coerce data type for IN expression: invalid argument.");
    }
    ArrayList localArrayList = new ArrayList();
    Object localObject1 = paramList.iterator();
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = (IColumnInfo)((Iterator)localObject1).next();
      if (IColumnInfo.ColumnType.NULL != ((IColumnInfo)localObject2).getColumnType()) {
        localArrayList.add(localObject2);
      }
    }
    if (localArrayList.isEmpty()) {
      localArrayList.add(paramList.get(0));
    }
    localObject1 = (IColumnInfo)localArrayList.get(0);
    Object localObject2 = MetadataUtilities.createColumnMetadata((IColumnInfo)localObject1);
    for (int i = 1; i < localArrayList.size(); i++)
    {
      localObject2 = generalizedCoercion(CoercionOperation.SET_OPERATION, (IColumnInfo)localObject1, (IColumnInfo)localArrayList.get(i));
      localObject1 = new MetadataColumnInfo((IColumn)localObject2, IColumnInfo.ColumnType.COLUMN);
    }
    return generalizedCoercion(CoercionOperation.IN, paramIColumnInfo, (IColumnInfo)localObject1);
  }
  
  public IColumn coerceLikeColumns(IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2)
    throws ErrorException
  {
    return generalizedCoercion(CoercionOperation.LIKE, paramIColumnInfo1, paramIColumnInfo2);
  }
  
  public IColumn coerceUnionColumns(IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2)
    throws ErrorException
  {
    return generalizedCoercion(CoercionOperation.SET_OPERATION, paramIColumnInfo1, paramIColumnInfo2);
  }
  
  public IColumn coerceDivisionColumns(IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2)
    throws ErrorException
  {
    return generalizedCoercion(CoercionOperation.DIVISION, paramIColumnInfo1, paramIColumnInfo2);
  }
  
  public IColumn coerceMinusColumns(IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2)
    throws ErrorException
  {
    return generalizedCoercion(CoercionOperation.MINUS, paramIColumnInfo1, paramIColumnInfo2);
  }
  
  public IColumn coercePlusColumns(IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2)
    throws ErrorException
  {
    return generalizedCoercion(CoercionOperation.PLUS, paramIColumnInfo1, paramIColumnInfo2);
  }
  
  public IColumn coerceMultiplicationColumns(IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2)
    throws ErrorException
  {
    return generalizedCoercion(CoercionOperation.MULTIPLICATION, paramIColumnInfo1, paramIColumnInfo2);
  }
  
  public IColumn determineLiteralColumn(String paramString, ICoercionHandler.LiteralType paramLiteralType)
    throws ErrorException
  {
    return this.m_literalMetadataFactory.determineLiteralType(paramString, paramLiteralType);
  }
  
  public IColumn coerceUnaryMinusColumn(IColumnInfo paramIColumnInfo)
    throws ErrorException
  {
    SqlTypes localSqlTypes = SqlTypes.getValueOf(paramIColumnInfo.getType());
    Object localObject;
    if ((localSqlTypes.isInteger()) || (localSqlTypes == SqlTypes.SQL_BIT))
    {
      localObject = null;
      if (localSqlTypes == SqlTypes.SQL_BIT) {
        localObject = SqlTypes.SQL_INTEGER;
      } else {
        localObject = AESqlTypeCoercer.upIntegerType(localSqlTypes);
      }
      localObject = this.m_typeNormalizer.normalizeType((SqlTypes)localObject);
      if (localObject == null) {
        throw SQLEngineExceptionFactory.invalidOperationException("Can not normalize type: all available coercion type is disabled.");
      }
      ColumnMetadata localColumnMetadata = new ColumnMetadata(TypeMetadata.createTypeMetadata(((SqlTypes)localObject).getSqlType(), true));
      localColumnMetadata.setNullable(paramIColumnInfo.getNullable());
      localColumnMetadata.setUpdatable(paramIColumnInfo.getUpdatable());
      localColumnMetadata.setName(null);
      return localColumnMetadata;
    }
    if (localSqlTypes.isNumber())
    {
      localObject = MetadataUtilities.createColumnMetadata(paramIColumnInfo);
      ((ColumnMetadata)localObject).getTypeMetadata().setSigned(true);
      ((ColumnMetadata)localObject).setName(null);
      return (IColumn)localObject;
    }
    if ((SqlTypes.SQL_DATE == localSqlTypes) || (SqlTypes.SQL_TIMESTAMP == localSqlTypes) || (SqlTypes.SQL_NULL == localSqlTypes))
    {
      localObject = MetadataUtilities.createColumnMetadata(paramIColumnInfo);
      ((ColumnMetadata)localObject).setName(null);
      return (IColumn)localObject;
    }
    throw SQLEngineExceptionFactory.incompatibleTypesException("NEGATION", localSqlTypes.name());
  }
  
  protected long calcColumnLength(CoercionOperation paramCoercionOperation, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2, IColumn paramIColumn)
    throws ErrorException
  {
    return this.m_metadataCoercer.calcColumnLength(paramCoercionOperation, paramIColumnInfo1, paramIColumnInfo2, paramIColumn);
  }
  
  protected PrecisionScale calcPrecisionScale(CoercionOperation paramCoercionOperation, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2, IColumn paramIColumn)
    throws ErrorException
  {
    return this.m_metadataCoercer.calcPrecisionScale(paramCoercionOperation, paramIColumnInfo1, paramIColumnInfo2, paramIColumn);
  }
  
  protected int coerceType(CoercionOperation paramCoercionOperation, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2)
    throws ErrorException
  {
    return this.m_typeCoercer.coerceType(paramCoercionOperation, paramIColumnInfo1, paramIColumnInfo2);
  }
  
  protected boolean coerceIsCurrency(CoercionOperation paramCoercionOperation, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2, IColumn paramIColumn)
    throws ErrorException
  {
    return this.m_metadataCoercer.coerceIsCurrency(paramCoercionOperation, paramIColumnInfo1, paramIColumnInfo2, paramIColumn);
  }
  
  protected boolean coerceIsSigned(CoercionOperation paramCoercionOperation, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2, IColumn paramIColumn)
    throws ErrorException
  {
    return this.m_metadataCoercer.coerceIsSigned(paramCoercionOperation, paramIColumnInfo1, paramIColumnInfo2, paramIColumn);
  }
  
  protected Nullable coerceNullable(CoercionOperation paramCoercionOperation, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2, IColumn paramIColumn)
    throws ErrorException
  {
    return this.m_metadataCoercer.coerceNullable(paramCoercionOperation, paramIColumnInfo1, paramIColumnInfo2, paramIColumn);
  }
  
  protected boolean coerceIsCaseSensitive(CoercionOperation paramCoercionOperation, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2, IColumn paramIColumn)
    throws ErrorException
  {
    return this.m_metadataCoercer.coerceIsCaseSensitive(paramCoercionOperation, paramIColumnInfo1, paramIColumnInfo2, paramIColumn);
  }
  
  protected Searchable coerceSearchability(CoercionOperation paramCoercionOperation, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2, IColumn paramIColumn)
    throws ErrorException
  {
    return this.m_metadataCoercer.coerceSearchability(paramCoercionOperation, paramIColumnInfo1, paramIColumnInfo2, paramIColumn);
  }
  
  protected Updatable coerceUpdatability(CoercionOperation paramCoercionOperation, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2, IColumn paramIColumn)
    throws ErrorException
  {
    return this.m_metadataCoercer.coerceUpdatability(paramCoercionOperation, paramIColumnInfo1, paramIColumnInfo2, paramIColumn);
  }
  
  protected boolean coerceIsAutoUnique(CoercionOperation paramCoercionOperation, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2, IColumn paramIColumn)
    throws ErrorException
  {
    return this.m_metadataCoercer.coerceIsAutoUnique(paramCoercionOperation, paramIColumnInfo1, paramIColumnInfo2, paramIColumn);
  }
  
  protected final void disableCoercionTypes(Set<Integer> paramSet)
    throws ErrorException
  {
    Iterator localIterator = paramSet.iterator();
    while (localIterator.hasNext())
    {
      int i = ((Integer)localIterator.next()).intValue();
      this.m_typeNormalizer.disableType(i);
    }
  }
  
  protected int normalizeCoercionType(int paramInt)
    throws ErrorException
  {
    return this.m_typeNormalizer.normalizeType(paramInt);
  }
  
  protected void finalizeCoercion(CoercionOperation paramCoercionOperation, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2, ColumnMetadata paramColumnMetadata)
    throws ErrorException
  {
    SqlTypes localSqlTypes1 = SqlTypes.getValueOf(paramColumnMetadata.getTypeMetadata().getType());
    if ((localSqlTypes1.isChar()) || (localSqlTypes1.isWChar()) || (localSqlTypes1.isBinary()))
    {
      SqlTypes localSqlTypes2 = this.m_typeNormalizer.fitBinaryOrCharType(localSqlTypes1, paramColumnMetadata.getColumnLength());
      if (localSqlTypes2 != localSqlTypes1) {
        paramColumnMetadata.setTypeMetadata(getBinaryOrCharTypeReplacement(paramColumnMetadata.getTypeMetadata(), localSqlTypes2));
      }
    }
    paramColumnMetadata.setName(null);
  }
  
  protected final void overrideCoercionType(CoercionOperation paramCoercionOperation, int paramInt1, int paramInt2, int paramInt3)
    throws ErrorException
  {
    this.m_typeCoercer.overrideCoercionType(paramCoercionOperation, paramInt1, paramInt2, paramInt3);
  }
  
  private ColumnMetadata generalizedCoercion(CoercionOperation paramCoercionOperation, IColumnInfo paramIColumnInfo1, IColumnInfo paramIColumnInfo2)
    throws ErrorException
  {
    if ((paramCoercionOperation == null) || (paramIColumnInfo1 == null) || (paramIColumnInfo2 == null)) {
      throw new NullPointerException("Coercion handler are called with null argument.");
    }
    ColumnMetadata localColumnMetadata1;
    if (IColumnInfo.ColumnType.NULL == paramIColumnInfo1.getColumnType())
    {
      localColumnMetadata1 = MetadataUtilities.createColumnMetadata(paramIColumnInfo2);
      finalizeCoercion(paramCoercionOperation, paramIColumnInfo1, paramIColumnInfo2, localColumnMetadata1);
      return localColumnMetadata1;
    }
    if (IColumnInfo.ColumnType.NULL == paramIColumnInfo2.getColumnType())
    {
      localColumnMetadata1 = MetadataUtilities.createColumnMetadata(paramIColumnInfo1);
      finalizeCoercion(paramCoercionOperation, paramIColumnInfo1, paramIColumnInfo2, localColumnMetadata1);
      return localColumnMetadata1;
    }
    int i = coerceType(paramCoercionOperation, paramIColumnInfo1, paramIColumnInfo2);
    i = normalizeCoercionType(i);
    TypeMetadata localTypeMetadata = TypeMetadata.createTypeMetadata(i);
    ColumnMetadata localColumnMetadata2 = new ColumnMetadata(localTypeMetadata);
    PrecisionScale localPrecisionScale = calcPrecisionScale(paramCoercionOperation, paramIColumnInfo1, paramIColumnInfo2, localColumnMetadata2);
    localTypeMetadata.setPrecision(localPrecisionScale.getPrecision());
    localTypeMetadata.setScale(localPrecisionScale.getScale());
    localTypeMetadata.setSigned(coerceIsSigned(paramCoercionOperation, paramIColumnInfo1, paramIColumnInfo2, localColumnMetadata2));
    localTypeMetadata.setIsCurrency(coerceIsCurrency(paramCoercionOperation, paramIColumnInfo1, paramIColumnInfo2, localColumnMetadata2));
    try
    {
      localColumnMetadata2.setColumnLength(calcColumnLength(paramCoercionOperation, paramIColumnInfo1, paramIColumnInfo2, localColumnMetadata2));
    }
    catch (NumericOverflowException localNumericOverflowException)
    {
      throw SQLEngineExceptionFactory.numericOverflowException(localNumericOverflowException.getMessage());
    }
    localColumnMetadata2.setNullable(coerceNullable(paramCoercionOperation, paramIColumnInfo1, paramIColumnInfo2, localColumnMetadata2));
    localColumnMetadata2.setCaseSensitive(coerceIsCaseSensitive(paramCoercionOperation, paramIColumnInfo1, paramIColumnInfo2, localColumnMetadata2));
    localColumnMetadata2.setSearchable(coerceSearchability(paramCoercionOperation, paramIColumnInfo1, paramIColumnInfo2, localColumnMetadata2));
    localColumnMetadata2.setUpdatable(coerceUpdatability(paramCoercionOperation, paramIColumnInfo1, paramIColumnInfo2, localColumnMetadata2));
    localColumnMetadata2.setAutoUnique(coerceIsAutoUnique(paramCoercionOperation, paramIColumnInfo1, paramIColumnInfo2, localColumnMetadata2));
    finalizeCoercion(paramCoercionOperation, paramIColumnInfo1, paramIColumnInfo2, localColumnMetadata2);
    return localColumnMetadata2;
  }
  
  private TypeMetadata getBinaryOrCharTypeReplacement(TypeMetadata paramTypeMetadata, SqlTypes paramSqlTypes)
    throws ErrorException
  {
    SqlTypes localSqlTypes = SqlTypes.getValueOf(paramTypeMetadata.getType());
    assert (((localSqlTypes.isBinary()) && (paramSqlTypes.isBinary())) || ((localSqlTypes.isChar()) && (paramSqlTypes.isChar())) || ((localSqlTypes.isWChar()) && (paramSqlTypes.isWChar())));
    TypeMetadata localTypeMetadata = TypeMetadata.createTypeMetadata(paramSqlTypes.getSqlType());
    localTypeMetadata.setIsSortable(paramTypeMetadata.isSortable());
    localTypeMetadata.setPrecision(paramTypeMetadata.getPrecision());
    return localTypeMetadata;
  }
  
  protected static final class PrecisionScale
  {
    private short m_precision;
    private short m_scale;
    
    public PrecisionScale(short paramShort1, short paramShort2)
    {
      this.m_precision = paramShort1;
      this.m_scale = paramShort2;
    }
    
    public short getPrecision()
    {
      return this.m_precision;
    }
    
    public void setPrecision(short paramShort)
    {
      this.m_precision = paramShort;
    }
    
    public short getScale()
    {
      return this.m_scale;
    }
    
    public void setScale(short paramShort)
    {
      this.m_scale = paramShort;
    }
  }
  
  protected static enum CoercionOperation
  {
    COMPARISON,  CONCAT,  IN,  LIKE,  SET_OPERATION,  DIVISION,  MINUS,  PLUS,  MULTIPLICATION;
    
    private CoercionOperation() {}
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/metadatautil/AEMetadataCoercionHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */