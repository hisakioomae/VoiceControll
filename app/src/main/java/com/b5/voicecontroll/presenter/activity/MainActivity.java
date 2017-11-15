package com.b5.voicecontroll.presenter.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.b5.voicecontroll.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void timeEdit(View view){
        Intent teIntent = new Intent(this, EditActivity.class);
        startActivity(teIntent);
    }
}
