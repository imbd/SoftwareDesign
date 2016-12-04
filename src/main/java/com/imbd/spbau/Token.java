package com.imbd.spbau;


import java.util.Objects;

/**
 * Unit of an input string parsing
 * Contains type and content
 */

public class Token {

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

    Token(Type type, String content) {

        this.type = type;
        this.content = content;
    }


    Type getType() {

        return type;
    }

    String getContent() {

        return content;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Token && ((Token) obj).getType() == this.getType() && Objects.equals(((Token) obj).getContent(), this.getContent());
    }
}
