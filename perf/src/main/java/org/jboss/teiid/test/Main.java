package org.jboss.teiid.test;

import static org.jboss.teiid.test.perf.MysqlPERFTESTClient.MB;

import org.jboss.teiid.test.perf.MysqlPERFTESTClient;

public class Main {

	public static void main(String[] args) throws Exception {

		//insert 100 MB to Mysql
		MysqlPERFTESTClient.insert(MB, true);
	}

}
