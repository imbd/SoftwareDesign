package com.imbd.spbau.commands;


import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.imbd.spbau.*;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Grep implements Command {

    private enum MatchingType {
        USUAL,
        INSENSITIVE,
        FOLLOWING_LINES,
        WHOLE_WORD
    }


    @Parameter(names = "-i")
    private boolean case_insensitive = false;

    @Parameter(names = "-A")
    private int lines_number = -1;

    @Parameter(names = "-w")
    private boolean whole_word = false;

    @Parameter
    private List<String> parameters = new LinkedList<>();


    @Override
    public InputStream execute(List<String> args, InputStream inputData) throws SyntaxException {

        String[] argsArray = new String[args.size()];
        args.toArray(argsArray);
        whole_word = false;
        lines_number = -1;
        case_insensitive = false;
        parameters.clear();
        new JCommander(this, argsArray);

        MatchingType matchingType = MatchingType.USUAL;
        if (case_insensitive) {
            matchingType = MatchingType.INSENSITIVE;
        }
        if (lines_number != -1) {
            matchingType = MatchingType.FOLLOWING_LINES;
        }
        if (whole_word) {
            matchingType = MatchingType.WHOLE_WORD;
        }

        if (parameters.size() < 2) {
            throw new SyntaxException("Bad construction in grep");
        }

        String regexp = parameters.get(1);

        return executeHelper(parameters.subList(2, parameters.size()), matchingType, regexp, lines_number);
    }

    private InputStream executeHelper(List<String> files, MatchingType matchingType, String regexp, int lines_number) {

        String result = "";
        Pattern pattern = Pattern.compile(regexp);
        if (matchingType == MatchingType.INSENSITIVE) {
            pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
        }
        if (matchingType == MatchingType.WHOLE_WORD) {
            regexp = "( |^)" + regexp + "( |$)";
            pattern = Pattern.compile(regexp);
        }

        for (String file : files) {

            String fileContent = "";
            int difference = lines_number + 1;
            boolean toPrint;
            try {
                fileContent = new String(Files.readAllBytes(Paths.get(file)), StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String[] lines = fileContent.split("\n");

            for (String line : lines) {

                difference++;
                toPrint = (difference <= lines_number);
                Matcher matcher = pattern.matcher(line);

                if (matcher.find() || toPrint) {
                    result += (line + '\n');
                    if (!toPrint) {
                        difference = 0;
                    }

                } else {
                    difference++;
                }

            }
        }

        return new ByteArrayInputStream(result.getBytes());
    }
}
