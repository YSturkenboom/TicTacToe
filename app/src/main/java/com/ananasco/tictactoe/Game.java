package com.ananasco.tictactoe;

import java.io.Serializable;

/**
 * Created by yuri on 31-3-18.
 */

public class Game implements Serializable {
    final private int BOARD_SIZE = 3;
    private Tile[][] board;

    private Boolean playerOneTurn;  // true if player 1's turn, false if player 2's turn
    private int movesPlayed;
    private Boolean gameOver;
    private GameState gameState;
    private Boolean AI;

    // I adapted the constructor so that the user may choose between an AI
    // and a 1v1 game
    public Game(Boolean aiEnabled) {
        board = new Tile[BOARD_SIZE][BOARD_SIZE];
        for(int i=0; i<BOARD_SIZE; i++)
            for(int j=0; j<BOARD_SIZE; j++)
                board[i][j] = Tile.BLANK;

        playerOneTurn = true;
        gameOver = false;
        movesPlayed = 0;
        gameState = GameState.IN_PROGRESS;
        AI = aiEnabled;
    }

    // This is the game logic part of drawing a circle or a cross.
    public Tile draw(int row, int column) {
        Tile currentState = board[row][column];

        if (currentState == Tile.BLANK) {
            movesPlayed ++;
            if (playerOneTurn) {
                board[row][column] = Tile.CIRCLE;
                playerOneTurn = !playerOneTurn;
                return Tile.CIRCLE;

            } else {
                board[row][column] = Tile.CROSS;
                playerOneTurn = !playerOneTurn;
                return Tile.CROSS;
            }
        }
        else {
            return Tile.INVALID;
        }
    }

    // I'd rather have this private, but because of how return is
    // used in the draw() function, it would lead to repeated code.
    public void detectGameOver(){

        // first, check for win conditions
        if (detectRowWin(0) || detectRowWin(1) ||
                detectRowWin(2)|| detectColWin(0) ||
                detectColWin(1)|| detectColWin(2) ||
                detectDiagWin()) {
            gameOver = true;

            // The turn flips after each draw, so the winner is
            // the person whose turn it is -not-
            if (playerOneTurn) {
                gameState = GameState.PLAYER_TWO;
            }
            else {
                gameState = GameState.PLAYER_ONE;
            }
        }

        // then, check for a draw
        else if (movesPlayed == 9){
            gameState = GameState.DRAW;
        }
    }

    // helper function to detect win conditions
    private Boolean detectColWin(int col){
        return (board[0][col] == board[1][col] &&
                board[1][col] == board[2][col]) &&
                (board[0][col] == Tile.CIRCLE ||
                board[0][col] == Tile.CROSS);
    }

    // helper function to detect win conditions
    private Boolean detectRowWin(int row){
        return (board[row][0] == board[row][1] &&
                board[row][1] == board[row][2]) &&
                (board[row][0] == Tile.CIRCLE ||
                board[row][0] == Tile.CROSS);
    }

    // helper function to detect win conditions
    private Boolean detectDiagWin(){
        //first diagonal
        return ((board[0][0] == board[1][1] &&
                board[1][1] == board[2][2]) &&
                (board[0][0] == Tile.CIRCLE ||
                board[0][0] == Tile.CROSS)) ||

                // second diagonal
                ((board[2][0] == board[1][1] &&
                        board[1][1] == board[0][2]) &&
                        (board[2][0] == Tile.CIRCLE ||
                        board[2][0] == Tile.CROSS));
    }

    // returns the Tile on the board for a given row and column
    public Tile getTileAt(int row, int column) {
        return board[row-1][column-1];
    }

    // public getter function for the GameState
    public GameState getGameState() {
        return gameState;
    }

    // public getter function for whether AI is enabled
    public Boolean getAI(){
        return AI;
    }

    /* This function simulates an "AI move". (right now
     * it just chooses the first empty square it sees)
     *
     * I wanted to implement the smart strategy that always
     * wins, but I think that time may be better spent on
     * the next apps.
     *
     * I return the position in a rather hacky way with an
     * array instead of an object because I forgot for a
     * second that Java doesn't allow multiple return values.
     */
    public int[] makeAIMove() {
        for (int i = 1; i < BOARD_SIZE+1; i++) {
            for (int j = 1; j < BOARD_SIZE+1; j++) {
                if (getTileAt(i, j) == Tile.BLANK) {
                    draw(i-1,j-1);
                    int[] pos = new int[2];
                    pos[0] = i;
                    pos[1] = j;
                    return pos;
                }
            }
        }

        // this should never be reached, but we need a default return value
        return null;
    }
}
