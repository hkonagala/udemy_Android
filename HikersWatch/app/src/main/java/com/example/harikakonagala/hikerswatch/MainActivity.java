package com.example.harikakonagala.hikerswatch;

import android.Manifest;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener{

    TextView lat, lon, accuracy, alt, addr;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lat = (TextView) findViewById(R.id.tv_lat);
        lon = (TextView) findViewById(R.id.tv_lon);
        accuracy = (TextView) findViewById(R.id.tv_acc);
        alt = (TextView) findViewById(R.id.tv_alt);
        addr = (TextView) findViewById(R.id.tv_addr);

    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PlacesActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onLocationChanged(Location location) {

        lat.setText("Latitude: " + location.getLatitude());
        lon.setText("Longitude: " + location.getLongitude());
        accuracy.setText("Accuracy: " + location.getAccuracy());
        alt.setText("Altitude: " + location.getAltitude());

        try {
            Geocoder geocoder = new Geocoder(MainActivity.this.getApplicationContext(), Locale.getDefault());
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if(addressList.isEmpty()){
                addr.setText("Address: Waiting for Location Update");
            }else if(addressList.size() >0){
                addr.setText("Address: " + addressList.get(0).getFeatureName() + ", " + addressList.get(0).getLocality() +"\r\n\t\t\t\t\t\t" + addressList.get(0).getAdminArea() + ", " + addressList.get(0).getCountryName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
