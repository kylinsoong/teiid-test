package org.teiid.test.embedded.metadata;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.teiid.adminapi.Model;
import org.teiid.adminapi.impl.ModelMetaData;
import org.teiid.adminapi.impl.VDBMetaData;
import org.teiid.metadata.Datatype;
import org.teiid.metadata.MetadataException;
import org.teiid.metadata.MetadataFactory;
import org.teiid.metadata.MetadataStore;
import org.teiid.metadata.Schema;
import org.teiid.query.function.FunctionTree;
import org.teiid.query.function.SystemFunctionManager;
import org.teiid.query.function.UDFSource;
import org.teiid.query.metadata.CompositeMetadataStore;
import org.teiid.query.metadata.QueryMetadataInterface;
import org.teiid.query.metadata.SystemMetadata;
import org.teiid.query.metadata.TransformationMetadata;
import org.teiid.query.parser.QueryParser;

public class MetadataUtils {
    
    
    public static TransformationMetadata queryMetadataInterface() throws MetadataException, FileNotFoundException {
        
        ModelMetaData mmd = new ModelMetaData();
        mmd.setName("TestModel");
        Map<String, Datatype> runtimeTypes = SystemMetadata.getInstance().getRuntimeTypeMap();
        MetadataFactory mf = new MetadataFactory("hbase", 1, runtimeTypes, mmd);
        mf.setParser(new QueryParser());
        mf.parse(new FileReader("src/main/resources/customer.ddl"));
        
        MetadataStore metadataStore = mf.asMetadataStore();
        CompositeMetadataStore cms = new CompositeMetadataStore(metadataStore);
        
        VDBMetaData vdbMetaData = new VDBMetaData();
        vdbMetaData.setName("TestVDB");
        vdbMetaData.setVersion(1);
        
        List<FunctionTree> udfs = new ArrayList<FunctionTree>();
        for (Schema schema : cms.getSchemas().values()){
            vdbMetaData.addModel(createModel(schema.getName(), schema.isPhysical()));
            
            if (!schema.getFunctions().isEmpty()) {
                udfs.add(new FunctionTree(schema.getName(), new UDFSource(schema.getFunctions().values()), true));
            }
            
            if (!schema.getProcedures().isEmpty()) {
                FunctionTree ft = FunctionTree.getFunctionProcedures(schema);
                if (ft != null) {
                    udfs.add(ft);
                }
            }
        }
        
        SystemFunctionManager sfm = new SystemFunctionManager();
        FunctionTree systemFunctions = sfm.getSystemFunctions();
        TransformationMetadata metadata = new TransformationMetadata(vdbMetaData, cms, null, systemFunctions, udfs);
        vdbMetaData.addAttchment(TransformationMetadata.class, metadata);
        vdbMetaData.addAttchment(QueryMetadataInterface.class, metadata);
        return metadata;
    }
    
    public static ModelMetaData createModel(String name, boolean source) {
        ModelMetaData model = new ModelMetaData();
        model.setName(name);
        if (source) {
            model.setModelType(Model.Type.PHYSICAL);
        }
        else {
            model.setModelType(Model.Type.VIRTUAL);
        }
        model.setVisible(true);
        model.setSupportsMultiSourceBindings(false);
        model.addSourceMapping(name, name, null);
        
        return model;
    }

}
