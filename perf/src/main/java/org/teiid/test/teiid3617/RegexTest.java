package org.teiid.test.teiid3617;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {
    
    static final String JDBC_PROTOCOL = "jdbc:teiid:"; 
    static final String URL_PATTERN = JDBC_PROTOCOL + "([\\w-\\.]+)(?:@([^;]*))?(;.*)?"; //$NON-NLS-1$
    static Pattern urlPattern = Pattern.compile(URL_PATTERN);

    static Pattern permissionPattern = Pattern.compile("\"([^\"]+)\"");
    
    public static void main(String[] args) {

        Matcher m = permissionPattern.matcher("jdbc,teiid");
        System.out.println(m.matches());
    }

}
