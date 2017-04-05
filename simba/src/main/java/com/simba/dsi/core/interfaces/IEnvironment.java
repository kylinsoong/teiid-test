package com.simba.dsi.core.interfaces;

import com.simba.dsi.core.utilities.Variant;
import com.simba.dsi.exceptions.BadAttrValException;
import com.simba.dsi.exceptions.BadPropertyKeyException;
import com.simba.dsi.exceptions.OptionalFeatureNotImplementedException;
import com.simba.support.IMessageSource;
import com.simba.support.IWarningListener;
import com.simba.support.exceptions.ErrorException;
import com.simba.support.security.ICredentials;
import javax.naming.OperationNotSupportedException;

public abstract interface IEnvironment
{
  public abstract void close();
  
  public abstract IConnection createConnection()
    throws ErrorException;
  
  public abstract IConnection createConnection(ICredentials paramICredentials)
    throws ErrorException, OperationNotSupportedException;
  
  public abstract IMessageSource getMessageSource();
  
  public abstract Variant getProperty(int paramInt)
    throws BadPropertyKeyException, ErrorException;
  
  public abstract IWarningListener getWarningListener();
  
  public abstract void registerWarningListener(IWarningListener paramIWarningListener);
  
  public abstract void setProperty(int paramInt, Variant paramVariant)
    throws BadAttrValException, OptionalFeatureNotImplementedException, ErrorException;
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/core/interfaces/IEnvironment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */