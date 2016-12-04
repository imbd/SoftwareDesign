package com.imbd.spbau;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TestParser {

    @Test
    public void testEcho() throws IOException {

        Environment environment = new Environment();
        Parser parser = new Parser();
        List<Token> tokens = Arrays.asList(
                new Token(Token.Type.TEXT, "echo"),
                new Token(Token.Type.TEXT, "abacaba"));
        InputStream inputStream = parser.execute(tokens, environment);
        assertTrue(IOUtils.toString(inputStream, "UTF-8").equals("abacaba\n"));
    }

    @Test
    public void testCatAndWc() throws IOException {

        Environment environment = new Environment();
        Parser parser = new Parser();
        List<Token> tokens = Arrays.asList(
                new Token(Token.Type.TEXT, "cat"),
                new Token(Token.Type.TEXT, "tmp.txt"),
                new Token(Token.Type.PIPE, "|"),
                new Token(Token.Type.TEXT, "wc"));
        InputStream inputStream = parser.execute(tokens, environment);
        assertTrue(IOUtils.toString(inputStream, "UTF-8").equals("3 6 23\n"));
    }

    @Test
    public void testPwd() throws IOException {
        Environment environment = new Environment();
        Parser parser = new Parser();
        List<Token> tokens = Arrays.asList(
                new Token(Token.Type.ASSIGNMENT, "ab=32"),
                new Token(Token.Type.PIPE, "|"),
                new Token(Token.Type.TEXT, "pwd"));
        InputStream inputStream = parser.execute(tokens, environment);
        assertTrue(IOUtils.toString(inputStream, "UTF-8").equals(Paths.get(".").toAbsolutePath().toString() + '\n'));
    }

    @Test
    public void testExternal() throws IOException {
        Environment environment = new Environment();
        Parser parser = new Parser();
        List<Token> tokens = Arrays.asList(
                new Token(Token.Type.TEXT, "python"),
                new Token(Token.Type.TEXT, "1.py"));
        InputStream inputStream = parser.execute(tokens, environment);
        String outputData = IOUtils.toString(inputStream, "UTF-8");
        assertTrue(outputData.equals("Hello, world!" + System.getProperty("line.separator")));
    }

    @Test
    public void testGrep1() throws IOException {
        Environment environment = new Environment();
        Parser parser = new Parser();
        List<Token> tokens = Arrays.asList(
                new Token(Token.Type.TEXT, "grep"),
                new Token(Token.Type.TEXT, "^aba"),
                new Token(Token.Type.TEXT, "tmp.txt"));
        InputStream inputStream = parser.execute(tokens, environment);
        String outputData = IOUtils.toString(inputStream, "UTF-8");
        assertTrue(outputData.equals("ababc bac" + System.getProperty("line.separator")));
    }

    @Test
    public void testGrep2() throws IOException {
        Environment environment = new Environment();
        Parser parser = new Parser();
        List<Token> tokens = Arrays.asList(
                new Token(Token.Type.TEXT, "grep"),
                new Token(Token.Type.TEXT, "-i"),
                new Token(Token.Type.TEXT, "aBA"),
                new Token(Token.Type.TEXT, "tmp.txt"));
        InputStream inputStream = parser.execute(tokens, environment);
        String outputData = IOUtils.toString(inputStream, "UTF-8");
        assertTrue(outputData.equals("ababc bac" + System.getProperty("line.separator")));
    }

    @Test
    public void testGrep3() throws IOException {
        Environment environment = new Environment();
        Parser parser = new Parser();
        List<Token> tokens = Arrays.asList(
                new Token(Token.Type.TEXT, "grep"),
                new Token(Token.Type.TEXT, "-w"),
                new Token(Token.Type.TEXT, "b"),
                new Token(Token.Type.TEXT, "tmp.txt"));
        InputStream inputStream = parser.execute(tokens, environment);
        String outputData = IOUtils.toString(inputStream, "UTF-8");
        assertTrue(outputData.equals("abbbb b b" + System.getProperty("line.separator")));
    }

    @Test
    public void testGrep4() throws IOException {
        Environment environment = new Environment();
        Parser parser = new Parser();
        List<Token> tokens = Arrays.asList(
                new Token(Token.Type.TEXT, "grep"),
                new Token(Token.Type.TEXT, "-A"),
                new Token(Token.Type.TEXT, "1"),
                new Token(Token.Type.TEXT, "abc"),
                new Token(Token.Type.TEXT, "tmp.txt"));
        InputStream inputStream = parser.execute(tokens, environment);
        String outputData = IOUtils.toString(inputStream, "UTF-8");
        assertTrue(outputData.equals("ababc bac" + System.getProperty("line.separator") + "abbbb b b" + System.getProperty("line.separator")));
    }

    @Test
    public void testGrep5() throws IOException {
        Environment environment = new Environment();
        Parser parser = new Parser();
        List<Token> tokens = Arrays.asList(
                new Token(Token.Type.TEXT, "grep"),
                new Token(Token.Type.TEXT, "abb* b b$"),
                new Token(Token.Type.TEXT, "tmp.txt"));
        InputStream inputStream = parser.execute(tokens, environment);
        String outputData = IOUtils.toString(inputStream, "UTF-8");
        assertTrue(outputData.equals("abbbb b b" + System.getProperty("line.separator")));
    }

}
