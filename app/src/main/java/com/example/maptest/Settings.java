package com.example.maptest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Settings extends AppCompatActivity {
    LinearLayout linearLayout;
    int[] images={R.drawable.laptop,R.mipmap.books_foreground,R.drawable.science,R.drawable.med,R.drawable.guitar,R.drawable.business,R.drawable.personal,R.drawable.maths,R.drawable.arch,R.drawable.art,R.drawable.phil};
    String[] text={"Development","Literature","Science","Health","Music","Business","Personal Development","Maths","Architecture","Art","Philosophy"};
    ArrayList<String> resultList= new ArrayList<String>();
    ArrayList<String> chosen=new ArrayList<>();
    Double chosen_radius;
    TextView countText;
    SeekBar seekBar;
    TextView kilometers;
    Vibrator vibe;
    int countTextCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);
        linearLayout=findViewById(R.id.horizontal_layout);
        vibe= (Vibrator) Settings.this.getSystemService(Context.VIBRATOR_SERVICE);
        countTextCount=0;
        countText= findViewById(R.id.count);
        kilometers=findViewById(R.id.kms);
        seekBar=findViewById(R.id.seekBar);
        chosen=getIntent().getExtras().getStringArrayList("chosen");
        chosen_radius=getIntent().getExtras().getDouble("radius");
        //Loop to create cards for each interest
        for(int i=0;i<11;i++)
        {
            createCard(i);
        }
        seekBar.setProgress(chosen_radius.intValue());
        kilometers.setText(String.valueOf(chosen_radius.intValue())+" Kilometers");
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                kilometers.setText(String.valueOf(progress)+" Kilometers");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                vibe.vibrate(50);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                kilometers.setText(String.valueOf(seekBar.getProgress())+" Kilometers");
            }
        });
        ImageButton tickButton= findViewById(R.id.tickButton);
        tickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibe.vibrate(50);
                //adding radius to last of String
                resultList.add(String.valueOf(seekBar.getProgress()));
                Intent resultIntent=new Intent();
                //Adding resultList to Intent
                resultIntent.putStringArrayListExtra("mapdata",resultList);
                setResult(4,resultIntent);
                finish();
            }
        });

    }
    //function to create card
    protected void createCard(int i)
    {
        CardView cardView=new CardView(this);
        LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(1070,LinearLayout.LayoutParams.MATCH_PARENT);
        //setting card layout
        cardView.setLayoutParams(layoutParams);
        cardView.setRadius(20);
        cardView.setMaxCardElevation(50);
        cardView.setElevation(20);
        cardView.setBackground(getDrawable(R.drawable.card_background));
        cardView.setTranslationZ(20);
        //creating compound view
        threeviews add_view=new threeviews(this);
        //setting respective image
        add_view.setImage(images[i]);
        add_view.setText(text[i]);
        cardView.addView(add_view);
        linearLayout.addView(cardView);
        if(chosen.contains(text[i])) {
            setTrue(add_view,cardView);
        }
        //setting listener for each card
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!add_view.getBoxCondition()) //if current box is not checked
                {
                    vibe.vibrate(50);
                    add_view.setCheckBoxTrue(); //making it true
                    resultList.add(add_view.getStringText()); //adding to interest list
                    countTextCount++;
                    String newtext= countTextCount+" Selected";
                    countText.setText(newtext);
                    cardView.setBackground(getDrawable(R.drawable.card_checked_background));
                }
                else //if current box is checked
                {
                    vibe.vibrate(50);
                    add_view.setCheckBoxFalse();
                    countTextCount--;
                    resultList.remove(add_view.getStringText()); //removing from interest list
                    String newtext= countTextCount+ " Selected";
                    cardView.setBackground(getDrawable(R.drawable.card_background));
                    countText.setText(newtext);
                }
            }
        });
    }
    protected void setTrue(threeviews add_view,CardView cardView) {
        vibe.vibrate(50);
        add_view.setCheckBoxTrue(); //making it true
        resultList.add(add_view.getStringText()); //adding to interest list
        countTextCount++;
        String newtext= countTextCount+" Selected";
        countText.setText(newtext);
        cardView.setBackground(getDrawable(R.drawable.card_checked_background));
    }
}
