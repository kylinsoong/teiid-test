package org.teiid.test.teiid3617;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TestUserSecurityDomain {
    
    public static final String AT = "@"; 
    
    static String getBaseUsername(String username) {
        if (username == null) {
            return username;
        }
        
        int index = getQualifierIndex(username);

        String result = username;
        
        if (index != -1) {
            result = username.substring(0, index);
        }
        
        //strip the escape character from the remaining ats
        return result.replaceAll("\\\\"+AT, AT); //$NON-NLS-1$
    }
    
    static int getQualifierIndex(String username) {
        int index = username.length();
        while ((index = username.lastIndexOf(AT, --index)) != -1) {
            if (index > 0 && username.charAt(index - 1) != '\\') {
                return index;
            }
        }
        
        return -1;
    }
    
    
    static String escapeName(String name) {
        if (name == null) {
            return name;
        }
        
        return name.replaceAll(AT, "\\\\"+AT); //$NON-NLS-1$
    }

    public static void main(String[] args) {

        System.out.println(escapeName("anonymous@teiid-security"));
        System.out.println(escapeName("anonymous"));
        System.out.println(getBaseUsername("anonymous@teiid-security"));
        System.out.println(getBaseUsername("anonymous"));
        System.out.println();
        System.out.println();
        
        Map<String, Integer> maxSessionPerUserCount =  new ConcurrentHashMap<>();
        maxSessionPerUserCount.put("sd", 12);
        
        Integer count = maxSessionPerUserCount.get("sd");
        System.out.println(maxSessionPerUserCount.remove("sd1"));
        
    }

}
