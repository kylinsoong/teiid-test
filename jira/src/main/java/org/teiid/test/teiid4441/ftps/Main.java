package org.teiid.test.teiid4441.ftps;

public class Main {

    public static void main(String[] args) {

        Connection file = new FileConnection(){};
        Connection ftps = new FTPSConnectionImpl();
        
        System.out.println(file instanceof FileConnection);
        System.out.println(file instanceof FTPSConnection);
        System.out.println(ftps.getClass().getName().equals(FTPSConnectionImpl.class.getName()));
    }

}
