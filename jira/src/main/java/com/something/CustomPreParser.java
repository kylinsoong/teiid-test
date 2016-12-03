package com.something;

import org.teiid.CommandContext;
import org.teiid.PreParser;

public class CustomPreParser implements PreParser {

    @Override
    public String preParse(String command, CommandContext context) {
        System.out.println(command);
        System.out.println(context);
        return command;
    }

}
