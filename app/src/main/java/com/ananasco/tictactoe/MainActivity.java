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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        game = new Game();

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.resetAction) {
           resetClicked();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("game", game);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        game = (Game) savedInstanceState.getSerializable("game");
        drawBoard();
    }

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
        }

        else {
            Toast.makeText(getApplicationContext(),
                    "Game is over! Press the reset button to play again!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void resetClicked() {
        game = new Game();
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
