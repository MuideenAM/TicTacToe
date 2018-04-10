package ng.com.mamconsulting.tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // Declare Button variables to store instances of variables.
    Button button00, button01, button02, button10, button11, button12, button20, button21, button22;
    Button button_play_stop, button_reset, button_board_change, button_player_select ;
    int turn; // Variable to change the turns of variables
    boolean end = false; // Variable to detect when the game ends
    String player_type = "double";
    String[][] board_cells = new String[3][3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Store the instances of the various buttons for their easy access.
        button00 = findViewById(R.id.button00);
        button01 = findViewById(R.id.button01);
        button02 = findViewById(R.id.button02);
        button10 = findViewById(R.id.button10);
        button11 = findViewById(R.id.button11);
        button12 = findViewById(R.id.button12);
        button20 = findViewById(R.id.button20);
        button21 = findViewById(R.id.button21);
        button22 = findViewById(R.id.button22);
        button_board_change = findViewById(R.id.board_button);
        button_play_stop = findViewById(R.id.play_stop_button);
        button_reset = findViewById(R.id.reset_button);
        button_player_select = findViewById(R.id.player_button);

        turn = 1;

        initialiseGame();

        /*
         * Listen for Board Layout change from 3 X 3 to 5 X 5 and vice versa.
         */
        button_board_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( button_board_change.getText().toString().equals(getString(R.string._5by5)) ){
                    button_board_change.setText(getString(R.string._3by3));
                }else{
                    button_board_change.setText(getString(R.string._5by5));
                }
            }
        });

        /*
         * Listen for Play/Stop button click event.
         */
        button_play_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                end = !end;
                if(end){
                    endGame();
                    button_play_stop.setText(getString(R.string.play));
                }else{
                    endGame();
                    button_play_stop.setText(getString(R.string.stop));
                }
            }
        });

        /*
         * Listen for Reset button click event.
         */
        button_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button00.setText("");
                button01.setText("");
                button02.setText("");
                button10.setText("");
                button11.setText("");
                button12.setText("");
                button20.setText("");
                button21.setText("");
                button22.setText("");
                button_play_stop.setText(getString(R.string.play));
            }
        });

        /*
         * Listen for Player Select (from double players to single and vice versa) button click event.
         */
        button_player_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(button_player_select.getText().toString().equals("2 Players")){
                    button_player_select.setText(getString(R.string.single_player));
                    player_type = "double";
                }else{
                    button_player_select.setText(getString(R.string.double_players));
                    player_type = "single";
                }
            }
        });

        /*
         * Takes care of row 1 column 1  of the board.
         */
        button00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player_type.equals("double")){
                    if(button00.getText().toString().equals("")){
                        if(turn == 1){
                            turn = 2;
                            button00.setText("X");
                        }else if(turn == 2){
                            turn = 1;
                            button00.setText("O");
                        }
                    }
                }else{
                    if(button00.getText().toString().equals("")){
                        button00.setText("X");
                        board_cells[0][0] = "X";
                        endGame();
                        if(!end) {
                            computerPlay();
                        }
                    }
                }

                endGame();
            }
        });

        /*
         * Takes care of  row 1 column 2 of the board.
         */
        button01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player_type.equals("double")){
                    if(button01.getText().toString().equals("")){
                        if(turn == 1){
                            turn = 2;
                            button01.setText("X");
                        }else if(turn == 2){
                            turn = 1;
                            button01.setText("O");
                        }
                    }
                }else {
                    if (button01.getText().toString().equals("")) {
                        button01.setText("X");
                        board_cells[0][1] = "X";
                        endGame();
                        if(!end) {
                            computerPlay();
                        }
                    }
                }
                endGame();
            }
        });

        /*
         * Takes care of row 1 column 3  of the board.
         */
        button02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player_type.equals("double")) {
                    if (button02.getText().toString().equals("")) {
                        if (turn == 1) {
                            turn = 2;
                            button02.setText("X");
                        } else if (turn == 2) {
                            turn = 1;
                            button02.setText("O");
                        }
                    }
                }
                else{
                    if(button02.getText().toString().equals("")){
                        button02.setText("X");
                        board_cells[0][2] = "X";
                        endGame();
                        if(!end) {
                            computerPlay();
                        }
                    }
                }
                endGame();
            }
        });

        /*
         * Takes care of row 2 column 1  of the board.
         */
        button10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player_type.equals("double")) {
                    if (button10.getText().toString().equals("")) {
                        if (turn == 1) {
                            turn = 2;
                            button10.setText("X");
                        } else if (turn == 2) {
                            turn = 1;
                            button10.setText("O");
                        }
                    }
                }else{
                    if(button10.getText().toString().equals("")){
                        button10.setText("X");
                        board_cells[1][0] = "X";
                        endGame();
                        if(!end) {
                            computerPlay();
                        }
                    }
                }
                endGame();
            }
        });

        /*
         * Takes care of row 2 column 2  of the board.
         */
        button11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player_type.equals("double")) {
                    if (button11.getText().toString().equals("")) {
                        if (turn == 1) {
                            turn = 2;
                            button11.setText("X");
                        } else if (turn == 2) {
                            turn = 1;
                            button11.setText("O");
                        }
                    }
                }else{
                    if(button11.getText().toString().equals("")){
                        button11.setText("X");
                        board_cells[1][1] = "X";
                        endGame();
                        if(!end) {
                            computerPlay();
                        }
                    }
                }
                endGame();
            }
        });

        /*
         * Takes care of row 2 column 3  of the board.
         */
        button12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player_type.equals("double")) {
                    if (button12.getText().toString().equals("")) {
                        if (turn == 1) {
                            turn = 2;
                            button12.setText("X");
                        } else if (turn == 2) {
                            turn = 1;
                            button12.setText("O");
                        }
                    }
                }else{
                    if(button12.getText().toString().equals("")){
                        button12.setText("X");
                        board_cells[1][2] = "X";
                        endGame();
                        if(!end) {
                            computerPlay();
                        }
                    }
                }
                endGame();
            }
        });

        /*
         * Takes care of row 3 column 1  of the board.
         */
        button20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player_type.equals("double")) {
                    if (button20.getText().toString().equals("")) {
                        if (turn == 1) {
                            turn = 2;
                            button20.setText("X");
                        } else if (turn == 2) {
                            turn = 1;
                            button20.setText("O");
                        }
                    }
                }else{
                    if(button20.getText().toString().equals("")){
                        button20.setText("X");
                        board_cells[2][0] = "X";
                        endGame();
                        if(!end) {
                            computerPlay();
                        }
                    }
                }
                endGame();
            }
        });

        /*
         * Takes care of row 3 column 2  of the board.
         */
        button21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player_type.equals("double")) {
                    if (button21.getText().toString().equals("")) {
                        if (turn == 1) {
                            turn = 2;
                            button21.setText("X");
                        } else if (turn == 2) {
                            turn = 1;
                            button21.setText("O");
                        }
                    }
                }else{
                    if(button21.getText().toString().equals("")){
                        button21.setText("X");
                        board_cells[2][1] = "X";
                        endGame();
                        if(!end) {
                            computerPlay();
                        }
                    }
                }
                endGame();
            }
        });

        /*
         * Takes care of row 3 column 3  of the board.
         */
        button22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player_type.equals("double")) {
                    if (button22.getText().toString().equals("")) {
                        if (turn == 1) {
                            turn = 2;
                            button22.setText("X");
                        } else if (turn == 2) {
                            turn = 1;
                            button22.setText("O");
                        }
                    }
                }else{
                    if(button22.getText().toString().equals("")){
                        button22.setText("X");
                        board_cells[2][2] = "X";
                        endGame();
                        if(!end) {
                            computerPlay();
                        }
                    }
                }
                endGame();
            }
        });

    }

    /*
     * Initialises the board by disabling game play mode. It also informs the Player(s) of the actions to take in order to play game.
     */
    public void initialiseGame(){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                board_cells[i][j] = "";
            }
        }
        end = true;
        button00.setEnabled(false);
        button01.setEnabled(false);
        button02.setEnabled(false);
        button10.setEnabled(false);
        button11.setEnabled(false);
        button12.setEnabled(false);
        button20.setEnabled(false);
        button21.setEnabled(false);
        button22.setEnabled(false);
        Toast.makeText(MainActivity.this,"Click Play button to start playing.",Toast.LENGTH_LONG ).show();
    }

    /*
     * Determines when the game ends or otherwise. It also activates the board or disables it as necessary.
     */
    public void endGame(){
        String val00, val01, val02, val10, val11,  val12, val20, val21, val22;
        //boolean end = false;

        val00 = button00.getText().toString();
        val01 = button01.getText().toString();
        val02 = button02.getText().toString();
        val10 = button10.getText().toString();
        val11 = button11.getText().toString();
        val12 = button12.getText().toString();
        val20 = button20.getText().toString();
        val21 = button21.getText().toString();
        val22 = button22.getText().toString();

        if(fullyFilled(board_cells)){
            Toast.makeText(MainActivity.this, "Board Fully filled!", Toast.LENGTH_LONG).show();
            return;
        }
        //First row winning for X
        if (val00.equals("X") && val01.equals("X") & val02.equals("X")){
            Toast.makeText(MainActivity.this, "Winner Player X!", Toast.LENGTH_LONG).show();
            end = true;
        }
        //First column winning for X
        if (val00.equals("X") && val10.equals("X") & val20.equals("X")){
            Toast.makeText(MainActivity.this, "Winner Player X!", Toast.LENGTH_LONG).show();
            end = true;
        }
        //Second row winning for X
        if (val10.equals("X") && val11.equals("X") & val12.equals("X")){
            Toast.makeText(MainActivity.this, "Winner Player X!", Toast.LENGTH_LONG).show();
            end = true;
        }
        //Second column winning for X
        if (val01.equals("X") && val11.equals("X") & val21.equals("X")){
            Toast.makeText(MainActivity.this, "Winner Player X!", Toast.LENGTH_LONG).show();
            end = true;
        }
        //Third row winning for X
        if (val20.equals("X") && val21.equals("X") & val22.equals("X")) {
            Toast.makeText(MainActivity.this, "Winner Player X!", Toast.LENGTH_LONG).show();
            end = true;
        }
        //Third column winning for X
        if (val02.equals("X") && val12.equals("X") & val22.equals("X")){
            Toast.makeText(MainActivity.this, "Winner Player X!", Toast.LENGTH_LONG).show();
            end = true;
        }
        //Leading diagonal winning for X
        if (val00.equals("X") && val11.equals("X") & val22.equals("X")){
            Toast.makeText(MainActivity.this, "Winner Player X!", Toast.LENGTH_LONG).show();
            end = true;
        }
        //Reversed diagonal winning for X
        if (val02.equals("X") && val11.equals("X") & val20.equals("X")){
            Toast.makeText(MainActivity.this, "Winner Player X!", Toast.LENGTH_LONG).show();
            end = true;
        }

        //First row winning for O
        if (val00.equals("O") && val01.equals("O") & val02.equals("O")){
            Toast.makeText(MainActivity.this, "Winner Player O!", Toast.LENGTH_LONG).show();
            end = true;
        }
        //First column winning for X
        if (val00.equals("O") && val10.equals("O") & val20.equals("O")){
            Toast.makeText(MainActivity.this, "Winner Player O!", Toast.LENGTH_LONG).show();
            end = true;
        }
        //Second row winning for O
        if (val10.equals("O") && val11.equals("O") & val12.equals("O")){
            Toast.makeText(MainActivity.this, "Winner Player O!", Toast.LENGTH_LONG).show();
            end = true;
        }
        //Second column winning for X
        if (val01.equals("O") && val11.equals("O") & val21.equals("O")){
            Toast.makeText(MainActivity.this, "Winner Player O!", Toast.LENGTH_LONG).show();
            end = true;
        }
        //Third row winning for X
        if (val20.equals("O") && val21.equals("O") & val22.equals("O")) {
            Toast.makeText(MainActivity.this, "Winner Player O!", Toast.LENGTH_LONG).show();
            end = true;
        }
        //Third column winning for X
        if (val02.equals("O") && val12.equals("O") & val22.equals("O")){
            Toast.makeText(MainActivity.this, "Winner Player O!", Toast.LENGTH_LONG).show();
            end = true;
        }
        //Leading diagonal winning for X
        if (val00.equals("O") && val11.equals("O") & val22.equals("O")){
            Toast.makeText(MainActivity.this, "Winner Player O!", Toast.LENGTH_LONG).show();
            end = true;
        }
        //Reversed diagonal winning for X
        if (val02.equals("O") && val11.equals("O") & val20.equals("O")){
            Toast.makeText(MainActivity.this, "Winner Player O!", Toast.LENGTH_LONG).show();
            end = true;
        }

        if(end){
            button00.setEnabled(false);
            button01.setEnabled(false);
            button02.setEnabled(false);
            button10.setEnabled(false);
            button11.setEnabled(false);
            button12.setEnabled(false);
            button20.setEnabled(false);
            button21.setEnabled(false);
            button22.setEnabled(false);
            //button_play_stop.setEnabled(true);
        }else{
            button00.setEnabled(true);
            button01.setEnabled(true);
            button02.setEnabled(true);
            button10.setEnabled(true);
            button11.setEnabled(true);
            button12.setEnabled(true);
            button20.setEnabled(true);
            button21.setEnabled(true);
            button22.setEnabled(true);
        }
    }

    public void computerPlay(){
        boolean played = false;
        while(!played){
            Random rand = new Random();
            int row = rand.nextInt(3),
                    col = rand.nextInt(3);
            if(board_cells[row][col].equals("")){
                switch (row){
                    case 0:
                        if(col == 0){
                            button00.setText("O");
                            played = true;
                        }else if(col == 1){
                            button01.setText("O");
                            played = true;
                        }else{
                            button02.setText("O");
                            played = true;
                        }
                        break;
                    case 1:
                        if(col == 0){
                            button10.setText("O");
                            played = true;
                        }else if(col == 1){
                            button11.setText("O");
                            played = true;
                        }else{
                            button12.setText("O");
                            played = true;
                        }
                        break;
                    case 2:
                        if(col == 0){
                            button20.setText("O");
                            played = true;
                        }else if(col == 1){
                            button21.setText("O");
                            played = true;
                        }else{
                            button22.setText("O");
                            played = true;
                        }
                        break;
                }
            }
        }
    }

    public boolean fullyFilled(String[][] cells){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(cells[i][j].equals("")){
                    return false;
                }
            }
        }
        return true;
    }
}
