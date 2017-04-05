package com.simba.sqlengine.executor.etree.temptable;

import com.simba.dsi.dataengine.interfaces.IColumn;
import com.simba.dsi.dataengine.utilities.TypeMetadata;
import com.simba.sqlengine.aeprocessor.aetree.AESortSpec;
import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.executor.etree.temptable.column.BooleanComparator;
import com.simba.sqlengine.executor.etree.temptable.column.CharComparator;
import com.simba.sqlengine.executor.etree.temptable.column.DateComparator;
import com.simba.sqlengine.executor.etree.temptable.column.DoubleComparator;
import com.simba.sqlengine.executor.etree.temptable.column.ExactNumComparator;
import com.simba.sqlengine.executor.etree.temptable.column.GuidComparator;
import com.simba.sqlengine.executor.etree.temptable.column.RealComparator;
import com.simba.sqlengine.executor.etree.temptable.column.SignedBigIntComparator;
import com.simba.sqlengine.executor.etree.temptable.column.SignedIntegerComparator;
import com.simba.sqlengine.executor.etree.temptable.column.SignedSmallIntComparator;
import com.simba.sqlengine.executor.etree.temptable.column.SignedTinyIntComparator;
import com.simba.sqlengine.executor.etree.temptable.column.TimeComparator;
import com.simba.sqlengine.executor.etree.temptable.column.TimestampComparator;
import com.simba.sqlengine.executor.etree.temptable.column.UnsignedBigIntComparator;
import com.simba.sqlengine.executor.etree.temptable.column.UnsignedIntegerComparator;
import com.simba.sqlengine.executor.etree.temptable.column.UnsignedSmallIntComparator;
import com.simba.sqlengine.executor.etree.temptable.column.UnsignedTinyIntComparator;
import com.simba.support.exceptions.ErrorException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class RowComparator
  implements Comparator<IRowView>
{
  private ArrayList<Comparator<IRowView>> m_comparators = new ArrayList();
  
  public static RowComparator createComparator(IColumn[] paramArrayOfIColumn, List<AESortSpec> paramList, NullCollation paramNullCollation)
    throws ErrorException
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      AESortSpec localAESortSpec = (AESortSpec)localIterator.next();
      TypeMetadata localTypeMetadata = paramArrayOfIColumn[localAESortSpec.getColumnNumber()].getTypeMetadata();
      Object localObject;
      switch (localTypeMetadata.getType())
      {
      case -7: 
      case 16: 
        localArrayList.add(new BooleanComparator(localAESortSpec.getColumnNumber(), localAESortSpec.isAscending(), nullsSortedFirst(localAESortSpec.isAscending(), paramNullCollation)));
        break;
      case -10: 
      case -9: 
      case -8: 
      case -1: 
      case 1: 
      case 12: 
        localArrayList.add(new CharComparator(localAESortSpec.getColumnNumber(), localAESortSpec.isAscending(), nullsSortedFirst(localAESortSpec.isAscending(), paramNullCollation)));
        break;
      case -5: 
        if (localTypeMetadata.isSigned()) {
          localObject = new SignedBigIntComparator(localAESortSpec.getColumnNumber(), localAESortSpec.isAscending(), nullsSortedFirst(localAESortSpec.isAscending(), paramNullCollation));
        } else {
          localObject = new UnsignedBigIntComparator(localAESortSpec.getColumnNumber(), localAESortSpec.isAscending(), nullsSortedFirst(localAESortSpec.isAscending(), paramNullCollation));
        }
        localArrayList.add(localObject);
        break;
      case 4: 
        if (localTypeMetadata.isSigned()) {
          localObject = new SignedIntegerComparator(localAESortSpec.getColumnNumber(), localAESortSpec.isAscending(), nullsSortedFirst(localAESortSpec.isAscending(), paramNullCollation));
        } else {
          localObject = new UnsignedIntegerComparator(localAESortSpec.getColumnNumber(), localAESortSpec.isAscending(), nullsSortedFirst(localAESortSpec.isAscending(), paramNullCollation));
        }
        localArrayList.add(localObject);
        break;
      case -6: 
        if (localTypeMetadata.isSigned()) {
          localObject = new SignedTinyIntComparator(localAESortSpec.getColumnNumber(), localAESortSpec.isAscending(), nullsSortedFirst(localAESortSpec.isAscending(), paramNullCollation));
        } else {
          localObject = new UnsignedTinyIntComparator(localAESortSpec.getColumnNumber(), localAESortSpec.isAscending(), nullsSortedFirst(localAESortSpec.isAscending(), paramNullCollation));
        }
        localArrayList.add(localObject);
        break;
      case 5: 
        if (localTypeMetadata.isSigned()) {
          localObject = new SignedSmallIntComparator(localAESortSpec.getColumnNumber(), localAESortSpec.isAscending(), nullsSortedFirst(localAESortSpec.isAscending(), paramNullCollation));
        } else {
          localObject = new UnsignedSmallIntComparator(localAESortSpec.getColumnNumber(), localAESortSpec.isAscending(), nullsSortedFirst(localAESortSpec.isAscending(), paramNullCollation));
        }
        localArrayList.add(localObject);
        break;
      case 7: 
        localArrayList.add(new RealComparator(localAESortSpec.getColumnNumber(), localAESortSpec.isAscending(), nullsSortedFirst(localAESortSpec.isAscending(), paramNullCollation)));
        break;
      case 6: 
      case 8: 
        localArrayList.add(new DoubleComparator(localAESortSpec.getColumnNumber(), localAESortSpec.isAscending(), nullsSortedFirst(localAESortSpec.isAscending(), paramNullCollation)));
        break;
      case -11: 
        localArrayList.add(new GuidComparator(localAESortSpec.getColumnNumber(), localAESortSpec.isAscending(), nullsSortedFirst(localAESortSpec.isAscending(), paramNullCollation)));
        break;
      case 2: 
      case 3: 
        localArrayList.add(new ExactNumComparator(localAESortSpec.getColumnNumber(), localAESortSpec.isAscending(), nullsSortedFirst(localAESortSpec.isAscending(), paramNullCollation)));
        break;
      case 91: 
        localArrayList.add(new DateComparator(localAESortSpec.getColumnNumber(), localAESortSpec.isAscending(), nullsSortedFirst(localAESortSpec.isAscending(), paramNullCollation)));
        break;
      case 92: 
        localArrayList.add(new TimeComparator(localAESortSpec.getColumnNumber(), localAESortSpec.isAscending(), nullsSortedFirst(localAESortSpec.isAscending(), paramNullCollation)));
        break;
      case 93: 
        localArrayList.add(new TimestampComparator(localAESortSpec.getColumnNumber(), localAESortSpec.isAscending(), nullsSortedFirst(localAESortSpec.isAscending(), paramNullCollation)));
        break;
      default: 
        throw SQLEngineExceptionFactory.invalidOrderByColumnException("" + (localAESortSpec.getColumnNumber() + 1));
      }
    }
    return new RowComparator(localArrayList);
  }
  
  public static NullCollation getDefaultNullCollation()
  {
    return NullCollation.NULLS_END;
  }
  
  public static AESortSpec createDefaultSortSpec(int paramInt)
  {
    return new AESortSpec(paramInt, false);
  }
  
  public RowComparator(List<Comparator<IRowView>> paramList)
  {
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      Comparator localComparator = (Comparator)localIterator.next();
      this.m_comparators.add(localComparator);
    }
  }
  
  public int compare(IRowView paramIRowView1, IRowView paramIRowView2)
  {
    for (int i = 0; i < this.m_comparators.size(); i++)
    {
      int j = ((Comparator)this.m_comparators.get(i)).compare(paramIRowView1, paramIRowView2);
      if (j != 0) {
        return j;
      }
    }
    return 0;
  }
  
  private static boolean nullsSortedFirst(boolean paramBoolean, NullCollation paramNullCollation)
  {
    switch (paramNullCollation)
    {
    case NULLS_END: 
      return false;
    case NULLS_HI: 
      return !paramBoolean;
    case NULLS_LO: 
      return paramBoolean;
    }
    return true;
  }
  
  public static enum NullCollation
  {
    NULLS_START,  NULLS_HI,  NULLS_LO,  NULLS_END;
    
    private NullCollation() {}
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/executor/etree/temptable/RowComparator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */