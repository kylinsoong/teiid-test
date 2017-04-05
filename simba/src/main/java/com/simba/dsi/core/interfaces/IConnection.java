package com.simba.dsi.core.interfaces;

import com.simba.dsi.core.utilities.ClientInfoData;
import com.simba.dsi.core.utilities.ConnSettingRequestMap;
import com.simba.dsi.core.utilities.ConnSettingResponseMap;
import com.simba.dsi.core.utilities.ConnectionSettingInfo;
import com.simba.dsi.core.utilities.PromptType;
import com.simba.dsi.core.utilities.Variant;
import com.simba.dsi.exceptions.BadAttrValException;
import com.simba.dsi.exceptions.BadAuthException;
import com.simba.dsi.exceptions.BadPropertyKeyException;
import com.simba.support.ILogger;
import com.simba.support.IMessageSource;
import com.simba.support.IWarningListener;
import com.simba.support.exceptions.ClientInfoException;
import com.simba.support.exceptions.ErrorException;
import java.util.Locale;
import java.util.Map;

public abstract interface IConnection {
    
    public abstract void beginTransaction() throws ErrorException;
    
    public abstract void close();

    public abstract void commit() throws ErrorException;
    
    public abstract void connect(ConnSettingRequestMap paramConnSettingRequestMap) throws BadAuthException, ErrorException;

    public abstract void createSavepoint(String paramString) throws ErrorException;
    
    public abstract IStatement createStatement() throws ErrorException;

    public abstract void disconnect() throws ErrorException;

    public abstract Map<Long, ConnectionSettingInfo> getConnectionSettingInfo();

    public abstract ILogger getConnectionLog();

    public abstract String getClientInfo(String paramString) throws ErrorException;

    public abstract Map<String, ClientInfoData> getClientInfoProperties();

    public abstract Variant getCustomProperty(int paramInt) throws ErrorException;

    public abstract int getCustomPropertyType(int paramInt) throws ErrorException;

    public abstract String getDataSourceName() throws ErrorException;

    public abstract Locale getLocale();

    public abstract IMessageSource getMessageSource();

    public abstract IEnvironment getParentEnvironment();

    public abstract Variant getProperty(int paramInt) throws BadPropertyKeyException, ErrorException;

    public abstract IWarningListener getWarningListener();

    public abstract boolean isAlive();
    
    public abstract boolean isCustomProperty(int paramInt) throws ErrorException;

    public abstract boolean promptDialog(ConnSettingResponseMap paramConnSettingResponseMap, ConnSettingRequestMap paramConnSettingRequestMap, long paramLong, PromptType paramPromptType);

    public abstract void registerTransactionStateListener(ITransactionStateListener paramITransactionStateListener);

    public abstract void registerWarningListener(IWarningListener paramIWarningListener);

    public abstract void releaseSavepoint(String paramString) throws ErrorException;

    public abstract void rollback() throws ErrorException;

    public abstract void rollback(String paramString) throws ErrorException;

    public abstract void setClientInfoProperty(String paramString1, String paramString2) throws ClientInfoException;
    
    public abstract void setCustomProperty(int paramInt, Variant paramVariant) throws BadAttrValException, ErrorException;
    
    public abstract void setInvokerAndClassLoader(Object paramObject1, Object paramObject2);
    
    public abstract void setLocale(Locale paramLocale);
    
    public abstract void setProperty(int paramInt, Variant paramVariant) throws BadAttrValException, ErrorException;
    
    public abstract String toNativeSQL(String paramString) throws ErrorException;
    
    public abstract ConnSettingResponseMap updateConnectionSettings(ConnSettingRequestMap paramConnSettingRequestMap) throws BadAuthException, ErrorException;
}
