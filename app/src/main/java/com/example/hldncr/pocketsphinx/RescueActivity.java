package com.example.hldncr.pocketsphinx;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RescueActivity extends AppCompatActivity {

    private Vibrator v ;
    private Context c;
    Boolean sw1,sw2 ;
    private View decorView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        decorView = getWindow().getDecorView();
        setContentView(R.layout.activity_rescue);

        //Get Context
        c = getApplicationContext();

        //Creating Vibrator Object and vibrate 2 second
        v = (Vibrator) this.c.getSystemService(Context.VIBRATOR_SERVICE) ;
        v.vibrate(2000);

        //Taking Switch Situatuion from Other Activity
        Intent intent = getIntent() ;
        sw1 = intent.getExtras().getBoolean("Switch1") ;
        sw2 = intent.getExtras().getBoolean("Switch2") ;

        // Dialing 911 if switch2 open
        if(sw2) {
            Intent phoneIntent = new Intent(Intent.ACTION_CALL);
            phoneIntent.setData(Uri.parse("tel:911"));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            //TODO Add Send Message Function To 911

            startActivity(phoneIntent);
        }

        // Vibrate 5 second after call
        v.vibrate(5000);
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

    public void GoBack(View view) {
        v.cancel();
        finish();
    }
}
