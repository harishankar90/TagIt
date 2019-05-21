package com.hvdcreations.tagit;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.RecycleViewHolder> {

    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewHolder recycleViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class RecycleViewHolder extends RecyclerView.ViewHolder{

        public RecycleViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
