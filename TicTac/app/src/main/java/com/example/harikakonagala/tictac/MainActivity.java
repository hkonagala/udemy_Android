package com.example.harikakonagala.tictac;

import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.example.harikakonagala.tictac.R.id.winnerMessage;

public class MainActivity extends AppCompatActivity {

    TextView winnerMessage;
    LinearLayout layout;
    GridLayout gridLayout;
    int activePlayer = 0; //0-player 1; 1-player 2
    boolean gameIsActive = true;

    int[] gameState = {2,2,2,2,2,2,2,2,2}; //unplayed state
    int[][] winnerState = {{0,1,2}, {3,4,5}, {6,7,8}, {0,3,6}, {1,4,7}, {2,5,8}, {0,4,8}, {2,4,6}}; //possible ways to win

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = (LinearLayout) findViewById(R.id.playAgain);
        gridLayout = (GridLayout) findViewById(R.id.grid_layout);
        winnerMessage = (TextView) findViewById(R.id.winnerMessage);
    }

    public void play(View view){

        ImageView counter = (ImageView) view; //initialize 3X3 positions

        int tappedCounter = Integer.parseInt(counter.getTag().toString());//getting the position on grid
        if(gameState[tappedCounter] == 2 && gameIsActive){
            gameState[tappedCounter] = activePlayer;
            counter.setTranslationY(-1000f);

            if(activePlayer == 0){
                counter.setImageResource(R.drawable.o);
                activePlayer = 1;
            }else{
                counter.setImageResource(R.drawable.x);
                activePlayer = 0;
            }
            counter.animate().translationYBy(1000f).rotation(360).setDuration(300);

            for(int[] winner : winnerState){

                if(gameState[winner[0]] == gameState[winner[1]] &&
                        gameState[winner[1]] == gameState[winner[2]] &&
                        gameState[winner[0]] != 2){
                    //someone has won

                    gameIsActive = false;

                    String win = "player X";

                    if(gameState[winner[0]] == 0){
                        win = "player O";
                    }

                    winnerMessage.setText(win + " has won!");
                    layout.setVisibility(View.VISIBLE);
                }else {
                    boolean gameIsOver = true;
                    for(int counterState : gameState){
                        if(counterState == 2) gameIsOver = false;
                    }

                    if(gameIsOver){
                        winnerMessage.setText(" It's a Tic Tac Toe Tie!");
                        layout.setVisibility(View.VISIBLE);
                    }
                }
            }
        }

    }

    public void playAgain(View view){

        gameIsActive = true;
        layout.setVisibility(View.INVISIBLE);

        activePlayer = 0;
        for(int i = 0; i< gameState.length ; i++){
            gameState[i] = 2;
        }//reset game state to unplayed

        for(int i =0 ; i< gridLayout.getChildCount(); i++){
            ((ImageView) gridLayout.getChildAt(i)).setImageResource(0);//refresh the game with invisible tokens
        }
    }
}
