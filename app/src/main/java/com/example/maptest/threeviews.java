package com.example.maptest;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

public class threeviews extends ConstraintLayout {
    CheckBox checkBox;
    TextView textView;
    ImageView imageView;
    Context mContext;
    public threeviews(Context context)
    {
        super(context);
        initializeViews(context);
    }
    public threeviews(Context context, AttributeSet attributeSet)
    {
        super(context,attributeSet);
        initializeViews(context);
    }
    public threeviews(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initializeViews(context);
    }


    private void initializeViews(Context context) {
        mContext=context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.threeviews, this);
        imageView=findViewById(R.id.imageView);
        textView=findViewById(R.id.textView);
        checkBox=findViewById(R.id.checkBox);
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //textView.setText("Hello world");
        //imageView.setImageResource(R.drawable.ic_launcher_foreground);
    }
    protected void setImage(int res)
    {
        this.imageView.setImageResource(res);
    }
    protected void setText(String res)
    {
        this.textView.setText(res);
    }
    protected String getStringText()
    {
        return this.textView.getText().toString();
    }
    protected  void setCheckBoxTrue()
    {
        this.checkBox.setChecked(true);
        this.checkBox.setActivated(true);
        this.checkBox.setEnabled(true);
    }
    protected void setCheckBoxFalse()
    {
        this.checkBox.setChecked(false);
        this.checkBox.setActivated(false);
        this.checkBox.setEnabled(false);
    }
    protected boolean getBoxCondition()
    {
        return checkBox.isActivated();
    }
}
