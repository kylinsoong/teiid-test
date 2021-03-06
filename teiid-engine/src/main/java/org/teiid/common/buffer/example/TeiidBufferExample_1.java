package org.teiid.common.buffer.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.teiid.common.buffer.BufferManager;
import org.teiid.common.buffer.BufferManager.TupleSourceType;
import org.teiid.common.buffer.BufferManagerFactory;
import org.teiid.common.buffer.TupleBuffer;
import org.teiid.common.buffer.TupleBuffer.TupleBufferTupleSource;
import org.teiid.core.TeiidComponentException;
import org.teiid.core.TeiidProcessingException;
import org.teiid.core.types.DataTypeManager;
import org.teiid.query.sql.symbol.ElementSymbol;

public class TeiidBufferExample_1 {

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
        buffer.addTuple(Arrays.asList(100, "IBM"));
        buffer.addTuple(Arrays.asList(101, "DELL"));
        buffer.addTuple(Arrays.asList(102, "HPQ"));
        buffer.addTuple(Arrays.asList(103, "GE"));
        buffer.addTuple(Arrays.asList(104, "SAP"));
        buffer.addTuple(Arrays.asList(105, "TM"));

        TupleBufferTupleSource tupleSource = buffer.createIndexedTupleSource();
        tupleSource.setReverse(true);
        while(tupleSource.hasNext()) {
            System.out.println(tupleSource.nextTuple());
        }
        tupleSource.closeSource();

    }

}
