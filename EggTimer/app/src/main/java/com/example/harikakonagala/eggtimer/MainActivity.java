package com.example.harikakonagala.eggtimer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.countDown;

public class MainActivity extends AppCompatActivity {

    SeekBar timerSeekbar;
    TextView timer;
    Button go;
    MediaPlayer mediaPlayer;
    Boolean counterIsActive = false;
    CountDownTimer countDownTimer;

    public void resetTimer(){

        timer.setText("0:30");
        timerSeekbar.setProgress(30);
        countDownTimer.cancel();
        go.setText("Go!");
        timerSeekbar.setEnabled(true);
        counterIsActive = false;
    }

    public void updateTimer(int secondsLeft){
        int mins = (int) secondsLeft/60;
        int secs = secondsLeft - mins*60;

        String secondsString = Integer.toString(secs);
        if(secs <= 9){
            secondsString = "0" + secondsString;
        }
        timer.setText(Integer.toString(mins) +":" +secondsString);
    }

    public void controlTimer(View view){
        if (counterIsActive = false) {

            counterIsActive = true;
            timerSeekbar.setEnabled(false);
            go.setText("Stop");

            countDownTimer = new CountDownTimer(timerSeekbar.getProgress() * 1000 + 100, 1000) {


                @Override
                public void onTick(long millisUntilFinished) {

                    updateTimer((int) millisUntilFinished / 1000);
                }

                @Override
                public void onFinish() {

                    resetTimer();
                    Toast.makeText(getApplicationContext(), "Timer Done!", Toast.LENGTH_LONG).show();
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.egg_cracking);
                    mediaPlayer.start();
                }
            }.start();
        }else {
            resetTimer();

        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerSeekbar = (SeekBar) findViewById(R.id.timer_seekbar);
        timer = (TextView) findViewById(R.id.timer_tv);
        go = (Button) findViewById(R.id.btn_reset);

        timerSeekbar.setMax(600);
        timerSeekbar.setProgress(30);
        timerSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                updateTimer(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

}
