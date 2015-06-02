package org.teiid.test.perf;

import org.jboss.teiid.test.rsCaching.ResultsCaching;

public class ResultsCachingDebug_ {

    public static void main(String[] args) throws Exception {

        ResultsCaching.main(args);
        
        Thread.sleep(Long.MAX_VALUE);
    }

}
