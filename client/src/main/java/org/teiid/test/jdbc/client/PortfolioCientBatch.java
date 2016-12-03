package org.teiid.test.jdbc.client;

import java.util.Arrays;


public class PortfolioCientBatch {
    

    public static void main(String[] args) throws Exception {
                
        Thread[] threads = new Thread[10];
        
        for(int i = 0 ; i < 10 ; i ++) {
            threads[i] = new Thread(() -> {
                for(int j = 0 ; j < 1000 ; j ++) {
                    try {
                        PortfolioCient.main(args);
                    } catch (Exception e) {

                    }
                }
            }); 
        }
        
        Arrays.stream(threads).forEach(t -> t.start());
        
        Arrays.stream(threads).forEach(t -> {
            try {
                t.join();
            } catch (Exception e) {
            }
        });
    }

}
