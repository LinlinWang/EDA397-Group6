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
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.DefaultClientConnection;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.ByteArrayBuffer;
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
    private String url = "https://www.pivotaltracker.com/services/v3/tokens/active";
    private String token = "";

    private PivotalFunctionality() {

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

    public Boolean pivotalLogin(String userName, String password) {
        try{
            Log.d("GitFunctionality", "Login");
            LoginPivotal task = new LoginPivotal();
            task.execute(userName, password);
            return task.get();
        } catch ( Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Async task to Authenticate a user against Pivotal tracker
     */
    private class LoginPivotal extends AsyncTask<String, Void, Boolean> {


        @Override
        protected Boolean doInBackground(String... str) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost post = new HttpPost(url);
                List<NameValuePair> np = new ArrayList<NameValuePair>();
                np.add(new BasicNameValuePair("username", str[0]));
                np.add(new BasicNameValuePair("password", str[1]));

                post.setEntity(new UrlEncodedFormEntity(np));

                //Response
                HttpResponse resp = httpclient.execute(post);
                Log.d("PivotalFunctionality", "RESPONSE CODE" + resp.getStatusLine().getStatusCode());
                String responseString = new BasicResponseHandler().handleResponse(resp);
                int start = responseString.indexOf("<guid>")+6;
                int end = responseString.indexOf("</guid>");
                token = responseString.substring(start, end);
                if(resp.getStatusLine().getStatusCode() == 200) {
                    Log.d("PivotalFunctionality", "Token: " + token);
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("PivotalFunctionality", "Login failed");
                return false;
            }
        }
    }

    private class getProjects extends AsyncTask<Void, Void, Boolean> {


        @Override
        protected Boolean doInBackground(Void... arg0) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost post = new HttpPost(url);
                List<NameValuePair> np = new ArrayList<NameValuePair>();

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
