package org.teiid.test.perf;

import org.teiid.deployers.CompositeVDB;
import org.teiid.deployers.VDBLifeCycleListener;

public class OutPutListener implements VDBLifeCycleListener {

    @Override
    public void added(String name, int version, CompositeVDB vdb,
            boolean reloading) {
        System.out.println("\n added() \n");

    }

    @Override
    public void beforeRemove(String name, int version, CompositeVDB vdb) {
        System.out.println("\n beforeRemove() \n");
    }

    @Override
    public void removed(String name, int version, CompositeVDB vdb) {
        System.out.println("\n removed() \n");

    }

    @Override
    public void finishedDeployment(String name, int version, CompositeVDB vdb,
            boolean reloading) {
        System.out.println("\n finishedDeployment() \n");

    }

}
