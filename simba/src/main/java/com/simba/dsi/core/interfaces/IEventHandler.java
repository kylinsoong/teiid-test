package com.simba.dsi.core.interfaces;

public abstract interface IEventHandler
{
  public abstract void handleEvent(EventID paramEventID, Object paramObject);
  
  public static enum EventID
  {
    EVENT_THREAD_START,  EVENT_THREAD_FINISH,  EVENT_START_ENVIRONMENT_FUNCTION,  EVENT_START_CONNECTION_FUNCTION,  EVENT_START_STATEMENT_FUNCTION,  EVENT_END_ODBC_FUNCTION;
    
    private EventID() {}
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/dsi/core/interfaces/IEventHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */