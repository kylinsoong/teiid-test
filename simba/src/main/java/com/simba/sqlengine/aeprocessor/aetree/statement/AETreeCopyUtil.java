package com.simba.sqlengine.aeprocessor.aetree.statement;

import com.simba.sqlengine.aeprocessor.aetree.AETreeWalker;
import com.simba.sqlengine.aeprocessor.aetree.IAENode;
import com.simba.sqlengine.aeprocessor.aetree.relation.AENamedRelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.relation.AERelationalExpr;
import com.simba.sqlengine.aeprocessor.aetree.value.AEColumnReference;
import com.simba.sqlengine.aeprocessor.aetree.value.AEProxyColumn;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class AETreeCopyUtil
{
  public static void updateColumns(IAEStatement paramIAEStatement)
  {
    HashMap localHashMap = new HashMap();
    ArrayList localArrayList1 = new ArrayList();
    ArrayList localArrayList2 = new ArrayList();
    prepareUpdate(paramIAEStatement, localHashMap, localArrayList1, localArrayList2);
    Iterator localIterator = localArrayList1.iterator();
    Object localObject2;
    while (localIterator.hasNext())
    {
      localObject1 = (AEColumnReference)localIterator.next();
      localObject2 = ((AEColumnReference)localObject1).getNamedRelationalExpr();
      ((AEColumnReference)localObject1).setNamedRelationalExpr((AENamedRelationalExpr)localHashMap.get(localObject2));
    }
    Object localObject1 = localArrayList2.iterator();
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = (AEProxyColumn)((Iterator)localObject1).next();
      AERelationalExpr localAERelationalExpr = ((AEProxyColumn)localObject2).getRelationalExpr();
      ((AEProxyColumn)localObject2).setRelationalExpr((AERelationalExpr)localHashMap.get(localAERelationalExpr));
    }
  }
  
  private static void prepareUpdate(IAEStatement paramIAEStatement, Map<AERelationalExpr, AERelationalExpr> paramMap, List<AEColumnReference> paramList, List<AEProxyColumn> paramList1)
  {
    AETreeWalker localAETreeWalker = new AETreeWalker(paramIAEStatement);
    while (localAETreeWalker.hasNext())
    {
      IAENode localIAENode = localAETreeWalker.next();
      if ((localIAENode instanceof AERelationalExpr))
      {
        AERelationalExpr localAERelationalExpr1 = (AERelationalExpr)localIAENode;
        AERelationalExpr localAERelationalExpr2 = localAERelationalExpr1.getOrigin();
        if (null != localAERelationalExpr2) {
          paramMap.put(localAERelationalExpr2, localAERelationalExpr1);
        } else {
          throw new IllegalStateException("AETree copy error.");
        }
      }
      else if ((localIAENode instanceof AEColumnReference))
      {
        paramList.add((AEColumnReference)localIAENode);
      }
      else if ((localIAENode instanceof AEProxyColumn))
      {
        paramList1.add((AEProxyColumn)localIAENode);
      }
    }
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aetree/statement/AETreeCopyUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */