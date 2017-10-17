package info.bdslab.android.asuper.Services;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

/**
 * Created by nikola on 10/10/2017.
 */

public class OAuth2IntentServiceReceiver extends ResultReceiver {
    private final String TAG = "OAuth2IntentSrvRcvr";

    private Token mToken;


    /**
     * Create a new ResultReceive to receive results.  Your
     * {@link #onReceiveResult} method will be called from the thread running
     * <var>handler</var> if given, or from an arbitrary thread if null.
     *
     * @param handler
     */
    public OAuth2IntentServiceReceiver(Handler handler) {
        super(handler);
    }



    public void setmToken(Token mToken) {
        this.mToken = mToken;
    }

    public interface Token {
        public void onReceiveResult(int resultCode, Bundle resultData);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {

        Log.e(TAG, "Usao u onReceiveResult... " + mToken.toString());
        if (mToken != null) {
            mToken.onReceiveResult(resultCode, resultData);
        }
    }
}
