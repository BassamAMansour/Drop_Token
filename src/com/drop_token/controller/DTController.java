package com.drop_token.controller;

import com.drop_token.game_utils.InputParser;
import com.drop_token.game_utils.InputParser.ParsedInput;
import com.drop_token.model.DTEngine;
import com.drop_token.model.IDTEngine.TokenInsertionStatus;
import com.drop_token.view.DTConsoleManager;

public class DTController implements IDTController {

    private DTConsoleManager consoleManager;
    private DTEngine engine;
    private InputParser inputParser;

    private boolean isExitCalled;

    public DTController() {
        this(new DTEngine(), new DTConsoleManager());
    }

    public DTController(DTEngine engine, DTConsoleManager consoleManager) {
        this.engine = engine;
        this.consoleManager = consoleManager;
        this.inputParser = new InputParser();
    }

    public void startGame() {

        ParsedInput parsedInput;

        while (!isExitCalled) {

                parsedInput = inputParser.parseInput(consoleManager.getNextInput());

                switch (parsedInput.getInputType()) {

                    case PUT:
                        commandPut(parsedInput.getArgs()[0]);
                        break;
                    case GET:
                        commandGet();
                        break;
                    case BOARD:
                        commandBoard();
                        break;
                    case EXIT:
                        commandExit();
                        break;
                    case ERROR:
                        notifyWrongCommand();
                        continue;
                }

            }

    }

    private void commandPut(int column) {
        TokenInsertionStatus tokenInsertionStatus = engine.insertToken(column - 1);
        consoleManager.displayInsertionResult(tokenInsertionStatus);
    }

    private void commandGet() {
        consoleManager.printInsertionSequence(engine.getInsertionSequence());
    }

    private void commandBoard() {
        consoleManager.printBoard(engine.getBoard());
    }

    private void commandExit() {
        isExitCalled = true;
        consoleManager.exit();
    }

    private void notifyWrongCommand() {
        consoleManager.notifyWrongInput();
    }
}
