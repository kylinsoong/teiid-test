package org.teiid.test;

public interface DDL {
	
	public final static String SQL_MAT_STATUS_MYSQL = "execute SYSADMIN.matViewStatus('Test', 'PERFTESTEXTER_MATVIEW')";
	
	public final static String SQL_MAT_STATUS_H2 = "execute SYSADMIN.matViewStatus('Stocks', 'MatView')";
	
	public final static String SQL_MAT_LOAD_H2 = "execute SYSADMIN.loadMatView('Stocks','MatView')";
	
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
	
	public final static String PRODUCT_VIEW_H2 = "CREATE VIEW PRODUCTView (\n" +
												 "    product_id integer,\n" +
												 "    symbol string\n" +
												 ")\n" +
												 "AS\n" +
												 "SELECT  A.ID, A.symbol FROM  Accounts.PRODUCT AS A\n";
	
	

}
