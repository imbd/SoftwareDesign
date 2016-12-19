package com.imbd.spbau;


import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.List;

/**
 * Main class
 */

public class Shell {

    private Environment environment = new Environment();
    private Tokenizer tokenizer = new Tokenizer();
    private Parser parser = new Parser();


    public static void main(String[] args) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        Shell shell = new Shell();
        while (true) {
            String LINE_START = "imbd@shell:~S ";
            System.out.print(LINE_START);
            String inputLine = bufferedReader.readLine();
            if (inputLine.length() > 0) {

                if (inputLine.equals("exit")) {
                    return;
                } else {
                    shell.execute(inputLine);
                }
            }
        }
    }

    private void execute(String inputLine) {

        List<Token> tokens = tokenizer.parse(inputLine);
        tokens = tokenizer.substitute(tokens, environment);
        InputStream inputStream = parser.execute(tokens, environment);

        String outputData = "";
        try {
            outputData = IOUtils.toString(new ByteArrayInputStream("".getBytes()), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputData = IOUtils.toString(inputStream, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(outputData);
    }

}
