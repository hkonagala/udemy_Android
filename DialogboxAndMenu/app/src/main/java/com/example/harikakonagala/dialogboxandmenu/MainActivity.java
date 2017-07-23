package com.example.harikakonagala.dialogboxandmenu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences;
    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = (TextView) findViewById(R.id.language_tv);
        preferences = this.getSharedPreferences("com.example.harikakonagala.dialogboxandmenu", Context.MODE_PRIVATE);
        String language = preferences.getString("language", "");
        if(language == ""){

            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_btn_speak_now)
                    .setTitle("choose a language")
                    .setMessage("which language would you like to choose?")
                    .setPositiveButton("english", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setLanguage("english");
                        }
                    })
                    .setNegativeButton("spanish", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setLanguage("spanish");
                        }
                    })
                    .show();
        }else{
            text.setText(language);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NewActivity.class);
                startActivity(i);
            }
        });

    }

    private void setLanguage(String language) {

        preferences.edit().putString("language", language).apply();
        text.setText(language);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to proceed further?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Toast.makeText(getApplicationContext(), "ok! suit yourself", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton("no", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Toast.makeText(getApplicationContext(), "good choice", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .show();
                return true;
            case R.id.menu:
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle("menu info")
                        .setMessage("you have clicked on menu")
                        .show();
                return true;
            case R.id.english:
                setLanguage("english");
                return true;
            case R.id.spanish:
                setLanguage("spanish");
                return true;
            case R.id.web:
                Intent i = new Intent(MainActivity.this, WebActivity.class);
                startActivity(i);
            default:
                return false;
        }

    }
}
