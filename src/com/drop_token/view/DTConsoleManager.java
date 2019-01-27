package com.drop_token.view;

import com.drop_token.model.IDTEngine.TokenInsertionStatus;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

public class DTConsoleManager implements IDTConsoleManager {

    private final Scanner in;
    private final PrintStream out;

    public DTConsoleManager() {
        this.in = new Scanner(System.in);
        this.out = System.out;
    }

    public String getNextInput() {
        out.print("> ");
        return in.nextLine();
    }

    @Override
    public void printInsertionSequence(LinkedList<Integer> insertionSequence) {
        for (Iterator<Integer> it = insertionSequence.descendingIterator(); it.hasNext(); ) {
            Integer column = it.next();
            out.println(column);
        }
    }

    @Override
    public void printBoard(int[][] board) {
        for (int[] row : board) {

            out.print("|");

            for (int j = 0; j < board[0].length; j++) {
                out.print(" " + row[j]);
            }

            out.println();
        }

        out.print("+");

        for (int i = 0; i < board[0].length; i++) {
            out.print("--");
        }

        out.println();

        out.print(" ");

        for (int i = 0; i < board[0].length; i++) {
            out.print(" " + (i + 1));
        }

        out.println();
    }

    @Override
    public void exit() {
        out.println("EXIT");
        in.close();
        out.close();
    }

    @Override
    public void displayInsertionResult(TokenInsertionStatus tokenInsertionStatus) {
        switch (tokenInsertionStatus) {
            case OK:
                out.println("OK");
                break;
            case ERROR:
                out.println("ERROR");
                break;
            case WIN:
                out.println("WIN");
                break;
            case DRAW:
                out.println("DRAW");
                break;
        }
    }

    @Override
    public void notifyWrongInput() {
        out.println("ERROR");
    }
}
