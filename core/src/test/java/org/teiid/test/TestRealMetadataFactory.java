package org.teiid.test;

import java.util.Map;

import org.junit.Test;
import org.teiid.metadata.Datatype;
import org.teiid.query.test.RealMetadataFactory;

public class TestRealMetadataFactory {
	
	@Test
	public void testDatatype() {
		
		 Map<String, Datatype> map = RealMetadataFactory.getDataTypes();
		 for(String key : map.keySet()) {
			 System.out.println(key + " -> " + map.get(key));
		 }
	}

}
