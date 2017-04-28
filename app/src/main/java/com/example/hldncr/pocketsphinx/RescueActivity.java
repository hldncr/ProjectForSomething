package com.example.hldncr.pocketsphinx;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class RescueActivity extends AppCompatActivity  {

    private Vibrator v ;
    private Context c;
    Boolean sw1,sw2 ;
    private View decorView ;
    private TextView vx,vy,maint ;
    LocationManager lm ;
    CountDownTimer ct;
    double x,y ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        decorView = getWindow().getDecorView();
        setContentView(R.layout.activity_rescue);

        vx = (TextView) findViewById(R.id.coordinatX) ;
        vy = (TextView) findViewById(R.id.coordinatY) ;
        maint = (TextView) findViewById(R.id.maintextview) ;

        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE) ;

        //Get Context
        c = getApplicationContext();

        //Creating Vibrator Object and vibrate 2 second
        v = (Vibrator) this.c.getSystemService(Context.VIBRATOR_SERVICE) ;
        v.vibrate(2000);

        //Taking Switch Situatuion from Other Activity
        Intent intent = getIntent() ;
        sw1 = intent.getExtras().getBoolean("Switch1") ;
        sw2 = intent.getExtras().getBoolean("Switch2") ;

        GetLastLocation();

        // Dialing 911 if switch2 open
        if(sw2) {
            // Vibrate 5 second after call
            v.vibrate(5000);

            // Call 911 end of the 5 second
            ct = new CountDownTimer(6000,1000){

                @Override
                public void onTick(long l) {
                    maint.setText("Calling 911 in " + (int)(l* .001f) + " second");
                }

                @Override
                public void onFinish() {
                    DialandMessage911();
                }
            };
            ct.start() ;
        }


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
        ct.cancel();
        finish();
    }

    public void DialandMessage911(){

        //Call 911
        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
        phoneIntent.setData(Uri.parse("tel:+38971763900"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(phoneIntent);

        //Sms 911
        //SmsManager smsManager = SmsManager.getDefault();
        //smsManager.sendTextMessage("911", null, "help me im in here \nx = " + x + "\ny =" + y, null, null);

    }

    public void GetLastLocation() {

        Location net_loc = null, gps_loc = null,pas_loc = null;
        gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        net_loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        pas_loc = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER) ;


        if (gps_loc != null) {
            x = gps_loc.getLatitude();
            y = gps_loc.getLongitude();
            makeUseOfNewLocation(gps_loc);
        }
        else if (net_loc != null) {
            x = net_loc.getLatitude();
            y = net_loc.getLongitude();
            makeUseOfNewLocation(net_loc);
        }
        else if(pas_loc != null){
            x = pas_loc.getLatitude();
            y = pas_loc.getLongitude();
            makeUseOfNewLocation(pas_loc);
        }
        else{
            vx.setText("Nothing Found");
            vy.setText("Nothing Found");
        }



    }

    private void makeUseOfNewLocation(Location t) {
        if (t != null) {
            vx.setText(String.valueOf(t.getLongitude()));
            vy.setText(String.valueOf(t.getLatitude()));
        } else {
            vx.setText("NULL Error");
            vy.setText("NULL Error");
        }
    }
}
