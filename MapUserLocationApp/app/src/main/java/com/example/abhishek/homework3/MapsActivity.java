package com.example.abhishek.homework3;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {
    LocationManager locationManager;
    LocationListener locationListener;
    private GoogleMap mMap;
    boolean temp = false;
    private static final String TAG = "MapsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setCompassEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
        datadisplay(googleMap);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i(TAG, "onLocationChanged: checkcheck");
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                SQLiteDatabase database = openOrCreateDatabase("mydata", MODE_PRIVATE, null);
                database.execSQL("CREATE TABLE IF NOT EXISTS Checkins(Number INTEGER PRIMARY KEY AUTOINCREMENT, Name VARCHAR,Address VARCHAR,Latitude DOUBLE, Longitude  DOUBLE, Time VARCHAR)");
                Cursor c = database.rawQuery("Select * from Checkins", null);
                int name = c.getColumnIndex("Name");
                int Address = c.getColumnIndex("Address");
                int Latitude = c.getColumnIndex("Latitude");
                int Longitude = c.getColumnIndex("Longitude");
                int time = c.getColumnIndex("Time");

                while (c.moveToNext()){
                    Location loc2 = new Location("");
                    loc2.setLatitude(c.getDouble(Latitude));
                    loc2.setLongitude(c.getDouble(Longitude));
                    float distance = loc2.distanceTo(location);
                    if(distance<30){
                        String Name = c.getString(name);
                        LatLng latLng = new LatLng(c.getDouble(Latitude), c.getDouble(Longitude));
                        mMap.addMarker(new MarkerOptions().position(latLng).title(c.getString(name))).showInfoWindow();

                        temp = true;
                        break;

                    }
                    else{
                        temp = false;
                    }
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter description");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng point) {
                if(input.getParent()!=null){
                    ((ViewGroup)input.getParent()).removeView(input);
                }
                builder.setView(input);

                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String description;
                        description = input.getText().toString();
                        mMap.clear();
                        SQLiteDatabase database = openOrCreateDatabase("mydata", MODE_PRIVATE, null);
                        Log.i(TAG, "mapdata: "+point.latitude);
                        database.execSQL("CREATE TABLE IF NOT EXISTS Mapentries(Number INTEGER PRIMARY KEY AUTOINCREMENT, Name VARCHAR,Latitude DOUBLE, Longitude  DOUBLE)");
                        database.execSQL("INSERT INTO Mapentries (Name, Latitude,Longitude) VALUES ('"+description+"','"+point.latitude+"', '"+point.longitude+"')");
                        datadisplay(mMap);

                    }

                });
                builder.show();

                input.setText("");

            }
        });

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }



    public void datadisplay(GoogleMap googleMap){
        mMap = googleMap;
        SQLiteDatabase database = openOrCreateDatabase("mydata", MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS Checkins(Number INTEGER PRIMARY KEY AUTOINCREMENT, Name VARCHAR,Address VARCHAR,Latitude DOUBLE, Longitude  DOUBLE, Time VARCHAR)");
        Cursor c = database.rawQuery("Select * from Checkins", null);
        int name = c.getColumnIndex("Name");
        int Address = c.getColumnIndex("Address");
        int Latitude = c.getColumnIndex("Latitude");
        int Longitude = c.getColumnIndex("Longitude");
        int time = c.getColumnIndex("Time");

        while (c.moveToNext()){
            LatLng mylocation = new LatLng(c.getDouble(Latitude),c.getDouble(Longitude));
            mMap.addMarker(new MarkerOptions().position(mylocation).title(c.getString(name)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(mylocation));

        }
        database.execSQL("CREATE TABLE IF NOT EXISTS Mapentries(Number INTEGER PRIMARY KEY AUTOINCREMENT, Name VARCHAR,Latitude DOUBLE, Longitude  DOUBLE)");
        Cursor d = database.rawQuery("Select * from Mapentries", null);
        int Latitudemap = d.getColumnIndex("Latitude");
        int Longitudemap = d.getColumnIndex("Longitude");
        int namemap = d.getColumnIndex("Name");

        while (d.moveToNext()){
            LatLng mylocation = new LatLng(d.getDouble(Latitudemap),d.getDouble(Longitudemap));
            mMap.addMarker(new MarkerOptions().position(mylocation).title(d.getString(namemap)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(mylocation));

        }


    }

}
