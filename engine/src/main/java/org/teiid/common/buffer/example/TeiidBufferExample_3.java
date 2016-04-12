package org.teiid.common.buffer.example;

import java.util.Arrays;
import java.util.List;

import org.teiid.common.buffer.TupleBatch;

public class TeiidBufferExample_3 {

    public static void main(String[] args) {

        TupleBatch batch = new TupleBatch(1, Arrays.asList(Arrays.asList(100, "IBM"), Arrays.asList(101, "DELL"), Arrays.asList(102, "HPQ")));
        batch.setTerminationFlag(true);
        long sourceRow = 1;
        while (true){
            if(batch.getRowCount() > 0 && sourceRow <= batch.getEndRow()){
                List<?> tuple = batch.getTuple(sourceRow);
                sourceRow++ ;
                System.out.println(tuple);
            }
            if(sourceRow > batch.getEndRow()) {
                break;
            }
        }     
        
        System.out.println(batch.getBeginRow());
        System.out.println(batch.getEndRow());
    }

}
