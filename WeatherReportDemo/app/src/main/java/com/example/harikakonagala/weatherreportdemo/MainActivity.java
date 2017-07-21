package com.example.harikakonagala.weatherreportdemo;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button search;
    TextView location, resultbox;
    EditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search = (Button) findViewById(R.id.search_button);
        location = (TextView) findViewById(R.id.tv_city);
        text = (EditText) findViewById(R.id.et_searchbox);
        resultbox = (TextView) findViewById(R.id.tv_result);



        search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromInputMethod(text.getWindowToken(), 0);


        try {
            downloadTask task = new downloadTask();
            //url encoder eliminates white spaces in url
            task.execute("http://samples.openweathermap.org/data/2.5/weather?q=" + URLEncoder.encode(text.getText().toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "could not find weather update", Toast.LENGTH_LONG).show();
        }

    }



    public class downloadTask extends AsyncTask<String, Void, String> {

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

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "could not find weather update", Toast.LENGTH_LONG).show();
            }


            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //JSON parsing
            try {
                String message = "";

                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("weather");

                for(int i =0; i< jsonArray.length(); i++){
                    JSONObject object = jsonArray.getJSONObject(i);

                    String main = object.getString("main");
                    String desc = object.getString("description");
                    Log.i("weather content", desc);

                    if(main != "" && desc != ""){
                        message += main + ":" + desc + "\r\n";
                    }
                   /* JSONObject main = object.getJSONObject("main");
                    String temp = main.getString("temp");
                    String pressure = main.getString("pressure");
                    String humidity = main.getString("humidity");

                    JSONArray weather = object.getJSONArray("weather");

                    for(int j =0; j< weather.length(); j++){
                        JSONObject weatherObject = weather.getJSONObject(j);
                        String desc = weatherObject.getString("description");


                    }*/

                }

                if(message != ""){
                    resultbox.setText(message);
                }else {
                    Toast.makeText(getApplicationContext(), "could not find weather update", Toast.LENGTH_LONG).show();
                }



            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "could not find weather update", Toast.LENGTH_LONG).show();
            }
        }
    }
}
