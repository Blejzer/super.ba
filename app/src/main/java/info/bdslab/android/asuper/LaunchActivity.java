package info.bdslab.android.asuper;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

public class LaunchActivity extends Activity {

    //prefs
    SharedPreferences prefs;

    // Process handler
    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);


        mHandler.postDelayed(new Runnable() {
            public void run() {
                Intent i = new Intent(LaunchActivity.this, MainPrefActivity.class);
                // Zatvaramo sve prethodne aktivnosti
                finish();
                startActivity(i);
            }
        }, 2100);
    }
}
