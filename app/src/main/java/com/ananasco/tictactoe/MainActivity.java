package com.ananasco.tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import static com.ananasco.tictactoe.Tile.BLANK;
import static com.ananasco.tictactoe.Tile.CROSS;

public class MainActivity extends AppCompatActivity {

    Game game;

    // By default, the AI is on. Added code sets up the custom Action Bar
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        game = new Game(true);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }

    // Necessary to inflate the custom menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    // This overridden function handles Action Bar Item selection.
    // The two different buttons start the game in AI or in 1v1 mode.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.resetAction:
                resetClicked();
                return true;
            case R.id.resetAIAction:
                System.out.println("ai");
                resetAIClicked();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // The game state is saved to make games persistent over
    // minimizing the app or rotating the screen
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("game", game);
    }

    // Restores the game state when the application life cycle calls this function
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        game = (Game) savedInstanceState.getSerializable("game");
        drawBoard();
    }

    // Helper function that uses drawTile(Tile, View) to draw the whole board.
    // Used in onRestoreInstanceState.
    public void drawBoard(){
        drawTile(game.getTileAt(1,1), findViewById(R.id.b1));
        drawTile(game.getTileAt(1,2), findViewById(R.id.b2));
        drawTile(game.getTileAt(1,3), findViewById(R.id.b3));
        drawTile(game.getTileAt(2,1), findViewById(R.id.b4));
        drawTile(game.getTileAt(2,2), findViewById(R.id.b5));
        drawTile(game.getTileAt(2,3), findViewById(R.id.b6));
        drawTile(game.getTileAt(3,1), findViewById(R.id.b7));
        drawTile(game.getTileAt(3,2), findViewById(R.id.b8));
        drawTile(game.getTileAt(3,3), findViewById(R.id.b9));
    }

    // Given a Tile and a View (an ImageButton) this function updates graphical
    // representation of the buttons (explosions!)
    public void drawTile(Tile tile, View buttonView){
        switch(tile) {
            case CROSS:
                ((ImageButton)buttonView).setImageResource(R.drawable.boom1);
                break;
            case CIRCLE:
                ((ImageButton)buttonView).setImageResource(R.drawable.boom2);
                break;
            case INVALID:
                Toast.makeText(getApplicationContext(), "Invalid move!", Toast.LENGTH_SHORT).show();
                break;
            case BLANK:
                ((ImageButton)buttonView).setImageResource(android.R.color.transparent);
                break;
        }
    }

    // This function is called when a tile is tapped and finds the corresponding
    // row and column, and calls game.draw() to update the board, and drawTile() to
    // update the UI.
    // It also checks for game over, and performs the AI move after each user move,
    // if AI is enabled.
    public void tileClicked(View view) {

        if (game.getGameState() == GameState.IN_PROGRESS) {
            int id = view.getId();

            //defaults to first button
            int row = 1, col = 1;
            switch (id) {
                case R.id.b1:
                    row = 1;
                    col = 1;
                    break;
                case R.id.b2:
                    row = 1;
                    col = 2;
                    break;
                case R.id.b3:
                    row = 1;
                    col = 3;
                    break;
                case R.id.b4:
                    row = 2;
                    col = 1;
                    break;
                case R.id.b5:
                    row = 2;
                    col = 2;
                    break;
                case R.id.b6:
                    row = 2;
                    col = 3;
                    break;
                case R.id.b7:
                    row = 3;
                    col = 1;
                    break;
                case R.id.b8:
                    row = 3;
                    col = 2;
                    break;
                case R.id.b9:
                    row = 3;
                    col = 3;
                    break;
            }

            // arrays are zero-indexed
            Tile result_tile = game.draw(row - 1, col - 1);
            drawTile(result_tile, view);

            // User interaction is done via Toasts
            game.detectGameOver();
            switch (game.getGameState()) {
                case DRAW:
                    Toast.makeText(getApplicationContext(),
                            "Game over! It's a draw!",
                            Toast.LENGTH_SHORT).show();
                    break;
                case PLAYER_ONE:
                    Toast.makeText(getApplicationContext(),
                            "Game over! Player one won",
                            Toast.LENGTH_SHORT).show();
                    break;
                case PLAYER_TWO:
                    Toast.makeText(getApplicationContext(),
                            "Game over! Player two won",
                            Toast.LENGTH_SHORT).show();
                    break;
            }

            // The AI may only move if enabled, the game is not a draw and the last move
            // wasn't invalid.
            if (game.getAI() && game.getGameState() != GameState.DRAW
                    && game.getGameState() != GameState.PLAYER_ONE
                    && game.getGameState() != GameState.PLAYER_TWO
                    && result_tile != Tile.INVALID){
                int[] pos = game.makeAIMove();
                row = pos[0]-1;
                col = pos[1]-1;

                //defaults to first button
                int bid = R.id.b1;
                if (row == 0){
                    if (col == 0) bid = R.id.b1;
                    if (col == 1) bid = R.id.b2;
                    if (col == 2) bid = R.id.b3;
                }
                if (row == 1){
                    if (col == 0) bid = R.id.b4;
                    if (col == 1) bid = R.id.b5;
                    if (col == 2) bid = R.id.b6;
                }
                if (row == 2){
                    if (col == 0) bid = R.id.b7;
                    if (col == 1) bid = R.id.b8;
                    if (col == 2) bid = R.id.b9;
                }

                drawTile(Tile.CROSS, findViewById(bid));
            }
        }

        else {
            Toast.makeText(getApplicationContext(),
                    "Game is over! Press the reset button to play again!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // This function is called when the reset button is pressed, and wipes the board
    // The game is then reset with AI -disabled-
    public void resetClicked() {
        game = new Game(false);
        drawTile(Tile.BLANK, findViewById(R.id.b1));
        drawTile(Tile.BLANK, findViewById(R.id.b2));
        drawTile(Tile.BLANK, findViewById(R.id.b3));
        drawTile(Tile.BLANK, findViewById(R.id.b4));
        drawTile(Tile.BLANK, findViewById(R.id.b5));
        drawTile(Tile.BLANK, findViewById(R.id.b6));
        drawTile(Tile.BLANK, findViewById(R.id.b7));
        drawTile(Tile.BLANK, findViewById(R.id.b8));
        drawTile(Tile.BLANK, findViewById(R.id.b9));

    }

    // This function is called when the reset button is pressed, and wipes the board
    // The game is then reset with AI -enabled-
    public void resetAIClicked() {
        game = new Game(true);
        drawTile(Tile.BLANK, findViewById(R.id.b1));
        drawTile(Tile.BLANK, findViewById(R.id.b2));
        drawTile(Tile.BLANK, findViewById(R.id.b3));
        drawTile(Tile.BLANK, findViewById(R.id.b4));
        drawTile(Tile.BLANK, findViewById(R.id.b5));
        drawTile(Tile.BLANK, findViewById(R.id.b6));
        drawTile(Tile.BLANK, findViewById(R.id.b7));
        drawTile(Tile.BLANK, findViewById(R.id.b8));
        drawTile(Tile.BLANK, findViewById(R.id.b9));

    }

}
