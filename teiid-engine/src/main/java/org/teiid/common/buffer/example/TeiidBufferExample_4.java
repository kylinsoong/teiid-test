package org.teiid.common.buffer.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.teiid.common.buffer.BufferManager;
import org.teiid.common.buffer.BufferManagerFactory;
import org.teiid.common.buffer.TupleBatch;
import org.teiid.common.buffer.TupleBuffer;
import org.teiid.common.buffer.BufferManager.TupleSourceType;
import org.teiid.core.TeiidComponentException;
import org.teiid.core.types.DataTypeManager;
import org.teiid.query.sql.symbol.ElementSymbol;

public class TeiidBufferExample_4 {
    
    public static void main(String[] args) throws TeiidComponentException {
        
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
        buffer.setBatchSize(1);
        
        TupleBatch batch = new TupleBatch(1, Arrays.asList(Arrays.asList(100, "IBM"), Arrays.asList(101, "DELL"), Arrays.asList(102, "HPQ")));
        batch.setTerminationFlag(true);
        
        buffer.addTupleBatch(batch, true);
        
        System.out.println("DONE");
        
    }

}
