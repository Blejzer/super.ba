package info.bdslab.android.asuper;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import info.bdslab.android.asuper.Library.OAuth2Client;
//import info.bdslab.android.asuper.Library.Token;
import ca.mimic.oauth2library.OAuth2Client;
import ca.mimic.oauth2library.OAuthError;
import ca.mimic.oauth2library.OAuthResponse;
import info.bdslab.android.asuper.Library.Token;
import info.bdslab.android.asuper.POJO.Article;
import info.bdslab.android.asuper.Services.OAuth2IntentServiceReceiver;
import info.bdslab.android.asuper.Utils.Config;
import info.bdslab.android.asuper.Utils.EndlessRecyclerViewScrollListener;
import info.bdslab.android.asuper.Utils.Utils;

public class Main2Activity extends AppCompatActivity {

    private final String LOG_MAIN = "MainActivity log: ";

    Pattern pattern = Pattern.compile(IMAGE_PATTERN);
    Matcher matcher;
    private static final String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    SharedPreferences sharedPreferences;
    List<Article> arrayOfList = new ArrayList<>();


    EditText emailText;
//    ListView responseView;
    ProgressBar progressBar;

    // Process handler
    Handler mHandler = new Handler();
    Intent returnIntent = new Intent();
    String articlesList;
    private OAuth2IntentServiceReceiver mReceiver;

    // Store a member variable for the listener
    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Configure the RecyclerView
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        RecyclerView rvItems = (RecyclerView) findViewById(R.id.rvArticles);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvItems.setLayoutManager(linearLayoutManager);
        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
            }
        };
        // Adds the scroll listener to RecyclerView
        rvItems.addOnScrollListener(scrollListener);
    }

    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi(int offset) {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
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
                Toast.makeText(Main2Activity.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();
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

        private ProgressDialog progressDialog = new ProgressDialog(Main2Activity.this);

        String articlesList;

        @Override
        protected Void doInBackground(String... strings) {

            Config config = new Config();

            OAuth2Client client = new OAuth2Client.Builder(config.getUSERNAME(), config.getPASSWORD(), config.getCLIENT_ID(), config.getCLIENT_SECRET(), config.getSITE()+config.getPATHTOKEN()).build();
            OAuthResponse response = null;
            try {
                response = client.requestAccessToken();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (response.isSuccessful()) {
                Token token = new Token(response.getExpiresIn(), response.getTokenType(), response.getRefreshToken(), response.getAccessToken());
//                String accessToken = response.getAccessToken();
//                String refreshToken = response.getRefreshToken();
//                articlesList = token.getResource(oAuth2Client, token, config.getPATHARTICLES());
            } else {
                OAuthError error = response.getOAuthError();
                String errorMsg = error.getError();
            }




            return null;
        } // protected Void doInBackground(String... params)

        protected void onPreExecute() {

            progressDialog.setMessage("Downloading latest news...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    Main2Activity.MyAsyncTask.this.cancel(true);
                }
            });
        }

        protected void onPostExecute(Void v) {
            //parse JSON data
            try {

                Log.i(LOG_MAIN, "Articles: " + articlesList);

                JSONObject jsonArticle = null;
                JSONObject jsonSource = null;
                JSONObject jsonPubDate = null;
                JSONArray jsonArray = null;

                jsonArticle = new JSONObject(articlesList);
                jsonArray = jsonArticle.getJSONArray("articles");
                for (int j = 0; j < jsonArray.length(); j++) {
                    Article article = new Article();
                    jsonArticle = jsonArray.getJSONObject(j);
                    article.setTitle(jsonArticle.getString("title"));
                    article.setDescription(jsonArticle.getString("description"));

                    jsonPubDate = jsonArticle.getJSONObject("pubDate");
                    article.setPubDate(jsonPubDate.getString("sec"));

                    jsonSource = jsonArticle.getJSONObject("source");
                    article.setSource(jsonSource.getString("title"));
                    article.setLogo(jsonSource.getString("logo"));

                    article.setImage(jsonArticle.getString("image"));
                    matcher = pattern.matcher(article.getImage());
                    if(!matcher.matches()){
                        article.setImage(article.getImage().replaceAll(pattern.pattern(), ""));
                    }

                    arrayOfList.add(article);

                }

//                Log.e(LOG_MAIN, "Broj elemenata: "+arrayOfList.size());

//                setAdapterToListview();



                this.progressDialog.dismiss();
            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            } // catch (JSONException e)
        } // protected void onPostExecute(Void v)

    } //class MyAsyncTask extends AsyncTask<String, String, Void>





    public boolean testWiFi() {

        if (Utils.isNetworkAvailable(Main2Activity.this) && Utils.isOnline()) {
            return true;
        } else {
            return false;
        }
    }


    private void buildAlertMessageNoWiFi() {
        LayoutInflater li = LayoutInflater.from(Main2Activity.this);
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
