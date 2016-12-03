package org.teiid.test.teiid4455.sql.symbol;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.teiid.query.sql.symbol.Constant;
import org.teiid.query.sql.symbol.Expression;
import org.teiid.query.sql.symbol.Function;
import org.teiid.query.sql.visitor.EvaluatableVisitor;

public class ExampleOfFunctionTimestampdiff {

    public static void main(String[] args) throws ParseException {

        String name = "mysql.timestampdiff";
        Constant arg0 = new Constant("MONTH", String.class);
        Constant arg1 = new Constant(formTimestamp("2016-05-18 00:00:00.0"), Timestamp.class);
        Constant arg2 = new Constant(formTimestamp("2016-10-11 00:00:00.0"), Timestamp.class);
        Function timestampdiff = new Function(name, new Expression[] {arg0, arg1, arg2});
        
        System.out.println(EvaluatableVisitor.isFullyEvaluatable(timestampdiff, true));
    }
    
    static Timestamp formTimestamp(String str) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        Date parsedDate = dateFormat.parse(str);
        Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
        return timestamp;
    }

}
