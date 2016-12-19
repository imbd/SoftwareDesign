package com.imbd.spbau.commands;

import com.imbd.spbau.*;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Command which returns a lines, words and bytes number of text or files given as its arguments
 */

public class Wc implements Command {

    /**
     * @param args      arguments, the first one is a command name
     * @param inputData result of previous operations
     * @return result of execution as InputStream
     */

    @Override
    public InputStream execute(List<String> args, InputStream inputData) {

        String outputData = "";
        int length = args.size();

        if (args.size() == 1) {

            try {
                return new ByteArrayInputStream(countAnswer(IOUtils.toString(inputData, "UTF-8")).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (int i = 1; i < length; i++) {

            try {
                outputData += (countAnswer(new String(Files.readAllBytes(Paths.get(args.get(i))), StandardCharsets.UTF_8)));
            } catch (IOException e) {
                new SyntaxException("No such file or no permission error in a cat execution").printStackTrace();
            }
        }

        return new ByteArrayInputStream(outputData.getBytes());
    }

    private String countAnswer(String fileContent) {

        int linesNumber = fileContent.split("\n").length;
        int wordsNumber = fileContent.split("\n| ").length;
        int bytesNumber = fileContent.length();

        return String.valueOf(linesNumber) + " " + String.valueOf(wordsNumber) + " " + String.valueOf(bytesNumber) + '\n';
    }
}
