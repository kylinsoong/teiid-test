package org.teiid.test;

public class Constants {
    
    public static final int KB = 1<<10;
    public static final int MB = 1<<20;
    public static final int GB = 1<<30;
    
    public static final String H2_JDBC_DRIVER = "org.h2.Driver";
    public static final String H2_JDBC_URL = "jdbc:h2:file:target/teiid-perf-ds-1;DB_CLOSE_ON_EXIT=FALSE;DB_CLOSE_DELAY=-1";
    public static final String H2_JDBC_USER = "sa";
    public static final String H2_JDBC_PASS = "sa";
    
    public static final String COL_ID = "1234";
    public static final String COL_A = "abcdefghabcdefgh";
    public static final String COL_B = "abcdefghigklmnopqrstabcdefghigklmnopqrst";
    public static final String COL_C = "1234567890123456789012345678901234567890";

}
