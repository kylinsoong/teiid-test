package org.teiid.test.embedded.plan;

public class TmpTester {

    public static void main(String[] args) {
        
        int a = 1 << 2;
        int b = 1 << 3;
        int c = 1 << 4;
        int d = 1 << 5;
        int e = 1 << 6;
        int f = 1 << 7;
        int g = a | b | c | d | e | f;
        int h = b & g;
        int i = e & g;
        
        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
        System.out.println(d);
        System.out.println(e);
        System.out.println(f);
        System.out.println(g);
        System.out.println(h);
        System.out.println(i);
    }

}
