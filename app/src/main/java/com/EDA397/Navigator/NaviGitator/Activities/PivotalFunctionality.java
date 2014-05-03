package com.EDA397.Navigator.NaviGitator.Activities;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.DefaultClientConnection;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.eclipse.egit.github.core.service.OAuthService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sajfer on 2014-05-01.
 */
public class PivotalFunctionality {

    private static PivotalFunctionality instance;


    private PivotalFunctionality() {
        String url = "https://www.pivotaltracker.com/services/v3/tokens/active";
        new LoginPivotal().execute(url);
    }

    /**
     * Return the current instance of PivotalFunctionality
     * @return The current instance of PivotalFunctionality
     */
    public static PivotalFunctionality getInstance() {
        if (instance == null) {
            instance = new PivotalFunctionality();
            Log.d("PivotalFunctionality", "Instance Created");
        }
        Log.d("PivotalFunctionality", "Instance Returned");
        return instance;
    }

    /**
     * Function to login to pivotalTracker
     * @param userName The username to be logged in
     * @param password The users password
     * @return the result of the login

    public String pivotalLogin() {
        String url = "https://www.pivotaltracker.com/services/v3/tokens/active";

            Log.d("PivotalFunctionality", "Login pivotal");

            return url;


    }
*/
    /**
     * Async task to Authenticate a user against Pivotal tracker
     */
    private class LoginPivotal extends AsyncTask<String, Void, Boolean> {


        @Override
        protected Boolean doInBackground(String... str) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost post = new HttpPost(str[0]);
                List<NameValuePair> np = new ArrayList<NameValuePair>();
                np.add(new BasicNameValuePair("username", "YOURUSERNAME"));
                np.add(new BasicNameValuePair("password", "PASSWORD"));

                post.setEntity(new UrlEncodedFormEntity(np));

                //Response
                HttpResponse resp = httpclient.execute(post);
                Log.d("PivotalFunctionality", "RESPONSE CODE" + resp.getStatusLine().getStatusCode());
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("PivotalFunctionality", "Login failed");
                return false;
            }
        }
    }


}
