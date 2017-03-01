package com.example.hldncr.pocketsphinx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RescueActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescue);
    }

    public void GoBack(View view) {
        finish();
    }
}
