package com.drop_token.view;

import com.drop_token.model.IDTEngine.TokenInsertionStatus;

import java.util.LinkedList;

public interface IDTConsoleManager {

    String getNextInput();

    void printInsertionSequence(LinkedList<Integer> insertionSequence);

    void printBoard(int[][] board);

    void displayInsertionResult(TokenInsertionStatus tokenInsertionStatus);

    void exit();

    void notifyWrongInput();
}
