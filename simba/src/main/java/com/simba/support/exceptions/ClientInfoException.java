package com.simba.support.exceptions;

import java.util.Map;

public class ClientInfoException
  extends ErrorException
{
  private static final long serialVersionUID = 718209731290675430L;
  private final Map<String, FailedPropertiesReason> m_failedProperties;
  
  public ClientInfoException(int paramInt, String paramString, Map<String, FailedPropertiesReason> paramMap)
  {
    super(DiagState.DIAG_GENERAL_ERROR, paramInt, paramString, -1, -1);
    this.m_failedProperties = paramMap;
  }
  
  public ClientInfoException(int paramInt1, String paramString, int paramInt2, int paramInt3, Map<String, FailedPropertiesReason> paramMap)
  {
    super(DiagState.DIAG_GENERAL_ERROR, paramInt1, paramString, paramInt2, paramInt3);
    this.m_failedProperties = paramMap;
  }
  
  public ClientInfoException(int paramInt1, String paramString, int paramInt2, int paramInt3, Throwable paramThrowable, Map<String, FailedPropertiesReason> paramMap)
  {
    super(DiagState.DIAG_GENERAL_ERROR, paramInt1, paramString, paramInt2, paramInt3, paramThrowable);
    this.m_failedProperties = paramMap;
  }
  
  public ClientInfoException(int paramInt, String paramString, String[] paramArrayOfString, Map<String, FailedPropertiesReason> paramMap)
  {
    super(DiagState.DIAG_GENERAL_ERROR, paramInt, paramString, paramArrayOfString, -1, -1);
    this.m_failedProperties = paramMap;
  }
  
  public ClientInfoException(int paramInt1, String paramString, String[] paramArrayOfString, int paramInt2, int paramInt3, Map<String, FailedPropertiesReason> paramMap)
  {
    super(DiagState.DIAG_GENERAL_ERROR, paramInt1, paramString, paramArrayOfString, paramInt2, paramInt3);
    this.m_failedProperties = paramMap;
  }
  
  public ClientInfoException(int paramInt1, String paramString, String[] paramArrayOfString, int paramInt2, int paramInt3, Throwable paramThrowable, Map<String, FailedPropertiesReason> paramMap)
  {
    super(DiagState.DIAG_GENERAL_ERROR, paramInt1, paramString, paramArrayOfString, paramInt2, paramInt3, paramThrowable);
    this.m_failedProperties = paramMap;
  }
  
  public ClientInfoException(int paramInt, String paramString, String[] paramArrayOfString, Throwable paramThrowable, Map<String, FailedPropertiesReason> paramMap)
  {
    super(DiagState.DIAG_GENERAL_ERROR, paramInt, paramString, paramArrayOfString, -1, -1, paramThrowable);
    this.m_failedProperties = paramMap;
  }
  
  public ClientInfoException(int paramInt, String paramString, Throwable paramThrowable, Map<String, FailedPropertiesReason> paramMap)
  {
    super(DiagState.DIAG_GENERAL_ERROR, paramInt, paramString, -1, -1);
    this.m_failedProperties = paramMap;
  }
  
  public ClientInfoException(String paramString, int paramInt, Map<String, FailedPropertiesReason> paramMap)
  {
    super(DiagState.DIAG_GENERAL_ERROR, paramString, paramInt, -1, -1);
    this.m_failedProperties = paramMap;
  }
  
  public ClientInfoException(String paramString, int paramInt1, int paramInt2, int paramInt3, Map<String, FailedPropertiesReason> paramMap)
  {
    super(DiagState.DIAG_GENERAL_ERROR, paramString, paramInt1, paramInt2, paramInt3);
    this.m_failedProperties = paramMap;
  }
  
  public ClientInfoException(String paramString, int paramInt1, int paramInt2, int paramInt3, Throwable paramThrowable, Map<String, FailedPropertiesReason> paramMap)
  {
    super(DiagState.DIAG_GENERAL_ERROR, paramString, paramInt1, paramInt2, paramInt3, paramThrowable);
    this.m_failedProperties = paramMap;
  }
  
  public ClientInfoException(String paramString, int paramInt, Throwable paramThrowable, Map<String, FailedPropertiesReason> paramMap)
  {
    super(DiagState.DIAG_GENERAL_ERROR, paramString, paramInt, -1, -1, paramThrowable);
    this.m_failedProperties = paramMap;
  }
  
  public ClientInfoException(DiagState paramDiagState, int paramInt, String paramString, Map<String, FailedPropertiesReason> paramMap)
  {
    super(paramDiagState, paramInt, paramString, -1, -1);
    this.m_failedProperties = paramMap;
  }
  
  public ClientInfoException(DiagState paramDiagState, int paramInt1, String paramString, int paramInt2, int paramInt3, Map<String, FailedPropertiesReason> paramMap)
  {
    super(paramDiagState, paramInt1, paramString, paramInt2, paramInt3);
    this.m_failedProperties = paramMap;
  }
  
  public ClientInfoException(DiagState paramDiagState, int paramInt1, String paramString, int paramInt2, int paramInt3, Throwable paramThrowable, Map<String, FailedPropertiesReason> paramMap)
  {
    super(paramDiagState, paramInt1, paramString, paramInt2, paramInt3, paramThrowable);
    this.m_failedProperties = paramMap;
  }
  
  public ClientInfoException(DiagState paramDiagState, int paramInt, String paramString, String[] paramArrayOfString, Map<String, FailedPropertiesReason> paramMap)
  {
    super(paramDiagState, paramInt, paramString, paramArrayOfString, -1, -1);
    this.m_failedProperties = paramMap;
  }
  
  public ClientInfoException(DiagState paramDiagState, int paramInt1, String paramString, String[] paramArrayOfString, int paramInt2, int paramInt3, Map<String, FailedPropertiesReason> paramMap)
  {
    super(paramDiagState, paramInt1, paramString, paramArrayOfString, paramInt2, paramInt3);
    this.m_failedProperties = paramMap;
  }
  
  public ClientInfoException(DiagState paramDiagState, int paramInt1, String paramString, String[] paramArrayOfString, int paramInt2, int paramInt3, Throwable paramThrowable, Map<String, FailedPropertiesReason> paramMap)
  {
    super(paramDiagState, paramInt1, paramString, paramArrayOfString, paramInt2, paramInt3, paramThrowable);
    this.m_failedProperties = paramMap;
  }
  
  public ClientInfoException(DiagState paramDiagState, int paramInt, String paramString, String[] paramArrayOfString, Throwable paramThrowable, Map<String, FailedPropertiesReason> paramMap)
  {
    super(paramDiagState, paramInt, paramString, paramArrayOfString, -1, -1, paramThrowable);
    this.m_failedProperties = paramMap;
  }
  
  public ClientInfoException(DiagState paramDiagState, int paramInt, String paramString, Throwable paramThrowable, Map<String, FailedPropertiesReason> paramMap)
  {
    super(paramDiagState, paramInt, paramString, -1, -1);
    this.m_failedProperties = paramMap;
  }
  
  public ClientInfoException(DiagState paramDiagState, String paramString, int paramInt, Map<String, FailedPropertiesReason> paramMap)
  {
    super(paramDiagState, paramString, paramInt, -1, -1);
    this.m_failedProperties = paramMap;
  }
  
  public ClientInfoException(DiagState paramDiagState, String paramString, int paramInt1, int paramInt2, int paramInt3, Map<String, FailedPropertiesReason> paramMap)
  {
    super(paramDiagState, paramString, paramInt1, paramInt2, paramInt3);
    this.m_failedProperties = paramMap;
  }
  
  public ClientInfoException(DiagState paramDiagState, String paramString, int paramInt1, int paramInt2, int paramInt3, Throwable paramThrowable, Map<String, FailedPropertiesReason> paramMap)
  {
    super(paramDiagState, paramString, paramInt1, paramInt2, paramInt3, paramThrowable);
    this.m_failedProperties = paramMap;
  }
  
  public ClientInfoException(DiagState paramDiagState, String paramString, int paramInt, Throwable paramThrowable, Map<String, FailedPropertiesReason> paramMap)
  {
    super(paramDiagState, paramString, paramInt, -1, -1, paramThrowable);
    this.m_failedProperties = paramMap;
  }
  
  public ClientInfoException(String paramString1, int paramInt, String paramString2, Map<String, FailedPropertiesReason> paramMap)
  {
    super(paramString1, paramInt, paramString2, -1, -1);
    this.m_failedProperties = paramMap;
  }
  
  public ClientInfoException(String paramString1, int paramInt1, String paramString2, int paramInt2, int paramInt3, Map<String, FailedPropertiesReason> paramMap)
  {
    super(paramString1, paramInt1, paramString2, paramInt2, paramInt3);
    this.m_failedProperties = paramMap;
  }
  
  public ClientInfoException(String paramString1, int paramInt1, String paramString2, int paramInt2, int paramInt3, Throwable paramThrowable, Map<String, FailedPropertiesReason> paramMap)
  {
    super(paramString1, paramInt1, paramString2, paramInt2, paramInt3, paramThrowable);
    this.m_failedProperties = paramMap;
  }
  
  public ClientInfoException(String paramString1, int paramInt, String paramString2, String[] paramArrayOfString, Map<String, FailedPropertiesReason> paramMap)
  {
    super(paramString1, paramInt, paramString2, paramArrayOfString, -1, -1);
    this.m_failedProperties = paramMap;
  }
  
  public ClientInfoException(String paramString1, int paramInt1, String paramString2, String[] paramArrayOfString, int paramInt2, int paramInt3, Map<String, FailedPropertiesReason> paramMap)
  {
    super(paramString1, paramInt1, paramString2, paramArrayOfString, paramInt2, paramInt3);
    this.m_failedProperties = paramMap;
  }
  
  public ClientInfoException(String paramString1, int paramInt1, String paramString2, String[] paramArrayOfString, int paramInt2, int paramInt3, Throwable paramThrowable, Map<String, FailedPropertiesReason> paramMap)
  {
    super(paramString1, paramInt1, paramString2, paramArrayOfString, paramInt2, paramInt3, paramThrowable);
    this.m_failedProperties = paramMap;
  }
  
  public ClientInfoException(String paramString1, int paramInt, String paramString2, String[] paramArrayOfString, Throwable paramThrowable, Map<String, FailedPropertiesReason> paramMap)
  {
    super(paramString1, paramInt, paramString2, paramArrayOfString, -1, -1, paramThrowable);
    this.m_failedProperties = paramMap;
  }
  
  public ClientInfoException(String paramString1, int paramInt, String paramString2, Throwable paramThrowable, Map<String, FailedPropertiesReason> paramMap)
  {
    super(paramString1, paramInt, paramString2, -1, -1, paramThrowable);
    this.m_failedProperties = paramMap;
  }
  
  public ClientInfoException(String paramString1, String paramString2, int paramInt, Map<String, FailedPropertiesReason> paramMap)
  {
    super(paramString1, paramString2, paramInt, -1, -1);
    this.m_failedProperties = paramMap;
  }
  
  public ClientInfoException(String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3, Map<String, FailedPropertiesReason> paramMap)
  {
    super(paramString1, paramString2, paramInt1, paramInt2, paramInt3);
    this.m_failedProperties = paramMap;
  }
  
  public ClientInfoException(String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3, Throwable paramThrowable, Map<String, FailedPropertiesReason> paramMap)
  {
    super(paramString1, paramString2, paramInt1, paramInt2, paramInt3, paramThrowable);
    this.m_failedProperties = paramMap;
  }
  
  public ClientInfoException(String paramString1, String paramString2, int paramInt, Throwable paramThrowable, Map<String, FailedPropertiesReason> paramMap)
  {
    super(paramString1, paramString2, paramInt, -1, -1, paramThrowable);
    this.m_failedProperties = paramMap;
  }
  
  public Map<String, FailedPropertiesReason> getFailedProperties()
  {
    return this.m_failedProperties;
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/exceptions/ClientInfoException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */