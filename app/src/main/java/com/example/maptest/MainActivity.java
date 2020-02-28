package com.example.maptest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.maps.MapView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivityForResult(new Intent(MainActivity.this,Selector.class), 2);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2) {
            if(resultCode== 2 && data!=null) {
            //Toast.makeText(this,"CAME BACK",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(MainActivity.this,MapActivity.class);
            intent.putStringArrayListExtra("mapdata",data.getStringArrayListExtra("data"));
            startActivityForResult(intent,3);
            }
        }
        if(requestCode==3 || resultCode==3 )
        {
            finish();
        }
    }
}
