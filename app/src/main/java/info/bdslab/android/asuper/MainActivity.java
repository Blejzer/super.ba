package info.bdslab.android.asuper;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import info.bdslab.android.asuper.Library.OAuth2Client;
import info.bdslab.android.asuper.Library.Token;
import info.bdslab.android.asuper.Utils.Config;
import info.bdslab.android.asuper.Utils.Utils;

public class MainActivity extends AppCompatActivity {
    private final String LOG_MAIN = "MainActivity log: ";

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    SharedPreferences sharedPreferences;

    EditText emailText;
    ListView responseView;
    ProgressBar progressBar;
    static final String API_KEY = "USE_YOUR_OWN_API_KEY";
    static final String API_URL = "https://api.fullcontact.com/v2/person.json?";
    // Process handler
    Handler mHandler = new Handler();
    Intent returnIntent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Log.i(LOG_MAIN, " sharedPrefs: "+ String.valueOf(sharedPreferences.contains("avaz")));

        setContentView(R.layout.activity_main);

        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        responseView = (ListView) findViewById(R.id.responseView);
        emailText = (EditText) findViewById(R.id.emailText);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


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
//                    Intent i = new Intent(LaunchActivity.this, MainActivity.class);
//                    // Zatvaramo sve prethodne aktivnosti
//                    finish();
//                    startActivity(i);
                }
            }, 2100);
        }else{
                //TODO create warning activity for testing internet connection
                buildAlertMessageNoWiFi();
                setResult(RESULT_CANCELED, returnIntent);
                finish();
            }



    }



    private void addDrawerItems() {
//        String[] osArray = new String[]{};
        List<String> where = new ArrayList<>();
        Map<String, ?> sharedPreferencesAll = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : sharedPreferencesAll.entrySet()) {
            where.add(entry.getKey());
        }

        String[] osArray = new String[ where.size() ];
        where.toArray( osArray );


        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("News selector");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }






    class MyAsyncTask extends AsyncTask<String, String, Void> {

        private ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

        String articlesList;

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

            articlesList = token.getResource(oAuth2Client, token, config.getPATHARTICLES());

//            Log.i(LOG_MAIN,"TEST articleList: " + articlesList);

//            android.preference.PreferenceManager.setDefaultValues(MainActivity.this, R.xml.sources, false);
//            sharedPreferences = android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(MainActivity.this);


//            Map<String, ?> sharedPreferencesAll = sharedPreferences.getAll();

//            SharedPreferences.Editor editor = sharedPreferences.edit();
////            Log.i(LOG_LAUNCH,"TEST prije brisanja: " + String.valueOf(sharedPreferences.contains("Avaz")));
////            for (Map.Entry<String, ?> entry : sharedPreferencesAll.entrySet()) {
////
////                Log.d(LOG_LAUNCH,"map values" + entry.getKey() + ": " + entry.getValue().toString());
////
////                editor.remove(entry.getKey());
////            }
//            editor.clear().apply();
//            Log.i(LOG_MAIN, "TEST nakon brisanja: " + String.valueOf(sharedPreferences.contains("Avaz")));


            return null;
        } // protected Void doInBackground(String... params)

        protected void onPreExecute() {

            progressBar.setVisibility(View.VISIBLE);
//            progressDialog.setMessage("Downloading your data...");
//            progressDialog.show();
//            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                public void onCancel(DialogInterface arg0) {
//                    MyAsyncTask.this.cancel(true);
//                }
//            });
        }

        protected void onPostExecute(Void v) {
            progressBar.setVisibility(View.INVISIBLE);
            //parse JSON data
            try {

                Log.i(LOG_MAIN, "Articles: " + articlesList);
                JSONObject jsonObject = null;
                JSONArray jsonArray = null;

                jsonObject = new JSONObject(articlesList);
                jsonArray = jsonObject.getJSONArray("articles");
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject jobj = jsonArray.getJSONObject(j);
                    Log.i(LOG_MAIN, "Article: " + jobj);

                }

//
//                Log.i(LOG_MAIN, "TEST nakon try: " + String.valueOf(sharedPreferences.contains("Avaz")));



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

        if (Utils.isNetworkAvailable(MainActivity.this) && Utils.isOnline()) {
            return true;
        } else {
            return false;
        }
    }


    private void buildAlertMessageNoWiFi() {
        LayoutInflater li = LayoutInflater.from(MainActivity.this);
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
