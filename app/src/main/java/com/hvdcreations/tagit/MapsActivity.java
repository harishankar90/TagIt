package com.hvdcreations.tagit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    LocationManager locationManager;

    Button TageLoc;
    Double lati,longi;
    String l,ll;

    GoogleApiClient client;
    LatLng startLL,endLL;
    Marker cMarker,dMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mv_loc);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getlocations();

        TageLoc = findViewById(R.id.btn_LocSave);
        TageLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MapsActivity.this, l+", "+ll, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MapsActivity.this,TaggingActivity.class);
                intent.putExtra("Latitude",l);
                intent.putExtra("Longitude",ll);
                startActivity(intent);
            }
        });




    }

    private void getlocations() {
        ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.

                        try{
                            if (location != null) {

                                startLL = new LatLng(location.getLatitude(),location.getLongitude());
                                /*Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                                try{
                                    List<Address> myAddress = geocoder.getFromLocation(startLL.latitude,startLL.longitude,1);
                                    String address = myAddress.get(0).getAddressLine(0);
                                    String city = myAddress.get(0).getLocality();
                                    Toast.makeText(this, address+"/"+city, Toast.LENGTH_SHORT).show();
                                }catch (IOException e){
                                    e.printStackTrace();
                                }*/

                                if (cMarker == null){
                                    MarkerOptions options = new MarkerOptions();
                                    options.position(startLL);
                                    options.title("current position");
                                    mMap.clear();
                                    cMarker = mMap.addMarker(options);
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLL,18));
                                }else{
                                    cMarker.setPosition(startLL);
                                }

                                mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                                    @Override
                                    public void onCameraIdle() {
                                        LatLng center = mMap.getCameraPosition().target;
                                        if (cMarker!=null){
                                            cMarker.remove();
                                            mMap.clear();
                                            cMarker = mMap.addMarker(new MarkerOptions().position(center));
                                            startLL = cMarker.getPosition();
                                            l = String.valueOf(startLL.latitude);
                                            ll = String.valueOf(startLL.longitude);
                                            Toast.makeText(MapsActivity.this, l+", "+ll, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }else{
                                Toast.makeText(MapsActivity.this, "Location is not available, Check Whether Internet/GPS is switched ON", Toast.LENGTH_SHORT).show();

                            }
                        }catch (Exception e){
                            Toast.makeText(MapsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            //tv.setText(e.getMessage());
                        }


                    }
                });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

       /* LatLng india = new LatLng(13.1007902,80.2024893);
        mMap.addMarker(new MarkerOptions().position(india).title("India"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(13.1007902,80.2024893),1));*/
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);


        ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);


    }

/*

   @Override
    public void onLocationChanged(Location location) {
        if (location == null){
           Toast.makeText(this, "Location not available, Check your connection !", Toast.LENGTH_SHORT).show();
       } else{
            startLL = new LatLng(location.getLatitude(),location.getLongitude());
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try{
                List<Address> myAddress = geocoder.getFromLocation(startLL.latitude,startLL.longitude,1);
                String address = myAddress.get(0).getAddressLine(0);
                String city = myAddress.get(0).getLocality();
                Toast.makeText(this, address+"/"+city, Toast.LENGTH_SHORT).show();
            }catch (IOException e){
                e.printStackTrace();
            }

            if (cMarker == null){
                MarkerOptions options = new MarkerOptions();
                options.position(startLL);
                options.title("current position");
                cMarker = mMap.addMarker(options);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLL,15));
            }else{
                cMarker.setPosition(startLL);
            }

            mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
                    LatLng center = mMap.getCameraPosition().target;
                    if (cMarker!=null){
                        cMarker.remove();
                        mMap.addMarker(new MarkerOptions().position(center));
                        startLL = cMarker.getPosition();

                        Toast.makeText(MapsActivity.this, startLL.latitude+","+startLL.longitude, Toast.LENGTH_SHORT).show();
                    }
                }
            });

       }



        */
/*double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(latLng));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));*//*

//        locationTv.setText("Latitude:" + latitude + ", Longitude:" + longitude);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
*/


}
