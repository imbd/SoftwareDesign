package com.imbd.spbau.commands;

import com.imbd.spbau.*;


import java.io.InputStream;
import java.util.List;

/**
 * Interface for all shell's commands
 */

public interface Command {

    InputStream execute(List<String> args, InputStream inputData)
            throws SyntaxException;
}
