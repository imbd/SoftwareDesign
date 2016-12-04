package com.imbd.spbau.commands;

import com.imbd.spbau.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * External command execution
 */

public class ExternalCommand implements Command {

    /**
     * @param args      arguments, the first one is a command name
     * @param inputData result of previous operations
     * @return result of execution as InputStream
     */

    @Override
    public InputStream execute(List<String> args, InputStream inputData) throws SyntaxException {

        ProcessBuilder processBuilder = new ProcessBuilder(args);
        Process process;
        try {
            process = processBuilder.start();


        } catch (IOException e) {
            throw new SyntaxException("External command not found");
        }

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        inputData = process.getInputStream();
        return inputData;
    }
}
