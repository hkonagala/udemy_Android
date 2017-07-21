package com.example.harikakonagala.jsondemo;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        downloadTask task = new downloadTask();
        task.execute("http://samples.openweathermap.org/data/2.5/forecast?id=524901&appid=b1b15e88fa797225412429c1c50c122a1");
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

                while(data != -1){
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


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //JSON parsing
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("list");

                for(int i =0; i< jsonArray.length(); i++){
                    JSONObject object = jsonArray.getJSONObject(i);

                    JSONArray weather = object.getJSONArray("weather");

                    for(int j =0; j< weather.length(); j++){
                        JSONObject weatherObject = weather.getJSONObject(j);
                        String desc = weatherObject.getString("description");

                        Log.i("weather content", desc);
                    }

                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
