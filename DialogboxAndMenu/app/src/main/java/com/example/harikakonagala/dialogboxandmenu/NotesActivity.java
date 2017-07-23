package com.example.harikakonagala.dialogboxandmenu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashSet;

import static android.R.attr.id;

public class NotesActivity extends AppCompatActivity {

    EditText editText;
    SharedPreferences preferences;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        editText = (EditText) findViewById(R.id.et_notes);
        Intent i = getIntent();
        id = i.getIntExtra("noteId", -1);

        if(id != -1){
            editText.setText(NewActivity.notes.get(id));
        }else{
            NewActivity.notes.add("");
            id = NewActivity.notes.size() - 1;
            NewActivity.arrayAdapter.notifyDataSetChanged();
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                NewActivity.notes.set(id, String.valueOf(s));
                NewActivity.arrayAdapter.notifyDataSetChanged();

                preferences = getApplicationContext().getSharedPreferences("com.example.harikakonagala.dialogboxandmenu", Context.MODE_PRIVATE);
                HashSet<String> set = new HashSet<String>(NewActivity.notes);
                preferences.edit().putStringSet("notes",set).apply();
            }

            @Override
            public void afterTextChanged(Editable s) {

                Toast.makeText(getApplicationContext(), "note saved", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
