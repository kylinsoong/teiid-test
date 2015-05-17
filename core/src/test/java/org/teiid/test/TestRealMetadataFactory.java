package org.teiid.test;

import java.util.Map;

import org.junit.Test;
import org.teiid.dqp.internal.process.DQPWorkContext;
import org.teiid.metadata.Datatype;
import org.teiid.metadata.Schema;
import org.teiid.query.metadata.CompositeMetadataStore;
import org.teiid.query.metadata.TransformationMetadata;
import org.teiid.query.test.RealMetadataFactory;

public class TestRealMetadataFactory {
	
	@Test
	public void testDatatype() {
		
		 Map<String, Datatype> map = RealMetadataFactory.getDataTypes();
		 for(String key : map.keySet()) {
			 System.out.println(key + " -> " + map.get(key));
		 }
	}
	
	@Test
	public void testBqt(){
		CompositeMetadataStore store = RealMetadataFactory.exampleBQTCached().getMetadataStore();
		for(Schema schema : store.getSchemaList()) {
			System.out.println(schema.getFullName() + " -> " + schema.getTables());
		}
		
		TransformationMetadata metadata = RealMetadataFactory.createTransformationMetadata(store, "bqt");
		System.out.println(metadata.getVdbMetaData());
		
		DQPWorkContext context = RealMetadataFactory.buildWorkContext(metadata);
		System.out.println(context);
		System.out.println(context.getAppName());
		System.out.println(context.getClientAddress());
		System.out.println(context.getClientHostname());
		System.out.println(context.getSecurityDomain());
		System.out.println(context.getSessionId());
		System.out.println(context.getUserName());
		System.out.println(context.getVdbName());
		System.out.println(context.getVdbVersion());
		System.out.println(context.getVDB());
	}

}
