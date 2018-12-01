package com.example.abhishek.homework3;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class automatic_mode extends Service {
    LocationManager locationManager_time;
    LocationManager locationManager_dist;
    LocationListener locationListener_time;
    LocationListener locationListener_dist;
    private static final String TAG = "automatic_mode";
    int count=0;



    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        locationManager_time = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager_dist = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        Log.i(TAG, "onStartCommandcheckcheck: ");




        locationListener_time = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i(TAG, "onLocationChanged_time: checkcheck");
                database(location);
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

        locationListener_dist = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(count !=0){
                    Log.i(TAG, "onLocationChanged_dist: checkcheck");

                    database(location);

                }
                count =1;
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

        locationManager_time.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 0, locationListener_time);
        locationManager_dist.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, locationListener_dist);

    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroyautomatic: ");
        stopSelf();
        locationManager_dist.removeUpdates(locationListener_time);
        locationManager_time.removeUpdates(locationListener_dist);
        super.onDestroy();
    }

    @Nullable
    @Override
    
    public IBinder onBind(Intent intent) {
        return null;
        
    }



    public void database(Location location){
        SQLiteDatabase myDatabase;
        double latitudeupdate =location.getLatitude();
        double longitudeupdate =location.getLongitude();

        Date currentTime = Calendar.getInstance().getTime();
        String currenttime = currentTime.toString();
        Log.i(TAG, "database_automatic: "+currenttime);


        myDatabase = this.openOrCreateDatabase("mydata", Context.MODE_PRIVATE, null);
        Cursor c = myDatabase.rawQuery("Select * from Checkins",null);

        int Latitude1 = c.getColumnIndex("Latitude");
        int Longitude1 = c.getColumnIndex("Longitude");
        int getname = c.getColumnIndex("Name");
        String Name = "";
        Geocoder geocoder = new Geocoder(getApplicationContext(),Locale.getDefault());
        String finaladdress = "";

        try {
            List<Address> address = geocoder.getFromLocation(latitudeupdate,longitudeupdate,1);
            Log.i(TAG, "onLocationChanged: "+address);

            if(address.size()>0){
                finaladdress = address.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (c.moveToNext()) {
            Location loc2 = new Location("");
            double latitude1 = c.getDouble(Latitude1);
            double longitude1 = c.getDouble(Longitude1);
            loc2.setLongitude(longitude1);
            loc2.setLatitude(latitude1);
            float distance = location.distanceTo(loc2);
            Log.i(TAG, "distance: "+distance);
            if(distance<30){
                Name = c.getString(getname);
                break;
            }
        }
        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS Checkins(Number INTEGER PRIMARY KEY AUTOINCREMENT, Name VARCHAR,Address VARCHAR,Latitude DOUBLE, Longitude  DOUBLE, Time VARCHAR)");
        myDatabase.execSQL("INSERT INTO Checkins (Name,Address, Latitude,Longitude,Time) VALUES ('"+Name+"','"+finaladdress+"','"+latitudeupdate+"', '"+longitudeupdate+"','"+currenttime+"')");

    }

}


