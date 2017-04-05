/*    */ package com.simba.couchbase.dataengine;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CBParamInfo
/*    */ {
/*    */   private boolean m_isPositional;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private int m_position;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private String m_name;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setPositional(boolean isPositional)
/*    */   {
/* 41 */     this.m_isPositional = isPositional;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setPosition(int position)
/*    */   {
/* 49 */     this.m_position = position;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setName(String name)
/*    */   {
/* 57 */     this.m_name = name;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isPositional()
/*    */   {
/* 67 */     return this.m_isPositional;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getPosition()
/*    */   {
/* 77 */     return this.m_position;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getName()
/*    */   {
/* 87 */     return this.m_name;
/*    */   }
/*    */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/couchbase/dataengine/CBParamInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */