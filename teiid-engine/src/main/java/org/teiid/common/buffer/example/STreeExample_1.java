package org.teiid.common.buffer.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.teiid.common.buffer.BufferManager;
import org.teiid.common.buffer.BufferManagerFactory;
import org.teiid.common.buffer.STree;
import org.teiid.common.buffer.TupleBatch;
import org.teiid.common.buffer.STree.InsertMode;
import org.teiid.core.TeiidComponentException;
import org.teiid.core.types.DataTypeManager;
import org.teiid.query.sql.symbol.ElementSymbol;

public class STreeExample_1 {

    public static void main(String[] args) throws TeiidComponentException {

        BufferManager bm = BufferManagerFactory.getStandaloneBufferManager();
        List<ElementSymbol> columns = new ArrayList<>();
        ElementSymbol id = new ElementSymbol("Test.PRODUCTView.product_id");
        id.setType(DataTypeManager.DefaultDataClasses.INTEGER);
        ElementSymbol symbol = new ElementSymbol("Test.PRODUCTView.symbol");
        symbol.setType(DataTypeManager.DefaultDataClasses.STRING);
        columns.add(id);
        columns.add(symbol);
        STree tree = bm.createSTree(columns, "sessionID", 1);
        
        TupleBatch batch = new TupleBatch(1, Arrays.asList(Arrays.asList(100, "IBM"), Arrays.asList(101, "DELL"), Arrays.asList(102, "HPQ")));
        batch.setTerminationFlag(true);
        int sourceRow = 1;
        while (true){
            if(batch.getRowCount() > 0 && sourceRow <= batch.getEndRow()){
                List<?> tuple = batch.getTuple(sourceRow);
                sourceRow++ ;
                tree.insert(tuple, InsertMode.NEW, -1);
            }
            
            if(sourceRow > batch.getEndRow()) {
                break;
            }
        }
        tree.setBatchInsert(false);
        tree.compact();
        System.out.println(tree.getRowCount());
    }

}
