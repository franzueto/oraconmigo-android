package org.guateora.oraconmigo;

import android.app.Application;

import com.parse.Parse;

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
    }

}
