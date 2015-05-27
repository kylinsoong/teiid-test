package org.jboss.teiid.test.embedded;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.jboss.teiid.test.DDL;
import org.teiid.adminapi.Model.Type;
import org.teiid.adminapi.impl.ModelMetaData;
import org.teiid.adminapi.impl.VDBMetaData;
import org.teiid.metadata.MetadataFactory;
import org.teiid.metadata.MetadataRepository;
import org.teiid.query.metadata.ChainingMetadataRepository;
import org.teiid.query.metadata.DDLMetadataRepository;
import org.teiid.query.metadata.DirectQueryMetadataRepository;
import org.teiid.query.metadata.NativeMetadataRepository;
import org.teiid.translator.ExecutionFactory;
import org.teiid.translator.TranslatorException;

public class TestEmbeddedDeploy {

	public static void main(String[] args) {

		VDBMetaData vdb = new VDBMetaData();
		
		Properties props = new Properties();
		props.setProperty("version", "1");
		props.setProperty("name", "MatViewMySQLVDB");
		
		vdb.setName(props.getProperty("name"));
		vdb.setVersion(Integer.parseInt(props.getProperty("version")));
		
		vdb.setDescription("Teiid Perf VDB");
		
		vdb.addProperty("UseConnectorMetadata", "true");
		
		ModelMetaData accounts = new ModelMetaData();
		accounts.setName("Accounts");
		accounts.setModelType("PHYSICAL");
		accounts.setVisible(true);
		accounts.setPath(null);
		accounts.addProperty("importer.useFullSchemaName", "false");
		accounts.addSourceMapping("mysql-connector", "translator-mysql", "java:/accounts-ds");
		vdb.addModel(accounts);
		
		ModelMetaData test = new ModelMetaData();
		test.setName("Test");
		test.setModelType("VIRTUAL");
		test.setVisible(true);
		test.setPath(null);
		test.addSourceMetadata("DDL", DDL.EXTER_MAT_MYSQL);
		vdb.addModel(test);
		
		vdb.setXmlDeployment(true);
		
		MetadataRepository<?, ?> defaultRepo = null;
		
		for (ModelMetaData model : vdb.getModelMetaDatas().values()){
			
			if (model.getModelType() == Type.FUNCTION || model.getModelType() == Type.OTHER) {
				continue;
			}
			
			MetadataRepository<?, ?> repo = getMetadataRepository(vdb, model, defaultRepo);
			model.addAttchment(MetadataRepository.class, repo);
			
			System.out.println(model);
		}
	}

	private static MetadataRepository<?, ?> getMetadataRepository(VDBMetaData vdb, ModelMetaData model, MetadataRepository<?, ?> defaultRepo) {
		
		if (model.getSourceMetadataType().isEmpty()) {
			if (defaultRepo != null) {
				return defaultRepo;
			}
			if (model.isSource()) {
				return new ChainingMetadataRepository(Arrays.asList(new NativeMetadataRepository(), new DirectQueryMetadataRepository()));
			}
		}
		
		List<MetadataRepository<?, ?>> repos = new ArrayList<MetadataRepository<?,?>>(2);
		
		for (int i = 0; i < model.getSourceMetadataType().size(); i++) {
			String schemaTypes = model.getSourceMetadataType().get(i);
			StringTokenizer st = new StringTokenizer(schemaTypes, ","); //$NON-NLS-1$
			while (st.hasMoreTokens()) {
				String repoType = st.nextToken().trim();
				MetadataRepository<?, ?> current = new DDLMetadataRepository();
				if (model.getSourceMetadataText().size() > i) {
					current = new MetadataRepositoryWrapper(current, model.getSourceMetadataText().get(i));
				}
				repos.add(current);
			}
		}
		return null;
	}
	
	private static class MetadataRepositoryWrapper<F, C> extends MetadataRepository<F, C> {

		private MetadataRepository<F, C> repo;
		private String text;
		
		public MetadataRepositoryWrapper(MetadataRepository<F, C> repo, String text) {
			this.repo = repo;
			this.text = text;
		}
		
		@Override
		public void loadMetadata(MetadataFactory factory,ExecutionFactory<F, C> executionFactory, F connectionFactory) throws TranslatorException {
			repo.loadMetadata(factory, executionFactory, connectionFactory, this.text);
		}
		
	};

}
