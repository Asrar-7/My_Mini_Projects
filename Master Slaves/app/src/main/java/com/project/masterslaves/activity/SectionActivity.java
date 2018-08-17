package com.project.masterslaves.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.project.masterslaves.R;

/**
 * Created by Intkonsys on 24/02/2017.
 */

public class SectionActivity extends AppCompatActivity implements View.OnClickListener
{
    ImageButton b1,b2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);
        b1=(ImageButton)findViewById(R.id.imageButton2);
        b1.setOnClickListener(this);
        b2=(ImageButton)findViewById(R.id.imageButton3);
        b2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==b1)
        {
          Intent in=new Intent(SectionActivity.this,UserLoginActivity.class);
          startActivity(in);
        }
        else
        {
            Intent in=new Intent(SectionActivity.this,BusSectionActivity.class);
            startActivity(in);
        }
    }
}
