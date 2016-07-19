package org.teiid.test.teiid3825.lambda1;

import java.util.ArrayList;
import java.util.List;

public class TestMain {
    
    static void test(Work work){
        work.doWork(null);
    }

    public static void main(String[] args) {

        List<Work> lists = new ArrayList<>();
        lists.add(people -> {
            people.action();
        });
        
        System.out.println(lists);
    }

}
