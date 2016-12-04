package com.imbd.spbau.commands;

import com.imbd.spbau.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.List;

/**
 * Command which returns a current directory
 */

public class Pwd implements Command {

    /**
     * @param args      arguments, the first one is a command name
     * @param inputData result of previous operations
     * @return result of execution as InputStream
     */

    @Override
    public InputStream execute(List<String> args, InputStream inputData) throws SyntaxException {

        if (args.size() != 1) {
            throw new SyntaxException("Wrong number of parameters in pwd");
        }

        return new ByteArrayInputStream((Paths.get(".").toAbsolutePath().toString() + '\n').getBytes());
    }
}
