package com.simba.dsi.core.interfaces;

import com.simba.dsi.core.utilities.Variant;
import com.simba.dsi.dataengine.interfaces.IDataEngine;
import com.simba.dsi.exceptions.BadAttrValException;
import com.simba.dsi.exceptions.BadPropertyKeyException;
import com.simba.support.ILogger;
import com.simba.support.IWarningListener;
import com.simba.support.exceptions.ErrorException;

public abstract interface IStatement {

    public abstract void close() throws ErrorException;

    public abstract IDataEngine createDataEngine() throws ErrorException;

    public abstract String getCursorName() throws ErrorException;

    public abstract Variant getCustomProperty(int paramInt) throws ErrorException;

    public abstract int getCustomPropertyType(int paramInt) throws ErrorException;

    public abstract ILogger getLog();

    public abstract IConnection getParentConnection();

    public abstract Variant getProperty(int paramInt) throws BadPropertyKeyException, ErrorException;

    public abstract Variant getSimilarValue(int paramInt, Variant paramVariant) throws ErrorException;

    public abstract IWarningListener getWarningListener(); 
    
    public abstract boolean isCustomProperty(int paramInt) throws ErrorException;

    public abstract boolean isValueSupported(int paramInt, Variant paramVariant) throws ErrorException;

    public abstract void notifyCursorNameChange(String paramString) throws ErrorException;

    public abstract void registerWarningListener(IWarningListener paramIWarningListener);
    
    public abstract void setCustomProperty(int paramInt, Variant paramVariant) throws BadAttrValException, ErrorException;
    
    public abstract void setProperty(int paramInt, Variant paramVariant) throws BadAttrValException, ErrorException;
}
