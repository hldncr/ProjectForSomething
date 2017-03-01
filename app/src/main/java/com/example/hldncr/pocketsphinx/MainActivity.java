package com.example.hldncr.pocketsphinx;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.PocketSphinx;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

public class MainActivity extends AppCompatActivity implements SpeechRecognizerManager.Communicate{

    private SpeechRecognizerManager manager ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = new SpeechRecognizerManager(this) ;
        manager.setCom(this);
    }



    //Opening the emergency time activity, when SpeechRecognizerManager calls this function with Interface
    @Override
    public void comResult(){
        Intent intent = new Intent(MainActivity.this,RescueActivity.class) ;
        startActivity(intent);
    }

}
