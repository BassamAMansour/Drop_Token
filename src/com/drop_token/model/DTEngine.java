package com.drop_token.model;

import com.drop_token.data_types.Slot;

import java.io.PrintStream;
import java.util.LinkedList;

public class DTEngine implements IDTEngine {

    private static final int DEFAULT_BOARD_SIZE = 4;
    private static final int DEFAULT_PLAYERS_COUNT = 2;
    private static final int DEFAULT_WINNING_STREAK = DEFAULT_BOARD_SIZE;

    private int playersCount;
    private int boardSize;

    private Slot[][] board;
    private int[] insertionIndices;
    private int tokensCount;

    private GameStatus gameStatus;

    private LinkedList<Integer> playersQueue = new LinkedList<>();
    private LinkedList<Integer> insertionSequence = new LinkedList<>();

    private int winningStreak;

    public DTEngine() {
        this(DEFAULT_BOARD_SIZE, DEFAULT_PLAYERS_COUNT, DEFAULT_WINNING_STREAK);
    }

    public DTEngine(int boardSize, int playersCount, int winningStreak) {
        this.boardSize = boardSize;
        this.playersCount = playersCount;
        this.winningStreak = winningStreak;
        this.board = new Slot[boardSize][boardSize];
        this.insertionIndices = new int[boardSize];
        this.gameStatus = GameStatus.PLAYING;

        initBoard();
        initInsertionIndices();
        initPlayers();
    }

    @Override
    public TokenInsertionStatus insertToken(int column) {

        if (gameStatus != GameStatus.PLAYING || !isInsertionValid(column)) {
            return TokenInsertionStatus.ERROR;
        }

        int currentPlayer = getNextPlayerToPlay();

        int insertionRow = insertionIndices[column];

        board[insertionRow][column] = new Slot(currentPlayer);

        boolean hasWon = updateStreaks(insertionRow, column);

        updateCounters(column);

        return getTokenInsertionStatus(hasWon);
    }

    private TokenInsertionStatus getTokenInsertionStatus(boolean hasWon) {

        TokenInsertionStatus tokenInsertionStatus = TokenInsertionStatus.OK;

        if (hasWon) {
            tokenInsertionStatus = TokenInsertionStatus.WIN;
            gameStatus = GameStatus.FINISHED_WIN;
        } else if (tokensCount >= boardSize * boardSize) {
            tokenInsertionStatus = TokenInsertionStatus.DRAW;
            gameStatus = GameStatus.FINISHED_DRAW;
        }

        return tokenInsertionStatus;
    }

    private boolean updateStreaks(int row, int column) {
        if (!areCoordsValid(row, column)) return false;

        return updateVerticalStreak(row, column)
                || updateDiagonalStreak(row, column)
                || updateHorizontalStreak(row, column);
    }

    /**
     * @param column the column to update its insertion index
     *               <p>
     *               This method updates the counters of the game.
     *               Updates the index of the next available slot in the given column.
     *               Updates the count of the tokens inserted into the board
     *               Updates the insertion sequence on the columns into the board
     */
    private void updateCounters(int column) {
        insertionIndices[column]--;
        tokensCount++;
        insertionSequence.addFirst(column + 1);
    }

    /**
     * @param column Slot column
     * @param row    Slot row
     *               <p>
     *               Updates the Horizontal streak of the given slot.
     *               Uses a Dynamic Programming approach;
     *               Storing the streak into a local variable inside the slot and updates it if it's a streak.
     *               Calculates the streak starting from the current slot and updates the streak of the slots on the right.
     *               This has a runtime of O(boardSize).
     * @return if the player won horizontally
     */
    private boolean updateHorizontalStreak(int row, int column) {

        Slot currentSlot = board[row][column];

        currentSlot.setStreakHorizontal(1);

        if (!isAtLeftEdge(column)) {
            Slot leftSlot = board[row][column - 1];
            if (currentSlot.equals(leftSlot))
                currentSlot.setStreakHorizontal(leftSlot.getStreakHorizontal() + 1);
        }

        boolean horizontallyWon = false;

        if (currentSlot.getStreakHorizontal() >= winningStreak) horizontallyWon = true;

        return horizontallyWon || updateHorizontalStreaksOnRight(row, column);
    }


    /**
     * @param column Slot column
     * @param row    Slot row
     *               <p>
     *               Updates the Horizontal streak of the slots to the right the given slot.
     *               This has a runtime of O(boardSize).
     * @return if the player won horizontally
     */
    private boolean updateHorizontalStreaksOnRight(int row, int column) {

        Slot currentSlot = board[row][column];
        Slot nextSlotOnRight;

        int maxHorizontalStreak = currentSlot.getStreakHorizontal();

        for (int columnIndex = column + 1; columnIndex < boardSize; columnIndex++) {

            nextSlotOnRight = board[row][columnIndex];

            if (currentSlot.equals(nextSlotOnRight)) {
                nextSlotOnRight.setStreakHorizontal(currentSlot.getStreakHorizontal() + 1);
                maxHorizontalStreak++;
            } else {
                break;
            }

        }

        return maxHorizontalStreak >= winningStreak;
    }

    /**
     * @param column Slot column
     * @param row    Slot row
     *               <p>
     *               Updates the Diagonal streak of the given slot.
     *               Uses a Dynamic Programming approach;
     *               Storing the streak into a local variable inside the slot and updates it if it's a streak.
     *               Gets the max of streaks of both the left and right diagonals.
     *               This has a runtime of O(1).
     * @return if the player won diagonally
     */
    private boolean updateDiagonalStreak(int row, int column) {

        Slot currentSlot = board[row][column];

        if (isAtBottom(row)) {
            currentSlot.setStreakDiagonal(1);
        } else {
            if (!isAtRightEdge(column)) {
                Slot bottomRightSlot = board[row + 1][column + 1];

                if (currentSlot.equals(bottomRightSlot)) {
                    currentSlot.setStreakDiagonal(
                            Math.max(bottomRightSlot.getStreakDiagonal() + 1, currentSlot.getStreakDiagonal()));
                } else {
                    currentSlot.setStreakDiagonal(1);
                }
            }

            if (!isAtLeftEdge(column)) {
                Slot bottomLeftSlot = board[row + 1][column - 1];

                if (currentSlot.equals(bottomLeftSlot)) {
                    currentSlot.setStreakDiagonal(
                            Math.max(bottomLeftSlot.getStreakDiagonal() + 1, currentSlot.getStreakDiagonal()));
                } else {
                    currentSlot.setStreakDiagonal(1);
                }
            }
        }

        return currentSlot.getStreakDiagonal() >= winningStreak;
    }

    /**
     * @param column Slot column
     * @param row    Slot row
     *               <p>
     *               Updates the vertical streak of the given slot.
     *               Uses a Dynamic Programming approach;
     *               Storing the streak into a local variable inside the slot and updates it if it's a streak.
     *               This has a runtime of O(1).
     * @return if the player won vertically
     */
    private boolean updateVerticalStreak(int row, int column) {
        Slot currentSlot = board[row][column];

        if (isAtBottom(row) || !currentSlot.equals(board[row + 1][column])) {
            currentSlot.setStreakVertical(1);
        } else {
            currentSlot.setStreakVertical(board[row + 1][column].getStreakVertical() + 1);
        }

        return currentSlot.getStreakVertical() >= winningStreak;
    }

    @Override
    public LinkedList<Integer> getInsertionSequence() {
        return insertionSequence;
    }

    @Override
    public int[][] getBoard() {
        int[][] outBoard = new int[board.length][board[0].length];

        for (int i = 0; i < board.length; i++) {
            Slot[] row = board[i];

            for (int j = 0; j < row.length; j++) {
                outBoard[i][j] = row[j].getTokenId();
            }
        }

        return outBoard;
    }

    @Override
    public int getCurrentPlayerId() {
        return playersQueue.getLast();
    }

    @Override
    public int getPreviousPlayerId() {
        return playersQueue.getFirst();
    }

    @Override
    public GameStatus getGameStatus() {
        return gameStatus;
    }

    private int getNextPlayerToPlay() {
        int nextPlayer = playersQueue.pollLast();
        playersQueue.addFirst(nextPlayer);
        return nextPlayer;
    }

    private boolean isInsertionValid(int column) {
        return isColumnValid(column) && insertionIndices[column] >= 0;
    }

    private boolean areCoordsValid(int row, int column) {
        return isRowValid(row) && isColumnValid(column);
    }

    private boolean isColumnValid(int column) {
        return column >= 0 && column < boardSize;
    }

    private boolean isRowValid(int row) {
        return row >= 0 && row < boardSize;
    }

    private void initBoard() {
        for (Slot[] row : board) {
            for (int j = 0; j < row.length; j++) {
                row[j] = new Slot(0);
            }
        }
    }

    private void initInsertionIndices() {
        for (int i = 0; i < insertionIndices.length; i++) {
            insertionIndices[i] = boardSize - 1;
        }
    }

    private void initPlayers() {
        for (int i = 1; i <= playersCount; i++) {
            playersQueue.addFirst(i);
        }
    }

    private boolean isAtBottom(int row) {
        return row == boardSize - 1;
    }

    private boolean isAtRightEdge(int column) {
        return column == boardSize - 1;
    }

    private boolean isAtLeftEdge(int column) {
        return column == 0;
    }


    //Used for debugging purposes
    private void debugBoard() {

        PrintStream out = System.out;

        out.println();

        for (Slot[] row : board) {

            for (int j = 0; j < board[0].length; j++) {
                out.print(" >" + "id:" + row[j].getTokenId() + ", "
                        + "v:" + row[j].getStreakVertical() + ", "
                        + "d:" + row[j].getStreakDiagonal() + ", "
                        + "h:" + row[j].getStreakHorizontal() + "< ");
            }

            out.println();
            out.println();
        }
        out.println();
    }

}
