package org.guateora.oraconmigo;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

import org.guateora.oraconmigo.utils.Keys;

/**
 * Created by franzueto on 5/20/15.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Initialize Parse
        Parse.initialize(this, Keys.PARSE_APP_ID, Keys.PARSE_CLIENT_KEY);

        // Initialize Facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());
        ParseFacebookUtils.initialize(getBaseContext());
    }

}
