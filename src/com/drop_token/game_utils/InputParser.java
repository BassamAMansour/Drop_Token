package com.drop_token.game_utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputParser {

    private static final String REGEX_COMMANDS = "^\\s*(?i)(PUT|GET|BOARD|EXIT)(\\s+(\\d+)?)?\\s*$";

    private static final int GROUP_POSITION_COMMAND = 1;
    private static final int GROUP_POSITION_COLUMN = 3;

    private static final String COMMAND_PUT = "PUT";
    private static final String COMMAND_GET = "GET";
    private static final String COMMAND_BOARD = "BOARD";
    private static final String COMMAND_EXIT = "EXIT";

    private final Pattern commandPattern;

    public InputParser() {
        this.commandPattern = Pattern.compile(REGEX_COMMANDS);
    }

    public ParsedInput parseInput(String input) {

        ParsedInput parsedInput = new ParsedInput();
        parsedInput.setInputType(InputType.ERROR);

        Matcher commandMatcher = commandPattern.matcher(input.trim());

        if(!commandMatcher.matches()) return parsedInput;

        String command = commandMatcher.group(GROUP_POSITION_COMMAND);

        if (command != null) {
            switch (command.toUpperCase()) {

                case COMMAND_PUT:
                    String column = commandMatcher.group(GROUP_POSITION_COLUMN);

                    if (column != null) {
                        parsedInput.setInputType(InputType.PUT);
                        parsedInput.setArgs(new int[]{Integer.valueOf(column)});
                    }

                    break;
                case COMMAND_GET:
                    parsedInput.setInputType(InputType.GET);
                    break;
                case COMMAND_BOARD:
                    parsedInput.setInputType(InputType.BOARD);
                    break;
                case COMMAND_EXIT:
                    parsedInput.setInputType(InputType.EXIT);
                    break;
            }
        }

        return parsedInput;
    }

    public class ParsedInput {
        private InputType inputType;
        private int[] args;

        public InputType getInputType() {
            return inputType;
        }

        public void setInputType(InputType inputType) {
            this.inputType = inputType;
        }

        public int[] getArgs() {
            return args;
        }

        public void setArgs(int[] args) {
            this.args = args;
        }
    }

    public enum InputType {PUT, GET, BOARD, EXIT, ERROR}
}
