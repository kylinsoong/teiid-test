/*     */ package com.simba.couchbase.jdbc4;
/*     */ 
/*     */ import com.simba.dsi.core.interfaces.IConnection;
/*     */ import com.simba.dsi.core.interfaces.IStatement;
/*     */ import com.simba.jdbc.common.SCallableStatement;
/*     */ import com.simba.jdbc.common.SConnection;
/*     */ import com.simba.jdbc.common.SDatabaseMetaData;
/*     */ import com.simba.jdbc.common.SPreparedStatement;
/*     */ import com.simba.jdbc.common.SStatement;
/*     */ import com.simba.jdbc.jdbc4.JDBC4ObjectFactory;
/*     */ import com.simba.support.ILogger;
/*     */ import java.sql.SQLException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CBJDBC4ObjectFactory
/*     */   extends JDBC4ObjectFactory
/*     */ {
/*     */   protected SCallableStatement createCallableStatement(String sql, IStatement dsiStatement, SConnection parentConnection, int concurrency)
/*     */     throws SQLException
/*     */   {
/*  59 */     return super.createCallableStatement(sql, dsiStatement, parentConnection, concurrency);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SConnection createConnection(IConnection dsiConnection, String url)
/*     */     throws SQLException
/*     */   {
/*  76 */     return super.createConnection(dsiConnection, url);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SDatabaseMetaData createDatabaseMetaData(SConnection conn, ILogger logger)
/*     */     throws SQLException
/*     */   {
/*  92 */     return super.createDatabaseMetaData(conn, logger);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SPreparedStatement createPreparedStatement(String sql, IStatement dsiStatement, SConnection parentConnection, int concurrency)
/*     */     throws SQLException
/*     */   {
/* 115 */     return super.createPreparedStatement(sql, dsiStatement, parentConnection, concurrency);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SStatement createStatement(IStatement dsiStatement, SConnection parentConnection, int concurrency)
/*     */     throws SQLException
/*     */   {
/* 134 */     return super.createStatement(dsiStatement, parentConnection, concurrency);
/*     */   }
/*     */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/couchbase/jdbc4/CBJDBC4ObjectFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */