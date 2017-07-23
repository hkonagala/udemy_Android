package com.example.harikakonagala.dialogboxandmenu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;

public class NewActivity extends AppCompatActivity {

    ListView listView;
    static ArrayAdapter arrayAdapter;
    static ArrayList<String> notes = new ArrayList<>();
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        listView = (ListView) findViewById(R.id.list);

        preferences = getApplicationContext().getSharedPreferences("com.example.harikakonagala.dialogboxandmenu", Context.MODE_PRIVATE);
        HashSet<String> set = (HashSet<String>) preferences.getStringSet("notes", null);
        if(set == null){
            notes.add("example note");
        }else{
            notes = new ArrayList(set);
        }


        arrayAdapter = new ArrayAdapter(NewActivity.this, android.R.layout.simple_list_item_1, notes);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(NewActivity.this, NotesActivity.class);
                i.putExtra("noteId", position);
                startActivity(i);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(NewActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete Note?")
                        .setMessage("do you want to delete the note?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notes.remove(position);
                                arrayAdapter.notifyDataSetChanged();

                                preferences = getApplicationContext().getSharedPreferences("com.example.harikakonagala.dialogboxandmenu", Context.MODE_PRIVATE);
                                HashSet<String> set = new HashSet<String>(NewActivity.notes);
                                preferences.edit().putStringSet("notes",set).apply();

                            }
                        }).setNegativeButton("no", null)
                        .show();

                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId() == R.id.notes){
            Intent i = new Intent(NewActivity.this, NotesActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
