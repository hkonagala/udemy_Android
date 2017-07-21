package com.example.harikakonagala.guessthecelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity{

    ImageView celebrity;
    Button first, second, third, fourth;
    GridLayout gridLayout;

    ArrayList<String> celebrityUrls = new ArrayList<String>();
    ArrayList<String> celebrityNames = new ArrayList<String>();
    int chosenCeleb = 0;
    int locationOfCorrectAnswer = 0;
    String[] answers = new String[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridLayout = (GridLayout) findViewById(R.id.gridlayout);
        celebrity = (ImageView) findViewById(R.id.celebrity_image);
        first = (Button) findViewById(R.id.first);
        second = (Button) findViewById(R.id.second);
        third = (Button) findViewById(R.id.third);
        fourth = (Button) findViewById(R.id.fourth);

        downloadTask task = new downloadTask();
        String result = null;

        try {
            result = task.execute("http://www.posh24.se/kandisar").get();
            String[] splitResult = result.split("<div class=\"sidebarContainer\">");

            //parse the html content for celebrity names and image urls
            Pattern pattern = Pattern.compile("<img src=\"(.*?)\"");
            Matcher matcher = pattern.matcher(splitResult[0]);

            while (matcher.find()){
                celebrityUrls.add(matcher.group(1));
            }

            pattern = Pattern.compile("alt=\"(.*?)\"");
            matcher = pattern.matcher(splitResult[0]);

            while (matcher.find()){
                celebrityNames.add(matcher.group(1));
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        generateNewQuestion();
    }

    public void generateNewQuestion(){

        //generate random images of celebrities
        Random random = new Random();
        chosenCeleb = random.nextInt(celebrityUrls.size());

        ImageTask imageTask = new ImageTask();
        Bitmap myImage;

        try {
            myImage = imageTask.execute(celebrityUrls.get(chosenCeleb)).get();

            celebrity.setImageBitmap(myImage);

            //generate random options

            locationOfCorrectAnswer = random.nextInt(4);

            int incorrectAnswerLocation;
            for(int i =0; i< 4 ; i++){
                if(i == locationOfCorrectAnswer){
                    answers[i] = celebrityNames.get(chosenCeleb);
                }else {

                    incorrectAnswerLocation = random.nextInt(celebrityUrls.size());

                    while(incorrectAnswerLocation == chosenCeleb){
                        incorrectAnswerLocation = random.nextInt(celebrityUrls.size());
                    }
                    answers[i] = celebrityNames.get(incorrectAnswerLocation);
                }
            }

            first.setText(answers[0]);
            second.setText(answers[1]);
            third.setText(answers[2]);
            fourth.setText(answers[3]);
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public  void selectOption(View view){
        if(view.getTag().toString().equals(Integer.toString(locationOfCorrectAnswer))){
            Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(), "Wrong, It's " + celebrityNames.get(chosenCeleb), Toast.LENGTH_LONG).show();
        }

        generateNewQuestion();
    }

    public class downloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {


            String result = "";
            URL url;
            HttpURLConnection httpURLConnection = null;

            try {
                url = new URL(params[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                int data = inputStreamReader.read();

                while (data !=-1){
                    char current = (char) data;
                    result += current;
                    data = inputStreamReader.read();
                }

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }

    public class ImageTask extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... params) {
            try{
                URL url = new URL(params[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;

            }catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
