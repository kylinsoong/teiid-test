package com.simba.sqlengine.executor.etree.value.scalar;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.datawrapper.ISqlDataWrapper;
import com.simba.sqlengine.executor.etree.ETDataRequest;
import com.simba.sqlengine.executor.etree.util.DataRetrievalUtil;
import com.simba.sqlengine.executor.etree.value.ETValueExpr;
import com.simba.support.exceptions.ErrorException;
import java.util.List;

public class ETSoundexFn
  extends ETScalarFn
{
  private static final char MIN_ALPHA = 'A';
  private static final char MAX_ALPHA = 'z';
  private static final char NO_MAPPING_CODE = '\000';
  private static final char HW_CHAR_CODE = '7';
  private static final char VOWEL_CODE = '8';
  private static final char[] SOUNDEX_MAP;
  private static final int RESULT_SIZE = 4;
  
  public ETSoundexFn(IColumn paramIColumn, List<ETValueExpr> paramList, List<IColumn> paramList1)
    throws ErrorException
  {
    super(paramIColumn, paramList, paramList1);
    assert (((IColumn)paramList1.get(0)).getTypeMetadata().isCharacterType());
    assert (paramIColumn.getTypeMetadata().isCharacterType());
    assert (paramIColumn.getColumnLength() >= 4L);
  }
  
  public boolean retrieveData(ETDataRequest paramETDataRequest)
    throws ErrorException
  {
    ISqlDataWrapper localISqlDataWrapper = getArgumentData(0);
    if (hasMoreData(0)) {
      throw SQLEngineExceptionFactory.invalidScalarFunctionDataException("SOUNDEX", 1);
    }
    if (localISqlDataWrapper.isNull())
    {
      paramETDataRequest.getData().setNull();
      return false;
    }
    String str = localISqlDataWrapper.getChar();
    int i = str.length();
    char[] arrayOfChar = { '0', '0', '0', '0' };
    int j = 0;
    int k = 0;
    int m = 0;
    while (k < i)
    {
      char c = str.charAt(k);
      int n;
      if ((c >= 'A') && (c <= 'z') && ('\000' != (n = SOUNDEX_MAP[c]))) {
        if (0 == m)
        {
          arrayOfChar[(m++)] = Character.toUpperCase(c);
          j = n;
        }
        else if (56 == n)
        {
          j = 0;
        }
        else if ((55 != n) && (j != n))
        {
          arrayOfChar[(m++)] = n;
          if (4 <= m) {
            break;
          }
          j = n;
        }
      }
      k++;
    }
    paramETDataRequest.getData().setChar(String.valueOf(arrayOfChar));
    return DataRetrievalUtil.retrieveCharData(paramETDataRequest.getData(), paramETDataRequest.getOffset(), paramETDataRequest.getMaxSize());
  }
  
  static
  {
    SOUNDEX_MAP = new char[123];
    char[] arrayOfChar = SOUNDEX_MAP;
    arrayOfChar[66] = (arrayOfChar[98] = arrayOfChar[70] = arrayOfChar[102] = arrayOfChar[80] = arrayOfChar[112] = arrayOfChar[86] = arrayOfChar[118] = 49);
    arrayOfChar[67] = (arrayOfChar[99] = arrayOfChar[71] = arrayOfChar[103] = arrayOfChar[74] = arrayOfChar[106] = arrayOfChar[75] = arrayOfChar[107] = arrayOfChar[81] = arrayOfChar[113] = arrayOfChar[83] = arrayOfChar[115] = arrayOfChar[88] = arrayOfChar[120] = arrayOfChar[90] = arrayOfChar[122] = 50);
    arrayOfChar[68] = (arrayOfChar[100] = arrayOfChar[84] = arrayOfChar[116] = 51);
    arrayOfChar[76] = (arrayOfChar[108] = 52);
    arrayOfChar[77] = (arrayOfChar[109] = arrayOfChar[78] = arrayOfChar[110] = 53);
    arrayOfChar[82] = (arrayOfChar[114] = 54);
    arrayOfChar[65] = (arrayOfChar[97] = arrayOfChar[69] = arrayOfChar[101] = arrayOfChar[73] = arrayOfChar[105] = arrayOfChar[79] = arrayOfChar[111] = arrayOfChar[85] = arrayOfChar[117] = arrayOfChar[89] = arrayOfChar[121] = 56);
    arrayOfChar[87] = (arrayOfChar[119] = arrayOfChar[72] = arrayOfChar[104] = 55);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/value/scalar/ETSoundexFn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */