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

    public Game() {
        board = new Tile[BOARD_SIZE][BOARD_SIZE];
        for(int i=0; i<BOARD_SIZE; i++)
            for(int j=0; j<BOARD_SIZE; j++)
                board[i][j] = Tile.BLANK;

        playerOneTurn = true;
        gameOver = false;
        movesPlayed = 0;
        gameState = GameState.IN_PROGRESS;
    }

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
            if (playerOneTurn) {
                gameState = GameState.PLAYER_TWO;
            }
            else {
                gameState = GameState.PLAYER_ONE;
            }
        }

        // then, check for a draw
        if (movesPlayed == 9){
            gameState = GameState.DRAW;
        }
    }

    private Boolean detectColWin(int col){
        return (board[0][col] == board[1][col] &&
                board[1][col] == board[2][col]) &&
                (board[0][col] == Tile.CIRCLE ||
                board[0][col] == Tile.CROSS);
    }

    private Boolean detectRowWin(int row){
        System.out.println(board[row][0]);
        System.out.println(board[row][1]);
        System.out.println(board[row][2]);
        System.out.println(board[row][0] == board[row][1]);
        return (board[row][0] == board[row][1] &&
                board[row][1] == board[row][2]) &&
                (board[row][0] == Tile.CIRCLE ||
                board[row][0] == Tile.CROSS);
    }

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

    public Tile getTileAt(int row, int column) {
        return board[row-1][column-1];
    }

    public GameState getGameState() {
        return gameState;
    }
}
