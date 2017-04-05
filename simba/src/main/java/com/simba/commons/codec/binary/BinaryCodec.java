/*     */ package com.simba.commons.codec.binary;
/*     */ 
/*     */ import com.simba.commons.codec.BinaryDecoder;
/*     */ import com.simba.commons.codec.BinaryEncoder;
/*     */ import com.simba.commons.codec.DecoderException;
/*     */ import com.simba.commons.codec.EncoderException;
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
/*     */ public class BinaryCodec
/*     */   implements BinaryDecoder, BinaryEncoder
/*     */ {
/*  41 */   private static final char[] EMPTY_CHAR_ARRAY = new char[0];
/*     */   
/*     */ 
/*  44 */   private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
/*     */   
/*     */ 
/*     */   private static final int BIT_0 = 1;
/*     */   
/*     */ 
/*     */   private static final int BIT_1 = 2;
/*     */   
/*     */ 
/*     */   private static final int BIT_2 = 4;
/*     */   
/*     */ 
/*     */   private static final int BIT_3 = 8;
/*     */   
/*     */ 
/*     */   private static final int BIT_4 = 16;
/*     */   
/*     */ 
/*     */   private static final int BIT_5 = 32;
/*     */   
/*     */ 
/*     */   private static final int BIT_6 = 64;
/*     */   
/*     */ 
/*     */   private static final int BIT_7 = 128;
/*     */   
/*  70 */   private static final int[] BITS = { 1, 2, 4, 8, 16, 32, 64, 128 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte[] encode(byte[] raw)
/*     */   {
/*  81 */     return toAsciiBytes(raw);
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
/*     */   public Object encode(Object raw)
/*     */     throws EncoderException
/*     */   {
/*  95 */     if (!(raw instanceof byte[])) {
/*  96 */       throw new EncoderException("argument not a byte array");
/*     */     }
/*  98 */     return toAsciiChars((byte[])raw);
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
/*     */   public Object decode(Object ascii)
/*     */     throws DecoderException
/*     */   {
/* 112 */     if (ascii == null) {
/* 113 */       return EMPTY_BYTE_ARRAY;
/*     */     }
/* 115 */     if ((ascii instanceof byte[])) {
/* 116 */       return fromAscii((byte[])ascii);
/*     */     }
/* 118 */     if ((ascii instanceof char[])) {
/* 119 */       return fromAscii((char[])ascii);
/*     */     }
/* 121 */     if ((ascii instanceof String)) {
/* 122 */       return fromAscii(((String)ascii).toCharArray());
/*     */     }
/* 124 */     throw new DecoderException("argument not a byte array");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte[] decode(byte[] ascii)
/*     */   {
/* 136 */     return fromAscii(ascii);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte[] toByteArray(String ascii)
/*     */   {
/* 148 */     if (ascii == null) {
/* 149 */       return EMPTY_BYTE_ARRAY;
/*     */     }
/* 151 */     return fromAscii(ascii.toCharArray());
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
/*     */   public static byte[] fromAscii(char[] ascii)
/*     */   {
/* 167 */     if ((ascii == null) || (ascii.length == 0)) {
/* 168 */       return EMPTY_BYTE_ARRAY;
/*     */     }
/*     */     
/* 171 */     byte[] l_raw = new byte[ascii.length >> 3];
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 176 */     int ii = 0; for (int jj = ascii.length - 1; ii < l_raw.length; jj -= 8) {
/* 177 */       for (int bits = 0; bits < BITS.length; bits++) {
/* 178 */         if (ascii[(jj - bits)] == '1') {
/* 179 */           int tmp58_57 = ii; byte[] tmp58_56 = l_raw;tmp58_56[tmp58_57] = ((byte)(tmp58_56[tmp58_57] | BITS[bits]));
/*     */         }
/*     */       }
/* 176 */       ii++;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 183 */     return l_raw;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte[] fromAscii(byte[] ascii)
/*     */   {
/* 194 */     if ((ascii == null) || (ascii.length == 0)) {
/* 195 */       return EMPTY_BYTE_ARRAY;
/*     */     }
/*     */     
/* 198 */     byte[] l_raw = new byte[ascii.length >> 3];
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 203 */     int ii = 0; for (int jj = ascii.length - 1; ii < l_raw.length; jj -= 8) {
/* 204 */       for (int bits = 0; bits < BITS.length; bits++) {
/* 205 */         if (ascii[(jj - bits)] == 49) {
/* 206 */           int tmp58_57 = ii; byte[] tmp58_56 = l_raw;tmp58_56[tmp58_57] = ((byte)(tmp58_56[tmp58_57] | BITS[bits]));
/*     */         }
/*     */       }
/* 203 */       ii++;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 210 */     return l_raw;
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
/*     */   public static byte[] toAsciiBytes(byte[] raw)
/*     */   {
/* 223 */     if ((raw == null) || (raw.length == 0)) {
/* 224 */       return EMPTY_BYTE_ARRAY;
/*     */     }
/*     */     
/* 227 */     byte[] l_ascii = new byte[raw.length << 3];
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 232 */     int ii = 0; for (int jj = l_ascii.length - 1; ii < raw.length; jj -= 8) {
/* 233 */       for (int bits = 0; bits < BITS.length; bits++) {
/* 234 */         if ((raw[ii] & BITS[bits]) == 0) {
/* 235 */           l_ascii[(jj - bits)] = 48;
/*     */         } else {
/* 237 */           l_ascii[(jj - bits)] = 49;
/*     */         }
/*     */       }
/* 232 */       ii++;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 241 */     return l_ascii;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static char[] toAsciiChars(byte[] raw)
/*     */   {
/* 253 */     if ((raw == null) || (raw.length == 0)) {
/* 254 */       return EMPTY_CHAR_ARRAY;
/*     */     }
/*     */     
/* 257 */     char[] l_ascii = new char[raw.length << 3];
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 262 */     int ii = 0; for (int jj = l_ascii.length - 1; ii < raw.length; jj -= 8) {
/* 263 */       for (int bits = 0; bits < BITS.length; bits++) {
/* 264 */         if ((raw[ii] & BITS[bits]) == 0) {
/* 265 */           l_ascii[(jj - bits)] = '0';
/*     */         } else {
/* 267 */           l_ascii[(jj - bits)] = '1';
/*     */         }
/*     */       }
/* 262 */       ii++;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 271 */     return l_ascii;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String toAsciiString(byte[] raw)
/*     */   {
/* 283 */     return new String(toAsciiChars(raw));
/*     */   }
/*     */ }


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/commons/codec/binary/BinaryCodec.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */