package com.example.hldncr.pocketsphinx;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.jar.Manifest;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.PocketSphinx;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

public class MainActivity extends AppCompatActivity implements SpeechRecognizerManager.Communicate{

    private SpeechRecognizerManager manager ;
    private boolean settings2 = false,settings1 = false ;
    private Switch sw1,sw2 ;
    private ImageView im1 ;
    private View decorView ;
    Context c ;

    boolean IsSecondActivityOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        decorView = getWindow().getDecorView();
        setContentView(R.layout.activity_main);


        ActivityCompat.requestPermissions(MainActivity.this,new String[]{android.Manifest.permission.RECORD_AUDIO},1);

        manager = new SpeechRecognizerManager(this) ;
        manager.setCom(this);

        c = MainActivity.this ;

        sw1 = (Switch) findViewById(R.id.switch1) ;
        sw2 = (Switch) findViewById(R.id.switch2) ;
        im1 = (ImageView) findViewById(R.id.imageView) ;


        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                settings1 = b ;
            }
        });

        sw2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                settings2 = b ;
            }
        });

    }



    //Opening the emergency time activity, when SpeechRecognizerManager calls this function with Interface
    @Override
    public void comResult(){
        if(settings1 && !IsSecondActivityOpen) {
            Intent intent = new Intent(MainActivity.this, RescueActivity.class);
            intent.putExtra("Switch1", settings1);
            intent.putExtra("Switch2", settings2);
            IsSecondActivityOpen = true ;
            startActivityForResult(intent,1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IsSecondActivityOpen = false;
    }

    @Override
    public void IsItRecording(int IsItRecord) {
        if(IsItRecord == 1)
            im1.setImageResource(R.drawable.success);
        else if(IsItRecord ==2)
            im1.setImageResource(R.drawable.refresh);
        else if(IsItRecord ==3)
            im1.setImageResource(R.drawable.error) ;
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }



}
