package org.jboss.teiid.dashboard;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;


public class LoggingHelper {
    
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

    }
    
    


}
