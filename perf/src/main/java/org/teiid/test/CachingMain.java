package org.teiid.test;

import org.jboss.teiid.test.mat.ExternalMaterialization;
import org.jboss.teiid.test.rsCaching.ResultsCaching;
import org.jboss.teiid.test.rsCaching.TranslatorResultsCaching;
import org.teiid.test.util.TableRenderer;
import org.teiid.test.util.TableRenderer.Column;
import org.teiid.test.util.TableRenderer.ColumnMetaData;

public class CachingMain {
    
    private static final int LENGTH = 150;

	public static void main(String[] args) throws Exception {
	    
	    resultsCachingExample();
	    
	    externalMatExample();
	    
	    translatorResultsCachingExample();

	}

    private static void translatorResultsCachingExample() throws Exception {

        ColumnMetaData[] meta = ColumnMetaData.Factory.create(ColumnMetaData.ALIGN_LEFT, formTxt("Teiid Translator Results Caching Example"));
        TableRenderer table = new TableRenderer(meta);
        StringBuffer sb = new StringBuffer();
        sb.append("This example demonstrate how Translators can contribute cache entries into the result set cache via the use of the CacheDirective object");
        table.addRow(Column.Factory.create(sb.toString()));
        table.renderer();
        Util.print("");
        TranslatorResultsCaching.main(null);
    }

    private static void resultsCachingExample() throws Exception {

        ColumnMetaData[] meta = ColumnMetaData.Factory.create(ColumnMetaData.ALIGN_LEFT, formTxt("Teiid Results Caching Example"));
        TableRenderer table = new TableRenderer(meta);
        StringBuffer sb = new StringBuffer();
        sb.append("This Example will demonstrate the following:");
        sb.append("\n");
        sb.append("* How to define a translator override to support native queries");
        sb.append("\n");
        sb.append("* How to enable Teiid cache the results of specific user query");
        sb.append("\n");
        sb.append("* A Comparison results(native query, query without cache, query with cache) shows enable Results Caching have thousands of performance improve");
        table.addRow(Column.Factory.create(sb.toString()));
        table.renderer();
        Util.print("");
        ResultsCaching.main(null);
    }

    private static void externalMatExample() throws Exception {

        ColumnMetaData[] meta = ColumnMetaData.Factory.create(ColumnMetaData.ALIGN_LEFT, formTxt("Teiid External Materialization Example"));
        TableRenderer table = new TableRenderer(meta);
        StringBuffer sb = new StringBuffer();
        sb.append("This Example will demonstrate the following:");
        sb.append("\n");
        sb.append("* How to define a translator override to support native queries");
        sb.append("\n");
        sb.append("* How to define a materialized VIEW using DDL and load the external materialized table");
        sb.append("\n");
        sb.append("* How to test and verify materialized VIEW catch up with external table's change");
        table.addRow(Column.Factory.create(sb.toString()));
        table.renderer();
        ExternalMaterialization.main(null);
    }

    private static String formTxt(String txt) {
        int size = txt.length();
        for(int i = size; i < LENGTH ; i ++){
            txt += " ";
        }
        return txt;
    }

}
