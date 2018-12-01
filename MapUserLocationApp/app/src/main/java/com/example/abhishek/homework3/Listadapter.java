package com.example.abhishek.homework3;


import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class Listadapter extends ArrayAdapter <listentries>{
    private Context Context;
    int Resources;
    public Listadapter(@NonNull Context context, int resource, @NonNull List<listentries> objects) {
        super(context, resource, objects);
        Context = context;
        Resources = resource;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(Context);
        convertView = inflater.inflate(Resources,parent,false);
        String latitude = getItem(position).getLatitude();
        String longitude = getItem(position).getLongitude();
        String name = getItem(position).getName();
        String timestamp = getItem(position).getTimestamp();
        String address = getItem(position).getAddress();
        TextView latitudeview = (TextView) convertView.findViewById(R.id.displaylatitude);
        TextView longitudeview = (TextView)convertView.findViewById(R.id.displaylongitude);
        TextView nameview = (TextView)convertView.findViewById(R.id.displayname);
        TextView timestampview = (TextView) convertView.findViewById(R.id.displaytime);
        TextView addressview = (TextView)convertView.findViewById(R.id.displayaddress);
        latitudeview.setText(latitude);
        longitudeview.setText(longitude);
        nameview.setText(name);
        addressview.setText(address);
        timestampview.setText(timestamp);
        return convertView;
    }
}


