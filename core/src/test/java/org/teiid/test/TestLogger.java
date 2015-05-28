package org.teiid.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;
import org.teiid.logging.LogManager;
import org.teiid.query.test.TestHelper;
import org.teiid.test.logger.TeiidFormatter;

public class TestLogger {
	
	@Test
	public void testLoggerManager() {
		TestHelper.enableLogger();
		LogManager.logInfo("org.teiid.RUNTIME.VDBLifeCycleListener", "TEIID40118 VDB MatViewMySQLVDB.1 added to the repository - is reloading false");
		Logger logger = Logger.getLogger("org.teiid.RUNTIME.VDBLifeCycleListener");
		Level javaLevel = Level.FINE;
		boolean isLoggable = logger.isLoggable(javaLevel);
		assertTrue(isLoggable);
		LogManager.logInfo("org.teiid.RUNTIME.VDBLifeCycleListener", "TEIID40118 VDB MatViewMySQLVDB.1 added to the repository - is reloading false");
	}
    
    @Test
    public void testRootLogger(){
        Logger rootLogger = Logger.getLogger("");
        for(Handler handler : rootLogger.getHandlers()) {
            assertNotNull( handler.getFormatter().getClass());
        }
    }
    
    @Test
    public void testLogger() {
        TestHelper.enableLogger();
        Logger logger = Logger.getLogger("org.teiid");
        for(Handler handler : logger.getHandlers()){
            assertEquals(TeiidFormatter.class, handler.getFormatter().getClass());
            assertEquals(Level.FINEST, handler.getLevel());
        }
    }
    
    @Test
    public void testLoggerOutput(){

        TestHelper.enableLogger();
        
        Logger logger = Logger.getLogger("org.teiid.x.y");
        
        logger.finest("This is finest test message");
        logger.finer("This is finer test message");
        logger.fine("This is fine test message");
        logger.config("This is config test message");
        logger.info("This is info test message");
        logger.warning("This is warning test message");
        logger.severe("This is severe test message");
    }
}
