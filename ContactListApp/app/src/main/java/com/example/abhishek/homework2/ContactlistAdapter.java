package com.example.abhishek.homework2;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ContactlistAdapter extends ArrayAdapter <info>{
    int Resource;
    private Context Context;
    public ContactlistAdapter(@NonNull Context context, int resource, @NonNull ArrayList<info> objects) {
        super(context, resource, objects);
        Context = context;
        Resource = resource;
    }

    public class ViewHolder{
        private TextView textView;
        private CheckBox checkBox;
    }


    @NonNull
    @Override
    public View getView(int position,  @Nullable View convertView, @NonNull ViewGroup parent) {

        final ViewHolder holder ;
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(Context);
            convertView=inflater.inflate(R.layout.contactlist,parent,false);
            holder = new ViewHolder();
            holder.textView = convertView.findViewById(R.id.textview);
            holder.checkBox= convertView.findViewById(R.id.checkBox);
            convertView.setTag(holder);
        }
        else {
            holder =(ViewHolder) convertView.getTag();
        }


        final info Info = getItem(position);

        holder.textView.setText(Info.getName());
        holder.checkBox.setChecked(Info.getCheck());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkBox.isChecked()){
                    Info.setCheck(true);
                    notifyDataSetChanged();
                    deletion(Info.getName());
                }
                else{
                    Info.setCheck(false);
                    notifyDataSetChanged();
                    canceldeletion(Info.getName());
                }
            }
        });

        if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Contactclick(holder.textView.getText().toString());
                    getContext().startActivity(new Intent(getContext(),Activity3.class));

                }
            });

        }

        if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Contactclick(holder.textView.getText().toString());
                    Fragment fragment = new ContactProfile();
                    FragmentManager fm = ((MainActivity)getContext()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.contactDetails, fragment);
                    fragmentTransaction.commit();

                }
            });

        }

        return convertView;
    }

    private void Contactclick(String s) {

        SQLiteDatabase relationsdatabase = getContext().openOrCreateDatabase("mydata", android.content.Context.MODE_PRIVATE,null);
        relationsdatabase.execSQL("create table if not exists clicked(Calling Varchar) ");
        relationsdatabase.execSQL("Delete from clicked");
        relationsdatabase.execSQL("insert into clicked (Calling) Values ('"+s+"')");

    }

    private void canceldeletion(String s){

        SQLiteDatabase relation = getContext().openOrCreateDatabase("mydata",Context.MODE_PRIVATE,null);
        relation.execSQL("CREATE TABLE IF NOT EXISTS relations(relatedpeople VARCHAR)");
        relation.execSQL("Delete from relations Where relatedpeople='"+s+"';");

    }


    private void deletion(String s) {
        SQLiteDatabase relationsdatabase = getContext().openOrCreateDatabase("mydata", android.content.Context.MODE_PRIVATE,null);
        relationsdatabase.execSQL("CREATE TABLE IF NOT EXISTS relations(relatedpeople VARCHAR)");
        relationsdatabase.execSQL("INSERT INTO relations(relatedpeople) VALUES('"+s+"')");
        return;
    }
}
