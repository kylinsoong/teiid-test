package org.teiid.test;

public class RoughTest {

	public static void main(String[] args) {

		String a = new String("123");
		String b = a;
		a = null;
		System.out.println(b);
	}

}
