package com.example.harikakonagala.hikerswatch;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);



        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        centerMapOnLocation(lastKnownLocation, "Your Location");

    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                centerMapOnLocation(lastKnownLocation, "Your location");

            }


        }

    }

    public void centerMapOnLocation(Location location, String title) {

        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

        mMap.clear();

        if (title != "Your location") {

            mMap.addMarker(new MarkerOptions().position(userLocation).title(title));

        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 10));

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //open particular location from the listview
        Intent intent = getIntent();
        if(intent.getIntExtra("placeNumber", 0) != 0){
            Location placeLocation = new Location(LocationManager.GPS_PROVIDER);
            placeLocation.setLatitude(PlacesActivity.locations.get(intent.getIntExtra("placeNumber", 0)).latitude);
            placeLocation.setLongitude(PlacesActivity.locations.get(intent.getIntExtra("placeNumber", 0)).longitude);

            centerMapOnLocation(placeLocation, PlacesActivity.places.get(intent.getIntExtra("placeNumber", 0)));
        }

        mMap.setOnMapLongClickListener(this);

    }

    @Override
    public void onLocationChanged(Location location) {


        centerMapOnLocation(location, "Your Location");

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

    @Override
    public void onMapLongClick(LatLng latLng) {


        //add marker and save the location
        Geocoder geocoder = new Geocoder(MapsActivity.this.getApplicationContext(), Locale.getDefault());
        String address = "";
        try {

            List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if(addressList != null &&  addressList.size() >0){
                if(addressList.get(0).getThoroughfare() != null){
                    if(addressList.get(0).getSubThoroughfare() !=null){
                        address += addressList.get(0).getSubThoroughfare() + "";
                    }
                    address += addressList.get(0).getThoroughfare();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(address == ""){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:HH yyyy-MM-dd");
            address = simpleDateFormat.format(new Date());
        }
        mMap.addMarker(new MarkerOptions().position(latLng).title("Your New Location"));

        //update the listview
        PlacesActivity.places.add(address);
        PlacesActivity.locations.add(latLng);
        PlacesActivity.arrayAdapter.notifyDataSetChanged();
        Toast.makeText(getApplicationContext(), "Location Saved", Toast.LENGTH_SHORT).show();
    }
}
