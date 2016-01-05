package org.jboss.teiid.dashboard;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;


public class TeiidLoggerFormatter extends Formatter {

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
