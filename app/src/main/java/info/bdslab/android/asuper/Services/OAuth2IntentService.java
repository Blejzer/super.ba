package info.bdslab.android.asuper.Services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import info.bdslab.android.asuper.Library.OAuth2Client;
import info.bdslab.android.asuper.Library.Token;
import info.bdslab.android.asuper.Utils.Config;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class OAuth2IntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = "OAuth2IntentService log";

    private static final String ACTION_NEW = "new";
    private static final String ACTION_REFRESH = "refresh";
    Config config = new Config();
    Token token;
    OAuth2Client oAuth2Client;

    // TODO: Rename parameters
//    private static final String EXTRA_PARAM1 = "info.bdslab.android.asuper.extra.PARAM1";
//    private static final String EXTRA_PARAM2 = "info.bdslab.android.asuper.extra.PARAM2";

    public OAuth2IntentService() {
        super(OAuth2IntentService.class.getName());
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionNewToken(Context context) {
        Intent intent = new Intent(context, OAuth2IntentService.class);
        intent.setAction(ACTION_NEW);
//        intent.putExtra(EXTRA_PARAM1, param1);
//        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionRefreshToken(Context context, Token token) {
        Intent intent = new Intent(context, OAuth2IntentService.class);
        intent.setAction(ACTION_REFRESH);
        intent.putExtra("TOKEN", token);
//        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.e(TAG, "Service Started!");


        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_NEW.equals(action)) {
                handleActionNew();
            } else if (ACTION_REFRESH.equals(action)) {
                final Token oldToken = (Token) intent.getSerializableExtra("TOKEN");
                handleActionRefresh(oldToken);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionNew() {
        // TODO: Handle action New
        oAuth2Client = new OAuth2Client(config.getUSERNAME(), config.getPASSWORD(), config.getCLIENT_ID(), config.getCLIENT_SECRET(), config.getSITE()+config.getPATHTOKEN());

        token = oAuth2Client.getAccessToken();

        oAuth2Client.setSite(config.getSITE());
//        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionRefresh(Token oldToken) {
        // TODO: Handle action Refresh
        if(oldToken.isExpired()){
            token = oldToken.refresh(oAuth2Client);
        }
//        throw new UnsupportedOperationException("Not yet implemented");
    }
}
