package org.teiid.test.embedded.plan.jta;

import java.util.UUID;

import javax.transaction.TransactionManager;

import com.arjuna.ats.arjuna.common.ObjectStoreEnvironmentBean;
import com.arjuna.ats.arjuna.common.arjPropertyManager;
import com.arjuna.common.internal.util.propertyservice.BeanPopulator;

public class JTAUtils {
    
    public static TransactionManager getTransactionManager() throws Exception {

        arjPropertyManager.getCoreEnvironmentBean().setNodeIdentifier(UUID.randomUUID().toString());
        arjPropertyManager.getCoreEnvironmentBean().setSocketProcessIdPort(0);
        arjPropertyManager.getCoreEnvironmentBean().setSocketProcessIdMaxPorts(10);

        arjPropertyManager.getCoordinatorEnvironmentBean().setEnableStatistics(false);
        arjPropertyManager.getCoordinatorEnvironmentBean().setDefaultTimeout(300);
        arjPropertyManager.getCoordinatorEnvironmentBean().setTransactionStatusManagerEnable(false);
        arjPropertyManager.getCoordinatorEnvironmentBean().setTxReaperTimeout(120000);

        String storeDir = "target";

        arjPropertyManager.getObjectStoreEnvironmentBean().setObjectStoreDir(storeDir);
        BeanPopulator.getNamedInstance(ObjectStoreEnvironmentBean.class, "communicationStore").setObjectStoreDir(storeDir); //$NON-NLS-1$

        return com.arjuna.ats.jta.TransactionManager.transactionManager();
}


}
