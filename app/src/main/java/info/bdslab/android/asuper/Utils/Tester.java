package info.bdslab.android.asuper.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import info.bdslab.android.asuper.R;

import static android.app.Activity.RESULT_OK;

/**
 * Created by nikola on 28/09/2017.
 */

public class Tester extends Activity {

    // Preferences
    SharedPreferences prefs;
    public static final String KEY_ID = "id";
    String kod = "";
    boolean test = false;
    //Process handler
    Handler mHandler = new Handler();
    Intent returnIntent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // then you use
        kod = prefs.getString(KEY_ID, "");

        //  UKOLIKO KOD POSTOJI, PROVJERI DA LI JE ISPRAVAN
        if(!kod.isEmpty() || !kod.equals("")){

            if(testWiFi()){
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        new MyTask().execute(kod);
                    }
                }, 2100);

            }else{
                buildAlertMessageNoWiFi();
                setResult(RESULT_CANCELED, returnIntent);
                finish();
            }

        }else{
            setResult(RESULT_CANCELED, returnIntent);
            finish();
        }


    }


    //Provjera jedinstvenog koda i vracanje rezultata provjere
    class MyTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.app_name, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // Return success result to the parent intent
            // Optional - you can add value to be passed
            // using putExtra option
            returnIntent.putExtra("result",true);
            setResult(RESULT_OK, returnIntent);
            finish();
        }
    }
    public boolean testWiFi(){

        if (Utils.isNetworkAvailable(Tester.this) && Utils.isOnline()) {
            return true;
        }else{
            return false;
        }
    }
    private void buildAlertMessageNoWiFi() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.konekcija, null);
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setView(promptsView);
        alertDialog.setCancelable(false);
        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.confirmation), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // here you can add functions
            }
        });
        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.show();
    }


}