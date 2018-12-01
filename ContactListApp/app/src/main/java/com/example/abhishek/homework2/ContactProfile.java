package com.example.abhishek.homework2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;


public class ContactProfile extends Fragment {
    TextView profileName;
    TextView profileNumber;
    ListView profileList;
    ArrayList<String> profileRelations = new ArrayList<>();
    String ProfileName;
    String ProfileNumber;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View profileview = inflater.inflate(R.layout.fragment_contact_profile,container,false);
        profileList = profileview.findViewById(R.id.profileList);
        getProfileList();
        updateProfileList();
        profileName=profileview.findViewById(R.id.profilename);
        profileNumber=profileview.findViewById(R.id.profilenumber);

        profileName.setText(ProfileName);
        profileNumber.setText(ProfileNumber);

        File file = new File(Environment.getExternalStorageDirectory().getPath(), "TestFolder");
        String filename = (file.getAbsolutePath() + "/"
                + ProfileName+ ".jpg");
        ImageView image = profileview.findViewById(R.id.profilephoto);
        image.setImageDrawable(Drawable.createFromPath(filename));
        return profileview;
    }

    private void updateProfileList() {
        finalAdapter adapter = new finalAdapter(getActivity(),R.layout.list_profile,profileRelations);
        profileList.setAdapter(adapter);
    }

    private void getProfileList() {
        SQLiteDatabase profiledata = getContext().openOrCreateDatabase("mydata", android.content.Context.MODE_PRIVATE,null);
        Cursor c= profiledata.rawQuery("Select * from clicked",null);
        int columnIndex = c.getColumnIndex("Calling");
        c.moveToNext();
        ProfileName = c.getString(columnIndex);


        Cursor c2 = profiledata.rawQuery("Select * from contact where Name ='"+ProfileName+"';",null);
        while (c2.moveToNext()) {

            ProfileNumber = c2.getString(c2.getColumnIndex("Number"));
        }
        profiledata.execSQL("Create table if not exists relatedPeopleList(Name VARCHAR, knows VARCHAR)");

        Cursor cursor = profiledata.rawQuery("Select * from relatedPeopleList where Name='"+ProfileName+"';",null);
        while (cursor.moveToNext()){
            profileRelations.add(cursor.getString(cursor.getColumnIndex("knows")));
        }

            }
}