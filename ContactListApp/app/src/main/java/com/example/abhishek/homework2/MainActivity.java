package com.example.abhishek.homework2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    ArrayList<String> people = new ArrayList<>();
    ArrayList<String> del =new ArrayList<>();
    public ArrayList<info> RetreivedData = new ArrayList<>();
    ListView ContactList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ContactList = findViewById(R.id.listview);
        SQLiteDatabase myDatabase;
        myDatabase = this.openOrCreateDatabase("mydata",Context.MODE_PRIVATE,null);
        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS contact ( Name VARCHAR PRIMARY KEY, Number VARCHAR )");

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){


        }
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            Button addbutton = (Button) findViewById(R.id.add_button);

            addbutton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Fragment fragment = new ContactDetails();
                    FragmentManager fm = getSupportFragmentManager();
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.contactDetails, fragment);
                    fragmentTransaction.commit();

                }
            });


        }

    }



    public FragmentManager getmanager(){
        return getSupportFragmentManager();
    }

    public void Add(View view) {
        Intent intent = new Intent(this, add_contact.class);
        startActivity(intent);
    }

    public void AddData(View view) {
        people.clear();
        SQLiteDatabase myDatabase;
        myDatabase = this.openOrCreateDatabase("mydata", Context.MODE_PRIVATE, null);
        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS contact ( Name VARCHAR PRIMARY KEY, Number VARCHAR )");
        EditText name = findViewById(R.id.Entername);
        EditText number = findViewById(R.id.EnterNumber);
        String inputName = name.getText().toString();
        String inputNumber = number.getText().toString();
        Cursor test = myDatabase.rawQuery("Select * from contact where Name='" + inputName + "';", null);

        if (inputName.matches("") || inputNumber.matches("")) {
            Toast.makeText(getApplicationContext(), "Name or Number field not entered", Toast.LENGTH_SHORT).show();
        } else if (test.moveToNext()) {
            Toast.makeText(getApplicationContext(), "This Contact number already exists", Toast.LENGTH_LONG).show();
            myDatabase.execSQL("Delete from relations ");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            ImageView imageView = (ImageView) findViewById(R.id.photo);
            Bitmap bm = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            FileOutputStream out = null;
            File file = new File(Environment.getExternalStorageDirectory().getPath(), "TestFolder");
            if (!file.exists()) {
                file.mkdirs();
            }
            String filename = (file.getAbsolutePath() + "/"
                    + inputName + ".jpg");
            try {
                out = new FileOutputStream(filename);
                bm.compress(Bitmap.CompressFormat.PNG, 100, out);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            myDatabase.execSQL("Insert into contact (Name , Number) " +
                    "Values('" + inputName + "','" + inputNumber + "')");
            myDatabase.execSQL("CREATE TABLE IF NOT EXISTS relations(relatedpeople VARCHAR)");
            Cursor cursor = myDatabase.rawQuery("SELECT * FROM relations", null);
            while (cursor.moveToNext()) {
                people.add(cursor.getString(cursor.getColumnIndex("relatedpeople")));
            }
            myDatabase.execSQL("DELETE FROM relations");


            for (int j = 0; j < people.size(); j++) {
                myDatabase.execSQL("Create table if not exists relatedPeopleList(Name VARCHAR, knows VARCHAR)");
                myDatabase.execSQL("Insert into relatedPeopleList (Name,knows) Values ('" + inputName + "','" + people.get(j) + "')");
            }


            for (int j = 0; j < people.size(); j++) {
                myDatabase.execSQL("Create table if not exists relatedPeopleList(Name VARCHAR, knows VARCHAR)");
                myDatabase.execSQL("Insert into relatedPeopleList (Name,knows) Values ('" + people.get(j) + "','" + inputName + "')");
            }

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        }

        public void DeleteEntries (View view){
            SQLiteDatabase data = this.openOrCreateDatabase("mydata", MODE_PRIVATE, null);
            del.clear();
            Cursor cursor = data.rawQuery("Select * from relations", null);
            int index = cursor.getColumnIndex("relatedpeople");
            while (cursor.moveToNext()) {
                del.add(cursor.getString(index));
            }


            data.execSQL("Delete from relations ");

            for (int j = 0; j < del.size(); j++) {
                data.execSQL("Delete from contact where Name ='" + del.get(j) + "';");
                data.execSQL("Delete from relatedPeopleList Where Name='" + del.get(j) + "' or knows='" + del.get(j) + "';");
            }

            SQLiteDatabase database = this.openOrCreateDatabase("mydata", MODE_PRIVATE, null);
            RetreivedData.clear();
            database.execSQL("CREATE TABLE IF NOT EXISTS contact ( Name VARCHAR PRIMARY KEY, Number VARCHAR )");

            Cursor c = database.rawQuery("Select * from contact", null);
            int nameIndex = c.getColumnIndex("Name");
            while (c.moveToNext()) {
                RetreivedData.add(new info(c.getString(nameIndex)));
            }

            ContactlistAdapter adapter = new ContactlistAdapter(this, R.layout.contactlist, RetreivedData);
            ContactList.setAdapter(adapter);
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Fragment fragment = new EmptyFragment();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.contactDetails, fragment);
                fragmentTransaction.commit();

            }

        }

}
