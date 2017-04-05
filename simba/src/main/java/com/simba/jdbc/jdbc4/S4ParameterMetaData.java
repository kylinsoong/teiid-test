package com.simba.jdbc.jdbc4;

import com.simba.dsi.dataengine.utilities.ParameterMetadata;
import com.simba.jdbc.common.SParameterMetaData;
import com.simba.support.ILogger;
import com.simba.support.IWarningListener;
import java.util.ArrayList;

public class S4ParameterMetaData
  extends SParameterMetaData
{
  public S4ParameterMetaData(ArrayList<ParameterMetadata> paramArrayList, ILogger paramILogger, IWarningListener paramIWarningListener)
  {
    super(paramArrayList, paramILogger, paramIWarningListener);
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/jdbc/jdbc4/S4ParameterMetaData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */