package org.teiid.test.teiid4121;

import org.teiid.core.types.DataTypeManager;

public class TestTmp {

    public static void main(String[] args) {

        System.out.println(DataTypeManager.DefaultDataTypes.STRING);
        System.out.println(DataTypeManager.DefaultDataTypes.BOOLEAN);
        System.out.println(DataTypeManager.DefaultDataTypes.LONG);
        System.out.println(DataTypeManager.DefaultDataTypes.TIMESTAMP);
    }

}
