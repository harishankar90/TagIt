package com.hvdcreations.tagit;

import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;

public class ShowLocation extends AppCompatActivity {

    DatabaseReference reference;
    RecyclerView recyclerView;
    RecycleAdapter adapter;
    ArrayList<tagdata> tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_location);

        SharedPreferences myprefs= getSharedPreferences("user", MODE_PRIVATE);
        final String number = myprefs.getString("mobile", null);

        Toast.makeText(this, number, Toast.LENGTH_SHORT).show();

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tags = new ArrayList<tagdata>();

        reference = FirebaseDatabase.getInstance().getReference().child("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try{
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child(number).getChildren()){
                        tagdata p = dataSnapshot1.getValue(tagdata.class);
                        tags.add(p);
                    }
                    adapter = new RecycleAdapter(ShowLocation.this,tags);
                    recyclerView.setAdapter(adapter);
                }catch (Exception e){
                    Toast.makeText(ShowLocation.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ShowLocation.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

}
