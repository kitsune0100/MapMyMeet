package com.example.maptest;


import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mmi.services.api.autosuggest.model.ELocation;

import java.util.ArrayList;
import static com.mapbox.mapboxsdk.MapmyIndia.getApplicationContext;

public class AutoSuggestAdapter extends RecyclerView.Adapter<AutoSuggestAdapter.MyViewholder> {
    private ArrayList<ELocation> list;
    private OnItemClickListener onItemClickListener;
    String search;
    public AutoSuggestAdapter(ArrayList<ELocation> list, String search, OnItemClickListener onItemClickListener) {
        this.list = list;
        this.search=search;
        this.onItemClickListener=onItemClickListener;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.auto_suggest_adapter_row, parent, false);
        return new MyViewholder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, final int position) {

        setSpannableText(holder, position);
        holder.viewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onItemClickListener.onItemClick(list.get(position));
                //placeName.nameOfPlace(list.get(position).placeName);
            }
        });
    }

    public void setSpannableText(@NonNull MyViewholder holder, int position)
    {
        String place=list.get(position).placeName+", ";
        String address=list.get(position).placeAddress;
        Log.d("Elocations : ",place+"\t"+address+"\t"+list.get(position).toString());
        SpannableString spannableString = new SpannableString(place+address);
        spannableString.setSpan(new RelativeSizeSpan(0.8f),place.length(),place.length()+address.length(),0);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getApplicationContext(),R.color.grey)),place.length(),place.length()+address.length(),0);
        spannableString.setSpan(new StyleSpan(Typeface.ITALIC),place.length(),place.length()+address.length(),0);

        int pos=-1;
        if((pos=place.toLowerCase().indexOf(search.toLowerCase().trim()))>-1){
            spannableString.setSpan(new StyleSpan(Typeface.BOLD),pos,pos+search.length(),0);
        }
        else if((pos=address.toLowerCase().indexOf(search.toLowerCase().trim()))>-1)
        {
            pos+=place.length();
            spannableString.setSpan(new StyleSpan(Typeface.BOLD),pos,pos+search.length(),0);
        }
        holder.viewName.setText(spannableString, TextView.BufferType.SPANNABLE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class MyViewholder extends RecyclerView.ViewHolder {

        TextView viewName;

        public MyViewholder(View itemView) {
            super(itemView);
            viewName = itemView.findViewById(R.id.textView);
        }
    }
}
