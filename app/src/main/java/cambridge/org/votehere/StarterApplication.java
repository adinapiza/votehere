/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package cambridge.org.votehere;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;


public class StarterApplication extends Application {

    final String applicationID = "nm4ZFm86oIluGijq42043AvddEKKd3WCAQt3RqBh";
    final String clientKey = "iSb1nDAdEAlsrEbraChX2BpxLG2yvMGE0A5nxA9f";
    ParseInstallation installation = new ParseInstallation();

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(this, applicationID, clientKey);
        ParseObject.registerSubclass(Candidate.class);
        installation = ParseInstallation.getCurrentInstallation();

        if (saveInstallationID()) {
            ParseUser.enableAutomaticUser();
            ParseACL defaultACL = new ParseACL();

            // Optionally enable public read access.
            // defaultACL.setPublicReadAccess(true);

            ParseACL.setDefaultACL(defaultACL, true);
        }
    }

    private boolean saveInstallationID() {
        try {
            installation.save();
            System.out.println(installation.getInstallationId() + " Installation ID PARSE");
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
