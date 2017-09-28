package info.bdslab.android.asuper;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class LaunchActivity extends Activity {

    //prefs
    SharedPreferences prefs;

    // Process handler
    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_launch);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        Log.i("tracking", String.valueOf(prefs.contains("switch1")));
        Log.i("tracking", String.valueOf(prefs.contains("switch_test")));
        Log.i("tracking", String.valueOf(prefs.contains("switch3")));


        mHandler.postDelayed(new Runnable() {
            public void run() {
                Intent i = new Intent(LaunchActivity.this, MainActivity.class);
                // Zatvaramo sve prethodne aktivnosti
                finish();
                startActivity(i);
            }
        }, 2100);
    }
}
