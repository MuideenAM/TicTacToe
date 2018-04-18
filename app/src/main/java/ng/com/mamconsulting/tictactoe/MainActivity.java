package ng.com.mamconsulting.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // Declare Button variables to store instances of variables.
    Button button00, button01, button02, button10, button11, button12, button20, button21, button22;
    Button button_play_stop, button_reset, button_board_change, button_player_select ;
    int turn; // Variable to change the turns of variables
    boolean end = false, point_taken = false; // Variable to detect when the game ends
    String player_type = "double", player1_marker = "", player2_marker="";
    String[][] board_cells = new String[3][3];
    TextView player1_point, player2, player2_point;
    Spinner player1_spinner, player2_spinner;

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
        player2 = findViewById(R.id.textview_player2);
        player1_point = findViewById(R.id.textview_player1_point);
        player2_point = findViewById(R.id.textview_player2_point);
        player1_spinner = findViewById(R.id.spinner_player1);
        player2_spinner = findViewById(R.id.spinner_player2);

        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.player_marker, android.R.layout.simple_spinner_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //player1_spinner.setAdapter(adapter);
        //player2_spinner.setAdapter(adapter);
        player1_spinner.setSelection(0);
        player1_marker = getString(R.string.x);
        player2_spinner.setSelection(1);
        player2_marker = getString(R.string.o);

        Intent  intent = getIntent();
        String EXTRA_MESSAGE = "";

        if( !(intent.getStringArrayExtra(EXTRA_MESSAGE) == null)){
            String[] controlSettings = intent.getStringArrayExtra(EXTRA_MESSAGE);

            button_board_change.setText(getString(R.string._5by5));
            button_play_stop.setText(getString(R.string.play));
            button_reset.setText(controlSettings[0]);
            button_player_select.setText(controlSettings[1]);
        }

        turn = 1;

        initialiseGame();

        /*
         * Listen for Board Layout change from 3 X 3 to 5 X 5 and vice versa.
         */
        button_board_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), _5by5Board.class);
                String[] controlSettings = new String[2];
                String EXTRA_MESSAGE = "";

                controlSettings[0] = button_reset.getText().toString();
                controlSettings[1] = button_player_select.getText().toString();

                intent.putExtra(EXTRA_MESSAGE, controlSettings);

                startActivity(intent);
                button_board_change.setText(getString(R.string._3by3));
            }
        });

        /*
         * Listen for Play/Stop button click event.
         */
        button_play_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                end = !end;
                if(end && point_taken){
                    endGame();
                    button_play_stop.setText(getString(R.string.play));
                    point_taken = false;
                    Toast.makeText(getApplicationContext(), "Reset the board to start new game.", Toast.LENGTH_LONG).show();
                }else if(end){
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
                for(int i = 0; i < 3; i++){
                    for(int j = 0; j < 3; j++){
                        board_cells[i][j] = "";
                    }
                }
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
                point_taken = false;
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
                    player2.setText(getString(R.string.player_label));
                    player1_point.setText("0");
                    player2_point.setText("0");
                }else{
                    button_player_select.setText(getString(R.string.double_players));
                    player_type = "single";
                    player2.setText(getString(R.string.computer_label));
                    player1_point.setText("0");
                    player2_point.setText("0");
                }
            }
        });

        player1_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(player1_spinner.getSelectedItemPosition() == 0){
                    player2_spinner.setSelection(1);
                    player1_marker = getString(R.string.x);
                    player2_marker = getString(R.string.o);
                }else if(player1_spinner.getSelectedItemPosition() == 1){
                    player2_spinner.setSelection(0);
                    player1_marker = getString(R.string.o);
                    player2_marker = getString(R.string.x);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        player2_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(player2_spinner.getSelectedItemPosition() == 0){
                    player1_spinner.setSelection(1);
                    player1_marker = getString(R.string.o);
                    player2_marker = getString(R.string.x);
                }else if(player2_spinner.getSelectedItemPosition() == 1){
                    player1_spinner.setSelection(0);
                    player1_marker = getString(R.string.x);
                    player2_marker = getString(R.string.o);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
                            button00.setText(player1_marker);
                        }else if(turn == 2){
                            turn = 1;
                            button00.setText(player2_marker);
                        }
                    }
                }else{
                    if(button00.getText().toString().equals("")){
                        button00.setText(player1_marker);
                        board_cells[0][0] = player1_marker;
                        endGame();
                        if(!end) {
                            if(!(intelligentBlocking() || intelligentWinning())) {
                                computerPlay();
                            }
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
                            button01.setText(player1_marker);
                        }else if(turn == 2){
                            turn = 1;
                            button01.setText(player2_marker);
                        }
                    }
                }else {
                    if (button01.getText().toString().equals("")) {
                        button01.setText(player1_marker);
                        board_cells[0][1] = player1_marker;
                        endGame();
                        if(!end) {
                            if(!(intelligentBlocking() || intelligentWinning())) {
                                computerPlay();
                            }
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
                            button02.setText(player1_marker);
                        } else if (turn == 2) {
                            turn = 1;
                            button02.setText(player2_marker);
                        }
                    }
                }
                else{
                    if(button02.getText().toString().equals("")){
                        button02.setText(player1_marker);
                        board_cells[0][2] = player1_marker;
                        endGame();
                        if(!end) {
                            if(!(intelligentBlocking() || intelligentWinning())) {
                                computerPlay();
                            }
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
                            button10.setText(player1_marker);
                        } else if (turn == 2) {
                            turn = 1;
                            button10.setText(player2_marker);
                        }
                    }
                }else{
                    if(button10.getText().toString().equals("")){
                        button10.setText(player1_marker);
                        board_cells[1][0] = player1_marker;
                        endGame();
                        if(!end) {
                            if(!(intelligentBlocking() || intelligentWinning())) {
                                computerPlay();
                            }
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
                            button11.setText(player1_marker);
                        } else if (turn == 2) {
                            turn = 1;
                            button11.setText(player2_marker);
                        }
                    }
                }else{
                    if(button11.getText().toString().equals("")){
                        button11.setText(player1_marker);
                        board_cells[1][1] = player1_marker;
                        endGame();
                        if(!end) {
                            if(!(intelligentBlocking() || intelligentWinning())) {
                                computerPlay();
                            }
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
                            button12.setText(player1_marker);
                        } else if (turn == 2) {
                            turn = 1;
                            button12.setText(player2_marker);
                        }
                    }
                }else{
                    if(button12.getText().toString().equals("")){
                        button12.setText(player1_marker);
                        board_cells[1][2] = player1_marker;
                        endGame();
                        if(!end) {
                            if(!(intelligentBlocking() || intelligentWinning())) {
                                computerPlay();
                            }
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
                            button20.setText(player1_marker);
                        } else if (turn == 2) {
                            turn = 1;
                            button20.setText(player2_marker);
                        }
                    }
                }else{
                    if(button20.getText().toString().equals("")){
                        button20.setText(player1_marker);
                        board_cells[2][0] = player1_marker;
                        endGame();
                        if(!end) {
                            if(!(intelligentBlocking() || intelligentWinning())) {
                                computerPlay();
                            }
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
                            button21.setText(player1_marker);
                        } else if (turn == 2) {
                            turn = 1;
                            button21.setText(player2_marker);
                        }
                    }
                }else{
                    if(button21.getText().toString().equals("")){
                        button21.setText(player1_marker);
                        board_cells[2][1] = player1_marker;
                        endGame();
                        if(!end) {
                            if(!(intelligentBlocking() || intelligentWinning())) {
                                computerPlay();
                            }
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
                            button22.setText(player1_marker);
                        } else if (turn == 2) {
                            turn = 1;
                            button22.setText(player2_marker);
                        }
                    }
                }else{
                    if(button22.getText().toString().equals("")){
                        button22.setText(player1_marker);
                        board_cells[2][2] = player1_marker;
                        endGame();
                        if(!end) {
                            if(!(intelligentBlocking() || intelligentWinning())) {
                                computerPlay();
                            }
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

        if(point_taken){
            return;
        }

        //First row winning for Player 1
        if (val00.equals(player1_marker) && val01.equals(player1_marker) && val02.equals(player1_marker)){
            Toast.makeText(MainActivity.this, "Winner Player " + player1_marker + "!", Toast.LENGTH_LONG).show();
            end = true;
            player1_point.setText(String.valueOf(Integer.parseInt(player1_point.getText().toString()) + 1));
            point_taken = true;
        }
        //First column winning for Player 1
        if (val00.equals(player1_marker) && val10.equals(player1_marker) && val20.equals(player1_marker)){
            Toast.makeText(MainActivity.this, "Winner Player " + player1_marker + "!", Toast.LENGTH_LONG).show();
            end = true;
            player1_point.setText(String.valueOf(Integer.parseInt(player1_point.getText().toString()) + 1));
            point_taken = true;
        }
        //Second row winning for Player 1
        if (val10.equals(player1_marker) && val11.equals(player1_marker) && val12.equals(player1_marker)){
            Toast.makeText(MainActivity.this, "Winner Player " + player1_marker + "!", Toast.LENGTH_LONG).show();
            end = true;
            player1_point.setText(String.valueOf(Integer.parseInt(player1_point.getText().toString()) + 1));
            point_taken = true;
        }
        //Second column winning for Player 1
        if (val01.equals(player1_marker) && val11.equals(player1_marker) && val21.equals(player1_marker)){
            Toast.makeText(MainActivity.this, "Winner Player " + player1_marker + "!", Toast.LENGTH_LONG).show();
            end = true;
            player1_point.setText(String.valueOf(Integer.parseInt(player1_point.getText().toString()) + 1));
            point_taken = true;
        }
        //Third row winning for Player 1
        if (val20.equals(player1_marker) && val21.equals(player1_marker) && val22.equals(player1_marker)) {
            Toast.makeText(MainActivity.this, "Winner Player " + player1_marker + "!", Toast.LENGTH_LONG).show();
            end = true;
            player1_point.setText(String.valueOf(Integer.parseInt(player1_point.getText().toString()) + 1));
            point_taken = true;
        }
        //Third column winning for Player 1
        if (val02.equals(player1_marker) && val12.equals(player1_marker) && val22.equals(player1_marker)){
            Toast.makeText(MainActivity.this, "Winner Player " + player1_marker + "!", Toast.LENGTH_LONG).show();
            end = true;
            player1_point.setText(String.valueOf(Integer.parseInt(player1_point.getText().toString()) + 1));
            point_taken = true;
        }
        //Leading diagonal winning for Player 1
        if (val00.equals(player1_marker) && val11.equals(player1_marker) && val22.equals(player1_marker)){
            Toast.makeText(MainActivity.this, "Winner Player " + player1_marker + "!", Toast.LENGTH_LONG).show();
            end = true;
            player1_point.setText(String.valueOf(Integer.parseInt(player1_point.getText().toString()) + 1));
            point_taken = true;
        }
        //Reversed diagonal winning for Player 1
        if (val02.equals(player1_marker) && val11.equals(player1_marker) && val20.equals(player1_marker)){
            Toast.makeText(MainActivity.this, "Winner Player " + player1_marker + "!", Toast.LENGTH_LONG).show();
            end = true;
            player1_point.setText(String.valueOf(Integer.parseInt(player1_point.getText().toString()) + 1));
            point_taken = true;
        }

        //First row winning for Player 2
        if (val00.equals(player2_marker) && val01.equals(player2_marker) && val02.equals(player2_marker)){
            Toast.makeText(MainActivity.this, "Winner Player " + player2_marker + "!", Toast.LENGTH_LONG).show();
            end = true;
            player2_point.setText(String.valueOf(Integer.parseInt(player2_point.getText().toString()) + 1));
            point_taken = true;
        }
        //First column winning for Player 2
        if (val00.equals(player2_marker) && val10.equals(player2_marker) && val20.equals(player2_marker)){
            Toast.makeText(MainActivity.this, "Winner Player " + player2_marker + "!", Toast.LENGTH_LONG).show();
            end = true;
            player2_point.setText(String.valueOf(Integer.parseInt(player2_point.getText().toString()) + 1));
            point_taken = true;
        }
        //Second row winning for Player 2
        if (val10.equals(player2_marker) && val11.equals(player2_marker) && val12.equals(player2_marker)){
            Toast.makeText(MainActivity.this, "Winner Player " + player2_marker + "!", Toast.LENGTH_LONG).show();
            end = true;
            player2_point.setText(String.valueOf(Integer.parseInt(player2_point.getText().toString()) + 1));
            point_taken = true;
        }
        //Second column winning for Player 2
        if (val01.equals(player2_marker) && val11.equals(player2_marker) && val21.equals(player2_marker)){
            Toast.makeText(MainActivity.this, "Winner Player " + player2_marker + "!", Toast.LENGTH_LONG).show();
            end = true;
            player2_point.setText(String.valueOf(Integer.parseInt(player2_point.getText().toString()) + 1));
            point_taken = true;
        }
        //Third row winning for Player 2
        if (val20.equals(player2_marker) && val21.equals(player2_marker) && val22.equals(player2_marker)) {
            Toast.makeText(MainActivity.this, "Winner Player " + player2_marker + "!", Toast.LENGTH_LONG).show();
            end = true;
            player2_point.setText(String.valueOf(Integer.parseInt(player2_point.getText().toString()) + 1));
            point_taken = true;
        }
        //Third column winning for Player 2
        if (val02.equals(player2_marker) && val12.equals(player2_marker) && val22.equals(player2_marker)){
            Toast.makeText(MainActivity.this, "Winner Player " + player2_marker + "!", Toast.LENGTH_LONG).show();
            end = true;
            player2_point.setText(String.valueOf(Integer.parseInt(player2_point.getText().toString()) + 1));
            point_taken = true;
        }
        //Leading diagonal winning for Player 2
        if (val00.equals(player2_marker) && val11.equals(player2_marker) && val22.equals(player2_marker)){
            Toast.makeText(MainActivity.this, "Winner Player " + player2_marker + "!", Toast.LENGTH_LONG).show();
            end = true;
            player2_point.setText(String.valueOf((Integer.parseInt(player2_point.getText().toString()) + 1)));
            point_taken = true;
        }
        //Reversed diagonal winning for Player 2
        if (val02.equals(player2_marker) && val11.equals(player2_marker) && val20.equals(player2_marker)){
            Toast.makeText(MainActivity.this, "Winner Player " + player2_marker + "!", Toast.LENGTH_LONG).show();
            end = true;
            player2_point.setText(String.valueOf(Integer.parseInt(player2_point.getText().toString()) + 1));
            point_taken = true;
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
                            button00.setText(player2_marker);
                            board_cells[row][col] = player2_marker;
                            played = true;
                        }else if(col == 1){
                            button01.setText(player2_marker);
                            board_cells[row][col] = player2_marker;
                            played = true;
                        }else{
                            button02.setText(player2_marker);
                            board_cells[row][col] = player2_marker;
                            played = true;
                        }
                        break;
                    case 1:
                        if(col == 0){
                            button10.setText(player2_marker);
                            board_cells[row][col] = player2_marker;
                            played = true;
                        }else if(col == 1){
                            button11.setText(player2_marker);
                            board_cells[row][col] = player2_marker;
                            played = true;
                        }else{
                            button12.setText(player2_marker);
                            board_cells[row][col] = player2_marker;
                            played = true;
                        }
                        break;
                    case 2:
                        if(col == 0){
                            button20.setText(player2_marker);
                            board_cells[row][col] = player2_marker;
                            played = true;
                        }else if(col == 1){
                            button21.setText(player2_marker);
                            board_cells[row][col] = player2_marker;
                            played = true;
                        }else{
                            button22.setText(player2_marker);
                            board_cells[row][col] = player2_marker;
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

    public boolean intelligentWinning(){
        if(button00.getText().toString().equals(player2_marker) && button01.getText().toString().equals(player2_marker) && button02.getText().toString().equals("")){
            button02.setText(player2_marker);
            return true;
        }else if(button00.getText().toString().equals(player2_marker) && button02.getText().toString().equals(player2_marker) && button01.getText().toString().equals("")){
            button01.setText(player2_marker);
            return true;
        }else if(button01.getText().toString().equals(player2_marker) && button02.getText().toString().equals(player2_marker) && button00.getText().toString().equals("")){
            button00.setText(player2_marker);
            return true;
        }else if(button10.getText().toString().equals(player2_marker) && button11.getText().toString().equals(player2_marker) && button12.getText().toString().equals("")){
            button12.setText(player2_marker);
            return true;
        }else if(button10.getText().toString().equals(player2_marker) && button12.getText().toString().equals(player2_marker) && button11.getText().toString().equals("")){
            button11.setText(player2_marker);
            return true;
        }else if(button11.getText().toString().equals(player2_marker) && button12.getText().toString().equals(player2_marker) && button10.getText().toString().equals("")){
            button10.setText(player2_marker);
            return true;
        }else if(button20.getText().toString().equals(player2_marker) && button21.getText().toString().equals(player2_marker) && button22.getText().toString().equals("")) {
            button22.setText(player2_marker);
            return true;
        }else if(button20.getText().toString().equals(player2_marker) && button22.getText().toString().equals(player2_marker) && button21.getText().toString().equals("")){
            button21.setText(player2_marker);
            return true;
        }else if(button21.getText().toString().equals(player2_marker) && button22.getText().toString().equals(player2_marker) && button20.getText().toString().equals("")){
            button20.setText(player2_marker);
            return true;
        }else if(button00.getText().toString().equals(player2_marker) && button10.getText().toString().equals(player2_marker) && button20.getText().toString().equals("")){
            button20.setText(player2_marker);
            return true;
        }else if(button00.getText().toString().equals(player2_marker) && button20.getText().toString().equals(player2_marker) && button10.getText().toString().equals("")){
            button10.setText(player2_marker);
            return true;
        }else if(button10.getText().toString().equals(player2_marker) && button20.getText().toString().equals(player2_marker) && button00.getText().toString().equals("")){
            button00.setText(player2_marker);
            return true;
        }else if(button01.getText().toString().equals(player2_marker) && button11.getText().toString().equals(player2_marker) && button21.getText().toString().equals("")){
            button21.setText(player2_marker);
            return true;
        }else if(button01.getText().toString().equals(player2_marker) && button21.getText().toString().equals(player2_marker) && button11.getText().toString().equals("")){
            button11.setText(player2_marker);
            return true;
        }else if(button11.getText().toString().equals(player2_marker) && button21.getText().toString().equals(player2_marker) && button01.getText().toString().equals("")){
            button01.setText(player2_marker);
            return true;
        }else if(button02.getText().toString().equals(player2_marker) && button12.getText().toString().equals(player2_marker) && button22.getText().toString().equals("")){
            button22.setText(player2_marker);
            return true;
        }else if(button02.getText().toString().equals(player2_marker) && button22.getText().toString().equals(player2_marker) && button12.getText().toString().equals("")){
            button12.setText(player2_marker);
            return true;
        }else if(button12.getText().toString().equals(player2_marker) && button22.getText().toString().equals(player2_marker) && button02.getText().toString().equals("")) {
            button02.setText(player2_marker);
            return true;
        }else if(button00.getText().toString().equals(player2_marker) && button11.getText().toString().equals(player2_marker) && button22.getText().toString().equals("")){
            button22.setText(player2_marker);
            return true;
        }else if(button00.getText().toString().equals(player2_marker) && button22.getText().toString().equals(player2_marker) && button11.getText().toString().equals("")){
            button11.setText(player2_marker);
            return true;
        }else if(button11.getText().toString().equals(player2_marker) && button22.getText().toString().equals(player2_marker) && button00.getText().toString().equals("")) {
            button00.setText(player2_marker);
            return true;
        }else if(button02.getText().toString().equals(player2_marker) && button11.getText().toString().equals(player2_marker) && button20.getText().toString().equals("")){
            button20.setText(player2_marker);
            return true;
        }else if(button02.getText().toString().equals(player2_marker) && button20.getText().toString().equals(player2_marker) && button11.getText().toString().equals("")){
            button11.setText(player2_marker);
            return true;
        }else if(button11.getText().toString().equals(player2_marker) && button20.getText().toString().equals(player2_marker) && button02.getText().toString().equals("")) {
            button02.setText(player2_marker);
            return true;
        }
        return false;
    }

    public boolean intelligentBlocking(){
        if(button00.getText().toString().equals(player1_marker) && button01.getText().toString().equals(player1_marker) && button02.getText().toString().equals("")){
            button02.setText(player2_marker);
            return true;
        }else if(button00.getText().toString().equals(player1_marker) && button02.getText().toString().equals(player1_marker) && button01.getText().toString().equals("")){
            button01.setText(player2_marker);
            return true;
        }else if(button01.getText().toString().equals(player1_marker) && button02.getText().toString().equals(player1_marker) && button00.getText().toString().equals("")){
            button00.setText(player2_marker);
            return true;
        }else if(button10.getText().toString().equals(player1_marker) && button11.getText().toString().equals(player1_marker) && button12.getText().toString().equals("")){
            button12.setText(player2_marker);
            return true;
        }else if(button10.getText().toString().equals(player1_marker) && button12.getText().toString().equals(player1_marker) && button11.getText().toString().equals("")){
            button11.setText(player2_marker);
            return true;
        }else if(button11.getText().toString().equals(player1_marker) && button12.getText().toString().equals(player1_marker) && button10.getText().toString().equals("")){
            button10.setText(player2_marker);
            return true;
        }else if(button20.getText().toString().equals(player1_marker) && button21.getText().toString().equals(player1_marker) && button22.getText().toString().equals("")) {
            button22.setText(player2_marker);
            return true;
        }else if(button20.getText().toString().equals(player1_marker) && button22.getText().toString().equals(player1_marker) && button21.getText().toString().equals("")){
            button21.setText(player2_marker);
            return true;
        }else if(button21.getText().toString().equals(player1_marker) && button22.getText().toString().equals(player1_marker) && button20.getText().toString().equals("")){
            button20.setText(player2_marker);
            return true;
        }else if(button00.getText().toString().equals(player1_marker) && button10.getText().toString().equals(player1_marker) && button20.getText().toString().equals("")){
            button20.setText(player2_marker);
            return true;
        }else if(button00.getText().toString().equals(player1_marker) && button20.getText().toString().equals(player1_marker) && button10.getText().toString().equals("")){
            button10.setText(player2_marker);
            return true;
        }else if(button10.getText().toString().equals(player1_marker) && button20.getText().toString().equals(player1_marker) && button00.getText().toString().equals("")){
            button00.setText(player2_marker);
            return true;
        }else if(button01.getText().toString().equals(player1_marker) && button11.getText().toString().equals(player1_marker) && button21.getText().toString().equals("")){
            button21.setText(player2_marker);
            return true;
        }else if(button01.getText().toString().equals(player1_marker) && button21.getText().toString().equals(player1_marker) && button11.getText().toString().equals("")){
            button11.setText(player2_marker);
            return true;
        }else if(button11.getText().toString().equals(player1_marker) && button21.getText().toString().equals(player1_marker) && button01.getText().toString().equals("")){
            button01.setText(player2_marker);
            return true;
        }else if(button02.getText().toString().equals(player1_marker) && button12.getText().toString().equals(player1_marker) && button22.getText().toString().equals("")){
            button22.setText(player2_marker);
            return true;
        }else if(button02.getText().toString().equals(player1_marker) && button22.getText().toString().equals(player1_marker) && button12.getText().toString().equals("")){
            button12.setText(player2_marker);
            return true;
        }else if(button12.getText().toString().equals(player1_marker) && button22.getText().toString().equals(player1_marker) && button02.getText().toString().equals("")) {
            button02.setText(player2_marker);
            return true;
        }else if(button00.getText().toString().equals(player1_marker) && button11.getText().toString().equals(player1_marker) && button22.getText().toString().equals("")){
            button22.setText(player2_marker);
            return true;
        }else if(button00.getText().toString().equals(player1_marker) && button22.getText().toString().equals(player1_marker) && button11.getText().toString().equals("")){
            button11.setText(player2_marker);
            return true;
        }else if(button11.getText().toString().equals(player1_marker) && button22.getText().toString().equals(player1_marker) && button00.getText().toString().equals("")) {
            button00.setText(player2_marker);
            return true;
        }else if(button02.getText().toString().equals(player1_marker) && button11.getText().toString().equals(player1_marker) && button20.getText().toString().equals("")){
            button20.setText(player2_marker);
            return true;
        }else if(button02.getText().toString().equals(player1_marker) && button20.getText().toString().equals(player1_marker) && button11.getText().toString().equals("")){
            button11.setText(player2_marker);
            return true;
        }else if(button11.getText().toString().equals(player1_marker) && button20.getText().toString().equals(player1_marker) && button02.getText().toString().equals("")) {
            button02.setText(player2_marker);
            return true;
        }
        return false;
    }
}
