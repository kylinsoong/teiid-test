package org.teiid.test.perf;

import java.io.Serializable;

public class PerfEntity implements Serializable {

	private static final long serialVersionUID = 6185799796645031473L;
	
	private String sql;
	
	private long queryTime;
	
	private long deserializeTime;

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public long getQueryTime() {
		return queryTime;
	}

	public void setQueryTime(long queryTime) {
		this.queryTime = queryTime;
	}

	public long getDeserializeTime() {
		return deserializeTime;
	}

	public void setDeserializeTime(long deserializeTime) {
		this.deserializeTime = deserializeTime;
	}

	@Override
	public String toString() {
		return "PerfEntity [sql=" + sql + ", queryTime=" + queryTime + ", deserializeTime=" + deserializeTime + "]";
	}

}
