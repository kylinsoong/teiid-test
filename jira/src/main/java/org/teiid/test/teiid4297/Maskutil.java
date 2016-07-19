package org.teiid.test.teiid4297;

import org.arrah.framework.datagen.ShuffleRTM;
import org.arrah.framework.ndtable.ResultsetToRTM;
import org.arrah.framework.util.StringCaseFormatUtil;

public class Maskutil {
    
    public static String random(String str){
        return ShuffleRTM.shuffleString(str);
    }
    
    public static String hash(String str) {
        return ResultsetToRTM.getMD5(str == null ? "" : str).toString();
    }
    
    public static String digit(String str) {
        return StringCaseFormatUtil.digitString(str);
    }

    public static void main(String[] args) {
        System.out.println(Maskutil.random("abcdefghigklmn"));
        System.out.println(Maskutil.hash("abcdefghigklmn"));
        System.out.println(Maskutil.digit("asd6a1dss2fdsf3sdf"));
        System.out.println("DONE");
    }

}
