package org.teiid.test.teiid4515;

public class Test {

    public static void main(String[] args) {

        int a = 1 << 20;
        int b = 1 << 21;
        int c = 1 << 28;
        
        System.out.println(a);
        System.out.println(b / a);
        System.out.println(c / a);
    }

}
