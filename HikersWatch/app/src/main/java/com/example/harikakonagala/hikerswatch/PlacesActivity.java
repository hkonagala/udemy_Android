package com.example.harikakonagala.hikerswatch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class PlacesActivity extends AppCompatActivity {

    ListView listView;

    static ArrayList<String> places = new ArrayList<>();
    static ArrayList<LatLng> locations = new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        listView = (ListView) findViewById(R.id.list);

        places.add("Add a Place");
        locations.add(new LatLng(0,0));

        arrayAdapter = new ArrayAdapter(PlacesActivity.this, android.R.layout.simple_list_item_1, places);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PlacesActivity.this, MapsActivity.class);
                intent.putExtra("placeNumber", position);
                startActivity(intent);
            }
        });



    }
}
