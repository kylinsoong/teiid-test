package org.teiid.test.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class TeiidFormatter extends Formatter {

	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm SSS");

	public String format(LogRecord record) {
		StringBuffer sb = new StringBuffer();
		sb.append(format.format(new Date(record.getMillis())) + " ");
		sb.append(getLevelString(record.getLevel()) + " ");
		sb.append("[" + record.getLoggerName() + "] (");
		sb.append(Thread.currentThread().getName() + ") ");
		sb.append(record.getMessage() + "\n");
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
