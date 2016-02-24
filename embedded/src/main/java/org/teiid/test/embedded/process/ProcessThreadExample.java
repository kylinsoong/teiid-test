package org.teiid.test.embedded.process;

import org.teiid.dqp.internal.process.TeiidExecutor;
import org.teiid.dqp.internal.process.ThreadReuseExecutor;

public class ProcessThreadExample {

    
    static final String PROCESS_PLAN_QUEUE_NAME = "QueryProcessorQueue"; //$NON-NLS-1$
    static final int DEFAULT_MAX_PROCESS_WORKERS = 64;
    
    public static void main(String[] args) {

        int maxThreads = DEFAULT_MAX_PROCESS_WORKERS;
        
        TeiidExecutor processWorkerPool = new ThreadReuseExecutor(PROCESS_PLAN_QUEUE_NAME, maxThreads);
        
        System.out.println(Integer.MAX_VALUE);
    }

}
