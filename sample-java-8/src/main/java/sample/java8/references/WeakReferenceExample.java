package sample.java8.references;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.WeakHashMap;

public class WeakReferenceExample {
    
    public static class Employee {
        
        String name;

        public Employee(String name) {
            this.name = name;
        }
    }
    
    public static class EmployeeVal {
        
        String name;

        public EmployeeVal(String name) {
            this.name = name;
        }
    }

    public static void main(String[] args) {
        
//        test_1();
        
//        test_2();
        
        test_3();

    }

    static void test_3() {
        HashMap<Employee, EmployeeVal> map = new HashMap<Employee, EmployeeVal>();
        WeakReference<HashMap<Employee, EmployeeVal>> aMap = new WeakReference<HashMap<Employee, EmployeeVal>>(map);
        map = null;
        while (null != aMap.get()){
            aMap.get().put(new Employee("Vinoth"), new EmployeeVal("Programmer"));
            System.out.println("Size of aMap " + aMap.get().size());
            System.gc();
        }
        System.out.println("Its garbage collected");
    }

    static void test_2() {

        WeakHashMap<Employee, EmployeeVal> aMap = new WeakHashMap<Employee, EmployeeVal>();
        
        Employee emp = new Employee("Vinoth");
        EmployeeVal val = new EmployeeVal("Programmer");
        
        aMap.put(emp, val);
        
        emp = null;
        
        int count = 0;
        while (0 != aMap.size()) {
            count ++;
            System.gc();
        }
        System.out.println("Took " + count
                + " calls to System.gc() to result in weakHashMap size of : "
                + aMap.size());
    }

    static void test_1() {
        
        HashMap<Employee, EmployeeVal> aMap = new HashMap<Employee, EmployeeVal>();
        
        Employee emp = new Employee("Vinoth");
        EmployeeVal val = new EmployeeVal("Programmer");
        
        aMap.put(emp, val);
        
        emp = null;
        
        for(int i = 0 ; i < 100 ; i ++) {
            System.gc();
        }
        System.out.println(aMap);
        
    }

}
