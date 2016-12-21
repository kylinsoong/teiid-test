package org.teiid.test.teiid4622;

public class LengthTest {

    public static void main(String[] args) {

        System.out.println("redhat".length());
        System.out.println(new String(padkey("redhat")).length());
    }
    
    private static byte[] padkey (String key) {
        byte padding[] = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
        if (key == null || "".equals(key)) return padding;
        try {
            byte[] keybtye = key.getBytes("UTF-8");
            int len = keybtye.length;
            if ( len == 16) { // 16 bytes is hard coded 
                return keybtye;
            } else {
                for (int i=0; i < 16 && i < len ; i++ )
                    padding[i] = keybtye[i];
                return padding;
            }
        } catch(Exception e) {
            System.out.println("Exception:" +e.getLocalizedMessage());
            return padding;
        } finally {
            
        }
        
    }

}
