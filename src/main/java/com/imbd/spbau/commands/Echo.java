package com.imbd.spbau.commands;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * Command which returns given arguments
 */

public class Echo implements Command {

    /**
     * @param args      arguments, the first one is a command name
     * @param inputData result of previous operations
     * @return result of execution as InputStream
     */

    @Override
    public InputStream execute(List<String> args, InputStream inputData) {

        String outputData = "";

        int length = args.size();
        for (int i = 1; i < length; i++) {

            outputData += args.get(i) + '\n';
        }

        return new ByteArrayInputStream(outputData.getBytes());
    }
}
