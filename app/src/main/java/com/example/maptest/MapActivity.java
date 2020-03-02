package com.example.maptest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.InfoWindow;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import org.w3c.dom.Text;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Map;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, LocationEngineListener {
    private long backPressedTime;
    private ArrayList<String> userData;
    private Double radius;
    private Double user_lat;
    private ConstraintLayout constLayout;
    private Double user_long;
    private int func_flag;
    private MapboxMap mapboxMap;
    private LocationEngine locationEngine;
    MapView mapView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        constLayout=findViewById(R.id.constraint);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        Intent extras = getIntent();
        StringBuilder string = new StringBuilder();
        userData = extras.getStringArrayListExtra("mapdata");
        for (String s : userData) {
            string.append(s);
            string.append("\n");
        }
        Log.d("detailsinmap", string.toString());
        radius = Double.parseDouble(userData.get(userData.size() - 1));
        //radius=radius/2;
        userData.remove(userData.size() - 1);
        BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.botnav);
        navigationView.setOnNavigationItemSelectedListener(mNavigationListener);
        setCameraOnPerson();
    }
    protected void setCameraOnPerson()
    {
        func_flag=1;
        mapView.getMapAsync(this);
    }

    @Override
    public void onConnected() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationEngine.requestLocationUpdates();
    }
    @Override
    public void onLocationChanged(Location location) {
        user_lat=location.getLatitude();
        user_long=location.getLongitude();
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 15));
        locationEngine.removeLocationEngineListener(this);
    }
    @Override
    public void onMapReady(final MapboxMap mapboxMap) {
        this.mapboxMap=mapboxMap;
        switch (func_flag) {
            case 1:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                this.mapboxMap.getLocationComponent().activateLocationComponent(this);
                this.mapboxMap.getLocationComponent().setLocationComponentEnabled(true);
                locationEngine=this.mapboxMap.getLocationComponent().getLocationEngine();
                locationEngine.addLocationEngineListener(this);
                break;
                //setCamera(mapboxMap);
        }
    }

    @Override
    public void onMapError(int i, String s) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==4)
        {
            if(resultCode==4 && data!=null)
            {
                StringBuilder string= new StringBuilder();
                userData= data.getStringArrayListExtra("mapdata");
                radius=Double.parseDouble(userData.get(userData.size()-1));
                userData.remove(userData.size()-1);
                for(String s: userData)
                {
                    string.append(s);
                    string.append("\n");
                }
                Log.d("detailsinmap",string.toString());
            }
        }
        if(requestCode==5)
        {
            if(resultCode==5 && data!=null)
            {
                Toast.makeText(this, "Adding marker", Toast.LENGTH_SHORT).show();
                String latitude=data.getStringExtra("latitude");
                String longitude=data.getStringExtra("longitude");
                double lat=0;
                if(latitude!=null)
                    lat=Double.parseDouble(latitude);
                double longi=0;
                if(longitude!=null)
                    longi=Double.parseDouble(longitude);
                Toast.makeText(this,"Adding at:"+latitude+" "+longitude,Toast.LENGTH_SHORT).show();
                String event_name=data.getStringExtra("event_name");
                String event_des=data.getStringExtra("description");
                String event_venue=data.getStringExtra("address");
                String event_interest=data.getStringExtra("interest");
                IconFactory iconFactory= IconFactory.getInstance(this);
                Icon icon= iconFactory.fromResource(R.drawable.mapbox_marker_icon_default);
                mapboxMap.addMarker(new MarkerOptions().position(new LatLng(lat,longi)).icon(icon).title(event_name).snippet(event_des+"\nVenue:\n"+event_venue));
                mapboxMap.setInfoWindowAdapter(new MapboxMap.InfoWindowAdapter() {
                    @Nullable
                    @Override
                    public View getInfoWindow(@NonNull Marker marker) {
                        View view= getLayoutInflater().inflate(R.layout.info_window,null);
                        TextView info_title=view.findViewById(R.id.info_title);
                        TextView info_des=view.findViewById(R.id.info_des);
                        info_title.setText(marker.getTitle());
                        info_des.setText(marker.getSnippet());
                        ImageButton ivbutton=view.findViewById(R.id.tickButton) ;
                        return view;
                    }
                });
                mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {

                        Toast.makeText(MapActivity.this, marker.getPosition().toString(), Toast.LENGTH_SHORT).show();
                        View view=mapboxMap.getInfoWindowAdapter().getInfoWindow(marker);
                        mapView.setVisibility(View.INVISIBLE);
                        ConstraintLayout.LayoutParams layoutParams=new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,ConstraintLayout.LayoutParams.MATCH_PARENT);
                        layoutParams.width=layoutParams.MATCH_PARENT;
                        layoutParams.height=layoutParams.MATCH_PARENT;
                        view.setLayoutParams(layoutParams);
                        constLayout.addView(view);
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                v.setVisibility(View.GONE);
                                constLayout.removeView(v);
                                mapView.setVisibility(View.VISIBLE);
                            }
                        });
                        return false;
                    }
                });
            }
        }
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mNavigationListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch(item.getItemId()){
                case R.id.settings :    Intent intent=new Intent(MapActivity.this, Settings.class);
                                        intent.putStringArrayListExtra("chosen",userData);
                                        intent.putExtra("radius",radius);
                                        startActivityForResult(intent,4);
                                        return true;
                                        //break;
                case R.id.host: startActivityForResult(new Intent(MapActivity.this, HostActivity.class),5);

                return true;
                                //break;

            }
            return false;
        }
    };
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if(backPressedTime+2000>System.currentTimeMillis()) {
            Intent intent=new Intent();
            setResult(3,intent);
            finish();
            return;
        }
        else
        {
            Toast.makeText(this, "Press back again to exit app", Toast.LENGTH_SHORT).show();
        }
        backPressedTime=System.currentTimeMillis();
    }
}
