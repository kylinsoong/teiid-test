package org.teiid.test.teiid4455.sql.symbol;

import java.sql.Timestamp;

import org.teiid.core.types.DataTypeManager;
import org.teiid.language.SQLConstants.NonReserved;
import org.teiid.query.function.FunctionLibrary;
import org.teiid.query.sql.symbol.Constant;
import org.teiid.query.sql.symbol.Expression;
import org.teiid.query.sql.symbol.Function;

public class ExampleOfFunctionTimestampadd {

    public static void main(String[] args) {
        
        
        String name = FunctionLibrary.TIMESTAMPADD;
        Constant arg0 = new Constant(NonReserved.SQL_TSI_SECOND);
        Constant arg1 = new Constant(1900000000, DataTypeManager.DefaultDataClasses.INTEGER);
        Constant arg2 = new Constant(new Timestamp(0));
        Function timestampadd = new Function(name, new Expression[] {arg0, arg1, arg2});
//        timestampadd.setFunctionDescriptor();
        timestampadd.setType(DataTypeManager.DefaultDataClasses.TIMESTAMP);
        System.out.println(timestampadd);
        
        Function from_unttime = new Function("FROM_UNIXTIME",  new Expression[]{new Constant(1900000000)});
        System.out.println(from_unttime);

    }

}
