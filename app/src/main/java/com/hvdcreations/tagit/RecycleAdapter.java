package com.hvdcreations.tagit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.RecycleViewHolder> {

    Context context;
    ArrayList<tagdata> tags;

    String latitude;
    String longitude;


    public RecycleAdapter(Context c, ArrayList<tagdata> p){
        context = c;
        tags = p;
    }

    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new RecycleViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewHolder recycleViewHolder, int i) {

        recycleViewHolder.title.setText(tags.get(i).getTitle());
        recycleViewHolder.navi.setText(tags.get(i).getLatitude());

        latitude = tags.get(i).getLatitude();
        longitude = tags.get(i).getLongitude();
        Toast.makeText(context, latitude+"/"+longitude, Toast.LENGTH_SHORT).show();


    }

    @Override
    public int getItemCount() {
        return tags.size();
    }


    class RecycleViewHolder extends RecyclerView.ViewHolder{

        TextView title, navi;

        public RecycleViewHolder(@NonNull final View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.cardview_title);
            navi = itemView.findViewById(R.id.cardview_navi);


        }

    }
}
