package com.imbd.spbau.commands;

import java.io.InputStream;
import java.util.List;

/**
 * Interface for all shell's commands
 */

public interface Command {

    /**
     * @param args      given to command arguments; the first one is a command name
     * @param inputData result of previous operations
     * @return result of execution as InputStream
     */
    InputStream execute(List<String> args, InputStream inputData);
}
