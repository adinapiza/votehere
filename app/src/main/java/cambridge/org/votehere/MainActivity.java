/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package cambridge.org.votehere;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;

import java.util.List;


public class MainActivity extends ActionBarActivity {

    WebView graphView;
    int[] votesArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        graphView = (WebView) findViewById(R.id.graphWebView);
        // WebSettings webSettings = graphView.getSettings();
        graphView.getSettings().setJavaScriptEnabled(true);
        
        doLogin();

        graphView.loadUrl("file:///android_asset/www/index.htm");
        List<Candidate> candidates = Candidate.fetchAll();
        ListView candidatesListView = (ListView) findViewById(R.id.candidateListView);
        String[] candidateNames = new String[candidates.size()];
        votesArray = new int[candidates.size()];
        for (int i = 0; i < candidates.size(); i++) {
            Candidate candidate = candidates.get(i);
            candidateNames[i] = candidate.getString("cadidateName");
            votesArray[i] = candidate.getInt("votes");
        }

        ArrayAdapter<String> candidatesAdapter = new ArrayAdapter<String>(this,
                R.layout.candidate_item_cell, R.id.candidateName, candidateNames);
        candidatesListView.setAdapter(candidatesAdapter);

        final MyJavaScriptInterface myJavaScriptInterface
                = new MyJavaScriptInterface(this);

        //graphView.loadUrl("javascript:buildChart()");


//    Parse.initialize(this, "nm4ZFm86oIluGijq42043AvddEKKd3WCAQt3RqBh", "iSb1nDAdEAlsrEbraChX2BpxLG2yvMGE0A5n");


    }

    private void doLogin() {
        // Determine whether the current user is an anonymous user
        if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            // If user is anonymous, send the user to LoginSignupActivity.class
            Intent intent = new Intent(MainActivity.this,
                    LoginSignUpActivity.class);
            startActivity(intent);
            finish();
        } else {
            // If current user is NOT anonymous user
            // Get current user data from Parse.com
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
                // Send logged in users to Welcome.class
                Intent intent = new Intent(MainActivity.this, Welcome.class);
                startActivity(intent);
                finish();
            } else {
                // Send user to LoginSignupActivity.class
                Intent intent = new Intent(MainActivity.this,
                        LoginSignUpActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }


    private String voteParam() {
        String param = "[";
        for (int i = 0; i < votesArray.length; i++) {
            param = param + votesArray[i] + ",";
        }
        param = param + ",";
        param = param.replace(",,", "]");

        System.out.print(param + " Parameter for building");
        return param;
    }

    public void vote(View v) {

        LinearLayout linear = (LinearLayout) v;
        TextView candidateTextiew = (TextView) linear.getChildAt(0);
        ListView candidatesListView = (ListView) findViewById(R.id.candidateListView);
        candidatesListView.setVisibility(View.GONE);
        Candidate.update(candidateTextiew.getText().toString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            System.out.println("Building Chart kit now");
            graphView.evaluateJavascript("buildPerformanceChart(" + voteParam() + ")", null);
            System.out.println("Building Chart kat done");
        } else {
            System.out.println("Building Chart kit not now");
            graphView.loadUrl("javascript:buildPerformanceChart(" + voteParam() + ")");
            System.out.println("Building Chart kat not done");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class MyJavaScriptInterface {
        Context mContext;

        MyJavaScriptInterface(Context c) {
            mContext = c;
        }

        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }

        public void openAndroidDialog() {

        }

    }
}
