package com.imbd.spbau;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class which performs parsing and variable substitution
 */

public class Tokenizer {

    private static final Map<Character, Token.Type> SYMBOL_TYPES = new HashMap<Character, Token.Type>() {
        {
            put('\'', Token.Type.SINGLE_QUOTE);
            put('"', Token.Type.DOUBLE_QUOTE);
            put('|', Token.Type.PIPE);
        }
    };

    private static final char SPACE_SYMBOL = ' ';
    private static final char SINGLE_QUOTE_SYMBOL = '\'';
    private static final char DOUBLE_QUOTE_SYMBOL = '"';
    private static final char SUBSTITUTION_SYMBOL = '$';
    private static final char ASSIGNMENT_SYMBOL = '=';
    private static final char PIPE_SYMBOL = '|';

    private static final String SPLIT_SYMBOLS = " '\"|";

    /**
     * Parsing of an input string
     *
     * @param inputLine input string
     * @return list of a resulting tokens
     */

    public List<Token> parse(String inputLine) {

        List<Token> tokens = new ArrayList<>();

        int length = inputLine.length();
        String currentToken = "";
        boolean wasAssignment = false;
        boolean wasSubstitution = false;

        int i = 0;
        while (i < length) {

            char symbol = inputLine.charAt(i);
            currentToken = String.valueOf(symbol);

            if (symbol == SPACE_SYMBOL) {

                i++;
                continue;
            }

            if (symbol == SINGLE_QUOTE_SYMBOL || symbol == DOUBLE_QUOTE_SYMBOL) {

                int num = -1;
                for (int j = i + 1; j < length; j++) {
                    currentToken += inputLine.charAt(j);
                    if (inputLine.charAt(j) == symbol) {
                        num = j;
                        break;
                    }
                }

                if (num >= 0) {

                    tokens.add(new Token(SYMBOL_TYPES.get(symbol), currentToken));
                    currentToken = "";
                    i = num + 1;
                    continue;

                } else {

                    new SyntaxException("no pair for a quote").printStackTrace();
                    tokens.clear();
                    return tokens;
                }
            }

            if (symbol == PIPE_SYMBOL) {

                tokens.add(new Token(SYMBOL_TYPES.get(symbol), currentToken));
                currentToken = "";
                i++;
                continue;
            }

            wasAssignment = false;
            wasSubstitution = false;

            if (symbol == ASSIGNMENT_SYMBOL) {
                wasAssignment = true;
            }
            if (symbol == SUBSTITUTION_SYMBOL) {
                wasSubstitution = true;
            }
            boolean wasStopped = false;
            for (int j = i + 1; j < length; j++) {

                i = length;
                char currentSymbol = inputLine.charAt(j);
                if (SPLIT_SYMBOLS.contains(String.valueOf(currentSymbol))) {

                    Token.Type currentType = Token.Type.TEXT;
                    if (wasAssignment) {
                        currentType = Token.Type.ASSIGNMENT;
                    }
                    if (wasSubstitution) {
                        currentType = Token.Type.SUBSTITUTION;
                    }
                    tokens.add(new Token(currentType, currentToken));
                    currentToken = "";
                    i = j;
                    wasStopped = true;
                    break;

                } else {
                    currentToken += currentSymbol;
                    if (currentSymbol == ASSIGNMENT_SYMBOL) {
                        wasAssignment = true;
                    }
                    if (currentSymbol == SUBSTITUTION_SYMBOL) {
                        wasSubstitution = true;
                    }
                }

            }
            if (!wasStopped) {
                i++;
            }
        }

        if (!currentToken.isEmpty()) {

            Token.Type currentType = Token.Type.TEXT;
            if (wasAssignment) {
                currentType = Token.Type.ASSIGNMENT;
            }
            if (wasSubstitution) {
                currentType = Token.Type.SUBSTITUTION;
            }
            tokens.add(new Token(currentType, currentToken));
        }

        return tokens;
    }

    /**
     * Substitution of quotes and '$' content
     *
     * @param tokens      list of tokens before substitution
     * @param environment environment with values of variables
     * @return list of tokens after substitution
     */

    public List<Token> substitute(List<Token> tokens, Environment environment) {

        List<Token> newTokens = new ArrayList<>();
        for (Token token : tokens) {

            if (token.getType() == Token.Type.SINGLE_QUOTE) {

                newTokens.add(new Token(Token.Type.TEXT, token.getContent().substring(1, token.getContent().length() - 1)));
                continue;
            }

            if (token.getType() == Token.Type.DOUBLE_QUOTE) {

                String newContent = substitution(token.getContent().substring(1, token.getContent().length() - 1), environment);
                Token.Type newType = Token.Type.TEXT;
                if (newContent.indexOf('=') != -1) {
                    newType = Token.Type.ASSIGNMENT;
                }
                newTokens.add(new Token(newType, newContent));
                continue;
            }

            if (token.getType() == Token.Type.SUBSTITUTION) {

                String newContent = substitution(token.getContent(), environment);
                Token.Type newType = Token.Type.TEXT;
                if (newContent.indexOf('=') != -1) {
                    newType = Token.Type.ASSIGNMENT;
                }
                newTokens.add(new Token(newType, newContent));
                continue;
            }

            newTokens.add(token);
        }

        return newTokens;
    }

    private String substitution(String content, Environment environment) {

        int length = content.length();
        String result = "";

        int i = 0;
        while (i < length) {

            char symbol = content.charAt(i);

            if (symbol == SUBSTITUTION_SYMBOL) {

                int j = i + 1;
                String variable = "";
                while (j < length && Character.isLetterOrDigit(content.charAt(j))) {
                    variable += content.charAt(j);
                    j++;
                }

                if (variable.length() == 0) {
                    result += symbol;
                } else {

                    result += environment.getValue(variable);
                }
                i = j;
            } else {

                result += symbol;
                i++;
            }


        }

        return result;
    }
}
