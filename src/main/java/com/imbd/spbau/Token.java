package com.imbd.spbau;


import java.util.Objects;

/**
 * Unit of an input string parsing
 * Contains type and content
 */

public class Token {

    /**
     * Enumeration for types of token
     */
    public enum Type {

        TEXT,
        SINGLE_QUOTE,
        DOUBLE_QUOTE,
        SUBSTITUTION,
        ASSIGNMENT,
        PIPE
    }

    private Type type;
    private String content;

    public Token(Type type, String content) {

        this.type = type;
        this.content = content;
    }


    public Type getType() {

        return type;
    }

    public String getContent() {

        return content;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Token && ((Token) obj).getType() == this.getType() && Objects.equals(((Token) obj).getContent(), this.getContent());
    }
}
