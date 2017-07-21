package com.example.harikakonagala.braintrainer;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button startButton, playAgain;
    TextView timer, score, result, question;
    Button first, second, third, fourth;
    ArrayList<Integer> answers = new ArrayList<Integer>();
    int locationOfCorrectAnswer;
    int scored =0;
    int numberOfQuestions = 0;
    CountDownTimer countDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //startButton = (Button) findViewById(R.id.startbutton);
        playAgain = (Button) findViewById(R.id.bt_playagain);

        timer = (TextView) findViewById(R.id.tv_timer);
        score = (TextView) findViewById(R.id.tv_score);
        result = (TextView) findViewById(R.id.tv_result);
        question = (TextView) findViewById(R.id.tv_question);

        first = (Button) findViewById(R.id.first);
        second = (Button) findViewById(R.id.second);
        third = (Button) findViewById(R.id.third);
        fourth = (Button) findViewById(R.id.fourth);

        play();


    }

    @Override
    protected void onResume() {
        super.onResume();
       // startButton.setOnClickListener(this);
        playAgain.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /*case R.id.startbutton:
               // startButton.setVisibility(View.INVISIBLE);
                break;*/
            case R.id.bt_playagain:
                //TODO
                scored = 0;
                numberOfQuestions =0;
                timer.setText("30s");
                score.setText("0/0");
                result.setText("");
                playAgain.setVisibility(View.INVISIBLE);

                play();
                break;
        }

    }

    private void play() {


        generateQuestion();
        countDownTimer = new CountDownTimer(30100, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                timer.setText(String.valueOf(millisUntilFinished/1000) + "s");
            }

            @Override
            public void onFinish() {

                playAgain.setVisibility(View.VISIBLE);
                timer.setText("0s");
                result.setText("Your Score " + Integer.toString(scored) + "/" + Integer.toString(numberOfQuestions));
            }
        }.start();
    }

    public void generateQuestion(){
        //generate question
        Random rand = new Random();
        int a = rand.nextInt(21);
        int b = rand.nextInt(21);
        question.setText(Integer.toString(a) + " + " + Integer.toString(b));

        locationOfCorrectAnswer = rand.nextInt(4);//random position in grid

        answers.clear();
        int incorrectAnswer;
        for(int i =0; i< 4 ; i++){
            if(i == locationOfCorrectAnswer){
                answers.add(a + b);
            }else{
                incorrectAnswer = rand.nextInt(41);
                while (incorrectAnswer == a + b){
                    incorrectAnswer = rand.nextInt(41);
                }
                answers.add(incorrectAnswer);
            }
        }

        first.setText(Integer.toString(answers.get(0)));
        second.setText(Integer.toString(answers.get(1)));
        third.setText(Integer.toString(answers.get(2)));
        fourth.setText(Integer.toString(answers.get(3)));
    }

    public void chooseAnswer(View view){

        if(view.getTag().toString().equals(Integer.toString(locationOfCorrectAnswer))){
            Log.i("correct", "correct");
            scored++;
            result.setText("Correct!");
        }else{
            result.setText("Wrong!");
        }

        numberOfQuestions++;
        score.setText(Integer.toString(scored) + "/" + Integer.toString(numberOfQuestions));
        generateQuestion();
    }
}
