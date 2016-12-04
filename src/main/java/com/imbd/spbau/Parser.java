package com.imbd.spbau;

import com.imbd.spbau.commands.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class which performs execution
 */


public class Parser {

    private static final Map<String, Command> ALL_COMMANDS = new HashMap<String, Command>() {
        {
            put("echo", new Echo());
            put("pwd", new Pwd());
            put("cat", new Cat());
            put("wc", new Wc());
            put("grep", new Grep());
        }
    };

    /**
     * Executing of given tokens
     *
     * @param tokens      list of tokens after substitution
     * @param environment environment with values of variables and possibility to change them
     * @return result as an InputStream
     */

    public InputStream execute(List<Token> tokens, Environment environment) throws SyntaxException {

        InputStream currentData = System.in;

        int length = tokens.size();
        List<Token> commandTokens = new ArrayList<>();

        for (int i = 0; i < length; i++) {

            Token token = tokens.get(i);
            if (token.getType() != Token.Type.PIPE) {
                commandTokens.add(token);
                if (i != length - 1) {
                    continue;
                }
            }


            if (commandTokens.isEmpty()) {
                throw new SyntaxException("Bad pipe construction");
            }

            if (commandTokens.get(0).getType() == Token.Type.ASSIGNMENT) {

                handleAssignment(commandTokens, environment);
                currentData = new ByteArrayInputStream("".getBytes());
                commandTokens.clear();
                continue;
            }

            if (ALL_COMMANDS.containsKey(commandTokens.get(0).getContent())) {

                currentData = handleCommand(commandTokens, currentData);
                commandTokens.clear();

            } else {

                currentData = handleExternalCommand(commandTokens, currentData);
                commandTokens.clear();
            }

        }

        return currentData;
    }

    private void handleAssignment(List<Token> assignmentTokens, Environment environment) {

        String content = assignmentTokens.get(0).getContent();
        int index = content.indexOf('=');

        if (assignmentTokens.size() != 1 || index == -1) {
            throw new SyntaxException("Bad assignment construction");
        }

        String variable = content.substring(0, index);
        String value = content.substring(index + 1, content.length());
        environment.setValue(variable, value);

    }

    private InputStream handleCommand(List<Token> commandTokens, InputStream inputStream) {

        return ALL_COMMANDS.get(commandTokens.get(0).getContent()).execute(commandTokens.stream().map(Token::getContent).collect(Collectors.toList()), inputStream);
    }

    private InputStream handleExternalCommand(List<Token> commandTokens, InputStream inputData) {

        return (new ExternalCommand().execute(commandTokens.stream().map(Token::getContent).collect(Collectors.toList()), inputData));
    }

}
