package org.weld.se.example;

public class TestClassLoader extends ClassLoader {

    public TestClassLoader(ClassLoader parent) {
        super(parent);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        System.out.println("load class " + name);
        return super.loadClass(name);
    }

}
