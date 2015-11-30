package org.teiid.test.embedded.plan;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;
import javax.xml.stream.XMLStreamException;

import org.h2.tools.RunScript;
import org.teiid.adminapi.Model;
import org.teiid.adminapi.impl.ModelMetaData;
import org.teiid.adminapi.impl.VDBMetaData;
import org.teiid.adminapi.impl.VDBMetadataParser;
import org.teiid.core.id.IDGenerator;
import org.teiid.deployers.UDFMetaData;
import org.teiid.dqp.internal.process.CachedFinder;
import org.teiid.example.EmbeddedHelper;
import org.teiid.metadata.Datatype;
import org.teiid.metadata.MetadataFactory;
import org.teiid.metadata.MetadataRepository;
import org.teiid.metadata.MetadataStore;
import org.teiid.metadata.VDBResource;
import org.teiid.query.analysis.AnalysisRecord;
import org.teiid.query.function.FunctionTree;
import org.teiid.query.function.SystemFunctionManager;
import org.teiid.query.metadata.ChainingMetadataRepository;
import org.teiid.query.metadata.CompositeMetadataStore;
import org.teiid.query.metadata.DDLMetadataRepository;
import org.teiid.query.metadata.DirectQueryMetadataRepository;
import org.teiid.query.metadata.MaterializationMetadataRepository;
import org.teiid.query.metadata.NativeMetadataRepository;
import org.teiid.query.metadata.QueryMetadataInterface;
import org.teiid.query.metadata.SystemMetadata;
import org.teiid.query.metadata.TempCapabilitiesFinder;
import org.teiid.query.metadata.TempMetadataAdapter;
import org.teiid.query.metadata.TransformationMetadata;
import org.teiid.query.metadata.VDBResources;
import org.teiid.query.optimizer.capabilities.CapabilitiesFinder;
import org.teiid.query.optimizer.relational.plantree.NodeConstants;
import org.teiid.query.optimizer.relational.plantree.NodeEditor;
import org.teiid.query.optimizer.relational.plantree.NodeFactory;
import org.teiid.query.optimizer.relational.plantree.PlanNode;
import org.teiid.query.parser.ParseInfo;
import org.teiid.query.parser.QueryParser;
import org.teiid.query.resolver.QueryResolver;
import org.teiid.query.rewriter.QueryRewriter;
import org.teiid.query.sql.LanguageObject.Util;
import org.teiid.query.sql.lang.Command;
import org.teiid.query.sql.lang.FromClause;
import org.teiid.query.sql.lang.Query;
import org.teiid.query.sql.lang.SubqueryContainer;
import org.teiid.query.sql.lang.UnaryFromClause;
import org.teiid.query.sql.lang.WithQueryCommand;
import org.teiid.query.sql.symbol.Expression;
import org.teiid.query.sql.symbol.GroupSymbol;
import org.teiid.query.sql.visitor.GroupsUsedByElementsVisitor;
import org.teiid.query.tempdata.TempTableStore;
import org.teiid.query.tempdata.TempTableStore.TransactionMode;
import org.teiid.query.util.CommandContext;
import org.teiid.resource.adapter.file.FileManagedConnectionFactory;
import org.teiid.translator.ExecutionFactory;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.file.FileExecutionFactory;
import org.teiid.translator.jdbc.h2.H2ExecutionFactory;

public class GeneratePlanDebug {
    
    static Object filecf = null;
    static Object h2cf = null;
    static FileExecutionFactory fileef = null;
    static H2ExecutionFactory h2ef = null;
    
    static Map<String, ExecutionFactory> efMap = new HashMap<>();   
    static Map<String, Object> cfMap = new HashMap<>();
    
    static {
        try {
            FileManagedConnectionFactory filemcf = new FileManagedConnectionFactory();
            filemcf.setParentDirectory("src/main/resources/data");
            filecf = filemcf.createConnectionFactory();
            cfMap.put("MarketData", filecf);
            
            fileef = new FileExecutionFactory();
            fileef.start();
            efMap.put("MarketData", fileef);
            
            DataSource ds = EmbeddedHelper.newDataSource("org.h2.Driver", "jdbc:h2:mem://localhost/~/account", "sa", "sa");
            RunScript.execute(ds.getConnection(), new InputStreamReader(ShowPlanSimple.class.getClassLoader().getResourceAsStream("data/customer-schema.sql")));
            h2cf = ds;
            cfMap.put("Accounts", h2cf);
            
            h2ef = new H2ExecutionFactory() ;
            h2ef.setSupportsDirectQueryProcedure(true);
            h2ef.start();
            efMap.put("Accounts", h2ef);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    static QueryMetadataInterface metadata = null;
    static CapabilitiesFinder capabilitiesFinder = null;
    
    public static void main(String[] args) throws Exception {
        
        initMetadata();
        
        CommandContext context = new CommandContext("1", "anonymous@teiid-security", null, "Portfolio", 1, true);
        IDGenerator idGenerator = new IDGenerator();
        
        QueryParser queryParser = QueryParser.getQueryParser();
        
        Command command = queryParser.parseCommand("SELECT * from Product", new ParseInfo());
        
        AnalysisRecord analysisRecord = new AnalysisRecord(true, true);
        
        QueryResolver.resolveCommand(command, metadata);
        
//        Command userCommand = (Command) command.clone();
        
        command = QueryRewriter.rewrite(command, metadata, context);
        
        analysisRecord.println("\n============================================================================");
        analysisRecord.println("USER COMMAND:\n" + command);
        
        analysisRecord.println("\n----------------------------------------------------------------------------");
        analysisRecord.println("OPTIMIZE: \n" + command);
        
        analysisRecord.println("\n----------------------------------------------------------------------------"); 
        analysisRecord.println("GENERATE CANONICAL: \n" + command);
        
        PlanNode plan = createQueryPlan((Query)command, null);
        
        analysisRecord.println("\nCANONICAL PLAN: \n" + plan);
        
        LinkedHashMap<String, WithQueryCommand> pushdownWith = new LinkedHashMap<String, WithQueryCommand>();
        for (PlanNode node : NodeEditor.findAllNodes(plan, NodeConstants.Types.PROJECT | NodeConstants.Types.SELECT | NodeConstants.Types.JOIN | NodeConstants.Types.SOURCE | NodeConstants.Types.GROUP | NodeConstants.Types.SORT)){
            node.addGroups(GroupsUsedByElementsVisitor.getGroups(node.getCorrelatedReferenceElements()));
        }
        
        List<Expression> topCols = Util.deepClone(command.getProjectedSymbols(), Expression.class);
        
//        System.out.println(analysisRecord.getDebugLog());
    }

    private static Set<GroupSymbol> getGroupSymbols(PlanNode plan) {
        Set<GroupSymbol> groupSymbols = new HashSet<GroupSymbol>();
        for (PlanNode source : NodeEditor.findAllNodes(plan, NodeConstants.Types.SOURCE | NodeConstants.Types.GROUP, NodeConstants.Types.GROUP)) {
            groupSymbols.addAll(source.getGroups());
        }
        return groupSymbols;
    }

    private static PlanNode createQueryPlan(Query command, Object object) {
        
        PlanNode plan = null;
        
        FromClause fromClause = command.getFrom().getClauses().get(0);
        PlanNode dummyRoot = new PlanNode();
        if(fromClause instanceof UnaryFromClause){
            UnaryFromClause ufc = (UnaryFromClause)fromClause;
            GroupSymbol group = ufc.getGroup();
            PlanNode node = NodeFactory.getNewNode(NodeConstants.Types.SOURCE);
            node.addGroup(group);
            dummyRoot.addLastChild(node);
        }
        
        plan = dummyRoot.getFirstChild();
        
        List<? extends Expression> select = command.getSelect().getProjectedSymbols();
        PlanNode projectNode = NodeFactory.getNewNode(NodeConstants.Types.PROJECT);
        projectNode.setProperty(NodeConstants.Info.PROJECT_COLS, select);
        projectNode.addGroups(GroupsUsedByElementsVisitor.getGroups(select));
        projectNode.addLastChild(plan);
        plan = projectNode;
        
        return plan;
    }

    private static void initMetadata() throws XMLStreamException, TranslatorException {
        
        VDBMetaData vdb = VDBMetadataParser.unmarshell(GeneratePlanDebug.class.getClassLoader().getResourceAsStream("portfolio-vdb.xml"));
        vdb.setXmlDeployment(true);
        
        MetadataStore store = new MetadataStore();
        AtomicInteger loadCount = new AtomicInteger(3);
        
        for (ModelMetaData model: vdb.getModelMetaDatas().values()){
            MetadataRepository<?, ?> repo = getMetadataRepository(vdb, model);
            model.addAttchment(MetadataRepository.class, repo);
        }
        
        for (ModelMetaData model: vdb.getModelMetaDatas().values()) {
            MetadataRepository<?,?> metadataRepository = model.getAttachment(MetadataRepository.class);
            if (model.getModelType() == Model.Type.PHYSICAL || model.getModelType() == Model.Type.VIRTUAL){
                loadMetadata(vdb, model, metadataRepository, store, loadCount);
            }
        }
        
        metadata = vdb.getAttachment(QueryMetadataInterface.class); 
        TempMetadataAdapter tma = new TempMetadataAdapter(metadata, new TempTableStore("1", TransactionMode.ISOLATE_WRITES).getMetadataStore());
        tma.setSession(true);
        metadata = tma;
        
        capabilitiesFinder = new CachedFinder(null, vdb);
        capabilitiesFinder = new TempCapabilitiesFinder(capabilitiesFinder);
         
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static void loadMetadata(VDBMetaData vdb, ModelMetaData model,MetadataRepository metadataRepository, MetadataStore store, AtomicInteger loadCount) throws TranslatorException {

        MetadataFactory factory = createMetadataFactory(vdb, model, Collections.EMPTY_MAP);
        ExecutionFactory ef = efMap.get(model.getName());
        Object cf = cfMap.get(model.getName());
        metadataRepository.loadMetadata(factory, ef, cf);
        factory.mergeInto(store);
        model.clearRuntimeMessages();
        model.setMetadataStatus(Model.MetadataStatus.LOADED);
        int load = loadCount.decrementAndGet();
        if(load == 0) {
            LinkedHashMap<String, VDBResources.Resource> visibilityMap = new LinkedHashMap<String, VDBResources.Resource>();
            UDFMetaData udfMetaData = new UDFMetaData();
            udfMetaData.setFunctionClassLoader(Thread.currentThread().getContextClassLoader());
            Collection <FunctionTree> udfs = new ArrayList<FunctionTree>();
            CompositeMetadataStore compositeStore = new CompositeMetadataStore(store);
            TransformationMetadata metadata =  new TransformationMetadata(vdb, compositeStore, visibilityMap, new SystemFunctionManager().getSystemFunctions(), udfs);
            metadata.setUseOutputNames(false);
            metadata.setWidenComparisonToString(false);
            QueryMetadataInterface qmi = metadata;
            vdb.addAttchment(QueryMetadataInterface.class, qmi);
        }
    }

    static MetadataFactory createMetadataFactory(VDBMetaData vdb,ModelMetaData model, Map<String, ? extends VDBResource> vdbResources) {
        Map<String, Datatype> datatypes = SystemMetadata.getInstance().getRuntimeTypeMap();
        MetadataFactory factory = new MetadataFactory(vdb.getName(), vdb.getVersion(), datatypes, model);
        factory.setBuiltinDataTypes(SystemMetadata.getInstance().getSystemStore().getDatatypes());
        factory.getSchema().setPhysical(model.isSource());
        factory.setParser(new QueryParser());
        factory.setVdbResources(vdbResources);
        return factory;
    }

    private static MetadataRepository<?, ?> getMetadataRepository(VDBMetaData vdb, ModelMetaData model) {
        
        if (model.getSourceMetadataType().isEmpty() && model.isSource()) {
            return new ChainingMetadataRepository(Arrays.asList(new NativeMetadataRepository(), new DirectQueryMetadataRepository()));
        }
        
        ConcurrentSkipListMap<String, MetadataRepository<?, ?>> repositories = new ConcurrentSkipListMap<String, MetadataRepository<?, ?>>(String.CASE_INSENSITIVE_ORDER);
        repositories.put("ddl", new DDLMetadataRepository()); 
        
        List<MetadataRepository<?, ?>> repos = new ArrayList<MetadataRepository<?,?>>(2);
        for (int i = 0; i < model.getSourceMetadataType().size(); i++){
            String schemaTypes = model.getSourceMetadataType().get(i);
            StringTokenizer st = new StringTokenizer(schemaTypes, ","); 
            while (st.hasMoreTokens()){
                String repoType = st.nextToken().trim();
                MetadataRepository<?, ?> current = repositories.get(repoType);
                if (model.getSourceMetadataText().size() > i){
                    current = new MetadataRepositoryWrapper(current, model.getSourceMetadataText().get(i));
                }
                repos.add(current);
            }
        }
        
        if (model.getModelType() == ModelMetaData.Type.VIRTUAL) {
            repos.add(new MaterializationMetadataRepository());
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
        public void loadMetadata(MetadataFactory factory,
                ExecutionFactory<F, C> executionFactory, F connectionFactory) throws TranslatorException {
            repo.loadMetadata(factory, executionFactory, connectionFactory, this.text);
        }
        
    };
}
