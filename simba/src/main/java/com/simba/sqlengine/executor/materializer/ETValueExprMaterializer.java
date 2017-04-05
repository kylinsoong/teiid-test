package com.simba.sqlengine.executor.materializer;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.aeprocessor.aetree.AENodeList;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.bool.AEBooleanExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AERelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEAdd;
import com.simba.sqlengine.aeprocessor.aetree.value.AEAggrFn;
import com.simba.sqlengine.aeprocessor.aetree.value.AEBinaryValueExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEColumnReference;
import com.simba.sqlengine.aeprocessor.aetree.value.AEConcat;
import com.simba.sqlengine.aeprocessor.aetree.value.AECountStarAggrFn;
import com.simba.sqlengine.aeprocessor.aetree.value.AECustomScalarFn;
import com.simba.sqlengine.aeprocessor.aetree.value.AEDefault;
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
import com.simba.sqlengine.aeprocessor.aetree.value.AESearchedWhenClause;
import com.simba.sqlengine.aeprocessor.aetree.value.AESimpleCase;
import com.simba.sqlengine.aeprocessor.aetree.value.AESimpleWhenClause;
import com.simba.sqlengine.aeprocessor.aetree.value.AESubtract;
import com.simba.sqlengine.aeprocessor.aetree.value.AEUnaryValueExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueExprList;
import com.simba.sqlengine.aeprocessor.aetree.value.AEValueSubQuery;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.conversions.ConversionUtil;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.datawrapper.SqlBigIntDataWrapper;
import com.simba.sqlengine.executor.datawrapper.SqlCharDataWrapper;
import com.simba.sqlengine.executor.datawrapper.SqlDateDataWrapper;
import com.simba.sqlengine.executor.datawrapper.SqlDoubleDataWrapper;
import com.simba.sqlengine.executor.datawrapper.SqlExactNumDataWrapper;
import com.simba.sqlengine.executor.datawrapper.SqlIntegerDataWrapper;
import com.simba.sqlengine.executor.datawrapper.SqlRealDataWrapper;
import com.simba.sqlengine.executor.datawrapper.SqlSmallIntDataWrapper;
import com.simba.sqlengine.executor.datawrapper.SqlTimeDataWrapper;
import com.simba.sqlengine.executor.datawrapper.SqlTimestampDataWrapper;
import com.simba.sqlengine.executor.datawrapper.SqlTinyIntDataWrapper;
import com.simba.sqlengine.executor.etree.IETNode;
import com.simba.sqlengine.executor.etree.bool.ETBooleanExpr;
import com.simba.sqlengine.executor.etree.relation.ETRelationalExpr;
import com.simba.sqlengine.executor.etree.value.ArithmeticExprType;
import com.simba.sqlengine.executor.etree.value.ETAggregateFn;
import com.simba.sqlengine.executor.etree.value.ETBinaryArithValueExpr;
import com.simba.sqlengine.executor.etree.value.ETColumnRef;
import com.simba.sqlengine.executor.etree.value.ETConstant;
import com.simba.sqlengine.executor.etree.value.ETDefault;
import com.simba.sqlengine.executor.etree.value.ETError;
import com.simba.sqlengine.executor.etree.value.ETParameter;
import com.simba.sqlengine.executor.etree.value.ETSearchedCase;
import com.simba.sqlengine.executor.etree.value.ETSearchedWhenClause;
import com.simba.sqlengine.executor.etree.value.ETSimpleCase;
import com.simba.sqlengine.executor.etree.value.ETSimpleWhenClause;
import com.simba.sqlengine.executor.etree.value.ETUnaryArithValueExpr;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.sqlengine.executor.etree.value.ETValueExprList;
import com.simba.sqlengine.executor.etree.value.aggregatefn.IAggregatorFactory;
import com.simba.sqlengine.executor.etree.value.functor.arithmetic.ArithmeticFunctorFactory;
import com.simba.sqlengine.executor.etree.value.functor.arithmetic.BinaryArithmeticOperator;
import com.simba.sqlengine.executor.etree.value.functor.arithmetic.IUnaryArithmeticFunctor;
import com.simba.sqlengine.executor.queryplan.IQueryPlan;
import com.simba.sqlengine.utilities.ExternalAlgorithmUtil.ExternalAlgorithmProperties;
import com.simba.support.Pair;
import com.simba.support.conv.CharConverter;
import com.simba.support.conv.ConversionResult;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ETValueExprMaterializer
  extends MaterializerBase<ETValueExpr>
{
  public ETValueExprMaterializer(IQueryPlan paramIQueryPlan, MaterializerContext paramMaterializerContext)
  {
    super(paramIQueryPlan, paramMaterializerContext);
  }
  
  public ETValueExpr visit(AEAdd paramAEAdd)
    throws ErrorException
  {
    return materializeBinArithExpr(paramAEAdd, ArithmeticExprType.ADDITION);
  }
  
  public ETValueExpr visit(AEColumnReference paramAEColumnReference)
    throws ErrorException
  {
    MaterializerContext localMaterializerContext = getContext();
    Pair localPair = localMaterializerContext.resolveJoinRelation(paramAEColumnReference.getNamedRelationalExpr());
    if (localPair == null)
    {
      localETRelationalExpr = localMaterializerContext.getMaterializedRelation(paramAEColumnReference.getNamedRelationalExpr());
      return new ETColumnRef(localETRelationalExpr, paramAEColumnReference.getColumnNum(), paramAEColumnReference.isOuterReference());
    }
    ETRelationalExpr localETRelationalExpr = localMaterializerContext.getMaterializedRelation((AERelationalExpr)localPair.key());
    if (localETRelationalExpr == null) {
      throw new IllegalStateException("Invalid materialized sequence.");
    }
    return new ETColumnRef(localETRelationalExpr, paramAEColumnReference.getColumnNum() + ((Integer)localPair.value()).intValue(), paramAEColumnReference.isOuterReference());
  }
  
  public ETValueExpr visit(AEConcat paramAEConcat)
    throws ErrorException
  {
    return materializeBinArithExpr(paramAEConcat, ArithmeticExprType.ADDITION);
  }
  
  public ETValueExpr visit(AECountStarAggrFn paramAECountStarAggrFn)
    throws ErrorException
  {
    return materializeAggregateFn(paramAECountStarAggrFn);
  }
  
  public ETValueExpr visit(AECustomScalarFn paramAECustomScalarFn)
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.featureNotImplementedException("Scalar function");
  }
  
  public ETValueExpr visit(AEDefault paramAEDefault)
    throws ErrorException
  {
    return new ETDefault();
  }
  
  public ETValueExpr visit(AEDivide paramAEDivide)
    throws ErrorException
  {
    return materializeBinArithExpr(paramAEDivide, ArithmeticExprType.DIVISION);
  }
  
  public ETValueExpr visit(AEGeneralAggrFn paramAEGeneralAggrFn)
    throws ErrorException
  {
    return materializeAggregateFn(paramAEGeneralAggrFn);
  }
  
  public ETValueExpr visit(AELiteral paramAELiteral)
    throws ErrorException
  {
    ConversionResult localConversionResult = new ConversionResult();
    ETConstant localETConstant = doLiteralConversion(paramAELiteral.getStringValue(), paramAELiteral.getTypeMetadata(), localConversionResult);
    if (localConversionResult.getState() != null) {
      try
      {
        ConversionUtil.checkResult(localConversionResult, getContext().getWarningListener(), -1, -1);
      }
      catch (ErrorException localErrorException)
      {
        return new ETError(localErrorException);
      }
    }
    return localETConstant;
  }
  
  public ETValueExpr visit(AEMultiply paramAEMultiply)
    throws ErrorException
  {
    return materializeBinArithExpr(paramAEMultiply, ArithmeticExprType.MULTIPLICATION);
  }
  
  public ETValueExpr visit(AENegate paramAENegate)
    throws ErrorException
  {
    return materializeUnaryArithExpr(paramAENegate, ArithmeticExprType.NEGATION);
  }
  
  public ETValueExpr visit(AENull paramAENull)
    throws ErrorException
  {
    SqlCharDataWrapper localSqlCharDataWrapper = new SqlCharDataWrapper(1);
    localSqlCharDataWrapper.setNull();
    return new ETConstant(localSqlCharDataWrapper);
  }
  
  public ETValueExpr visit(AEParameter paramAEParameter)
    throws ErrorException
  {
    ETParameter localETParameter = new ETParameter(paramAEParameter.getInferredOrSetColumn());
    getContext().registerParameter(paramAEParameter.getIndex(), localETParameter);
    return localETParameter;
  }
  
  public ETValueExpr visit(AEProxyColumn paramAEProxyColumn)
    throws ErrorException
  {
    MaterializerContext localMaterializerContext = getContext();
    ETRelationalExpr localETRelationalExpr = localMaterializerContext.getMaterializedRelation(paramAEProxyColumn.getRelationalExpr());
    if (null == localETRelationalExpr) {
      throw new NullPointerException("No materialized relation for: " + paramAEProxyColumn);
    }
    return new ETColumnRef(localETRelationalExpr, paramAEProxyColumn.getColumnNumber(), false);
  }
  
  public ETValueExpr visit(AERename paramAERename)
    throws ErrorException
  {
    return (ETValueExpr)paramAERename.getOperand().acceptVisitor(this);
  }
  
  public ETValueExpr visit(AEScalarFn paramAEScalarFn)
    throws ErrorException
  {
    AEValueExprList localAEValueExprList = paramAEScalarFn.getArguments();
    List localList = paramAEScalarFn.getExpectedArgMetadata();
    ArrayList localArrayList = new ArrayList(localAEValueExprList.getNumChildren());
    for (int i = 0; i < localAEValueExprList.getNumChildren(); i++)
    {
      AEValueExpr localAEValueExpr = (AEValueExpr)localAEValueExprList.getChild(i);
      ETValueExpr localETValueExpr = (ETValueExpr)localAEValueExpr.acceptVisitor(this);
      localETValueExpr = ConvMaterializeUtil.addConversionNodeWhenNeeded(localETValueExpr, localAEValueExpr.getColumn(), (IColumn)localList.get(i), getContext());
      localArrayList.add(localETValueExpr);
    }
    return ETScalarFnFactory.makeNewScalarFn(paramAEScalarFn, localList, localArrayList, getContext());
  }
  
  public ETValueExpr visit(AESearchedCase paramAESearchedCase)
    throws ErrorException
  {
    Iterator localIterator = paramAESearchedCase.getWhenClauseList().getChildItr();
    ETValueExprList localETValueExprList = new ETValueExprList();
    while (localIterator.hasNext()) {
      localETValueExprList.addNode((IETNode)((AESearchedWhenClause)localIterator.next()).acceptVisitor(this));
    }
    ETValueExpr localETValueExpr = (ETValueExpr)paramAESearchedCase.getElseClause().acceptVisitor(this);
    return new ETSearchedCase(localETValueExprList, localETValueExpr);
  }
  
  public ETValueExpr visit(AESearchedWhenClause paramAESearchedWhenClause)
    throws ErrorException
  {
    ETBooleanExpr localETBooleanExpr = (ETBooleanExpr)paramAESearchedWhenClause.getWhenCondition().acceptVisitor(new ETBoolExprMaterializer(getQueryPlan(), getContext()));
    ETValueExpr localETValueExpr = (ETValueExpr)paramAESearchedWhenClause.getThenExpression().acceptVisitor(this);
    return new ETSearchedWhenClause(localETBooleanExpr, localETValueExpr);
  }
  
  public ETValueExpr visit(AESimpleCase paramAESimpleCase)
    throws ErrorException
  {
    AENodeList localAENodeList = paramAESimpleCase.getWhenClauseList();
    ETValueExprList localETValueExprList = new ETValueExprList();
    ArrayList localArrayList = new ArrayList();
    Object localObject1 = localAENodeList.iterator();
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = (AESimpleWhenClause)((Iterator)localObject1).next();
      ETValueExpr localETValueExpr = (ETValueExpr)((AESimpleWhenClause)localObject2).acceptVisitor(this);
      localETValueExprList.addNode(localETValueExpr);
      localArrayList.add(((AESimpleWhenClause)localObject2).getComparisonMetadata());
    }
    localObject1 = (ETValueExpr)paramAESimpleCase.getCaseOperand().acceptVisitor(this);
    Object localObject2 = (ETValueExpr)paramAESimpleCase.getElseOperand().acceptVisitor(this);
    return new ETSimpleCase((ETValueExpr)localObject1, paramAESimpleCase.getCaseOperand().getColumn(), localETValueExprList, (ETValueExpr)localObject2, localArrayList, getContext());
  }
  
  public ETValueExpr visit(AESimpleWhenClause paramAESimpleWhenClause)
    throws ErrorException
  {
    ETValueExpr localETValueExpr1 = (ETValueExpr)paramAESimpleWhenClause.getWhenExpression().acceptVisitor(this);
    localETValueExpr1 = ConvMaterializeUtil.addConversionNodeWhenNeeded(localETValueExpr1, paramAESimpleWhenClause.getWhenExpression().getColumn(), paramAESimpleWhenClause.getComparisonMetadata(), getContext());
    ETValueExpr localETValueExpr2 = (ETValueExpr)paramAESimpleWhenClause.getThenExpression().acceptVisitor(this);
    localETValueExpr2 = ConvMaterializeUtil.addConversionNodeWhenNeeded(localETValueExpr2, paramAESimpleWhenClause.getThenExpression().getColumn(), ((AEValueExpr)paramAESimpleWhenClause.getParent().getParent()).getColumn(), getContext());
    return new ETSimpleWhenClause(localETValueExpr1, localETValueExpr2);
  }
  
  public ETValueExpr visit(AESubtract paramAESubtract)
    throws ErrorException
  {
    return materializeBinArithExpr(paramAESubtract, ArithmeticExprType.SUBTRACTION);
  }
  
  public ETValueExpr visit(AEValueSubQuery paramAEValueSubQuery)
    throws ErrorException
  {
    throw SQLEngineExceptionFactory.featureNotImplementedException("Value subquery");
  }
  
  private ETConstant doLiteralConversion(String paramString, TypeMetadata paramTypeMetadata, ConversionResult paramConversionResult)
    throws ErrorException
  {
    Object localObject = null;
    int i = paramTypeMetadata.getType();
    switch (i)
    {
    case -5: 
      localObject = new SqlBigIntDataWrapper();
      ((ISqlDataWrapper)localObject).setBigInt(CharConverter.toBigInt(paramString, paramConversionResult, paramTypeMetadata.isSigned()));
      break;
    case 2: 
    case 3: 
      localObject = new SqlExactNumDataWrapper(i);
      ((ISqlDataWrapper)localObject).setExactNumber(CharConverter.toExactNum(paramString, paramConversionResult, paramTypeMetadata.getPrecision(), paramTypeMetadata.getScale()));
      break;
    case 6: 
    case 8: 
      localObject = new SqlDoubleDataWrapper(i);
      ((ISqlDataWrapper)localObject).setDouble(CharConverter.toDouble(paramString, paramConversionResult));
      break;
    case 7: 
      localObject = new SqlRealDataWrapper();
      ((ISqlDataWrapper)localObject).setReal(CharConverter.toReal(paramString, paramConversionResult));
      break;
    case -10: 
    case -9: 
    case -8: 
    case -1: 
    case 1: 
    case 12: 
      localObject = new SqlCharDataWrapper(i);
      ((ISqlDataWrapper)localObject).setChar(paramString);
      break;
    case 91: 
      localObject = new SqlDateDataWrapper();
      ((ISqlDataWrapper)localObject).setDate(CharConverter.toDate(paramString, paramConversionResult, null));
      break;
    case 92: 
      localObject = new SqlTimeDataWrapper();
      ((ISqlDataWrapper)localObject).setTime(CharConverter.toTime(paramString, paramConversionResult, paramTypeMetadata.getPrecision(), null));
      break;
    case 93: 
      localObject = new SqlTimestampDataWrapper();
      ((ISqlDataWrapper)localObject).setTimestamp(CharConverter.toTimestamp(paramString, paramConversionResult, paramTypeMetadata.getPrecision(), null));
      break;
    case 4: 
      localObject = new SqlIntegerDataWrapper();
      ((ISqlDataWrapper)localObject).setInteger(CharConverter.toInteger(paramString, paramConversionResult, paramTypeMetadata.isSigned()));
      break;
    case 5: 
      localObject = new SqlSmallIntDataWrapper();
      ((ISqlDataWrapper)localObject).setSmallInt(CharConverter.toSmallint(paramString, paramConversionResult, paramTypeMetadata.isSigned()));
      break;
    case -6: 
      localObject = new SqlTinyIntDataWrapper();
      ((ISqlDataWrapper)localObject).setTinyInt(CharConverter.toTinyint(paramString, paramConversionResult, paramTypeMetadata.isSigned()));
      break;
    case -11: 
    case -7: 
    case -4: 
    case -3: 
    case -2: 
    case 16: 
      throw SQLEngineExceptionFactory.featureNotImplementedException(paramTypeMetadata.getTypeName() + " literal.");
    case 0: 
    case 9: 
    case 10: 
    case 11: 
    case 13: 
    case 14: 
    case 15: 
    case 17: 
    case 18: 
    case 19: 
    case 20: 
    case 21: 
    case 22: 
    case 23: 
    case 24: 
    case 25: 
    case 26: 
    case 27: 
    case 28: 
    case 29: 
    case 30: 
    case 31: 
    case 32: 
    case 33: 
    case 34: 
    case 35: 
    case 36: 
    case 37: 
    case 38: 
    case 39: 
    case 40: 
    case 41: 
    case 42: 
    case 43: 
    case 44: 
    case 45: 
    case 46: 
    case 47: 
    case 48: 
    case 49: 
    case 50: 
    case 51: 
    case 52: 
    case 53: 
    case 54: 
    case 55: 
    case 56: 
    case 57: 
    case 58: 
    case 59: 
    case 60: 
    case 61: 
    case 62: 
    case 63: 
    case 64: 
    case 65: 
    case 66: 
    case 67: 
    case 68: 
    case 69: 
    case 70: 
    case 71: 
    case 72: 
    case 73: 
    case 74: 
    case 75: 
    case 76: 
    case 77: 
    case 78: 
    case 79: 
    case 80: 
    case 81: 
    case 82: 
    case 83: 
    case 84: 
    case 85: 
    case 86: 
    case 87: 
    case 88: 
    case 89: 
    case 90: 
    default: 
      throw SQLEngineExceptionFactory.featureNotImplementedException("ISqlDataWrapper for type: " + paramTypeMetadata.getTypeName());
    }
    return new ETConstant((ISqlDataWrapper)localObject);
  }
  
  private ETValueExpr materializeAggregateFn(AEAggrFn paramAEAggrFn)
    throws ErrorException
  {
    IAggregatorFactory localIAggregatorFactory = ETAggregateFnFactory.makeNewAggregatorFactory(paramAEAggrFn, getContext().getExternalAlgorithmProperties().getCellMemoryLimit());
    if (localIAggregatorFactory.requiresDistinct()) {
      throw SQLEngineExceptionFactory.featureNotImplementedException("Aggregate Function with DISTINCT");
    }
    ETValueExprList localETValueExprList = new ETValueExprList();
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = paramAEAggrFn.getChildItr();
    while (localIterator.hasNext())
    {
      AEValueExpr localAEValueExpr = (AEValueExpr)localIterator.next();
      localETValueExprList.addNode((IETNode)localAEValueExpr.acceptVisitor(this));
      localArrayList.add(localAEValueExpr.getColumn());
    }
    return new ETAggregateFn(paramAEAggrFn.getAggrFnId(), localETValueExprList, localArrayList, localIAggregatorFactory.createAggregator());
  }
  
  private ETValueExpr materializeBinArithExpr(AEBinaryValueExpr paramAEBinaryValueExpr, ArithmeticExprType paramArithmeticExprType)
    throws ErrorException
  {
    AEValueExpr localAEValueExpr1 = paramAEBinaryValueExpr.getLeftOperand();
    AEValueExpr localAEValueExpr2 = paramAEBinaryValueExpr.getRightOperand();
    IColumn localIColumn1 = localAEValueExpr1.getColumn();
    IColumn localIColumn2 = localAEValueExpr2.getColumn();
    ETValueExpr localETValueExpr1 = (ETValueExpr)localAEValueExpr1.acceptVisitor(this);
    ETValueExpr localETValueExpr2 = (ETValueExpr)localAEValueExpr2.acceptVisitor(this);
    BinaryArithmeticOperator localBinaryArithmeticOperator = ArithmeticFunctorFactory.getBinaryArithFunctor(paramArithmeticExprType, paramAEBinaryValueExpr.getColumn(), localIColumn1, localIColumn2);
    localETValueExpr1 = ConvMaterializeUtil.addConversionNodeWhenNeeded(localETValueExpr1, localIColumn1, localBinaryArithmeticOperator.getLeftMetadata(), getContext());
    localETValueExpr2 = ConvMaterializeUtil.addConversionNodeWhenNeeded(localETValueExpr2, localIColumn2, localBinaryArithmeticOperator.getRightMetadata(), getContext());
    return new ETBinaryArithValueExpr(localBinaryArithmeticOperator.getLeftMetadata(), localETValueExpr1, localBinaryArithmeticOperator.getRightMetadata(), localETValueExpr2, localBinaryArithmeticOperator.getFunctor());
  }
  
  private ETValueExpr materializeUnaryArithExpr(AEUnaryValueExpr paramAEUnaryValueExpr, ArithmeticExprType paramArithmeticExprType)
    throws ErrorException
  {
    AEValueExpr localAEValueExpr = paramAEUnaryValueExpr.getOperand();
    IColumn localIColumn1 = localAEValueExpr.getColumn();
    IColumn localIColumn2 = paramAEUnaryValueExpr.getColumn();
    ETValueExpr localETValueExpr = (ETValueExpr)localAEValueExpr.acceptVisitor(this);
    localETValueExpr = ConvMaterializeUtil.addConversionNodeWhenNeeded(localETValueExpr, localIColumn1, localIColumn2, getContext());
    IUnaryArithmeticFunctor localIUnaryArithmeticFunctor = ArithmeticFunctorFactory.getUnaryArithFunctor(paramArithmeticExprType, paramAEUnaryValueExpr.getTypeMetadata(), localIColumn1.getTypeMetadata());
    return new ETUnaryArithValueExpr(localIColumn1, localETValueExpr, localIUnaryArithmeticFunctor);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/materializer/ETValueExprMaterializer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */