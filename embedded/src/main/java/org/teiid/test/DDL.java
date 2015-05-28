package org.teiid.test;

public interface DDL {
	
	public final static String EXTER_MAT_H2 = "CREATE VIEW PERFTESTEXTER_MATVIEW (\n" +
												 "id varchar,\n" +
												 "col_a varchar,\n" +
												 "col_b varchar,\n" +
												 "col_c varchar\n" +
												 ") OPTIONS (\n" +
												 "	  MATERIALIZED 'TRUE',\n" +
												 "    UPDATABLE 'TRUE',\n" +
												 "    MATERIALIZED_TABLE 'Accounts.PERFTEST_MAT',\n" +
												 "    \"teiid_rel:ALLOW_MATVIEW_MANAGEMENT\" 'true',\n" +
												 "    \"teiid_rel:MATVIEW_STATUS_TABLE\" 'Accounts.status',\n" +
												 "    \"teiid_rel:MATERIALIZED_STAGE_TABLE\" 'Accounts.PERFTEST_STAGING'\n" +
												 ")\n" +
												 "AS\n" +
												 "SELECT A.id, A.col_a, A.col_b, A.col_c FROM Accounts.PERFTEST AS A\n";
	
	public final static String EXTER_MAT_H2_MIN = "CREATE VIEW PERFTESTEXTER_MATVIEW (\n" +
												 "id varchar,\n" +
												 "col_a varchar,\n" +
												 "col_b varchar,\n" +
												 "col_c varchar\n" +
												 ") OPTIONS (\n" +
												 "	  MATERIALIZED 'TRUE',\n" +
												 "    UPDATABLE 'TRUE',\n" +
												 "    MATERIALIZED_TABLE 'Accounts.PERFTEST_MAT',\n" +
												 "    \"teiid_rel:ALLOW_MATVIEW_MANAGEMENT\" 'false',\n" +
												 "    \"teiid_rel:MATVIEW_STATUS_TABLE\" 'n/a'\n" +
												 ")\n" +
												 "AS\n" +
												 "SELECT A.id, A.col_a, A.col_b, A.col_c FROM Accounts.PERFTEST AS A\n";
	
	

}
