package com.simba.sqlengine.executor.etree.value.functor.arithmetic;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.ColumnMetadata;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.exceptions.SQLEngineException;
import com.simba.sqlengine.executor.etree.value.ArithmeticExprType;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.exceptions.ErrorException;
import java.util.HashMap;
import java.util.Map;

public class ArithmeticFunctorFactory
{
  private static Map<FunctorMapKey, IBinaryArithmeticFunctor> s_binaryFunctorMap;
  private static Map<FunctorMapKey, IUnaryArithmeticFunctor> s_unaryFunctorMap;
  
  public static BinaryArithmeticOperator getBinaryArithFunctor(ArithmeticExprType paramArithmeticExprType, IColumn paramIColumn1, IColumn paramIColumn2, IColumn paramIColumn3)
    throws ErrorException
  {
    TypeMetadata localTypeMetadata = paramIColumn1.getTypeMetadata();
    int i = localTypeMetadata.getType();
    if (i == 93) {
      return getTimestampBinFunctor(paramArithmeticExprType, paramIColumn1, paramIColumn2, paramIColumn3);
    }
    if (i == 91) {
      return getDateBinFunctor(paramArithmeticExprType, paramIColumn1, paramIColumn2, paramIColumn3);
    }
    ArithmeticFunctorFactory.FunctorMapKey.Signedness localSignedness = ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA;
    switch (localTypeMetadata.getType())
    {
    case -6: 
    case -5: 
    case 4: 
    case 5: 
      localSignedness = localTypeMetadata.isSigned() ? ArithmeticFunctorFactory.FunctorMapKey.Signedness.SIGNED : ArithmeticFunctorFactory.FunctorMapKey.Signedness.UNSIGNED;
    }
    FunctorMapKey localFunctorMapKey = new FunctorMapKey(i, localSignedness, paramArithmeticExprType);
    IBinaryArithmeticFunctor localIBinaryArithmeticFunctor = (IBinaryArithmeticFunctor)s_binaryFunctorMap.get(localFunctorMapKey);
    if (localIBinaryArithmeticFunctor == null) {
      throw new SQLEngineException(SQLEngineMessageKey.UNSUPPORT_ARITH_OP.name(), new String[] { "Operation: " + paramArithmeticExprType.name() + ", result type: " + localTypeMetadata.getTypeName() + ", left operand type: " + paramIColumn2.getTypeMetadata().getTypeName() + ", right operand type: " + paramIColumn3.getTypeMetadata().getTypeName() });
    }
    return new BinaryArithmeticOperator(localIBinaryArithmeticFunctor, paramIColumn1, paramIColumn1);
  }
  
  private static BinaryArithmeticOperator getDateBinFunctor(ArithmeticExprType paramArithmeticExprType, IColumn paramIColumn1, IColumn paramIColumn2, IColumn paramIColumn3)
    throws ErrorException
  {
    TypeMetadata localTypeMetadata1 = paramIColumn1.getTypeMetadata();
    assert (localTypeMetadata1.getType() == 91);
    TypeMetadata localTypeMetadata2 = paramIColumn2.getTypeMetadata();
    TypeMetadata localTypeMetadata3 = paramIColumn3.getTypeMetadata();
    if ((localTypeMetadata2.getType() == 91) || (localTypeMetadata3.getType() == 91))
    {
      TypeMetadata localTypeMetadata4;
      if (paramArithmeticExprType == ArithmeticExprType.ADDITION)
      {
        if ((localTypeMetadata3.getType() == -5) || (localTypeMetadata2.getType() == -5)) {
          return new BinaryArithmeticOperator(new DateAddBigIntFunctor(), paramIColumn2, paramIColumn3);
        }
        if (localTypeMetadata3.isIntegerType())
        {
          localTypeMetadata4 = TypeMetadata.createTypeMetadata(4, paramIColumn3.getTypeMetadata().isSigned());
          return new BinaryArithmeticOperator(new DateAddIntFunctor(), paramIColumn2, new ColumnMetadata(localTypeMetadata4));
        }
        if (localTypeMetadata2.isIntegerType())
        {
          localTypeMetadata4 = TypeMetadata.createTypeMetadata(4, paramIColumn2.getTypeMetadata().isSigned());
          return new BinaryArithmeticOperator(new DateAddIntFunctor(), new ColumnMetadata(localTypeMetadata4), paramIColumn3);
        }
      }
      else if (paramArithmeticExprType == ArithmeticExprType.SUBTRACTION)
      {
        if ((localTypeMetadata3.getType() == -5) || (localTypeMetadata2.getType() == -5)) {
          return new BinaryArithmeticOperator(new DateMinusBigIntFunctor(), paramIColumn2, paramIColumn3);
        }
        if (localTypeMetadata3.isIntegerType())
        {
          localTypeMetadata4 = TypeMetadata.createTypeMetadata(4, paramIColumn3.getTypeMetadata().isSigned());
          return new BinaryArithmeticOperator(new DateMinusIntFunctor(), paramIColumn2, new ColumnMetadata(localTypeMetadata4));
        }
        if (localTypeMetadata2.isIntegerType())
        {
          localTypeMetadata4 = TypeMetadata.createTypeMetadata(4, paramIColumn2.getTypeMetadata().isSigned());
          return new BinaryArithmeticOperator(new DateMinusIntFunctor(), new ColumnMetadata(localTypeMetadata4), paramIColumn3);
        }
      }
    }
    throw new SQLEngineException(SQLEngineMessageKey.UNSUPPORT_ARITH_OP.name(), new String[] { "Operation: " + paramArithmeticExprType.name() + ", result type: " + localTypeMetadata1.getTypeName() + ", left operand type: " + localTypeMetadata2.getTypeName() + ", right operand type: " + localTypeMetadata3.getTypeName() });
  }
  
  private static BinaryArithmeticOperator getTimestampBinFunctor(ArithmeticExprType paramArithmeticExprType, IColumn paramIColumn1, IColumn paramIColumn2, IColumn paramIColumn3)
    throws ErrorException
  {
    TypeMetadata localTypeMetadata1 = paramIColumn1.getTypeMetadata();
    assert (localTypeMetadata1.getType() == 93);
    TypeMetadata localTypeMetadata2 = paramIColumn2.getTypeMetadata();
    TypeMetadata localTypeMetadata3 = paramIColumn3.getTypeMetadata();
    if ((localTypeMetadata2.getType() == 93) || (localTypeMetadata3.getType() == 93))
    {
      TypeMetadata localTypeMetadata4;
      if (paramArithmeticExprType == ArithmeticExprType.ADDITION)
      {
        if ((localTypeMetadata3.getType() == -5) || (localTypeMetadata3.getType() == -5)) {
          return new BinaryArithmeticOperator(new TimestampAddBigIntFunctor(), paramIColumn2, paramIColumn3);
        }
        if (localTypeMetadata3.isIntegerType())
        {
          localTypeMetadata4 = TypeMetadata.createTypeMetadata(4, paramIColumn3.getTypeMetadata().isSigned());
          return new BinaryArithmeticOperator(new TimestampAddIntFunctor(), paramIColumn2, new ColumnMetadata(localTypeMetadata4));
        }
        if (localTypeMetadata2.isIntegerType())
        {
          localTypeMetadata4 = TypeMetadata.createTypeMetadata(4, paramIColumn2.getTypeMetadata().isSigned());
          return new BinaryArithmeticOperator(new TimestampAddIntFunctor(), new ColumnMetadata(localTypeMetadata4), paramIColumn3);
        }
      }
      else if (paramArithmeticExprType == ArithmeticExprType.SUBTRACTION)
      {
        if ((localTypeMetadata3.getType() == -5) || (localTypeMetadata3.getType() == -5)) {
          return new BinaryArithmeticOperator(new TimestampMinusBigIntFunctor(), paramIColumn2, paramIColumn3);
        }
        if (localTypeMetadata3.isIntegerType())
        {
          localTypeMetadata4 = TypeMetadata.createTypeMetadata(4, paramIColumn3.getTypeMetadata().isSigned());
          return new BinaryArithmeticOperator(new TimestampMinusIntFunctor(), paramIColumn2, new ColumnMetadata(localTypeMetadata4));
        }
        if (localTypeMetadata2.isIntegerType())
        {
          localTypeMetadata4 = TypeMetadata.createTypeMetadata(4, paramIColumn2.getTypeMetadata().isSigned());
          return new BinaryArithmeticOperator(new TimestampMinusIntFunctor(), new ColumnMetadata(localTypeMetadata4), paramIColumn3);
        }
      }
    }
    throw new SQLEngineException(SQLEngineMessageKey.UNSUPPORT_ARITH_OP.name(), new String[] { "Operation: " + paramArithmeticExprType.name() + ", result type: " + localTypeMetadata1.getTypeName() + ", left operand type: " + localTypeMetadata2.getTypeName() + ", right operand type: " + localTypeMetadata3.getTypeName() });
  }
  
  public static IUnaryArithmeticFunctor getUnaryArithFunctor(ArithmeticExprType paramArithmeticExprType, TypeMetadata paramTypeMetadata1, TypeMetadata paramTypeMetadata2)
    throws SQLEngineException
  {
    assert (paramArithmeticExprType == ArithmeticExprType.NEGATION);
    int i = paramTypeMetadata1.getType();
    if ((91 != i) && (93 != i) && (!paramTypeMetadata1.isSigned())) {
      throw new SQLEngineException(SQLEngineMessageKey.UNSUPPORT_ARITH_OP.name(), new String[] { "Negation resulting in unsigned type" });
    }
    ArithmeticFunctorFactory.FunctorMapKey.Signedness localSignedness = ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA;
    FunctorMapKey localFunctorMapKey = new FunctorMapKey(i, localSignedness, paramArithmeticExprType);
    IUnaryArithmeticFunctor localIUnaryArithmeticFunctor = (IUnaryArithmeticFunctor)s_unaryFunctorMap.get(localFunctorMapKey);
    if (localIUnaryArithmeticFunctor == null) {
      throw new SQLEngineException(SQLEngineMessageKey.UNSUPPORT_ARITH_OP.name(), new String[] { "Operation: " + paramArithmeticExprType.name() + ", result type: " + paramTypeMetadata1.getTypeName() + ", operand type: " + paramTypeMetadata2.getTypeName() });
    }
    return localIUnaryArithmeticFunctor;
  }
  
  static
  {
    s_binaryFunctorMap = new HashMap();
    s_binaryFunctorMap.put(new FunctorMapKey(1, ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA, ArithmeticExprType.ADDITION), new CharAddFunctor());
    s_binaryFunctorMap.put(new FunctorMapKey(-8, ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA, ArithmeticExprType.ADDITION), new CharAddFunctor());
    s_binaryFunctorMap.put(new FunctorMapKey(12, ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA, ArithmeticExprType.ADDITION), new CharAddFunctor());
    s_binaryFunctorMap.put(new FunctorMapKey(-9, ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA, ArithmeticExprType.ADDITION), new CharAddFunctor());
    s_binaryFunctorMap.put(new FunctorMapKey(-1, ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA, ArithmeticExprType.ADDITION), new CharAddFunctor());
    s_binaryFunctorMap.put(new FunctorMapKey(-10, ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA, ArithmeticExprType.ADDITION), new CharAddFunctor());
    s_binaryFunctorMap.put(new FunctorMapKey(-6, ArithmeticFunctorFactory.FunctorMapKey.Signedness.SIGNED, ArithmeticExprType.ADDITION), new TinyIntAddFunctor(true));
    s_binaryFunctorMap.put(new FunctorMapKey(-6, ArithmeticFunctorFactory.FunctorMapKey.Signedness.SIGNED, ArithmeticExprType.SUBTRACTION), new TinyIntSubtractFunctor(true));
    s_binaryFunctorMap.put(new FunctorMapKey(-6, ArithmeticFunctorFactory.FunctorMapKey.Signedness.SIGNED, ArithmeticExprType.MULTIPLICATION), new TinyIntMultiplyFunctor(true));
    s_binaryFunctorMap.put(new FunctorMapKey(-6, ArithmeticFunctorFactory.FunctorMapKey.Signedness.SIGNED, ArithmeticExprType.DIVISION), new TinyIntDivideFunctor(true));
    s_binaryFunctorMap.put(new FunctorMapKey(-6, ArithmeticFunctorFactory.FunctorMapKey.Signedness.UNSIGNED, ArithmeticExprType.ADDITION), new TinyIntAddFunctor(false));
    s_binaryFunctorMap.put(new FunctorMapKey(-6, ArithmeticFunctorFactory.FunctorMapKey.Signedness.UNSIGNED, ArithmeticExprType.SUBTRACTION), new TinyIntSubtractFunctor(false));
    s_binaryFunctorMap.put(new FunctorMapKey(-6, ArithmeticFunctorFactory.FunctorMapKey.Signedness.UNSIGNED, ArithmeticExprType.MULTIPLICATION), new TinyIntMultiplyFunctor(false));
    s_binaryFunctorMap.put(new FunctorMapKey(-6, ArithmeticFunctorFactory.FunctorMapKey.Signedness.UNSIGNED, ArithmeticExprType.DIVISION), new TinyIntDivideFunctor(false));
    s_binaryFunctorMap.put(new FunctorMapKey(5, ArithmeticFunctorFactory.FunctorMapKey.Signedness.SIGNED, ArithmeticExprType.ADDITION), new SmallIntAddFunctor(true));
    s_binaryFunctorMap.put(new FunctorMapKey(5, ArithmeticFunctorFactory.FunctorMapKey.Signedness.SIGNED, ArithmeticExprType.SUBTRACTION), new SmallIntSubtractFunctor(true));
    s_binaryFunctorMap.put(new FunctorMapKey(5, ArithmeticFunctorFactory.FunctorMapKey.Signedness.SIGNED, ArithmeticExprType.MULTIPLICATION), new SmallIntMultiplyFunctor(true));
    s_binaryFunctorMap.put(new FunctorMapKey(5, ArithmeticFunctorFactory.FunctorMapKey.Signedness.SIGNED, ArithmeticExprType.DIVISION), new SmallIntDivideFunctor(true));
    s_binaryFunctorMap.put(new FunctorMapKey(5, ArithmeticFunctorFactory.FunctorMapKey.Signedness.UNSIGNED, ArithmeticExprType.ADDITION), new SmallIntAddFunctor(false));
    s_binaryFunctorMap.put(new FunctorMapKey(5, ArithmeticFunctorFactory.FunctorMapKey.Signedness.UNSIGNED, ArithmeticExprType.SUBTRACTION), new SmallIntSubtractFunctor(false));
    s_binaryFunctorMap.put(new FunctorMapKey(5, ArithmeticFunctorFactory.FunctorMapKey.Signedness.UNSIGNED, ArithmeticExprType.MULTIPLICATION), new SmallIntMultiplyFunctor(false));
    s_binaryFunctorMap.put(new FunctorMapKey(5, ArithmeticFunctorFactory.FunctorMapKey.Signedness.UNSIGNED, ArithmeticExprType.DIVISION), new SmallIntDivideFunctor(false));
    s_binaryFunctorMap.put(new FunctorMapKey(4, ArithmeticFunctorFactory.FunctorMapKey.Signedness.SIGNED, ArithmeticExprType.ADDITION), new IntegerAddFunctor(true));
    s_binaryFunctorMap.put(new FunctorMapKey(4, ArithmeticFunctorFactory.FunctorMapKey.Signedness.SIGNED, ArithmeticExprType.SUBTRACTION), new IntegerSubtractFunctor(true));
    s_binaryFunctorMap.put(new FunctorMapKey(4, ArithmeticFunctorFactory.FunctorMapKey.Signedness.SIGNED, ArithmeticExprType.MULTIPLICATION), new IntegerMultiplyFunctor(true));
    s_binaryFunctorMap.put(new FunctorMapKey(4, ArithmeticFunctorFactory.FunctorMapKey.Signedness.SIGNED, ArithmeticExprType.DIVISION), new IntegerDivideFunctor(true));
    s_binaryFunctorMap.put(new FunctorMapKey(4, ArithmeticFunctorFactory.FunctorMapKey.Signedness.UNSIGNED, ArithmeticExprType.ADDITION), new IntegerAddFunctor(false));
    s_binaryFunctorMap.put(new FunctorMapKey(4, ArithmeticFunctorFactory.FunctorMapKey.Signedness.UNSIGNED, ArithmeticExprType.SUBTRACTION), new IntegerSubtractFunctor(false));
    s_binaryFunctorMap.put(new FunctorMapKey(4, ArithmeticFunctorFactory.FunctorMapKey.Signedness.UNSIGNED, ArithmeticExprType.MULTIPLICATION), new IntegerMultiplyFunctor(false));
    s_binaryFunctorMap.put(new FunctorMapKey(4, ArithmeticFunctorFactory.FunctorMapKey.Signedness.UNSIGNED, ArithmeticExprType.DIVISION), new IntegerDivideFunctor(false));
    s_binaryFunctorMap.put(new FunctorMapKey(-5, ArithmeticFunctorFactory.FunctorMapKey.Signedness.SIGNED, ArithmeticExprType.ADDITION), new BigIntAddFunctor());
    s_binaryFunctorMap.put(new FunctorMapKey(-5, ArithmeticFunctorFactory.FunctorMapKey.Signedness.SIGNED, ArithmeticExprType.SUBTRACTION), new BigIntSubtractFunctor());
    s_binaryFunctorMap.put(new FunctorMapKey(-5, ArithmeticFunctorFactory.FunctorMapKey.Signedness.SIGNED, ArithmeticExprType.MULTIPLICATION), new BigIntMultiplyFunctor());
    s_binaryFunctorMap.put(new FunctorMapKey(-5, ArithmeticFunctorFactory.FunctorMapKey.Signedness.SIGNED, ArithmeticExprType.DIVISION), new BigIntDivideFunctor());
    s_binaryFunctorMap.put(new FunctorMapKey(-5, ArithmeticFunctorFactory.FunctorMapKey.Signedness.UNSIGNED, ArithmeticExprType.ADDITION), new BigIntAddFunctor());
    s_binaryFunctorMap.put(new FunctorMapKey(-5, ArithmeticFunctorFactory.FunctorMapKey.Signedness.UNSIGNED, ArithmeticExprType.SUBTRACTION), new BigIntSubtractFunctor());
    s_binaryFunctorMap.put(new FunctorMapKey(-5, ArithmeticFunctorFactory.FunctorMapKey.Signedness.UNSIGNED, ArithmeticExprType.MULTIPLICATION), new BigIntMultiplyFunctor());
    s_binaryFunctorMap.put(new FunctorMapKey(-5, ArithmeticFunctorFactory.FunctorMapKey.Signedness.UNSIGNED, ArithmeticExprType.DIVISION), new BigIntDivideFunctor());
    s_binaryFunctorMap.put(new FunctorMapKey(7, ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA, ArithmeticExprType.ADDITION), new RealAddFunctor());
    s_binaryFunctorMap.put(new FunctorMapKey(7, ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA, ArithmeticExprType.SUBTRACTION), new RealSubtractFunctor());
    s_binaryFunctorMap.put(new FunctorMapKey(7, ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA, ArithmeticExprType.MULTIPLICATION), new RealMultiplyFunctor());
    s_binaryFunctorMap.put(new FunctorMapKey(7, ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA, ArithmeticExprType.DIVISION), new RealDivideFunctor());
    s_binaryFunctorMap.put(new FunctorMapKey(6, ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA, ArithmeticExprType.ADDITION), new DoubleAddFunctor());
    s_binaryFunctorMap.put(new FunctorMapKey(6, ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA, ArithmeticExprType.SUBTRACTION), new DoubleSubtractFunctor());
    s_binaryFunctorMap.put(new FunctorMapKey(6, ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA, ArithmeticExprType.MULTIPLICATION), new DoubleMultiplyFunctor());
    s_binaryFunctorMap.put(new FunctorMapKey(6, ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA, ArithmeticExprType.DIVISION), new DoubleDivideFunctor());
    s_binaryFunctorMap.put(new FunctorMapKey(8, ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA, ArithmeticExprType.ADDITION), new DoubleAddFunctor());
    s_binaryFunctorMap.put(new FunctorMapKey(8, ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA, ArithmeticExprType.SUBTRACTION), new DoubleSubtractFunctor());
    s_binaryFunctorMap.put(new FunctorMapKey(8, ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA, ArithmeticExprType.MULTIPLICATION), new DoubleMultiplyFunctor());
    s_binaryFunctorMap.put(new FunctorMapKey(8, ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA, ArithmeticExprType.DIVISION), new DoubleDivideFunctor());
    s_binaryFunctorMap.put(new FunctorMapKey(3, ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA, ArithmeticExprType.ADDITION), new DecimalAddFunctor());
    s_binaryFunctorMap.put(new FunctorMapKey(3, ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA, ArithmeticExprType.SUBTRACTION), new DecimalSubtractFunctor());
    s_binaryFunctorMap.put(new FunctorMapKey(3, ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA, ArithmeticExprType.MULTIPLICATION), new DecimalMultiplyFunctor());
    s_binaryFunctorMap.put(new FunctorMapKey(3, ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA, ArithmeticExprType.DIVISION), new DecimalDivideFunctor());
    s_binaryFunctorMap.put(new FunctorMapKey(2, ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA, ArithmeticExprType.ADDITION), new DecimalAddFunctor());
    s_binaryFunctorMap.put(new FunctorMapKey(2, ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA, ArithmeticExprType.SUBTRACTION), new DecimalSubtractFunctor());
    s_binaryFunctorMap.put(new FunctorMapKey(2, ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA, ArithmeticExprType.MULTIPLICATION), new DecimalMultiplyFunctor());
    s_binaryFunctorMap.put(new FunctorMapKey(2, ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA, ArithmeticExprType.DIVISION), new DecimalDivideFunctor());
    s_unaryFunctorMap = new HashMap();
    s_unaryFunctorMap.put(new FunctorMapKey(-6, ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA, ArithmeticExprType.NEGATION), new TinyIntNegateFunctor());
    s_unaryFunctorMap.put(new FunctorMapKey(5, ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA, ArithmeticExprType.NEGATION), new SmallIntNegateFunctor());
    s_unaryFunctorMap.put(new FunctorMapKey(4, ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA, ArithmeticExprType.NEGATION), new IntegerNegateFunctor());
    s_unaryFunctorMap.put(new FunctorMapKey(-5, ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA, ArithmeticExprType.NEGATION), new BigIntNegateFunctor());
    s_unaryFunctorMap.put(new FunctorMapKey(6, ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA, ArithmeticExprType.NEGATION), new DoubleNegateFunctor());
    s_unaryFunctorMap.put(new FunctorMapKey(7, ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA, ArithmeticExprType.NEGATION), new RealNegateFunctor());
    s_unaryFunctorMap.put(new FunctorMapKey(8, ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA, ArithmeticExprType.NEGATION), new DoubleNegateFunctor());
    s_unaryFunctorMap.put(new FunctorMapKey(3, ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA, ArithmeticExprType.NEGATION), new DecimalNegateFunctor());
    s_unaryFunctorMap.put(new FunctorMapKey(3, ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA, ArithmeticExprType.NEGATION), new DecimalNegateFunctor());
    s_unaryFunctorMap.put(new FunctorMapKey(2, ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA, ArithmeticExprType.NEGATION), new DecimalNegateFunctor());
    s_unaryFunctorMap.put(new FunctorMapKey(2, ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA, ArithmeticExprType.NEGATION), new DecimalNegateFunctor());
    s_unaryFunctorMap.put(new FunctorMapKey(91, ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA, ArithmeticExprType.NEGATION), new DateNegateFunctor());
    s_unaryFunctorMap.put(new FunctorMapKey(93, ArithmeticFunctorFactory.FunctorMapKey.Signedness.NA, ArithmeticExprType.NEGATION), new TimestampNegateFunctor());
  }
  
  private static class FunctorMapKey
  {
    private final int m_sqlType;
    private final Signedness m_signedness;
    private final ArithmeticExprType m_opType;
    
    public FunctorMapKey(int paramInt, Signedness paramSignedness, ArithmeticExprType paramArithmeticExprType)
    {
      if ((null == paramSignedness) || (null == paramArithmeticExprType)) {
        throw new NullPointerException();
      }
      this.m_sqlType = paramInt;
      this.m_signedness = paramSignedness;
      this.m_opType = paramArithmeticExprType;
    }
    
    public int hashCode()
    {
      int i = 1;
      i = 31 * i + this.m_opType.hashCode();
      i = 31 * i + this.m_signedness.hashCode();
      i = 31 * i + this.m_sqlType;
      return i;
    }
    
    public boolean equals(Object paramObject)
    {
      if (this == paramObject) {
        return true;
      }
      if (paramObject == null) {
        return false;
      }
      if (getClass() != paramObject.getClass()) {
        return false;
      }
      FunctorMapKey localFunctorMapKey = (FunctorMapKey)paramObject;
      if (this.m_opType != localFunctorMapKey.m_opType) {
        return false;
      }
      if (this.m_signedness != localFunctorMapKey.m_signedness) {
        return false;
      }
      return this.m_sqlType == localFunctorMapKey.m_sqlType;
    }
    
    public static enum Signedness
    {
      SIGNED,  UNSIGNED,  NA;
      
      private Signedness() {}
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/functor/arithmetic/ArithmeticFunctorFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */