package org.teiid.test.embedded;

import static org.teiid.test.Constants.H2_JDBC_DRIVER;
import static org.teiid.test.Constants.H2_JDBC_PASS;
import static org.teiid.test.Constants.H2_JDBC_URL;
import static org.teiid.test.Constants.H2_JDBC_USER;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.resource.ResourceException;

import org.teiid.adminapi.impl.ModelMetaData;
import org.teiid.adminapi.impl.VDBMetaData;
import org.teiid.example.EmbeddedHelper;
import org.teiid.metadata.Datatype;
import org.teiid.metadata.MetadataFactory;
import org.teiid.metadata.Procedure;
import org.teiid.metadata.Table;
import org.teiid.query.metadata.SystemMetadata;
import org.teiid.query.parser.QueryParser;
import org.teiid.test.util.TableRenderer;
import org.teiid.test.util.TableRenderer.Column;
import org.teiid.test.util.TableRenderer.ColumnMetaData;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.jdbc.JDBCMetdataProcessor;

public class TestJDBCMetdataProcessor {

	public static void main(String[] args) throws SQLException, ResourceException, TranslatorException {

		VDBMetaData vdb = new VDBMetaData();
		
		vdb.setName("TestVDB");
		vdb.setVersion(1);
		vdb.addProperty("UseConnectorMetadata", "true");
		
		ModelMetaData model = new ModelMetaData();
		model.setName("Accounts");
		model.setModelType("PHYSICAL");
		model.setVisible(true);
		model.setPath(null);
		model.addProperty("importer.useFullSchemaName", "false");
		model.addSourceMapping("h2-connector", "translator-h2", "java:/accounts-ds");
		vdb.addModel(model);
		
		Map<String, Datatype> datatypes = SystemMetadata.getInstance().getRuntimeTypeMap();
		MetadataFactory factory = new MetadataFactory(vdb.getName(), vdb.getVersion(), datatypes, model);
		factory.setBuiltinDataTypes(datatypes);
		factory.getSchema().setPhysical(model.isSource());
		factory.setParser(new QueryParser());
		
		Connection conn = EmbeddedHelper.newDataSource(H2_JDBC_DRIVER, H2_JDBC_URL, H2_JDBC_USER, H2_JDBC_PASS).getConnection();
		
		JDBCMetdataProcessor processor = new JDBCMetdataProcessor();
		processor.process(factory, conn);
		
		dumpTables(factory.getSchema().getTables());
		dumpProcedures(factory.getSchema().getProcedures());
	}

	private static void dumpProcedures(Map<String, Procedure> procedures) {
		
		System.out.println(procedures);
		
		for(Procedure proc : procedures.values()) {
			System.out.println(proc);
		}
		
	}

	private static void dumpTables(Map<String, Table> map) {
		ColumnMetaData[] meta = ColumnMetaData.Factory.create(ColumnMetaData.ALIGN_LEFT, "Name", "TableType", "Materialized");
		TableRenderer renderer = new TableRenderer(meta);
		for(Table table : map.values()) {
			Column[] row = Column.Factory.create(table.getName(),  table.getTableType().toString(), table.isMaterialized() + "");
			renderer.addRow(row);
		}
		renderer.renderer();
		
	}

	static void dumpTables_(Map<String, Table> map) {

		ColumnMetaData[] meta = ColumnMetaData.Factory.create(ColumnMetaData.ALIGN_LEFT, "UUID", "Name", "FullName", "TableType", "UpdatePlan", "SQLString", "SourceName", "Materialized", "SelectTransformation", "Properties");
		TableRenderer renderer = new TableRenderer(meta);
		for(Table table : map.values()) {
			Column[] row = Column.Factory.create(table.getUUID(), table.getName(), table.getFullName(), table.getTableType().toString(), table.getUpdatePlan(), 
					table.getSQLString(), table.getSourceName(), table.isMaterialized() + "", table.getSelectTransformation(), table.getProperties().toString());
			renderer.addRow(row);
		}
		renderer.renderer();
	}
	
	

}
