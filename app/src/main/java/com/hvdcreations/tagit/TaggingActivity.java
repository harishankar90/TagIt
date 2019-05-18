package com.hvdcreations.tagit;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class TaggingActivity extends AppCompatActivity implements OnMapReadyCallback {

    TextView TVtitle,TVdesc,TVloc,TVadrs;
    EditText ETtitle,ETdesc;
    Button saveLoc;
    GoogleMap mMap;
    MapFragment mapFragment;

    String lati,longi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagging);

        TVtitle = findViewById(R.id.txt_title);
        TVdesc = findViewById(R.id.txt_desc);
        TVloc = findViewById(R.id.txt_Location);
        TVadrs = findViewById(R.id.text_adrs);

        ETtitle = findViewById(R.id.et_title);
        ETdesc = findViewById(R.id.et_desc);

        saveLoc = findViewById(R.id.btn_save);

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mv_loc);
        mapFragment.getMapAsync(TaggingActivity.this);

        Intent intent = getIntent();
        lati = intent.getStringExtra("Latitude");
        longi = intent.getStringExtra("Longitude");


        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(Double.valueOf(lati), Double.valueOf(longi), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();

            TVadrs.setText(address);
            Toast.makeText(this, address+"/"+city+"/"+country+"/"+postalCode, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setBuildingsEnabled(true);

        LatLng location = new LatLng(Double.valueOf(lati), Double.valueOf(longi));
        mMap.addMarker(new MarkerOptions().position(location));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,18));

    }
}
