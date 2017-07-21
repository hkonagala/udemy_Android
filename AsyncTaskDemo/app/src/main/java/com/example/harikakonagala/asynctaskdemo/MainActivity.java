package com.example.harikakonagala.asynctaskdemo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadTask task = new DownloadTask();
        String result = null;
        try{
            result = task.execute("https://www.ecowebhosting.co.uk/").get();
        }catch (InterruptedException e){

            e.printStackTrace();
        }catch (ExecutionException e){
            e.printStackTrace();
        }

        Log.i("contents of url", result);
    }

    public class DownloadTask extends AsyncTask<String , Void, String >{

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection httpURLConnection = null;

            try {
                url = new URL(urls[0]);
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
            }
            catch (IOException e) {
                e.printStackTrace();
                return "failed";
            }

        }
    }
}
