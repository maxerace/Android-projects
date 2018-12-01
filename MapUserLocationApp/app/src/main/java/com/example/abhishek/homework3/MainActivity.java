package com.example.abhishek.homework3;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;
    boolean switch1state = false;
    boolean switch2state = false;
    long time1;
    long time2;
    String provider;
    public ArrayList<listentries> RetreivedData = new ArrayList<>();
    private ListView listView;
    Listadapter adapter;
    private static final String TAG = "MainActivity";
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                Log.i(TAG, "PERMISSION CHECK");
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Switch switch1 = (Switch)findViewById(R.id.switch1);
        Switch switch2  = (Switch)findViewById(R.id.switch2);


        LoadPreferences();
        switch1.setChecked(switch1state);
        switch2.setChecked(switch2state);
        listView = (ListView) findViewById(R.id.ListView);
        getdatafromdatabase();
        SQLiteDatabase database = openOrCreateDatabase("mydata", MODE_PRIVATE,null);
        Cursor c = database.rawQuery("SELECT * FROM Checkins",null);

        if (c.getCount() == 0){

            double lat[] = {40.521884,40.518704,40.523642, 40.523454,40.525907};
            double lon[] = {-74.463243,-74.461511,-74.465014,-74.457992,-74.458569};
            String x[] = {"Hillcenter", "Werblin", "ARC","Busch Students centre","Davidson Hall"};
            for(int i = 0; i<5;i++){
                Geocoder geocoder = new Geocoder(getApplicationContext(),Locale.getDefault());
                String finaladdress = "";

                try {
                    List<Address> address = geocoder.getFromLocation(lat[i],lon[i],1);

                    if(address.size()>0){
                        finaladdress = address.get(0).getAddressLine(0);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String name = x[i];
                database.execSQL("INSERT INTO Checkins (Name,Address, Latitude,Longitude)" +
                        " VALUES ('"+name+"','"+finaladdress+"','"+lat[i]+"', '"+lon[i]+"')");


            }
            RetreivedData.clear();
            getdatafromdatabase();
        }
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b){
                    Log.i(TAG, "onCheckedChangedcheckcheck: ");
                    switch1state =  b;
                    startService(new Intent(MainActivity.this, automatic_mode.class));                }
                else{
                    switch1state =  b;
                    stopService(new Intent(MainActivity.this, automatic_mode.class));
                    Log.i(TAG, "returnedcheckcheck: ");
                    RetreivedData.clear();
                    getdatafromdatabase();
                }
            }
        });
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                Log.i(TAG, "ACCURACY = "+location.getAccuracy());
                time2 = System.currentTimeMillis();
                long time = time2 - time1;
                Log.i(TAG, "Delay: "+provider+":"+time);
                time1 = System.currentTimeMillis();
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                Geocoder geocoder = new Geocoder(getApplicationContext(),Locale.getDefault());
                String finaladdress = "";

                try {
                    List<Address> address = geocoder.getFromLocation(latitude,longitude,1);

                    if(address.size()>0){
                        finaladdress = address.get(0).getAddressLine(0);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                TextView Latitude = (TextView) findViewById(R.id.latitude);
                Latitude.setText(String.valueOf(latitude));

                TextView Longitude = (TextView) findViewById(R.id.longitude);
                Longitude.setText(String.valueOf(longitude));


                TextView address = (TextView) findViewById(R.id.address);
                address.setText(String.valueOf(finaladdress));

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        }

            if(switch2state == false){

                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "location from GPS");
                    provider = "GPS";
                    locationManager.removeUpdates(locationListener);
                    time1 = System.currentTimeMillis();
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }

            }
            if(switch2state == true){

                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "location from Network");
                    provider = "Network";

                    locationManager.removeUpdates(locationListener);
                    time1 = System.currentTimeMillis();
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

                }

            }


        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        switch2state =  b;
                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            Log.i(TAG, "location from Network");
                            provider = "Network";
                            locationManager.removeUpdates(locationListener);
                            time1 = System.currentTimeMillis();
                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                        }
                    }
                    else{
                        switch2state =  b;
                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            provider = "GPS";
                            Log.i(TAG, "location from GPS");
                            locationManager.removeUpdates(locationListener);
                            time1 = System.currentTimeMillis();
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);


                        }
                    }
                }
            });





    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: called");
        savePreferences();
        super.onBackPressed();
    }

    private void savePreferences() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("switch1",switch1state);
        editor.putBoolean("switch2",switch2state);
        editor.apply();
    }

    private void LoadPreferences(){
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        switch1state = sharedPreferences.getBoolean("switch1", false);
        switch2state = sharedPreferences.getBoolean("switch2",false);

    }

    public void database(View view){
        SQLiteDatabase myDatabase;
        TextView address = findViewById(R.id.address);
        String Address = address.getText().toString();

        TextView latitude = findViewById(R.id.latitude);
        Double Latitude = Double.parseDouble(latitude.getText().toString());


        TextView longitude = findViewById(R.id.longitude);
        Double Longitude = Double.parseDouble(longitude.getText().toString());

        EditText locationname = findViewById(R.id.locationname);
        String Name = locationname.getText().toString();


        Date currentTime = Calendar.getInstance().getTime();
        String currenttime = currentTime.toString();
        Log.i(TAG, "database: "+currenttime);

        Location loc1 = new Location("");
        loc1.setLatitude(Latitude);
        loc1.setLongitude(Longitude);
        myDatabase = this.openOrCreateDatabase("mydata", Context.MODE_PRIVATE, null);
        Cursor c = myDatabase.rawQuery("Select * from Checkins",null);

        int Latitude1 = c.getColumnIndex("Latitude");
        int Longitude1 = c.getColumnIndex("Longitude");
        int getname = c.getColumnIndex("Name");
        while (c.moveToNext()) {
            Location loc2 = new Location("");
            double latitude1 = c.getDouble(Latitude1);
            double longitude1 = c.getDouble(Longitude1);
            loc2.setLongitude(longitude1);
            loc2.setLatitude(latitude1);
            float distance = loc1.distanceTo(loc2);
            Log.i(TAG, "distance: "+distance);
            if(distance<30){
                Log.i(TAG, "database:"+c.getString(getname));
                if (c.getString(getname).matches("")) {
                    Name = c.getString(getname);
                }
                break;
            }
        }
            myDatabase.execSQL("CREATE TABLE IF NOT EXISTS Checkins(Number INTEGER PRIMARY KEY AUTOINCREMENT, Name VARCHAR,Address VARCHAR,Latitude DOUBLE, Longitude  DOUBLE, Time VARCHAR)");
        myDatabase.execSQL("INSERT INTO Checkins (Name,Address, Latitude,Longitude,Time) VALUES ('"+Name+"','"+Address+"','"+Latitude+"', '"+Longitude+"','"+currenttime+"')");
        if(RetreivedData.size()==0){
            getdatafromdatabase();
        }
        else if(RetreivedData.size()>0){
            RetreivedData.add(new listentries(Latitude.toString(),Longitude.toString(),Name,currenttime,Address));
            adapter.notifyDataSetChanged();
        }
    }

    public void openmap(View view) {
        Intent intent=  new Intent(this, MapsActivity.class);
        startActivity(intent);
    }



    public void getdatafromdatabase() {
        SQLiteDatabase database = openOrCreateDatabase("mydata", MODE_PRIVATE,null);
        database.execSQL("CREATE TABLE IF NOT EXISTS Checkins(Number INTEGER PRIMARY KEY AUTOINCREMENT, Name VARCHAR,Address VARCHAR,Latitude DOUBLE, Longitude  DOUBLE, Time VARCHAR)");

        Cursor c = database.rawQuery("Select * from Checkins",null);
        int name = c.getColumnIndex("Name");
        int Address = c.getColumnIndex("Address");
        int Latitude = c.getColumnIndex("Latitude");
        int Longitude = c.getColumnIndex("Longitude");
        int time = c.getColumnIndex("Time");


        while (c.moveToNext()){
            Log.i(TAG, "getdatafromdatabase: "+c.getString(Address));
            RetreivedData.add(new listentries(c.getString(Latitude),c.getString(Longitude),c.getString(name),c.getString(time),c.getString(Address)));
        }
        adapter = new Listadapter(this,R.layout.list,RetreivedData);
        listView.setAdapter(adapter);
    }


}
