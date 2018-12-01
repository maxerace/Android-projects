package com.example.abhishek.homework2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class finalAdapter extends ArrayAdapter<String> {

    int Resource;
    private Context newContext;

    public finalAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> objects) {
        super(context, resource,  objects);
        Resource = resource;
        newContext=context;
    }

    @NonNull
    @Override
    public View getView(int position,  @Nullable View convertView,  @NonNull ViewGroup parent) {

        String RelName = getItem(position);

        LayoutInflater inflater = LayoutInflater.from(newContext);
        convertView = inflater.inflate(Resource,parent,false);
        TextView textView = convertView.findViewById(R.id.textview);
        textView.setText(RelName);
        return convertView;
    }
}
