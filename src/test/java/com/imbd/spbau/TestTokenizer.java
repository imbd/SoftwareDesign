package com.imbd.spbau;

import org.junit.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TestTokenizer {


    @Test
    public void testTokenParse1() {

        Tokenizer tokenizer = new Tokenizer();

        assertEquals(tokenizer.parse("echo 123"), Arrays.asList(
                new Token(Token.Type.TEXT, "echo"),
                new Token(Token.Type.TEXT, "123")));

    }


    @Test
    public void testTokenParse2() {

        Tokenizer tokenizer = new Tokenizer();

        assertEquals(tokenizer.parse("\"echo\" $a123|'a=0'|ccc '$d'"), Arrays.asList(
                new Token(Token.Type.DOUBLE_QUOTE, "\"echo\""),
                new Token(Token.Type.SUBSTITUTION, "$a123"),
                new Token(Token.Type.PIPE, "|"),
                new Token(Token.Type.SINGLE_QUOTE, "'a=0'"),
                new Token(Token.Type.PIPE, "|"),
                new Token(Token.Type.TEXT, "ccc"),
                new Token(Token.Type.SINGLE_QUOTE, "'$d'")));
    }

    @Test
    public void testTokenParse3() {

        Tokenizer tokenizer = new Tokenizer();

        assertEquals(tokenizer.parse("echo 123 |    a=0| echo '$d'"), Arrays.asList(
                new Token(Token.Type.TEXT, "echo"),
                new Token(Token.Type.TEXT, "123"),
                new Token(Token.Type.PIPE, "|"),
                new Token(Token.Type.ASSIGNMENT, "a=0"),
                new Token(Token.Type.PIPE, "|"),
                new Token(Token.Type.TEXT, "echo"),
                new Token(Token.Type.SINGLE_QUOTE, "'$d'")));
    }

    @Test(expected = SyntaxException.class)
    public void testTokenParse4() {

        Tokenizer tokenizer = new Tokenizer();

        tokenizer.parse("echo \"123");

    }

    @Test
    public void testSubstitution1() {

        Environment environment = new Environment();
        Tokenizer tokenizer = new Tokenizer();

        List<Token> tokens = Arrays.asList(
                new Token(Token.Type.TEXT, "b123"),
                new Token(Token.Type.SINGLE_QUOTE, "'aaa'"),
                new Token(Token.Type.DOUBLE_QUOTE, "\"bbb\""));
        List<Token> subTokens = Arrays.asList(
                new Token(Token.Type.TEXT, "b123"),
                new Token(Token.Type.TEXT, "aaa"),
                new Token(Token.Type.TEXT, "bbb"));

        assertEquals(subTokens, tokenizer.substitute(tokens, environment));

    }

    @Test
    public void testSubstitution2() {

        Environment environment = new Environment();
        Tokenizer tokenizer = new Tokenizer();
        environment.setValue("a", "2");

        List<Token> tokens = Arrays.asList(
                new Token(Token.Type.SINGLE_QUOTE, "'$a'"),
                new Token(Token.Type.DOUBLE_QUOTE, "\"$a\""));
        List<Token> subTokens = Arrays.asList(
                new Token(Token.Type.TEXT, "$a"),
                new Token(Token.Type.TEXT, "2"));

        assertEquals(subTokens, tokenizer.substitute(tokens, environment));

    }

    @Test
    public void testSubstitution3() {

        Environment environment = new Environment();
        Tokenizer tokenizer = new Tokenizer();
        environment.setValue("a", "2");

        List<Token> tokens = Arrays.asList(
                new Token(Token.Type.SINGLE_QUOTE, "'$a'"),
                new Token(Token.Type.DOUBLE_QUOTE, "\"$a\""),
                new Token(Token.Type.SUBSTITUTION, "b=$a"));
        List<Token> subTokens = Arrays.asList(
                new Token(Token.Type.TEXT, "$a"),
                new Token(Token.Type.TEXT, "2"),
                new Token(Token.Type.ASSIGNMENT, "b=2"));

        assertEquals(subTokens, tokenizer.substitute(tokens, environment));

    }


}
