package com.imbd.spbau.commands;

import com.imbd.spbau.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Command which returns a content of files given as its arguments
 */

public class Cat implements Command {

    /**
     * @param args      arguments, the first one is a command name
     * @param inputData result of previous operations
     * @return result of execution as InputStream
     */

    @Override
    public InputStream execute(List<String> args, InputStream inputData) throws SyntaxException {

        String outputData = "";
        int length = args.size();

        for (int i = 1; i < length; i++) {

            try {
                outputData += new String(Files.readAllBytes(Paths.get(args.get(i))), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new SyntaxException("No such file or no permission error in a cat execution");
            }
        }

        return new ByteArrayInputStream(outputData.getBytes());
    }
}
