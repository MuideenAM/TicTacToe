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

public class _5by5Board extends AppCompatActivity {
    Button button00, button01, button02, button03, button04, button10, button11, button12, button13,
            button14, button20, button21, button22, button23, button24, button30, button31, button32,
            button33, button34, button40, button41, button42, button43, button44;
    Button button_board_change, button_play_stop, button_reset, button_player_select;
    int turn;
    boolean end = false, point_taken = false; // Variable to detect when the game ends
    String player_type = "double", player1_marker = "", player2_marker="";
    String board_cells[][] = new String[5][5];
    TextView player1_point, player2, player2_point;
    Spinner player1_spinner, player2_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__5by5_board);

        // Store the instances of the various buttons for their easy access.
        button00 = findViewById(R.id.button00);
        button01 = findViewById(R.id.button01);
        button02 = findViewById(R.id.button02);
        button03 = findViewById(R.id.button03);
        button04 = findViewById(R.id.button04);
        button10 = findViewById(R.id.button10);
        button11 = findViewById(R.id.button11);
        button12 = findViewById(R.id.button12);
        button13 = findViewById(R.id.button13);
        button14 = findViewById(R.id.button14);
        button20 = findViewById(R.id.button20);
        button21 = findViewById(R.id.button21);
        button22 = findViewById(R.id.button22);
        button23 = findViewById(R.id.button23);
        button24 = findViewById(R.id.button24);
        button30 = findViewById(R.id.button30);
        button31 = findViewById(R.id.button31);
        button32 = findViewById(R.id.button32);
        button33 = findViewById(R.id.button33);
        button34 = findViewById(R.id.button34);
        button40 = findViewById(R.id.button40);
        button41 = findViewById(R.id.button41);
        button42 = findViewById(R.id.button42);
        button43 = findViewById(R.id.button43);
        button44 = findViewById(R.id.button44);
        button_board_change = findViewById(R.id.board_button);
        button_play_stop = findViewById(R.id.play_stop_button);
        button_reset = findViewById(R.id.reset_button);
        button_player_select = findViewById(R.id.player_button);
        player2 = findViewById(R.id.textview_player2);
        player1_point = findViewById(R.id.textview_player1_point);
        player2_point = findViewById(R.id.textview_player2_point);
        player1_spinner = findViewById(R.id.spinner_player1);
        player2_spinner = findViewById(R.id.spinner_player2);
        player1_spinner = findViewById(R.id.spinner_player1);
        player2_spinner = findViewById(R.id.spinner_player2);

        player1_spinner.setSelection(0);
        player1_marker = getString(R.string.x);
        player2_spinner.setSelection(1);
        player2_marker = getString(R.string.o);

        Intent intent = getIntent();
        String EXTRA_MESSAGE = "";

        if( !(intent.getStringArrayExtra(EXTRA_MESSAGE) == null)){
            String[] controlSettings = intent.getStringArrayExtra(EXTRA_MESSAGE);

            button_board_change.setText(getString(R.string._3by3));
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
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                String[] controlSettings = new String[2];
                String EXTRA_MESSAGE = "";

                controlSettings[0] = button_reset.getText().toString();
                controlSettings[1] = button_player_select.getText().toString();

                intent.putExtra(EXTRA_MESSAGE, controlSettings);

                startActivity(intent);
                button_board_change.setText(getString(R.string._5by5));
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
                for(int i = 0; i < 5; i++){
                    for(int j = 0; j < 5; j++){
                        board_cells[i][j] = "";
                    }
                }
                button00.setText("");
                button01.setText("");
                button02.setText("");
                button03.setText("");
                button04.setText("");
                button10.setText("");
                button11.setText("");
                button12.setText("");
                button13.setText("");
                button14.setText("");
                button20.setText("");
                button21.setText("");
                button22.setText("");
                button23.setText("");
                button24.setText("");
                button30.setText("");
                button31.setText("");
                button32.setText("");
                button33.setText("");
                button34.setText("");
                button40.setText("");
                button41.setText("");
                button42.setText("");
                button43.setText("");
                button44.setText("");
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
         * Takes care of row 1 column 4  of the board.
         */
        button03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player_type.equals("double")) {
                    if (button03.getText().toString().equals("")) {
                        if (turn == 1) {
                            turn = 2;
                            button03.setText(player1_marker);
                        } else if (turn == 2) {
                            turn = 1;
                            button03.setText(player2_marker);
                        }
                    }
                }
                else{
                    if(button03.getText().toString().equals("")){
                        button03.setText(player1_marker);
                        board_cells[0][3] = player1_marker;
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
         * Takes care of row 1 column 5  of the board.
         */
        button04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player_type.equals("double")) {
                    if (button04.getText().toString().equals("")) {
                        if (turn == 1) {
                            turn = 2;
                            button04.setText(player1_marker);
                        } else if (turn == 2) {
                            turn = 1;
                            button04.setText(player2_marker);
                        }
                    }
                }
                else{
                    if(button04.getText().toString().equals("")){
                        button04.setText(player1_marker);
                        board_cells[0][4] = player1_marker;
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
         * Takes care of row 2 column 4  of the board.
         */
        button13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player_type.equals("double")) {
                    if (button13.getText().toString().equals("")) {
                        if (turn == 1) {
                            turn = 2;
                            button13.setText(player1_marker);
                        } else if (turn == 2) {
                            turn = 1;
                            button13.setText(player2_marker);
                        }
                    }
                }else{
                    if(button13.getText().toString().equals("")){
                        button13.setText(player1_marker);
                        board_cells[1][3] = player1_marker;
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
         * Takes care of row 2 column 5  of the board.
         */
        button14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player_type.equals("double")) {
                    if (button14.getText().toString().equals("")) {
                        if (turn == 1) {
                            turn = 2;
                            button14.setText(player1_marker);
                        } else if (turn == 2) {
                            turn = 1;
                            button14.setText(player2_marker);
                        }
                    }
                }else{
                    if(button14.getText().toString().equals("")){
                        button14.setText(player1_marker);
                        board_cells[1][4] = player1_marker;
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

        /*
         * Takes care of row 3 column 4  of the board.
         */
        button23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player_type.equals("double")) {
                    if (button23.getText().toString().equals("")) {
                        if (turn == 1) {
                            turn = 2;
                            button23.setText(player1_marker);
                        } else if (turn == 2) {
                            turn = 1;
                            button23.setText(player2_marker);
                        }
                    }
                }else{
                    if(button23.getText().toString().equals("")){
                        button23.setText(player1_marker);
                        board_cells[2][3] = player1_marker;
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
         * Takes care of row 3 column 5  of the board.
         */
        button24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player_type.equals("double")) {
                    if (button24.getText().toString().equals("")) {
                        if (turn == 1) {
                            turn = 2;
                            button24.setText(player1_marker);
                        } else if (turn == 2) {
                            turn = 1;
                            button24.setText(player2_marker);
                        }
                    }
                }else{
                    if(button24.getText().toString().equals("")){
                        button24.setText(player1_marker);
                        board_cells[2][4] = player1_marker;
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
         * Takes care of row 4 column 1  of the board.
         */
        button30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player_type.equals("double")){
                    if(button30.getText().toString().equals("")){
                        if(turn == 1){
                            turn = 2;
                            button30.setText(player1_marker);
                        }else if(turn == 2){
                            turn = 1;
                            button30.setText(player2_marker);
                        }
                    }
                }else{
                    if(button30.getText().toString().equals("")){
                        button30.setText(player1_marker);
                        board_cells[3][0] = player1_marker;
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
         * Takes care of  row 4 column 2 of the board.
         */
        button31.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player_type.equals("double")){
                    if(button31.getText().toString().equals("")){
                        if(turn == 1){
                            turn = 2;
                            button31.setText(player1_marker);
                        }else if(turn == 2){
                            turn = 1;
                            button31.setText(player2_marker);
                        }
                    }
                }else {
                    if (button31.getText().toString().equals("")) {
                        button31.setText(player1_marker);
                        board_cells[3][1] = player1_marker;
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
         * Takes care of row 4 column 3  of the board.
         */
        button32.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player_type.equals("double")) {
                    if (button32.getText().toString().equals("")) {
                        if (turn == 1) {
                            turn = 2;
                            button32.setText(player1_marker);
                        } else if (turn == 2) {
                            turn = 1;
                            button32.setText(player2_marker);
                        }
                    }
                }
                else{
                    if(button32.getText().toString().equals("")){
                        button32.setText(player1_marker);
                        board_cells[3][2] = player1_marker;
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
         * Takes care of row 4 column 4  of the board.
         */
        button33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player_type.equals("double")) {
                    if (button33.getText().toString().equals("")) {
                        if (turn == 1) {
                            turn = 2;
                            button33.setText(player1_marker);
                        } else if (turn == 2) {
                            turn = 1;
                            button33.setText(player2_marker);
                        }
                    }
                }
                else{
                    if(button33.getText().toString().equals("")){
                        button33.setText(player1_marker);
                        board_cells[3][3] = player1_marker;
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
         * Takes care of row 4 column 5  of the board.
         */
        button34.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player_type.equals("double")) {
                    if (button34.getText().toString().equals("")) {
                        if (turn == 1) {
                            turn = 2;
                            button34.setText(player1_marker);
                        } else if (turn == 2) {
                            turn = 1;
                            button34.setText(player2_marker);
                        }
                    }
                }
                else{
                    if(button34.getText().toString().equals("")){
                        button34.setText(player1_marker);
                        board_cells[3][4] = player1_marker;
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
         * Takes care of row 5 column 1  of the board.
         */
        button40.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player_type.equals("double")){
                    if(button40.getText().toString().equals("")){
                        if(turn == 1){
                            turn = 2;
                            button40.setText(player1_marker);
                        }else if(turn == 2){
                            turn = 1;
                            button40.setText(player2_marker);
                        }
                    }
                }else{
                    if(button40.getText().toString().equals("")){
                        button40.setText(player1_marker);
                        board_cells[4][0] = player1_marker;
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
         * Takes care of  row 5 column 2 of the board.
         */
        button41.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player_type.equals("double")){
                    if(button41.getText().toString().equals("")){
                        if(turn == 1){
                            turn = 2;
                            button41.setText(player1_marker);
                        }else if(turn == 2){
                            turn = 1;
                            button41.setText(player2_marker);
                        }
                    }
                }else {
                    if (button41.getText().toString().equals("")) {
                        button41.setText(player1_marker);
                        board_cells[4][1] = player1_marker;
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
         * Takes care of row 5 column 3  of the board.
         */
        button42.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player_type.equals("double")) {
                    if (button42.getText().toString().equals("")) {
                        if (turn == 1) {
                            turn = 2;
                            button42.setText(player1_marker);
                        } else if (turn == 2) {
                            turn = 1;
                            button42.setText(player2_marker);
                        }
                    }
                }
                else{
                    if(button42.getText().toString().equals("")){
                        button42.setText(player1_marker);
                        board_cells[4][2] = player1_marker;
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
         * Takes care of row 5 column 4  of the board.
         */
        button43.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player_type.equals("double")) {
                    if (button43.getText().toString().equals("")) {
                        if (turn == 1) {
                            turn = 2;
                            button43.setText(player1_marker);
                        } else if (turn == 2) {
                            turn = 1;
                            button43.setText(player2_marker);
                        }
                    }
                }
                else{
                    if(button43.getText().toString().equals("")){
                        button43.setText(player1_marker);
                        board_cells[4][3] = player1_marker;
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
         * Takes care of row 5 column 5  of the board.
         */
        button44.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player_type.equals("double")) {
                    if (button44.getText().toString().equals("")) {
                        if (turn == 1) {
                            turn = 2;
                            button44.setText(player1_marker);
                        } else if (turn == 2) {
                            turn = 1;
                            button44.setText(player2_marker);
                        }
                    }
                }
                else{
                    if(button44.getText().toString().equals("")){
                        button44.setText(player1_marker);
                        board_cells[4][4] = player1_marker;
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
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                board_cells[i][j] = "";
            }
        }
        button00.setEnabled(false);
        button01.setEnabled(false);
        button02.setEnabled(false);
        button03.setEnabled(false);
        button04.setEnabled(false);
        button10.setEnabled(false);
        button11.setEnabled(false);
        button12.setEnabled(false);
        button13.setEnabled(false);
        button14.setEnabled(false);
        button20.setEnabled(false);
        button21.setEnabled(false);
        button22.setEnabled(false);
        button23.setEnabled(false);
        button24.setEnabled(false);
        button30.setEnabled(false);
        button31.setEnabled(false);
        button32.setEnabled(false);
        button33.setEnabled(false);
        button34.setEnabled(false);
        button40.setEnabled(false);
        button41.setEnabled(false);
        button42.setEnabled(false);
        button43.setEnabled(false);
        button44.setEnabled(false);
        Toast.makeText(_5by5Board.this,"Click Play button to start playing.",Toast.LENGTH_LONG ).show();
    }

    /*
     * Determines when the game ends or otherwise. It also activates the board or disables it as necessary.
     */
    public void endGame(){
        String val00, val01, val02, val03, val04, val10, val11,  val12, val13, val14, val20, val21,
                val22, val23, val24, val30, val31, val32, val33, val34, val40, val41,  val42, val43,
                val44 ;

        val00 = button00.getText().toString();
        val01 = button01.getText().toString();
        val02 = button02.getText().toString();
        val03 = button03.getText().toString();
        val04 = button04.getText().toString();
        val10 = button10.getText().toString();
        val11 = button11.getText().toString();
        val12 = button12.getText().toString();
        val13 = button13.getText().toString();
        val14 = button14.getText().toString();
        val20 = button20.getText().toString();
        val21 = button21.getText().toString();
        val22 = button22.getText().toString();
        val23 = button23.getText().toString();
        val24 = button24.getText().toString();
        val30 = button30.getText().toString();
        val31 = button31.getText().toString();
        val32 = button32.getText().toString();
        val33 = button33.getText().toString();
        val34 = button34.getText().toString();
        val40 = button40.getText().toString();
        val41 = button41.getText().toString();
        val42 = button42.getText().toString();
        val43 = button43.getText().toString();
        val44 = button44.getText().toString();

        if(fullyFilled(board_cells)){
            Toast.makeText(_5by5Board.this, "Board Fully filled!", Toast.LENGTH_LONG).show();
            return;
        }

        if(point_taken){
            return;
        }

        //First row winning for Player 1
        if (val00.equals(player1_marker) && val01.equals(player1_marker) && val02.equals(player1_marker) && val03.equals(player1_marker) && val04.equals(player1_marker)){
            Toast.makeText(_5by5Board.this, "Winner Player X!", Toast.LENGTH_LONG).show();
            end = true;
            player1_point.setText(String.valueOf(Integer.parseInt(player1_point.getText().toString()) + 1));
            point_taken = true;
        }
        //First column winning for Player 1
        if (val00.equals(player1_marker) && val10.equals(player1_marker) && val20.equals(player1_marker) && val30.equals(player1_marker) && val40.equals(player1_marker)){
            Toast.makeText(_5by5Board.this, "Winner Player X!", Toast.LENGTH_LONG).show();
            end = true;
            player1_point.setText(String.valueOf(Integer.parseInt(player1_point.getText().toString()) + 1));
            point_taken = true;
        }
        //Second row winning for Player 1
        if (val10.equals(player1_marker) && val11.equals(player1_marker) && val12.equals(player1_marker) && val13.equals(player1_marker) && val14.equals(player1_marker)){
            Toast.makeText(_5by5Board.this, "Winner Player X!", Toast.LENGTH_LONG).show();
            end = true;
            player1_point.setText(String.valueOf(Integer.parseInt(player1_point.getText().toString()) + 1));
            point_taken = true;
        }
        //Second column winning for Player 1
        if (val01.equals(player1_marker) && val11.equals(player1_marker) && val21.equals(player1_marker) && val31.equals(player1_marker) && val41.equals(player1_marker)){
            Toast.makeText(_5by5Board.this, "Winner Player X!", Toast.LENGTH_LONG).show();
            end = true;
            player1_point.setText(String.valueOf(Integer.parseInt(player1_point.getText().toString()) + 1));
            point_taken = true;
        }
        //Third row winning for Player 1
        if (val20.equals(player1_marker) && val21.equals(player1_marker) && val22.equals(player1_marker) && val23.equals(player1_marker) && val24.equals(player1_marker)) {
            Toast.makeText(_5by5Board.this, "Winner Player X!", Toast.LENGTH_LONG).show();
            end = true;
            player1_point.setText(String.valueOf(Integer.parseInt(player1_point.getText().toString()) + 1));
            point_taken = true;
        }
        //Third column winning for Player 1
        if (val02.equals(player1_marker) && val12.equals(player1_marker) && val22.equals(player1_marker) && val32.equals(player1_marker) && val42.equals(player1_marker)){
            Toast.makeText(_5by5Board.this, "Winner Player X!", Toast.LENGTH_LONG).show();
            end = true;
            player1_point.setText(String.valueOf(Integer.parseInt(player1_point.getText().toString()) + 1));
            point_taken = true;
        }
        //Fourth row winning for Player 1
        if (val30.equals(player1_marker) && val31.equals(player1_marker) && val32.equals(player1_marker) && val33.equals(player1_marker) && val34.equals(player1_marker)){
            Toast.makeText(_5by5Board.this, "Winner Player X!", Toast.LENGTH_LONG).show();
            end = true;
            player1_point.setText(String.valueOf(Integer.parseInt(player1_point.getText().toString()) + 1));
            point_taken = true;
        }
        //Fourth column winning for Player 1
        if (val03.equals(player1_marker) && val13.equals(player1_marker) && val23.equals(player1_marker) && val33.equals(player1_marker) && val43.equals(player1_marker)){
            Toast.makeText(_5by5Board.this, "Winner Player X!", Toast.LENGTH_LONG).show();
            end = true;
            player1_point.setText(String.valueOf(Integer.parseInt(player1_point.getText().toString()) + 1));
            point_taken = true;
        }
        //Fifth row winning for Player 1
        if (val40.equals(player1_marker) && val41.equals(player1_marker) && val42.equals(player1_marker) && val43.equals(player1_marker) && val44.equals(player1_marker)){
            Toast.makeText(_5by5Board.this, "Winner Player X!", Toast.LENGTH_LONG).show();
            end = true;
            player1_point.setText(String.valueOf(Integer.parseInt(player1_point.getText().toString()) + 1));
            point_taken = true;
        }
        //Fifth column winning for Player 1
        if (val04.equals(player1_marker) && val14.equals(player1_marker) && val24.equals(player1_marker) && val34.equals(player1_marker) && val44.equals(player1_marker)){
            Toast.makeText(_5by5Board.this, "Winner Player X!", Toast.LENGTH_LONG).show();
            end = true;
            player1_point.setText(String.valueOf(Integer.parseInt(player1_point.getText().toString()) + 1));
            point_taken = true;
        }

        //Leading diagonal winning for Player 1
        if (val00.equals(player1_marker) && val11.equals(player1_marker) && val22.equals(player1_marker) && val33.equals(player1_marker) && val44.equals(player1_marker)){
            Toast.makeText(_5by5Board.this, "Winner Player X!", Toast.LENGTH_LONG).show();
            end = true;
            player1_point.setText(String.valueOf(Integer.parseInt(player1_point.getText().toString()) + 1));
            point_taken = true;
        }
        //Reversed diagonal winning for Player 1
        if (val04.equals(player1_marker) && val13.equals(player1_marker) && val22.equals(player1_marker) && val31.equals(player1_marker) && val40.equals(player1_marker)){
            Toast.makeText(_5by5Board.this, "Winner Player X!", Toast.LENGTH_LONG).show();
            end = true;
            player1_point.setText(String.valueOf(Integer.parseInt(player1_point.getText().toString()) + 1));
            point_taken = true;
        }

        //First row winning for Player 2
        if (val00.equals(player2_marker) && val01.equals(player2_marker) && val02.equals(player2_marker) && val03.equals(player2_marker) && val04.equals(player2_marker)){
            Toast.makeText(_5by5Board.this, "Winner Player O!", Toast.LENGTH_LONG).show();
            end = true;
            player2_point.setText(String.valueOf(Integer.parseInt(player2_point.getText().toString()) + 1));
            point_taken = true;

        }
        //First column winning for Player 2
        if (val00.equals(player2_marker) && val10.equals(player2_marker) && val20.equals(player2_marker) && val30.equals(player2_marker) && val40.equals(player2_marker)){
            Toast.makeText(_5by5Board.this, "Winner Player O!", Toast.LENGTH_LONG).show();
            end = true;
            player2_point.setText(String.valueOf(Integer.parseInt(player2_point.getText().toString()) + 1));
            point_taken = true;
        }
        //Second row winning for Player 2
        if (val10.equals(player2_marker) && val11.equals(player2_marker) && val12.equals(player2_marker) && val13.equals(player2_marker) && val14.equals(player2_marker)){
            Toast.makeText(_5by5Board.this, "Winner Player O!", Toast.LENGTH_LONG).show();
            end = true;
            player2_point.setText(String.valueOf(Integer.parseInt(player2_point.getText().toString()) + 1));
            point_taken = true;
        }
        //Second column winning for Player 2
        if (val01.equals(player2_marker) && val11.equals(player2_marker) && val21.equals(player2_marker) && val31.equals(player2_marker) && val41.equals(player2_marker)){
            Toast.makeText(_5by5Board.this, "Winner Player O!", Toast.LENGTH_LONG).show();
            end = true;
            player2_point.setText(String.valueOf(Integer.parseInt(player2_point.getText().toString()) + 1));
            point_taken = true;
        }
        //Third row winning for Player 2
        if (val20.equals(player2_marker) && val21.equals(player2_marker) && val22.equals(player2_marker) && val23.equals(player2_marker) && val24.equals(player2_marker)) {
            Toast.makeText(_5by5Board.this, "Winner Player O!", Toast.LENGTH_LONG).show();
            end = true;
            player2_point.setText(String.valueOf(Integer.parseInt(player2_point.getText().toString()) + 1));
            point_taken = true;
        }
        //Third column winning for Player 2
        if (val02.equals(player2_marker) && val12.equals(player2_marker) && val22.equals(player2_marker) && val32.equals(player2_marker) && val42.equals(player2_marker)){
            Toast.makeText(_5by5Board.this, "Winner Player O!", Toast.LENGTH_LONG).show();
            end = true;
            player2_point.setText(String.valueOf(Integer.parseInt(player2_point.getText().toString()) + 1));
            point_taken = true;
        }
        //Fourth row winning for Player 2
        if (val30.equals(player2_marker) && val31.equals(player2_marker) && val32.equals(player2_marker) && val33.equals(player2_marker) && val34.equals(player2_marker)){
            Toast.makeText(_5by5Board.this, "Winner Player X!", Toast.LENGTH_LONG).show();
            end = true;
            player2_point.setText(String.valueOf(Integer.parseInt(player2_point.getText().toString()) + 1));
            point_taken = true;
        }
        //Fourth column winning for Player 2
        if (val03.equals(player2_marker) && val13.equals(player2_marker) && val23.equals(player2_marker) && val33.equals(player2_marker) && val43.equals(player2_marker)){
            Toast.makeText(_5by5Board.this, "Winner Player X!", Toast.LENGTH_LONG).show();
            end = true;
            player2_point.setText(String.valueOf(Integer.parseInt(player2_point.getText().toString()) + 1));
            point_taken = true;
        }
        //Fifth row winning for Player 2
        if (val40.equals(player2_marker) && val41.equals(player2_marker) && val42.equals(player2_marker) && val43.equals(player2_marker) && val44.equals(player2_marker)){
            Toast.makeText(_5by5Board.this, "Winner Player X!", Toast.LENGTH_LONG).show();
            end = true;
            player2_point.setText(String.valueOf(Integer.parseInt(player2_point.getText().toString()) + 1));
            point_taken = true;
        }
        //Fifth column winning for Player 2
        if (val04.equals(player2_marker) && val14.equals(player2_marker) && val24.equals(player2_marker) && val34.equals(player2_marker) && val44.equals(player2_marker)){
            Toast.makeText(_5by5Board.this, "Winner Player X!", Toast.LENGTH_LONG).show();
            end = true;
            player2_point.setText(String.valueOf(Integer.parseInt(player2_point.getText().toString()) + 1));
            point_taken = true;
        }

        //Leading diagonal winning for Player 2
        if (val00.equals(player2_marker) && val11.equals(player2_marker) && val22.equals(player2_marker) && val33.equals(player2_marker) && val44.equals(player2_marker)){
            Toast.makeText(_5by5Board.this, "Winner Player O!", Toast.LENGTH_LONG).show();
            end = true;
            player2_point.setText(String.valueOf(Integer.parseInt(player2_point.getText().toString()) + 1));
            point_taken = true;
        }
        //Reversed diagonal winning for Player 2
        if (val04.equals(player2_marker) && val13.equals(player2_marker) && val22.equals(player2_marker) && val31.equals(player2_marker) && val40.equals(player2_marker)){
            Toast.makeText(_5by5Board.this, "Winner Player O!", Toast.LENGTH_LONG).show();
            end = true;
            player2_point.setText(String.valueOf(Integer.parseInt(player2_point.getText().toString()) + 1));
            point_taken = true;
        }

        if(end){
            button00.setEnabled(false);
            button01.setEnabled(false);
            button02.setEnabled(false);
            button03.setEnabled(false);
            button04.setEnabled(false);
            button10.setEnabled(false);
            button11.setEnabled(false);
            button12.setEnabled(false);
            button13.setEnabled(false);
            button14.setEnabled(false);
            button20.setEnabled(false);
            button21.setEnabled(false);
            button22.setEnabled(false);
            button23.setEnabled(false);
            button24.setEnabled(false);
            button30.setEnabled(false);
            button31.setEnabled(false);
            button32.setEnabled(false);
            button33.setEnabled(false);
            button34.setEnabled(false);
            button40.setEnabled(false);
            button41.setEnabled(false);
            button42.setEnabled(false);
            button43.setEnabled(false);
            button44.setEnabled(false);
        }else{
            button00.setEnabled(true);
            button01.setEnabled(true);
            button02.setEnabled(true);
            button03.setEnabled(true);
            button04.setEnabled(true);
            button10.setEnabled(true);
            button11.setEnabled(true);
            button12.setEnabled(true);
            button13.setEnabled(true);
            button14.setEnabled(true);
            button20.setEnabled(true);
            button21.setEnabled(true);
            button22.setEnabled(true);
            button23.setEnabled(true);
            button24.setEnabled(true);
            button30.setEnabled(true);
            button31.setEnabled(true);
            button32.setEnabled(true);
            button33.setEnabled(true);
            button34.setEnabled(true);
            button40.setEnabled(true);
            button41.setEnabled(true);
            button42.setEnabled(true);
            button43.setEnabled(true);
            button44.setEnabled(true);
        }
    }

    public void computerPlay(){
        boolean played = false;

        while(!played){
            Random rand = new Random();
            int row = rand.nextInt(5),
                    col = rand.nextInt(5);
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
                        }else if(col == 2){
                            button02.setText(player2_marker);
                            board_cells[row][col] = player2_marker;
                            played = true;
                        }else if(col == 3){
                            button03.setText(player2_marker);
                            board_cells[row][col] = player2_marker;
                            played = true;
                        }else{
                            button04.setText(player2_marker);
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
                        }else if(col == 2){
                            button12.setText(player2_marker);
                            board_cells[row][col] = player2_marker;
                            played = true;
                        }else if(col == 3){
                            button13.setText(player2_marker);
                            board_cells[row][col] = player2_marker;
                            played = true;
                        }else{
                            button14.setText(player2_marker);
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
                        }else if(col == 2){
                            button22.setText(player2_marker);
                            board_cells[row][col] = player2_marker;
                            played = true;
                        }else if(col == 3){
                            button23.setText(player2_marker);
                            board_cells[row][col] = player2_marker;
                            played = true;
                        }else{
                            button24.setText(player2_marker);
                            board_cells[row][col] = player2_marker;
                            played = true;
                        }
                        break;
                    case 3:
                        if(col == 0){
                            button30.setText(player2_marker);
                            board_cells[row][col] = player2_marker;
                            played = true;
                        }else if(col == 1){
                            button31.setText(player2_marker);
                            board_cells[row][col] = player2_marker;
                            played = true;
                        }else if(col == 2){
                            button32.setText(player2_marker);
                            board_cells[row][col] = player2_marker;
                            played = true;
                        }else if(col == 3){
                            button33.setText(player2_marker);
                            board_cells[row][col] = player2_marker;
                            played = true;
                        }else{
                            button34.setText(player2_marker);
                            board_cells[row][col] = player2_marker;
                            played = true;
                        }
                        break;
                    case 4:
                        if(col == 0){
                            button40.setText(player2_marker);
                            board_cells[row][col] = player2_marker;
                            played = true;
                        }else if(col == 1){
                            button41.setText(player2_marker);
                            board_cells[row][col] = player2_marker;
                            played = true;
                        }else if(col == 2){
                            button42.setText(player2_marker);
                            board_cells[row][col] = player2_marker;
                            played = true;
                        }else if(col == 3){
                            button43.setText(player2_marker);
                            board_cells[row][col] = player2_marker;
                            played = true;
                        }else{
                            button44.setText(player2_marker);
                            board_cells[row][col] = player2_marker;
                            played = true;
                        }
                        break;
                }
            }
        }
    }

    public boolean fullyFilled(String[][] cells){
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                if(cells[i][j].equals("")){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean intelligentWinning(){
        if(button00.getText().toString().equals(player2_marker) && button01.getText().toString().equals(player2_marker) && button02.getText().toString().equals(player2_marker) && button03.getText().toString().equals(player2_marker) && button04.getText().toString().equals("")){
            button04.setText(player2_marker);
            return true;
        }else if(button00.getText().toString().equals(player2_marker) && button01.getText().toString().equals(player2_marker) && button02.getText().toString().equals(player2_marker) && button04.getText().toString().equals(player2_marker) && button03.getText().toString().equals("")){
            button03.setText(player2_marker);
            return true;
        }else if(button00.getText().toString().equals(player2_marker) && button01.getText().toString().equals(player2_marker) && button03.getText().toString().equals(player2_marker) && button04.getText().toString().equals(player2_marker) && button02.getText().toString().equals("")){
            button02.setText(player2_marker);
            return true;
        }else if(button00.getText().toString().equals(player2_marker) && button02.getText().toString().equals(player2_marker) && button03.getText().toString().equals(player2_marker) && button04.getText().toString().equals(player2_marker) && button01.getText().toString().equals("")){
            button01.setText(player2_marker);
            return true;
        }else if(button01.getText().toString().equals(player2_marker) && button02.getText().toString().equals(player2_marker) && button03.getText().toString().equals(player2_marker) && button04.getText().toString().equals(player2_marker) && button00.getText().toString().equals("")){
            button00.setText(player2_marker);
            return true;
        }else if(button10.getText().toString().equals(player2_marker) && button11.getText().toString().equals(player2_marker) && button12.getText().toString().equals(player2_marker) && button13.getText().toString().equals(player2_marker) && button14.getText().toString().equals("")){
            button14.setText(player2_marker);
            return true;
        }else if(button10.getText().toString().equals(player2_marker) && button11.getText().toString().equals(player2_marker) && button12.getText().toString().equals(player2_marker) && button14.getText().toString().equals(player2_marker) && button13.getText().toString().equals("")){
            button13.setText(player2_marker);
            return true;
        }else if(button10.getText().toString().equals(player2_marker) && button11.getText().toString().equals(player2_marker) && button13.getText().toString().equals(player2_marker) && button14.getText().toString().equals(player2_marker) && button12.getText().toString().equals("")){
            button12.setText(player2_marker);
            return true;
        }else if(button10.getText().toString().equals(player2_marker) && button12.getText().toString().equals(player2_marker) && button13.getText().toString().equals(player2_marker) && button14.getText().toString().equals(player2_marker) && button11.getText().toString().equals("")){
            button11.setText(player2_marker);
            return true;
        }else if(button11.getText().toString().equals(player2_marker) && button12.getText().toString().equals(player2_marker) && button13.getText().toString().equals(player2_marker) && button14.getText().toString().equals(player2_marker) && button10.getText().toString().equals("")){
            button10.setText(player2_marker);
            return true;
        }else if(button20.getText().toString().equals(player2_marker) && button21.getText().toString().equals(player2_marker) && button22.getText().toString().equals(player2_marker) && button23.getText().toString().equals(player2_marker) && button24.getText().toString().equals("")){
            button24.setText(player2_marker);
            return true;
        }else if(button20.getText().toString().equals(player2_marker) && button21.getText().toString().equals(player2_marker) && button22.getText().toString().equals(player2_marker) && button24.getText().toString().equals(player2_marker) && button23.getText().toString().equals("")){
            button23.setText(player2_marker);
            return true;
        }else if(button20.getText().toString().equals(player2_marker) && button21.getText().toString().equals(player2_marker) && button23.getText().toString().equals(player2_marker) && button24.getText().toString().equals(player2_marker) && button22.getText().toString().equals("")){
            button22.setText(player2_marker);
            return true;
        }else if(button20.getText().toString().equals(player2_marker) && button22.getText().toString().equals(player2_marker) && button23.getText().toString().equals(player2_marker) && button24.getText().toString().equals(player2_marker) && button21.getText().toString().equals("")){
            button21.setText(player2_marker);
            return true;
        }else if(button21.getText().toString().equals(player2_marker) && button22.getText().toString().equals(player2_marker) && button23.getText().toString().equals(player2_marker) && button24.getText().toString().equals(player2_marker) && button20.getText().toString().equals("")){
            button20.setText(player2_marker);
            return true;
        }else if(button30.getText().toString().equals(player2_marker) && button31.getText().toString().equals(player2_marker) && button32.getText().toString().equals(player2_marker) && button33.getText().toString().equals(player2_marker) && button34.getText().toString().equals("")  ){
            button34.setText(player2_marker);
            return true;
        }else if(button30.getText().toString().equals(player2_marker) && button31.getText().toString().equals(player2_marker) && button32.getText().toString().equals(player2_marker) && button34.getText().toString().equals(player2_marker) && button33.getText().toString().equals("")){
            button33.setText(player2_marker);
            return true;
        }else if(button30.getText().toString().equals(player2_marker) && button31.getText().toString().equals(player2_marker) && button33.getText().toString().equals(player2_marker) && button34.getText().toString().equals(player2_marker) && button32.getText().toString().equals("")){
            button32.setText(player2_marker);
            return true;
        }else if(button30.getText().toString().equals(player2_marker) && button32.getText().toString().equals(player2_marker) && button33.getText().toString().equals(player2_marker) && button34.getText().toString().equals(player2_marker) && button31.getText().toString().equals("")){
            button31.setText(player2_marker);
            return true;
        }else if(button31.getText().toString().equals(player2_marker) && button32.getText().toString().equals(player2_marker) && button33.getText().toString().equals(player2_marker) && button34.getText().toString().equals(player2_marker) && button31.getText().toString().equals("")){
            button30.setText(player2_marker);
            return true;
        }else if(button40.getText().toString().equals(player2_marker) && button41.getText().toString().equals(player2_marker) && button42.getText().toString().equals(player2_marker) && button43.getText().toString().equals(player2_marker) && button44.getText().toString().equals("")){
            button44.setText(player2_marker);
            return true;
        }else if(button40.getText().toString().equals(player2_marker) && button41.getText().toString().equals(player2_marker) && button42.getText().toString().equals(player2_marker) && button44.getText().toString().equals(player2_marker) && button43.getText().toString().equals("")){
            button43.setText(player2_marker);
            return true;
        }else if(button40.getText().toString().equals(player2_marker) && button41.getText().toString().equals(player2_marker) && button43.getText().toString().equals(player2_marker) && button44.getText().toString().equals(player2_marker) && button42.getText().toString().equals("")){
            button42.setText(player2_marker);
            return true;
        }else if(button40.getText().toString().equals(player2_marker) && button42.getText().toString().equals(player2_marker) && button43.getText().toString().equals(player2_marker) && button44.getText().toString().equals(player2_marker) && button41.getText().toString().equals("")){
            button41.setText(player2_marker);
            return true;
        }else if(button41.getText().toString().equals(player2_marker) && button42.getText().toString().equals(player2_marker) && button43.getText().toString().equals(player2_marker) && button44.getText().toString().equals(player2_marker) && button40.getText().toString().equals("")){
            button40.setText(player2_marker);
            return true;
        }else if(button00.getText().toString().equals(player2_marker) && button10.getText().toString().equals(player2_marker) && button20.getText().toString().equals(player2_marker) && button30.getText().toString().equals(player2_marker) && button40.getText().toString().equals("")){
            button40.setText(player2_marker);
            return true;
        }else if(button00.getText().toString().equals(player2_marker) && button10.getText().toString().equals(player2_marker) && button20.getText().toString().equals(player2_marker) && button40.getText().toString().equals(player2_marker) && button30.getText().toString().equals("")){
            button30.setText(player2_marker);
            return true;
        }else if(button00.getText().toString().equals(player2_marker) && button10.getText().toString().equals(player2_marker) && button30.getText().toString().equals(player2_marker) && button40.getText().toString().equals(player2_marker) && button20.getText().toString().equals("")){
            button20.setText(player2_marker);
            return true;
        }else if(button00.getText().toString().equals(player2_marker) && button20.getText().toString().equals(player2_marker) && button30.getText().toString().equals(player2_marker) && button40.getText().toString().equals(player2_marker) && button10.getText().toString().equals("")){
            button10.setText(player2_marker);
            return true;
        }else if(button10.getText().toString().equals(player2_marker) && button20.getText().toString().equals(player2_marker) && button30.getText().toString().equals(player2_marker) && button40.getText().toString().equals(player2_marker) && button00.getText().toString().equals("")){
            button00.setText(player2_marker);
            return true;
        }else if(button01.getText().toString().equals(player2_marker) && button11.getText().toString().equals(player2_marker) && button21.getText().toString().equals(player2_marker) && button31.getText().toString().equals(player2_marker) && button41.getText().toString().equals("")){
            button41.setText(player2_marker);
            return true;
        }else if(button01.getText().toString().equals(player2_marker) && button11.getText().toString().equals(player2_marker) && button21.getText().toString().equals(player2_marker) && button41.getText().toString().equals(player2_marker) && button31.getText().toString().equals("")){
            button31.setText(player2_marker);
            return true;
        }else if(button01.getText().toString().equals(player2_marker) && button11.getText().toString().equals(player2_marker) && button31.getText().toString().equals(player2_marker) && button41.getText().toString().equals(player2_marker) && button21.getText().toString().equals("")){
            button21.setText(player2_marker);
            return true;
        }else if(button01.getText().toString().equals(player2_marker) && button21.getText().toString().equals(player2_marker) && button31.getText().toString().equals(player2_marker) && button41.getText().toString().equals(player2_marker) && button11.getText().toString().equals("")){
            button11.setText(player2_marker);
            return true;
        }else if(button11.getText().toString().equals(player2_marker) && button21.getText().toString().equals(player2_marker) && button31.getText().toString().equals(player2_marker) && button41.getText().toString().equals(player2_marker) && button01.getText().toString().equals("")){
            button01.setText(player2_marker);
            return true;
        }else if(button02.getText().toString().equals(player2_marker) && button12.getText().toString().equals(player2_marker) && button22.getText().toString().equals(player2_marker) && button32.getText().toString().equals(player2_marker) && button42.getText().toString().equals("")){
            button42.setText(player2_marker);
            return true;
        }else if(button02.getText().toString().equals(player2_marker) && button12.getText().toString().equals(player2_marker) && button22.getText().toString().equals(player2_marker) && button42.getText().toString().equals(player2_marker) && button32.getText().toString().equals("")){
            button32.setText(player2_marker);
            return true;
        }else if(button02.getText().toString().equals(player2_marker) && button12.getText().toString().equals(player2_marker) && button32.getText().toString().equals(player2_marker) && button42.getText().toString().equals(player2_marker) && button22.getText().toString().equals("")){
            button22.setText(player2_marker);
            return true;
        }else if(button02.getText().toString().equals(player2_marker) && button22.getText().toString().equals(player2_marker) && button32.getText().toString().equals(player2_marker) && button42.getText().toString().equals(player2_marker) && button12.getText().toString().equals("")){
            button12.setText(player2_marker);
            return true;
        }else if(button12.getText().toString().equals(player2_marker) && button22.getText().toString().equals(player2_marker) && button32.getText().toString().equals(player2_marker) && button42.getText().toString().equals(player2_marker) && button02.getText().toString().equals("")){
            button02.setText(player2_marker);
            return true;
        }else if(button03.getText().toString().equals(player2_marker) && button13.getText().toString().equals(player2_marker) && button23.getText().toString().equals(player2_marker) && button33.getText().toString().equals(player2_marker) && button43.getText().toString().equals("")){
            button43.setText(player2_marker);
            return true;
        }else if(button03.getText().toString().equals(player2_marker) && button13.getText().toString().equals(player2_marker) && button23.getText().toString().equals(player2_marker) && button43.getText().toString().equals(player2_marker) && button33.getText().toString().equals("")){
            button33.setText(player2_marker);
            return true;
        }else if(button03.getText().toString().equals(player2_marker) && button13.getText().toString().equals(player2_marker) && button33.getText().toString().equals(player2_marker) && button43.getText().toString().equals(player2_marker) && button23.getText().toString().equals("")){
            button23.setText(player2_marker);
            return true;
        }else if(button03.getText().toString().equals(player2_marker) && button23.getText().toString().equals(player2_marker) && button33.getText().toString().equals(player2_marker) && button43.getText().toString().equals(player2_marker) && button13.getText().toString().equals("")){
            button13.setText(player2_marker);
            return true;
        }else if(button13.getText().toString().equals(player2_marker) && button23.getText().toString().equals(player2_marker) && button33.getText().toString().equals(player2_marker) && button43.getText().toString().equals(player2_marker) && button03.getText().toString().equals("")){
            button03.setText(player2_marker);
            return true;
        }else if(button04.getText().toString().equals(player2_marker) && button14.getText().toString().equals(player2_marker) && button24.getText().toString().equals(player2_marker) && button34.getText().toString().equals(player2_marker) && button44.getText().toString().equals("")){
            button44.setText(player2_marker);
            return true;
        }else if(button04.getText().toString().equals(player2_marker) && button14.getText().toString().equals(player2_marker) && button24.getText().toString().equals(player2_marker) && button44.getText().toString().equals(player2_marker) && button34.getText().toString().equals("")){
            button34.setText(player2_marker);
            return true;
        }else if(button04.getText().toString().equals(player2_marker) && button14.getText().toString().equals(player2_marker) && button34.getText().toString().equals(player2_marker) && button44.getText().toString().equals(player2_marker) && button24.getText().toString().equals("")){
            button24.setText(player2_marker);
            return true;
        }else if(button04.getText().toString().equals(player2_marker) && button24.getText().toString().equals(player2_marker) && button34.getText().toString().equals(player2_marker) && button44.getText().toString().equals(player2_marker) && button41.getText().toString().equals("")){
            button41.setText(player2_marker);
            return true;
        }else if(button14.getText().toString().equals(player2_marker) && button24.getText().toString().equals(player2_marker) && button34.getText().toString().equals(player2_marker) && button44.getText().toString().equals(player2_marker) && button04.getText().toString().equals("")){
            button04.setText(player2_marker);
            return true;
        }else if(button00.getText().toString().equals(player2_marker) && button11.getText().toString().equals(player2_marker) && button22.getText().toString().equals(player2_marker) && button33.getText().toString().equals(player2_marker) && button44.getText().toString().equals("")){
            button44.setText(player2_marker);
            return true;
        }else if(button00.getText().toString().equals(player2_marker) && button11.getText().toString().equals(player2_marker) && button22.getText().toString().equals(player2_marker) && button44.getText().toString().equals(player2_marker) && button33.getText().toString().equals("")){
            button33.setText(player2_marker);
            return true;
        }else if(button00.getText().toString().equals(player2_marker) && button11.getText().toString().equals(player2_marker) && button33.getText().toString().equals(player2_marker) && button44.getText().toString().equals(player2_marker) && button22.getText().toString().equals("")){
            button22.setText(player2_marker);
            return true;
        }else if(button00.getText().toString().equals(player2_marker) && button22.getText().toString().equals(player2_marker) && button33.getText().toString().equals(player2_marker) && button44.getText().toString().equals(player2_marker) && button11.getText().toString().equals("")){
            button11.setText(player2_marker);
            return true;
        }else if(button11.getText().toString().equals(player2_marker) && button22.getText().toString().equals(player2_marker) && button33.getText().toString().equals(player2_marker) && button44.getText().toString().equals(player2_marker) && button00.getText().toString().equals("")){
            button00.setText(player2_marker);
            return true;
        }else if(button04.getText().toString().equals(player2_marker) && button13.getText().toString().equals(player2_marker) && button22.getText().toString().equals(player2_marker) && button31.getText().toString().equals(player2_marker) && button40.getText().toString().equals("")){
            button40.setText(player2_marker);
            return true;
        }else if(button04.getText().toString().equals(player2_marker) && button13.getText().toString().equals(player2_marker) && button22.getText().toString().equals(player2_marker) && button40.getText().toString().equals(player2_marker) && button31.getText().toString().equals("")){
            button31.setText(player2_marker);
            return true;
        }else if(button04.getText().toString().equals(player2_marker) && button13.getText().toString().equals(player2_marker) && button31.getText().toString().equals(player2_marker) && button40.getText().toString().equals(player2_marker) && button22.getText().toString().equals("")){
            button22.setText(player2_marker);
            return true;
        }else if(button04.getText().toString().equals(player2_marker) && button22.getText().toString().equals(player2_marker) && button31.getText().toString().equals(player2_marker) && button40.getText().toString().equals(player2_marker) && button13.getText().toString().equals("")){
            button13.setText(player2_marker);
            return true;
        }else if(button13.getText().toString().equals(player2_marker) && button22.getText().toString().equals(player2_marker) && button31.getText().toString().equals(player2_marker) && button40.getText().toString().equals(player2_marker) && button04.getText().toString().equals("")){
            button04.setText(player2_marker);
            return true;
        }
        return false;
    }

    public boolean intelligentBlocking(){
        if(button00.getText().toString().equals(player1_marker) && button01.getText().toString().equals(player1_marker) && button02.getText().toString().equals(player1_marker) && button03.getText().toString().equals(player1_marker) && button04.getText().toString().equals("")){
            button04.setText(player2_marker);
            return true;
        }else if(button00.getText().toString().equals(player1_marker) && button01.getText().toString().equals(player1_marker) && button02.getText().toString().equals(player1_marker) && button04.getText().toString().equals(player1_marker) && button03.getText().toString().equals("")){
            button03.setText(player2_marker);
            return true;
        }else if(button00.getText().toString().equals(player1_marker) && button01.getText().toString().equals(player1_marker) && button03.getText().toString().equals(player1_marker) && button04.getText().toString().equals(player1_marker) && button02.getText().toString().equals("")){
            button02.setText(player2_marker);
            return true;
        }else if(button00.getText().toString().equals(player1_marker) && button02.getText().toString().equals(player1_marker) && button03.getText().toString().equals(player1_marker) && button04.getText().toString().equals(player1_marker) && button01.getText().toString().equals("")){
            button01.setText(player2_marker);
            return true;
        }else if(button01.getText().toString().equals(player1_marker) && button02.getText().toString().equals(player1_marker) && button03.getText().toString().equals(player1_marker) && button04.getText().toString().equals(player1_marker) && button00.getText().toString().equals("")){
            button00.setText(player2_marker);
            return true;
        }else if(button10.getText().toString().equals(player1_marker) && button11.getText().toString().equals(player1_marker) && button12.getText().toString().equals(player1_marker) && button13.getText().toString().equals(player1_marker) && button14.getText().toString().equals("")){
            button14.setText(player2_marker);
            return true;
        }else if(button10.getText().toString().equals(player1_marker) && button11.getText().toString().equals(player1_marker) && button12.getText().toString().equals(player1_marker) && button14.getText().toString().equals(player1_marker) && button13.getText().toString().equals("")){
            button13.setText(player2_marker);
            return true;
        }else if(button10.getText().toString().equals(player1_marker) && button11.getText().toString().equals(player1_marker) && button13.getText().toString().equals(player1_marker) && button14.getText().toString().equals(player1_marker) && button12.getText().toString().equals("")){
            button12.setText(player2_marker);
            return true;
        }else if(button10.getText().toString().equals(player1_marker) && button12.getText().toString().equals(player1_marker) && button13.getText().toString().equals(player1_marker) && button14.getText().toString().equals(player1_marker) && button11.getText().toString().equals("")){
            button11.setText(player2_marker);
            return true;
        }else if(button11.getText().toString().equals(player1_marker) && button12.getText().toString().equals(player1_marker) && button13.getText().toString().equals(player1_marker) && button14.getText().toString().equals(player1_marker) && button10.getText().toString().equals("")){
            button10.setText(player2_marker);
            return true;
        }else if(button20.getText().toString().equals(player1_marker) && button21.getText().toString().equals(player1_marker) && button22.getText().toString().equals(player1_marker) && button23.getText().toString().equals(player1_marker) && button24.getText().toString().equals("")){
            button24.setText(player2_marker);
            return true;
        }else if(button20.getText().toString().equals(player1_marker) && button21.getText().toString().equals(player1_marker) && button22.getText().toString().equals(player1_marker) && button24.getText().toString().equals(player1_marker) && button23.getText().toString().equals("")){
            button23.setText(player2_marker);
            return true;
        }else if(button20.getText().toString().equals(player1_marker) && button21.getText().toString().equals(player1_marker) && button23.getText().toString().equals(player1_marker) && button24.getText().toString().equals(player1_marker) && button22.getText().toString().equals("")){
            button22.setText(player2_marker);
            return true;
        }else if(button20.getText().toString().equals(player1_marker) && button22.getText().toString().equals(player1_marker) && button23.getText().toString().equals(player1_marker) && button24.getText().toString().equals(player1_marker) && button21.getText().toString().equals("")){
            button21.setText(player2_marker);
            return true;
        }else if(button21.getText().toString().equals(player1_marker) && button22.getText().toString().equals(player1_marker) && button23.getText().toString().equals(player1_marker) && button24.getText().toString().equals(player1_marker) && button20.getText().toString().equals("")){
            button20.setText(player2_marker);
            return true;
        }else if(button30.getText().toString().equals(player1_marker) && button31.getText().toString().equals(player1_marker) && button32.getText().toString().equals(player1_marker) && button33.getText().toString().equals(player1_marker) && button34.getText().toString().equals("")){
            button34.setText(player2_marker);
            return true;
        }else if(button30.getText().toString().equals(player1_marker) && button31.getText().toString().equals(player1_marker) && button32.getText().toString().equals(player1_marker) && button34.getText().toString().equals(player1_marker) && button33.getText().toString().equals("")){
            button33.setText(player2_marker);
            return true;
        }else if(button30.getText().toString().equals(player1_marker) && button31.getText().toString().equals(player1_marker) && button33.getText().toString().equals(player1_marker) && button34.getText().toString().equals(player1_marker) && button32.getText().toString().equals("")){
            button32.setText(player2_marker);
            return true;
        }else if(button30.getText().toString().equals(player1_marker) && button32.getText().toString().equals(player1_marker) && button33.getText().toString().equals(player1_marker) && button34.getText().toString().equals(player1_marker) && button31.getText().toString().equals("")){
            button31.setText(player2_marker);
            return true;
        }else if(button31.getText().toString().equals(player1_marker) && button32.getText().toString().equals(player1_marker) && button33.getText().toString().equals(player1_marker) && button34.getText().toString().equals(player1_marker) && button30.getText().toString().equals("")){
            button30.setText(player2_marker);
            return true;
        }else if(button40.getText().toString().equals(player1_marker) && button41.getText().toString().equals(player1_marker) && button42.getText().toString().equals(player1_marker) && button43.getText().toString().equals(player1_marker) && button44.getText().toString().equals("")){
            button44.setText(player2_marker);
            return true;
        }else if(button40.getText().toString().equals(player1_marker) && button41.getText().toString().equals(player1_marker) && button42.getText().toString().equals(player1_marker) && button44.getText().toString().equals(player1_marker) && button43.getText().toString().equals("")){
            button43.setText(player2_marker);
            return true;
        }else if(button40.getText().toString().equals(player1_marker) && button41.getText().toString().equals(player1_marker) && button43.getText().toString().equals(player1_marker) && button44.getText().toString().equals(player1_marker) && button42.getText().toString().equals("") ){
            button42.setText(player2_marker);
            return true;
        }else if(button40.getText().toString().equals(player1_marker) && button42.getText().toString().equals(player1_marker) && button43.getText().toString().equals(player1_marker) && button44.getText().toString().equals(player1_marker) && button41.getText().toString().equals("")){
            button41.setText(player2_marker);
            return true;
        }else if(button41.getText().toString().equals(player1_marker) && button42.getText().toString().equals(player1_marker) && button43.getText().toString().equals(player1_marker) && button44.getText().toString().equals(player1_marker) && button40.getText().toString().equals("")){
            button40.setText(player2_marker);
            return true;
        }else if(button00.getText().toString().equals(player1_marker) && button10.getText().toString().equals(player1_marker) && button20.getText().toString().equals(player1_marker) && button30.getText().toString().equals(player1_marker) && button40.getText().toString().equals("")){
            button40.setText(player2_marker);
            return true;
        }else if(button00.getText().toString().equals(player1_marker) && button10.getText().toString().equals(player1_marker) && button20.getText().toString().equals(player1_marker) && button40.getText().toString().equals(player1_marker) && button40.getText().toString().equals("")){
            button30.setText(player2_marker);
            return true;
        }else if(button00.getText().toString().equals(player1_marker) && button10.getText().toString().equals(player1_marker) && button30.getText().toString().equals(player1_marker) && button40.getText().toString().equals(player1_marker) && button20.getText().toString().equals("")){
            button20.setText(player2_marker);
            return true;
        }else if(button00.getText().toString().equals(player1_marker) && button20.getText().toString().equals(player1_marker) && button30.getText().toString().equals(player1_marker) && button40.getText().toString().equals(player1_marker) && button10.getText().toString().equals("")){
            button10.setText(player2_marker);
            return true;
        }else if(button10.getText().toString().equals(player1_marker) && button20.getText().toString().equals(player1_marker) && button30.getText().toString().equals(player1_marker) && button40.getText().toString().equals(player1_marker) && button00.getText().toString().equals("")){
            button00.setText(player2_marker);
            return true;
        }else if(button01.getText().toString().equals(player1_marker) && button11.getText().toString().equals(player1_marker) && button21.getText().toString().equals(player1_marker) && button31.getText().toString().equals(player1_marker) && button41.getText().toString().equals("")){
            button41.setText(player2_marker);
            return true;
        }else if(button01.getText().toString().equals(player1_marker) && button11.getText().toString().equals(player1_marker) && button21.getText().toString().equals(player1_marker) && button41.getText().toString().equals(player1_marker) && button31.getText().toString().equals("")){
            button31.setText(player2_marker);
            return true;
        }else if(button01.getText().toString().equals(player1_marker) && button11.getText().toString().equals(player1_marker) && button31.getText().toString().equals(player1_marker) && button41.getText().toString().equals(player1_marker) && button21.getText().toString().equals("")){
            button21.setText(player2_marker);
            return true;
        }else if(button01.getText().toString().equals(player1_marker) && button21.getText().toString().equals(player1_marker) && button31.getText().toString().equals(player1_marker) && button41.getText().toString().equals(player1_marker) && button11.getText().toString().equals("")){
            button11.setText(player2_marker);
            return true;
        }else if(button11.getText().toString().equals(player1_marker) && button21.getText().toString().equals(player1_marker) && button31.getText().toString().equals(player1_marker) && button41.getText().toString().equals(player1_marker) && button01.getText().toString().equals("")){
            button01.setText(player2_marker);
            return true;
        }else if(button02.getText().toString().equals(player1_marker) && button12.getText().toString().equals(player1_marker) && button22.getText().toString().equals(player1_marker) && button32.getText().toString().equals(player1_marker) && button42.getText().toString().equals("")){
            button42.setText(player2_marker);
            return true;
        }else if(button02.getText().toString().equals(player1_marker) && button12.getText().toString().equals(player1_marker) && button22.getText().toString().equals(player1_marker) && button42.getText().toString().equals(player1_marker) && button32.getText().toString().equals("")){
            button32.setText(player2_marker);
            return true;
        }else if(button02.getText().toString().equals(player1_marker) && button12.getText().toString().equals(player1_marker) && button32.getText().toString().equals(player1_marker) && button42.getText().toString().equals(player1_marker) && button22.getText().toString().equals("")){
            button22.setText(player2_marker);
            return true;
        }else if(button02.getText().toString().equals(player1_marker) && button22.getText().toString().equals(player1_marker) && button32.getText().toString().equals(player1_marker) && button42.getText().toString().equals(player1_marker) && button12.getText().toString().equals("")){
            button12.setText(player2_marker);
            return true;
        }else if(button12.getText().toString().equals(player1_marker) && button22.getText().toString().equals(player1_marker) && button32.getText().toString().equals(player1_marker) && button42.getText().toString().equals(player1_marker) && button02.getText().toString().equals("")){
            button02.setText(player2_marker);
            return true;
        }else if(button03.getText().toString().equals(player1_marker) && button13.getText().toString().equals(player1_marker) && button23.getText().toString().equals(player1_marker) && button33.getText().toString().equals(player1_marker) && button43.getText().toString().equals("")){
            button43.setText(player2_marker);
            return true;
        }else if(button03.getText().toString().equals(player1_marker) && button13.getText().toString().equals(player1_marker) && button23.getText().toString().equals(player1_marker) && button43.getText().toString().equals(player1_marker) && button33.getText().toString().equals("")){
            button33.setText(player2_marker);
            return true;
        }else if(button03.getText().toString().equals(player1_marker) && button13.getText().toString().equals(player1_marker) && button33.getText().toString().equals(player1_marker) && button43.getText().toString().equals(player1_marker) && button23.getText().toString().equals("")){
            button23.setText(player2_marker);
            return true;
        }else if(button03.getText().toString().equals(player1_marker) && button23.getText().toString().equals(player1_marker) && button33.getText().toString().equals(player1_marker) && button43.getText().toString().equals(player1_marker) && button13.getText().toString().equals("")){
            button13.setText(player2_marker);
            return true;
        }else if(button13.getText().toString().equals(player1_marker) && button23.getText().toString().equals(player1_marker) && button33.getText().toString().equals(player1_marker) && button43.getText().toString().equals(player1_marker) && button03.getText().toString().equals("")){
            button03.setText(player2_marker);
            return true;
        }else if(button04.getText().toString().equals(player1_marker) && button14.getText().toString().equals(player1_marker) && button24.getText().toString().equals(player1_marker) && button34.getText().toString().equals(player1_marker) && button44.getText().toString().equals("")){
            button44.setText(player2_marker);
            return true;
        }else if(button04.getText().toString().equals(player1_marker) && button14.getText().toString().equals(player1_marker) && button24.getText().toString().equals(player1_marker) && button44.getText().toString().equals(player1_marker) && button34.getText().toString().equals("")){
            button34.setText(player2_marker);
            return true;
        }else if(button04.getText().toString().equals(player1_marker) && button14.getText().toString().equals(player1_marker) && button34.getText().toString().equals(player1_marker) && button44.getText().toString().equals(player1_marker) && button24.getText().toString().equals("")){
            button24.setText(player2_marker);
            return true;
        }else if(button04.getText().toString().equals(player1_marker) && button24.getText().toString().equals(player1_marker) && button34.getText().toString().equals(player1_marker) && button44.getText().toString().equals(player1_marker) && button41.getText().toString().equals("")){
            button41.setText(player2_marker);
            return true;
        }else if(button14.getText().toString().equals(player1_marker) && button24.getText().toString().equals(player1_marker) && button34.getText().toString().equals(player1_marker) && button44.getText().toString().equals(player1_marker) && button04.getText().toString().equals("")){
            button04.setText(player2_marker);
            return true;
        }else if(button00.getText().toString().equals(player1_marker) && button11.getText().toString().equals(player1_marker) && button22.getText().toString().equals(player1_marker) && button33.getText().toString().equals(player1_marker) && button44.getText().toString().equals("")){
            button44.setText(player2_marker);
            return true;
        }else if(button00.getText().toString().equals(player1_marker) && button11.getText().toString().equals(player1_marker) && button22.getText().toString().equals(player1_marker) && button44.getText().toString().equals(player1_marker) && button33.getText().toString().equals("")){
            button33.setText(player2_marker);
            return true;
        }else if(button00.getText().toString().equals(player1_marker) && button11.getText().toString().equals(player1_marker) && button33.getText().toString().equals(player1_marker) && button44.getText().toString().equals(player1_marker) && button22.getText().toString().equals("")){
            button22.setText(player2_marker);
            return true;
        }else if(button00.getText().toString().equals(player1_marker) && button22.getText().toString().equals(player1_marker) && button33.getText().toString().equals(player1_marker) && button44.getText().toString().equals(player1_marker) && button11.getText().toString().equals("")){
            button11.setText(player2_marker);
            return true;
        }else if(button11.getText().toString().equals(player1_marker) && button22.getText().toString().equals(player1_marker) && button33.getText().toString().equals(player1_marker) && button44.getText().toString().equals(player1_marker) && button00.getText().toString().equals("")){
            button00.setText(player2_marker);
            return true;
        }else if(button04.getText().toString().equals(player1_marker) && button13.getText().toString().equals(player1_marker) && button22.getText().toString().equals(player1_marker) && button31.getText().toString().equals(player1_marker) && button40.getText().toString().equals("")){
            button40.setText(player2_marker);
            return true;
        }else if(button04.getText().toString().equals(player1_marker) && button13.getText().toString().equals(player1_marker) && button22.getText().toString().equals(player1_marker) && button40.getText().toString().equals(player1_marker) && button31.getText().toString().equals("")){
            button31.setText(player2_marker);
            return true;
        }else if(button04.getText().toString().equals(player1_marker) && button13.getText().toString().equals(player1_marker) && button31.getText().toString().equals(player1_marker) && button40.getText().toString().equals(player1_marker) && button22.getText().toString().equals("")){
            button22.setText(player2_marker);
            return true;
        }else if(button04.getText().toString().equals(player1_marker) && button22.getText().toString().equals(player1_marker) && button31.getText().toString().equals(player1_marker) && button40.getText().toString().equals(player1_marker) && button13.getText().toString().equals("")){
            button13.setText(player2_marker);
            return true;
        }else if(button13.getText().toString().equals(player1_marker) && button22.getText().toString().equals(player1_marker) && button31.getText().toString().equals(player1_marker) && button40.getText().toString().equals(player1_marker) && button04.getText().toString().equals("")){
            button04.setText(player2_marker);
            return true;
        }
        return false;
    }
}
