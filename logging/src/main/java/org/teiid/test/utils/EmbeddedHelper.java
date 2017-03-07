package org.teiid.test.utils;

import java.io.File;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.transaction.TransactionManager;

import org.teiid.logging.MessageLevel;

import com.arjuna.ats.arjuna.common.ObjectStoreEnvironmentBean;
import com.arjuna.ats.arjuna.common.arjPropertyManager;
import com.arjuna.common.internal.util.propertyservice.BeanPopulator;

public class EmbeddedHelper {
    
	
	public static TransactionManager getTransactionManager() throws Exception {
		
		arjPropertyManager.getCoreEnvironmentBean().setNodeIdentifier(UUID.randomUUID().toString());
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
		String defDir = getSystemProperty("user.home") + File.separator + ".teiid/embedded/data"; //$NON-NLS-1$ //$NON-NLS-2$
		return getSystemProperty("teiid.embedded.txStoreDir", defDir); //$NON-NLS-1$
	}


	private static String getSystemProperty(final String name, final String value) {
		return AccessController.doPrivileged(new PrivilegedAction<String>(){

			@Override
			public String run() {
				return System.getProperty(name, value);
			}});
	}
	
	private static String getSystemProperty(final String name) {
		return AccessController.doPrivileged(new PrivilegedAction<String>(){

			@Override
			public String run() {
				return System.getProperty(name);
			}});
	}
	
	private static void setSystemProperty(final String key, final String value) {
        if (System.getSecurityManager() == null) {
            System.setProperty(key, value);
        } else {
            AccessController.doPrivileged(new PrivilegedAction<Object>() {
                @Override
                public Object run() {
                    System.setProperty(key, value);
                    return null;
                }
            });
        }
    }
	
	public static void enableLogger(Level level) {
        enableLogger(level, "org.teiid"); //$NON-NLS-1$
    }
    
	/**
     * This method supply function for adjusting Teiid logging while running the Embedded mode.
     * 
     * For example, with parameters names is 'org.teiid.COMMAND_LOG' and level is 'FINEST' Teiid
     * will show command logs in console.
     * 
     * @param formatter
     *          The Formatter used in ConsoleHandler 
     * @param level
     *          The logger level used in Logger
     * @param names
     *          The logger's name
     */
    public static void enableLogger(Level level, String... names){
        enableLogger(new TeiidLoggerFormatter(), Level.SEVERE, level, names);
    }
    
    public static void enableLogger(Formatter formatter, Level rootLevel, Level level, String... names){
        
        Logger rootLogger = Logger.getLogger("");
        for(Handler handler : rootLogger.getHandlers()){
            handler.setFormatter(formatter);
            handler.setLevel(rootLevel);
        }
        
        for(String name : names) {
            Logger logger = Logger.getLogger(name);
            logger.setLevel(level);
            ConsoleHandler handler = new ConsoleHandler();
            handler.setLevel(level);
            handler.setFormatter(formatter);
            logger.addHandler(handler);
            logger.setUseParentHandlers(false);
        }
        
        org.teiid.logging.LogManager.isMessageToBeRecorded("org.teiid", MessageLevel.INFO);
    }
    
    public static class TeiidLoggerFormatter extends Formatter {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm SSS"); //$NON-NLS-1$ 

        public String format(LogRecord record) {
            StringBuffer sb = new StringBuffer();
            sb.append(format.format(new Date(record.getMillis())) + " "); //$NON-NLS-1$ 
            sb.append(getLevelString(record.getLevel()) + " "); //$NON-NLS-1$ 
            sb.append("[" + record.getLoggerName() + "] ("); //$NON-NLS-1$ //$NON-NLS-2$  
            sb.append(Thread.currentThread().getName() + ") "); //$NON-NLS-1$ 
            sb.append(record.getMessage() + "\n"); //$NON-NLS-1$ 
            return sb.toString();
        }

        private String getLevelString(Level level) {
            String name = level.toString();
            int size = name.length();
            for(int i = size; i < 7 ; i ++){
                name += " ";
            }
            return name;
        }
    }

}
