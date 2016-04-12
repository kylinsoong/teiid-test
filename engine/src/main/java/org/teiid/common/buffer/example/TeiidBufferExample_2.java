package org.teiid.common.buffer.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.teiid.common.buffer.BufferManager;
import org.teiid.common.buffer.BufferManager.TupleSourceType;
import org.teiid.common.buffer.BufferManagerFactory;
import org.teiid.common.buffer.TupleBatch;
import org.teiid.common.buffer.TupleBuffer;
import org.teiid.common.buffer.TupleBuffer.TupleBufferTupleSource;
import org.teiid.core.TeiidComponentException;
import org.teiid.core.TeiidProcessingException;
import org.teiid.core.types.DataTypeManager;
import org.teiid.query.sql.symbol.ElementSymbol;

public class TeiidBufferExample_2 {

    public static void main(String[] args) throws TeiidComponentException, TeiidProcessingException {

        BufferManager bm = BufferManagerFactory.getStandaloneBufferManager();
        List<ElementSymbol> elements = new ArrayList<>();
        ElementSymbol id = new ElementSymbol("Test.PRODUCTView.product_id");
        id.setType(DataTypeManager.DefaultDataClasses.INTEGER);
        ElementSymbol symbol = new ElementSymbol("Test.PRODUCTView.symbol");
        symbol.setType(DataTypeManager.DefaultDataClasses.STRING);
        elements.add(id);
        elements.add(symbol);
                
        TupleBuffer buffer = bm.createTupleBuffer(elements, "ConnectionId", TupleSourceType.PROCESSOR);
        buffer.setForwardOnly(false);
        TupleBatch batch = new TupleBatch(1, Arrays.asList(Arrays.asList(100, "IBM"), Arrays.asList(101, "DELL"), Arrays.asList(102, "HPQ"), Arrays.asList(103, "GE"), Arrays.asList(104, "SAP"), Arrays.asList(105, "TM")));
        buffer.addTupleBatch(batch, true);
        
        TupleBufferTupleSource tupleSource = buffer.createIndexedTupleSource();
        tupleSource.setReverse(true);   
        while(tupleSource.hasNext()) {
            System.out.println(tupleSource.nextTuple());
        }
        tupleSource.closeSource();

//        TupleBatch batch = resultsBuffer.getBatch(0);
//        for(int i = 0 ; i < batch.getRowCount() ; i ++) {
//            System.out.println(batch.getTuple(i));
//        }
    }

}
