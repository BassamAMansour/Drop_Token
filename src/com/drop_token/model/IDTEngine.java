package com.drop_token.model;

import java.util.LinkedList;

public interface IDTEngine {

    /**
     * Calling this function adds the token into the specified column.
     * The engine determines which is the next player to be able to make the play.
     * The returning status code determines the insertion status.
     *
     * @param column Column number required to insert the token into.
     *               Columns indexing start at 0.
     * @return TokenInsertionStatus. Either OK, ERROR, WIN, DRAW
     */
    TokenInsertionStatus insertToken(int column);

    /**
     * Gets the insertion sequence of the tokens into the board.
     * Only counts for the successful insertions.
     *
     * @return LinkedList of the insertion sequence in columns.
     */
    LinkedList<Integer> getInsertionSequence();

    /**
     * @return an int matrix of the tokenIds, which corresponds to the players ids.
     */
    int[][] getBoard();

    /**
     * @return The id of the player who has the turn to play.
     */
    int getCurrentPlayerId();

    /**
     * @return The id of the player who has just finished his play.
     */
    int getPreviousPlayerId();

    GameStatus getGameStatus();

    enum TokenInsertionStatus {OK, ERROR, WIN, DRAW}

    enum GameStatus {PLAYING, FINISHED_WIN, FINISHED_DRAW}
}
