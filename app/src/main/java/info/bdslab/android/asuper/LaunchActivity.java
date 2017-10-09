package info.bdslab.android.asuper;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import info.bdslab.android.asuper.Library.OAuth2Client;
import info.bdslab.android.asuper.Library.Token;
import info.bdslab.android.asuper.Utils.Config;
import info.bdslab.android.asuper.Utils.Utils;

public class LaunchActivity extends Activity {

    private final String LOG_LAUNCH = "LaunchActivity log";

    //prefs
    SharedPreferences prefs;

    // Process handler
    Handler mHandler = new Handler();
    Intent returnIntent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_launch);

        PreferenceManager.setDefaultValues(this, R.xml.sources, false);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if(testWiFi()){

            mHandler.postDelayed(new Runnable() {
                public void run() {
                    new MyAsyncTask().execute();
                }
            }, 2100);

//            ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.activity_main);
//            ListView listView = (ListView) findViewById(R.id.responseView);
//            listView.setAdapter(adapter);

            mHandler.postDelayed(new Runnable() {
                public void run() {
                    Intent i = new Intent(LaunchActivity.this, MainActivity.class);
                    // Zatvaramo sve prethodne aktivnosti
                    finish();
                    startActivity(i);
                }
            }, 2100);

        } else{
            //TODO create warning activity for testing internet connection
            buildAlertMessageNoWiFi();
            setResult(RESULT_CANCELED, returnIntent);
            finish();
        }


    }


    class MyAsyncTask extends AsyncTask<String, String, Void> {

        private ProgressDialog progressDialog = new ProgressDialog(LaunchActivity.this);
        SharedPreferences sharedPreferences;
        String sourcesList;

        @Override
        protected Void doInBackground(String... strings) {

            Config config = new Config();

            // grant_type=password&
//            String client_id = "5952144f7e664a87a18c158b_2fpcotn1f8n4so8oo8s4gwg8ogsgk8g48oksc044s0o4k0kow0";
//            String client_secret = "34vrb64rxx8g8kc8s4ck8s4wocc4kcgkws4cookcocog0k8gcw";
//            String username = "iOSApp@super.ba";
//            String password= "thereisnopass";
//
//            String site = "https://super.ba/";
//            String pathToken = "oauth/v2/token?";
//            String pathApiVersion = "api";
//            String pathArticles = "api/v1/articles";
//            String pathSources = "api/v1/sources";

            OAuth2Client oAuth2Client = new OAuth2Client(config.getUSERNAME(), config.getPASSWORD(), config.getCLIENT_ID(), config.getCLIENT_SECRET(), config.getSITE()+config.getPATHTOKEN());

            Token token = oAuth2Client.getAccessToken();

            oAuth2Client.setSite(config.getSITE());

            sourcesList = token.getResource(oAuth2Client, token, config.getPATHSOURCES());

            PreferenceManager.setDefaultValues(LaunchActivity.this, R.xml.sources, false);
            sharedPreferences = android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(LaunchActivity.this);


//            Map<String, ?> sharedPreferencesAll = sharedPreferences.getAll();

            SharedPreferences.Editor editor = sharedPreferences.edit();
//            Log.i(LOG_LAUNCH,"TEST prije brisanja: " + String.valueOf(sharedPreferences.contains("Avaz")));
//            for (Map.Entry<String, ?> entry : sharedPreferencesAll.entrySet()) {
//
//                Log.d(LOG_LAUNCH,"map values" + entry.getKey() + ": " + entry.getValue().toString());
//
//                editor.remove(entry.getKey());
//            }
            editor.clear().apply();
            Log.i(LOG_LAUNCH,"TEST nakon brisanja: " + String.valueOf(sharedPreferences.contains("Avaz")));


            return null;
        } // protected Void doInBackground(String... params)

        protected void onPreExecute() {
//            progressDialog.setMessage("Downloading your data...");
//            progressDialog.show();
//            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                public void onCancel(DialogInterface arg0) {
//                    MyAsyncTask.this.cancel(true);
//                }
//            });
        }

        protected void onPostExecute(Void v) {
            //parse JSON data
            try {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Log.i(LOG_LAUNCH,"Sources: " + sourcesList);
                JSONObject jsonObject = null;
                JSONArray jsonArray = null;

                jsonObject = new JSONObject(sourcesList);
                jsonArray = jsonObject.getJSONArray("sources");
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject jobj = jsonArray.getJSONObject(j);

                    editor.putString(jobj.getString("title"), jobj.getString("logo"));
                    Log.i(LOG_LAUNCH,"Title: " + jobj.getString("title"));
                    Log.i(LOG_LAUNCH,"Logo: " + jobj.getString("logo"));
                }
                editor.apply();

                Log.i(LOG_LAUNCH,"TEST nakon try: " + String.valueOf(sharedPreferences.contains("Avaz")));



//                JSONArray jArray = new JSONArray(result);
//
//                for(int i=0; i < jArray.length(); i++) {
//
//                    JSONObject jObject = jArray.getJSONObject(i);
//
//                    String name = jObject.getString("name");
//                    String tab1_text = jObject.getString("tab1_text");
//                    int active = jObject.getInt("active");
//
//                } // End Loop
//
//                JSONArray arr = new JSONArray(result);
//
//                List<String> list = new ArrayList<String>();
//
//                for(int i = 0; i < arr.length(); i++){
//
//                    String info = arr.getJSONObject(i).getString("name");
//                    list.add(info);
//                }

//                this.progressDialog.dismiss();
            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            } // catch (JSONException e)
        } // protected void onPostExecute(Void v)

    } //class MyAsyncTask extends AsyncTask<String, String, Void>


    public boolean testWiFi() {

        if (Utils.isNetworkAvailable(LaunchActivity.this) && Utils.isOnline()) {
            return true;
        } else {
            return false;
        }
    }


    private void buildAlertMessageNoWiFi() {
        LayoutInflater li = LayoutInflater.from(LaunchActivity.this);
        View promptsView = li.inflate(R.layout.konekcija, null);
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setView(promptsView);
        alertDialog.setCancelable(false);
        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "U redu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.show();
    }
}
