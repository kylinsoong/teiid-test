package org.teiid.test.embedded;

import static org.teiid.test.Constants.H2_JDBC_DRIVER;
import static org.teiid.test.Constants.H2_JDBC_PASS;
import static org.teiid.test.Constants.H2_JDBC_URL;
import static org.teiid.test.Constants.H2_JDBC_USER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import javax.resource.ResourceException;
import javax.sql.DataSource;

import org.teiid.adminapi.Model;
import org.teiid.adminapi.Model.Type;
import org.teiid.adminapi.VDB.Status;
import org.teiid.adminapi.impl.ModelMetaData;
import org.teiid.adminapi.impl.SourceMappingMetadata;
import org.teiid.adminapi.impl.VDBMetaData;
import org.teiid.deployers.UDFMetaData;
import org.teiid.deployers.VDBRepository;
import org.teiid.deployers.VirtualDatabaseException;
import org.teiid.dqp.internal.datamgr.ConnectorManager;
import org.teiid.dqp.internal.datamgr.ConnectorManagerRepository;
import org.teiid.dqp.internal.datamgr.ConnectorManagerRepository.ConnectorManagerException;
import org.teiid.dqp.internal.datamgr.ConnectorManagerRepository.ExecutionFactoryProvider;
import org.teiid.dqp.internal.datamgr.ProviderAwareConnectorManagerRepository;
import org.teiid.example.EmbeddedHelper;
import org.teiid.logging.LogConstants;
import org.teiid.logging.LogManager;
import org.teiid.logging.MessageLevel;
import org.teiid.metadata.Datatype;
import org.teiid.metadata.MetadataFactory;
import org.teiid.metadata.MetadataRepository;
import org.teiid.metadata.MetadataStore;
import org.teiid.metadata.Procedure;
import org.teiid.metadata.Table;
import org.teiid.metadata.VDBResource;
import org.teiid.query.function.SystemFunctionManager;
import org.teiid.query.metadata.ChainingMetadataRepository;
import org.teiid.query.metadata.DDLMetadataRepository;
import org.teiid.query.metadata.DDLStringVisitor;
import org.teiid.query.metadata.DirectQueryMetadataRepository;
import org.teiid.query.metadata.MaterializationMetadataRepository;
import org.teiid.query.metadata.NativeMetadataRepository;
import org.teiid.query.metadata.VDBResources;
import org.teiid.query.parser.QueryParser;
import org.teiid.query.test.TestHelper;
import org.teiid.runtime.EmbeddedServer.SimpleConnectionFactoryProvider;
import org.teiid.test.DDL;
import org.teiid.test.util.TableRenderer;
import org.teiid.test.util.TableRenderer.Column;
import org.teiid.test.util.TableRenderer.ColumnMetaData;
import org.teiid.translator.ExecutionFactory;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;

public class TestEmbeddedDeploy {
	
	static {
		TestHelper.enableLogger();
	}
	
	static Logger logger = Logger.getLogger(TestEmbeddedDeploy.class.getName());

	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws ResourceException, VirtualDatabaseException, TranslatorException, ConnectorManagerException {

		
		logger.info("start");
		/**
		 * 1. Form a VDBMetaData
		 */
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
		accounts.addSourceMapping("mysql-h2", "translator-h2", "java:/accounts-ds");
		vdb.addModel(accounts);
		
		ModelMetaData test = new ModelMetaData();
		test.setName("Test");
		test.setModelType("VIRTUAL");
		test.setVisible(true);
		test.setPath(null);
		test.addSourceMetadata("DDL", DDL.EXTER_MAT_H2_MIN);
		vdb.addModel(test);
		
		vdb.setXmlDeployment(true);
		
		MetadataRepository<?, ?> defaultRepo = null;
		
		/**
		 * 2. ConnectorManagerRepository createConnectorManagers
		 */
		final ExecutionFactory<?, ?> factory = new H2ExecutionFactory();
        factory.start();
        factory.setSupportsDirectQueryProcedure(true);
		
		DataSource ds = EmbeddedHelper.newDataSource(H2_JDBC_DRIVER, H2_JDBC_URL, H2_JDBC_USER, H2_JDBC_PASS);
		ConnectorManagerRepository cmr = new ProviderAwareConnectorManagerRepository("java:/accounts-ds", new SimpleConnectionFactoryProvider<DataSource>(ds));
		
		cmr.createConnectorManagers(vdb,new ExecutionFactoryProvider(){

			@SuppressWarnings("unchecked")
			@Override
			public ExecutionFactory<Object, Object> getExecutionFactory(String name) throws ConnectorManagerException {
				return (ExecutionFactory<Object, Object>) factory;
			}});
		
		/**
		 * 3. assignMetadataRepositories
		 */
		for (ModelMetaData model : vdb.getModelMetaDatas().values()){
			
			if (model.getModelType() == Type.FUNCTION || model.getModelType() == Type.OTHER) {
				continue;
			}
			
			MetadataRepository<?, ?> repo = getMetadataRepository(vdb, model, defaultRepo);
			model.addAttchment(MetadataRepository.class, repo);
		}
		
		for (ModelMetaData model : vdb.getModelMetaDatas().values()) {
			MetadataRepository metadataRepository = model.getAttachment(MetadataRepository.class);
			System.out.println(metadataRepository);
		}
		
		/**
		 * 4. VDBRepository addVDB
		 */
		VDBRepository repo = new VDBRepository();
		repo.setSystemFunctionManager(new SystemFunctionManager());
		repo.start();
		MetadataStore store = new MetadataStore();
		LinkedHashMap<String, VDBResources.Resource> visibilityMap = new LinkedHashMap<String, VDBResources.Resource>();
		UDFMetaData udfMetaData = new UDFMetaData();
		udfMetaData.setFunctionClassLoader(Thread.currentThread().getContextClassLoader());
		repo.addVDB(vdb, store, visibilityMap, udfMetaData, cmr, false);
		
		
		/**
		 * 5. loadMetadata
		 */
		VDBResources vdbResources = null;
		AtomicInteger loadCount = new AtomicInteger(2);
		for (ModelMetaData model: vdb.getModelMetaDatas().values()) {
			MetadataRepository metadataRepository = model.getAttachment(MetadataRepository.class);
			loadMetadata(repo, vdb, model, cmr, metadataRepository, store, loadCount, vdbResources);
		}
	}

	@SuppressWarnings("unchecked")
	private static void loadMetadata(VDBRepository repo, VDBMetaData vdb, ModelMetaData model, ConnectorManagerRepository cmr, MetadataRepository metadataRepository, MetadataStore store, AtomicInteger loadCount, VDBResources vdbResources) throws TranslatorException {

		MetadataFactory factory = createMetadataFactory(repo, vdb, model, vdbResources==null?Collections.EMPTY_MAP:vdbResources.getEntriesPlusVisibilities());
	
		ExecutionFactory ef = null;
		Object cf = null;
		
		Exception te = null;
		for (ConnectorManager cm : getConnectorManagers(model, cmr)) {
			
			if (cm != null) {
				ef = cm.getExecutionFactory();
				cf = cm.getConnectionFactory();
			}
			
			if (LogManager.isMessageToBeRecorded(LogConstants.CTX_RUNTIME, MessageLevel.TRACE)) {
				LogManager.logTrace(LogConstants.CTX_RUNTIME, "CREATE SCHEMA", factory.getSchema().getName(), ";\n", DDLStringVisitor.getDDLString(factory.getSchema(), null, null)); //$NON-NLS-1$ //$NON-NLS-2$
			}
			
			metadataRepository.loadMetadata(factory, ef, cf);
			
			logMetadata(metadataRepository, factory);
			
			break;
		}
		
		metadataLoaded(vdb, model, store, loadCount, factory, true, false);
	}

	private static void logMetadata(MetadataRepository metadataRepository, MetadataFactory factory) {
		logger.info(metadataRepository.getClass().getName() + " logging tables");
	
		ColumnMetaData[] meta = ColumnMetaData.Factory.create(ColumnMetaData.ALIGN_LEFT, "UUID", "Name", "FullName", "TableType", "UpdatePlan", "SQLString", "SourceName", "Materialized", "SelectTransformation", "Properties");
		TableRenderer renderer = new TableRenderer(meta);
		for(Table table : factory.getSchema().getTables().values()) {
			Column[] row = Column.Factory.create(table.getUUID(), table.getName(), table.getFullName(), table.getTableType().toString(), table.getUpdatePlan(), 
					table.getSQLString(), table.getSourceName(), table.isMaterialized() + "", table.getSelectTransformation(), table.getProperties().toString());
			renderer.addRow(row);
		}
		renderer.renderer();
		
		logger.info(metadataRepository.getClass().getName() + " logging procedures");
		
		meta = ColumnMetaData.Factory.create(ColumnMetaData.ALIGN_LEFT, "UUID", "Name", "SQLString");
		renderer = new TableRenderer(meta);
		for(Procedure procedure : factory.getSchema().getProcedures().values()) {
			Column[] row = Column.Factory.create(procedure.getUUID(), procedure.getName(), procedure.getSQLString());
			renderer.addRow(row);
		}
		renderer.renderer();
	}

	private static void metadataLoaded(VDBMetaData vdb, ModelMetaData model, MetadataStore vdbMetadataStore, AtomicInteger loadCount, MetadataFactory factory, boolean success, boolean reloading) {
		
		if (success) {
			factory.mergeInto(vdbMetadataStore);
			model.clearRuntimeMessages();
			model.setMetadataStatus(Model.MetadataStatus.LOADED);
		} else {
			model.setMetadataStatus(Model.MetadataStatus.FAILED);
			vdb.setStatus(Status.FAILED);
		}
		
	}

	private static List<ConnectorManager> getConnectorManagers(ModelMetaData model, ConnectorManagerRepository cmr) {
		if (model.isSource()){
			Collection<SourceMappingMetadata> mappings = model.getSources().values();
			List<ConnectorManager> result = new ArrayList<ConnectorManager>(mappings.size());
			for (SourceMappingMetadata mapping:mappings) {
				result.add(cmr.getConnectorManager(mapping.getName()));
			}
			return result;
		}
		
		return Collections.singletonList(null);
	}

	private static MetadataFactory createMetadataFactory(VDBRepository repo, VDBMetaData vdb, ModelMetaData model, Map<String, ? extends VDBResource> vdbResources) {
		
		Map<String, Datatype> datatypes = repo.getRuntimeTypeMap();
		MetadataFactory factory = new MetadataFactory(vdb.getName(), vdb.getVersion(), datatypes, model);
		factory.setBuiltinDataTypes(repo.getSystemStore().getDatatypes());
		factory.getSchema().setPhysical(model.isSource());
		factory.setParser(new QueryParser());
		factory.setVdbResources(vdbResources);
		return factory;
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
		
		if (model.getModelType() == ModelMetaData.Type.PHYSICAL) {
			repos.add(new DirectQueryMetadataRepository());
		}
		
		if (model.getModelType() == ModelMetaData.Type.VIRTUAL) {
			repos.add(new MaterializationMetadataRepository());
		}
		
		if (repos.size() == 1) {
			return repos.get(0);
		}
		
		return new ChainingMetadataRepository(repos);
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
