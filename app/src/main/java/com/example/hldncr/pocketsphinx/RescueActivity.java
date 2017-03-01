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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescue);

        c = getApplicationContext() ;
        v = (Vibrator) this.c.getSystemService(Context.VIBRATOR_SERVICE) ;
        v.vibrate(10000);

        /*Intent phoneIntent = new Intent(Intent.ACTION_CALL);
        phoneIntent.setData(Uri.parse("tel:911"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(phoneIntent);
*/
    }

    public void GoBack(View view) {
        v.cancel();
        finish();
    }
}
