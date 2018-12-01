package com.example.abhishek.homework2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class relativelistAdapter extends ArrayAdapter <info>{
    private static final String TAG = "relativelistAdapter";
    private int newResource;
    private  Context newContext;
    public relativelistAdapter(@NonNull Context context, int resource,  @NonNull ArrayList<info> objects) {
        super(context, resource,  objects);
        this.newContext=context;
        this.newResource=resource;
    }


    public class ViewHolder{
        private TextView textView;
        private CheckBox checkBox;
    }

    @NonNull
    @Override
    public View getView(int position,@Nullable View convertView,  @NonNull ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(newContext);
            convertView=inflater.inflate(R.layout.relativeslist,parent,false);
            holder=new ViewHolder();
            holder.textView= convertView.findViewById(R.id.relativetextview);
            holder.checkBox=convertView.findViewById(R.id.relativecheckBox);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
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
                    relations(Info.getName());
                }
                else{
                    Info.setCheck(false);
                    notifyDataSetChanged();
                    cancelRelation(Info.getName());
                }
            }
        });

        return convertView;
    }
    private void cancelRelation(String s){

        SQLiteDatabase relation = getContext().openOrCreateDatabase("mydata",Context.MODE_PRIVATE,null);
        relation.execSQL("CREATE TABLE IF NOT EXISTS relations(relatedpeople VARCHAR)");
        relation.execSQL("Delete from relations Where relatedpeople='"+s+"';");

    }
    private void relations(String s) {
        Log.d(TAG, "relations: Called");
        SQLiteDatabase relationsdatabase = getContext().openOrCreateDatabase("mydata", android.content.Context.MODE_PRIVATE,null);
        relationsdatabase.execSQL("CREATE TABLE IF NOT EXISTS relations(relatedpeople VARCHAR)");
        relationsdatabase.execSQL("INSERT INTO relations(relatedpeople) VALUES('"+s+"')");
        return;
    }
}
