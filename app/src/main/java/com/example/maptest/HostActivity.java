package com.example.maptest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mmi.services.api.autosuggest.MapmyIndiaAutoSuggest;
import com.mmi.services.api.autosuggest.model.AutoSuggestAtlasResponse;
import com.mmi.services.api.autosuggest.model.ELocation;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HostActivity extends AppCompatActivity implements TextWatcher,OnItemClickListener, AdapterView.OnItemSelectedListener {

    private EditText autoSuggestText;
    private RecyclerView recyclerView;
    private Handler handler;
    String latitude,longitude;
    String place;
    ImageButton tickButton;
    Vibrator vibe;
    OnItemClickListener onItemClickListener;
    EditText name,description;
    TextView address;
    Spinner interests;
    String interest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        initReferences();
        initListeners();
    }

    private void initReferences() {
        tickButton=findViewById(R.id.tick);
        autoSuggestText = findViewById(R.id.auto_suggest);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(HostActivity.this));
        recyclerView.setVisibility(View.GONE);
        handler = new Handler();
        name=findViewById(R.id.event_name);
        description=findViewById(R.id.description);
        address=findViewById(R.id.address);
        vibe= (Vibrator) HostActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
        onItemClickListener=this;

        interests=findViewById(R.id.interests);
        String[] text={"Development","Literature","Science","Health","Music","Business","Personal Development","Maths","Architecture","Art","Philosophy"};
        ArrayAdapter<String> interestAdapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,text);
        interestAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        interests.setAdapter(interestAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent,View view,int position,long id)
    {
        interest=parent.getItemAtPosition(position).toString();
        showToast(interest);
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0)
    {}

    private void initListeners() {
        autoSuggestText.addTextChangedListener(this);
        tickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibe.vibrate(50);
                Intent resultIntent=new Intent();
                resultIntent.putExtra("latitude",latitude);
                resultIntent.putExtra("longitude",longitude);
                resultIntent.putExtra("event_name",name.getText().toString());
                resultIntent.putExtra("description",description.getText().toString());
                resultIntent.putExtra("address",address.getText().toString());
                resultIntent.putExtra("interest",interest);
                setResult(5,resultIntent);
                finish();
            }
        });
    }

    private void callAutoSuggestApi(String searchString) {
        MapmyIndiaAutoSuggest.builder()
                .query(searchString)
                .build()
                .enqueueCall(new Callback<AutoSuggestAtlasResponse>() {
                    @Override
                    public void onResponse(Call<AutoSuggestAtlasResponse> call, Response<AutoSuggestAtlasResponse> response) {
                        if (response.code() == 200 && response.body() != null) {
                            ArrayList<ELocation> suggestedList = response.body().getSuggestedLocations();
                            if (suggestedList.size() > 0) {
                                recyclerView.setVisibility(View.VISIBLE);
                                AutoSuggestAdapter autoSuggestAdapter = new AutoSuggestAdapter(suggestedList, searchString, onItemClickListener);
                                recyclerView.setAdapter(autoSuggestAdapter);
                    } } }
                    @Override
                    public void onFailure(Call<AutoSuggestAtlasResponse> call, Throwable t) { showToast(t.toString()); }
        }); }

    @Override
    public void onItemClick(ELocation location)
    {
        recyclerView.setVisibility(View.INVISIBLE);
        place=location.placeName+", "+location.placeAddress;
        latitude=location.latitude;
        longitude=location.longitude;
        autoSuggestText.setText("");
        showToast(latitude+" "+longitude+"\t"+place);
        address.setText(place);
    }

    private void showToast(String msg) { Toast.makeText(HostActivity.this, msg, Toast.LENGTH_SHORT).show(); }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        handler.postDelayed(() -> {
            recyclerView.setVisibility(View.GONE);
            if (s.length() < 3)
                recyclerView.setAdapter(null);

            if (s != null && s.toString().trim().length() < 2) {
                recyclerView.setAdapter(null);
                return;
            }

            if (s.length() > 2) {
                if (CheckInternet.isNetworkAvailable(HostActivity.this)) {
                    callAutoSuggestApi(s.toString());
                } else {
                    showToast(getString(R.string.pleaseCheckInternetConnection));
                }
            }
        }, 400);

    }
    @Override
    public void afterTextChanged(Editable s) { }
}
