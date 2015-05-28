package org.jboss.teiid.test;

import org.teiid.test.perf.H2PERFTESTClient;
import org.teiid.test.perf.MaterializedViews;
import org.teiid.test.perf.ResultsCaching;

public class Main {

	public static void main(String[] args) throws Exception {
	    
	    H2PERFTESTClient.main(args);
	    
	    ResultsCaching.main(args);
	    
	    MaterializedViews.main(args);
	}

}
