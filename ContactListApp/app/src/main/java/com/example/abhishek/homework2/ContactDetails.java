package com.example.abhishek.homework2;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.abhishek.homework2.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


/**
 * Created by User on 4/9/2017.
 */

public class ContactDetails extends Fragment {
    public ArrayList<info> RetreivedData = new ArrayList<>();
    private ListView Contactlist;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public Bitmap imageBitmap;
    public ImageView camera;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_contact_details, container, false);

        Contactlist = view.findViewById(R.id.relationshiplistview);
        getdatafromdatabase();
        camera = view.findViewById(R.id.photo);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }


            }
        });

        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            File photoFile = null;
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            camera.setImageBitmap(imageBitmap);
        }
    }

    private void getdatafromdatabase() {

        SQLiteDatabase database = getActivity().openOrCreateDatabase("mydata", Context.MODE_PRIVATE,null);
        Cursor c = database.rawQuery("Select * from contact",null);
        int nameIndex = c.getColumnIndex("Name");
        while (c.moveToNext()){
            RetreivedData.add(new info(c.getString(nameIndex)));
        }

        relativelistAdapter adapter = new relativelistAdapter(getContext(),R.layout.relativeslist,RetreivedData);
        Contactlist.setAdapter(adapter);
    }



    }
