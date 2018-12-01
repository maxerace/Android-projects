package com.example.abhishek.homework2;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class BlankFragment extends Fragment {
private ListView Contactlist;
    ArrayList<String> del = new ArrayList<>();

    public ArrayList<info> RetreivedData = new ArrayList<>();
    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View Contacts =  inflater.inflate(R.layout.fragment_blank, container, false);
        Contactlist = Contacts.findViewById(R.id.listview);
        getdatafromdatabase();

        Button button = getActivity().findViewById(R.id.Delete);
        return Contacts;
    }

    public void onResume(){
        super.onResume();
        RetreivedData.clear();
        getdatafromdatabase();
    }

    public void getdatafromdatabase() {
        SQLiteDatabase database = getActivity().openOrCreateDatabase("mydata", MODE_PRIVATE,null);
        RetreivedData.clear();
        database.execSQL("CREATE TABLE IF NOT EXISTS contact ( Name VARCHAR PRIMARY KEY, Number VARCHAR )");

        Cursor c = database.rawQuery("Select * from contact",null);
        int nameIndex = c.getColumnIndex("Name");
        while (c.moveToNext()){
            RetreivedData.add(new info(c.getString(nameIndex)));
        }
        ContactlistAdapter adapter = new ContactlistAdapter(getActivity(),R.layout.contactlist,RetreivedData);
        Contactlist.setAdapter(adapter);
    }




}
