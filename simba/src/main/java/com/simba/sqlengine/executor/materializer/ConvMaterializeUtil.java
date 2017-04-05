package com.simba.sqlengine.executor.materializer;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.sqlengine.executor.conversions.ConversionUtil;
import com.simba.sqlengine.executor.conversions.ISqlConverter;
import com.simba.sqlengine.executor.conversions.SqlConverterGenerator;
import com.simba.sqlengine.executor.etree.relation.ETRelationalConvert;
import com.simba.sqlengine.executor.etree.relation.ETRelationalExpr;
import com.simba.sqlengine.executor.etree.value.ETConstant;
import com.simba.sqlengine.executor.etree.value.ETConvert;
import com.simba.sqlengine.executor.etree.value.ETDefault;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.List;

public final class ConvMaterializeUtil
{
  public static ETRelationalConvert makeNewRelationConvertNode(List<IColumn> paramList, ETRelationalExpr paramETRelationalExpr, boolean[] paramArrayOfBoolean, boolean paramBoolean, MaterializerContext paramMaterializerContext)
    throws ErrorException
  {
    if (paramList.size() != paramETRelationalExpr.getColumnCount()) {
      throw new IllegalArgumentException("column number mismatch");
    }
    ArrayList localArrayList = new ArrayList();
    for (int i = 0; i < paramList.size(); i++) {
      localArrayList.add(ConversionUtil.createConverter(paramMaterializerContext.getSqlConverterGenerator(), paramETRelationalExpr.getColumn(i), (IColumn)paramList.get(i)));
    }
    return new ETRelationalConvert(paramList, localArrayList, paramBoolean, paramETRelationalExpr, paramArrayOfBoolean);
  }
  
  public static ETValueExpr addConversionNodeWhenNeeded(ETValueExpr paramETValueExpr, IColumn paramIColumn1, IColumn paramIColumn2, MaterializerContext paramMaterializerContext)
    throws ErrorException
  {
    return addConversionNodeWhenNeeded(paramETValueExpr, paramIColumn1, paramIColumn2, false, paramMaterializerContext);
  }
  
  public static ETValueExpr addConversionNodeWhenNeeded(ETValueExpr paramETValueExpr, IColumn paramIColumn1, IColumn paramIColumn2, boolean paramBoolean, MaterializerContext paramMaterializerContext)
    throws ErrorException
  {
    if ((paramETValueExpr instanceof ETConstant))
    {
      if (((ETConstant)paramETValueExpr).isNull()) {
        return paramETValueExpr;
      }
    }
    else if ((paramETValueExpr instanceof ETDefault)) {
      return paramETValueExpr;
    }
    SqlConverterGenerator localSqlConverterGenerator = paramMaterializerContext.getSqlConverterGenerator();
    ISqlConverter localISqlConverter = ConversionUtil.createConverter(localSqlConverterGenerator, paramIColumn1, paramIColumn2);
    if (localISqlConverter == null) {
      return paramETValueExpr;
    }
    return new ETConvert(paramETValueExpr, paramIColumn1, localISqlConverter, paramBoolean);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/materializer/ConvMaterializeUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */