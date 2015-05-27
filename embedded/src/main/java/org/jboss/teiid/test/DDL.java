package org.jboss.teiid.test;

public interface DDL {
	
	public final static String EXTER_MAT_MYSQL = "CREATE VIEW PERFTESTEXTER_MATVIEW (" +
												 "id long," +
												 "col_a varchar," +
												 "col_b varchar," +
												 "col_c varchar" +
												 ") OPTIONS (" +
												 "	  MATERIALIZED 'TRUE'," +
												 "    UPDATABLE 'TRUE'," +
												 "    MATERIALIZED_TABLE 'Accounts.PERFTEST_MAT'," +
												 "    \"teiid_rel:MATVIEW_TTL\" 3600000," +
												 "    \"teiid_rel:MATERIALIZED_STAGE_TABLE\" 'Accounts.PERFTEST_STAGING'," +
												 "    \"teiid_rel:MATVIEW_SHARE_SCOPE\" 'NONE'," +
												 "    \"teiid_rel:MATVIEW_ONERROR_ACTION\" 'THROW_EXCEPTION'" +
												 ")" +
												 "AS" +
												 "SELECT A.id, A.col_a, A.col_b, A.col_c FROM Accounts.PERFTEST AS A";

}
