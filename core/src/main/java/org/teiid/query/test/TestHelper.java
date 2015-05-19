package org.teiid.query.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.ResultSet;
import java.util.List;

import javax.transaction.TransactionManager;

import org.teiid.client.RequestMessage;
import org.teiid.client.ResultsMessage;
import org.teiid.test.util.TableRenderer;
import org.teiid.test.util.TableRenderer.Column;
import org.teiid.test.util.TableRenderer.ColumnMetaData;

import com.arjuna.ats.arjuna.common.CoreEnvironmentBeanException;
import com.arjuna.ats.arjuna.common.ObjectStoreEnvironmentBean;
import com.arjuna.ats.arjuna.common.arjPropertyManager;
import com.arjuna.common.internal.util.propertyservice.BeanPopulator;

public class TestHelper {
	
	public static TransactionManager getTransactionManager() throws CoreEnvironmentBeanException{

        arjPropertyManager.getCoreEnvironmentBean().setNodeIdentifier("1"); //$NON-NLS-1$
        arjPropertyManager.getCoreEnvironmentBean().setSocketProcessIdPort(0);
        arjPropertyManager.getCoreEnvironmentBean().setSocketProcessIdMaxPorts(10);

        arjPropertyManager.getCoordinatorEnvironmentBean().setEnableStatistics(false);
        arjPropertyManager.getCoordinatorEnvironmentBean().setDefaultTimeout(300);
        arjPropertyManager.getCoordinatorEnvironmentBean().setTransactionStatusManagerEnable(false);
        arjPropertyManager.getCoordinatorEnvironmentBean().setTxReaperTimeout(120000);

        String storeDir = getStoreDir();

        arjPropertyManager.getObjectStoreEnvironmentBean().setObjectStoreDir(storeDir);
        BeanPopulator.getNamedInstance(ObjectStoreEnvironmentBean.class, "communicationStore").setObjectStoreDir(storeDir); //$NON-NLS-1$

        return com.arjuna.ats.jta.TransactionManager.transactionManager();
	}


	private static String getStoreDir() {
		String defDir = System.getProperty("user.home") + File.separator + ".teiid/embedded/data"; //$NON-NLS-1$ //$NON-NLS-2$
	    return System.getProperty("teiid.embedded.txStoreDir", defDir); //$NON-NLS-1$
	}
	
	public static RequestMessage exampleRequestMessage(String sql) {
        RequestMessage msg = new RequestMessage(sql);
        msg.setCursorType(ResultSet.TYPE_SCROLL_INSENSITIVE);
        msg.setFetchSize(10);
        msg.setPartialResults(false);
        msg.setExecutionId(100);
        return msg;
    }
	
	public static void dumpMsg(ResultsMessage results) {

		ColumnMetaData[] header = ColumnMetaData.Factory.form(results.getColumnNames());
	       
        TableRenderer renderer = new TableRenderer(header);
        for(List<?> list : results.getResultsList()) {
        	renderer.addRow(Column.Factory.form(list));
        }
        renderer.renderer();
	}
	
	@SuppressWarnings("unchecked")
	public static final <T extends Serializable> T helpSerialize(T object) throws IOException, ClassNotFoundException {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(object);
		oos.flush();
		
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
		return (T)ois.readObject();
	}	


}
