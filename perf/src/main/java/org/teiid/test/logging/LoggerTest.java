package org.teiid.test.logging;

import java.util.logging.Level;

import org.teiid.test.perf.ResultsCachingDebugSimple;
import org.teiid.test.util.EmbeddedHelper;

public class LoggerTest {

	public static void main(String[] args) throws Exception {

		EmbeddedHelper.enableLogger(Level.ALL);
		
//		EmbeddedHelper.configureLogManager("TRACE", "teiid.log");
		
		ResultsCachingDebugSimple.startup();
	}

}
