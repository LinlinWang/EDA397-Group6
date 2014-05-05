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
    private String urlProject = "http://www.pivotaltracker.com/services/v3/projects";
    private String urlStories = "http://www.pivotaltracker.com/services/v3/projects/1043912/stories";
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

    /**
     * Executing the asynctask for pivotal login
     * @return boolean if user is logged into pivotaltracker
     */
    public Boolean pivotalLogin(String userName, String password) {
        try{
            Log.d("PivotalFunctionality", "Login");
            LoginPivotal task = new LoginPivotal();
            task.execute(userName, password);
            return task.get();
        } catch ( Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Executing the asynctask for get pivotaltracker projects
     * @return boolean if getting the pivotaltracker projects are successful
     */
    public Boolean getPivotalProjects() {
        try{
            Log.d("PivotalFunctionality", "getprojects");
            getProjects task = new getProjects();
            task.execute();
            return task.get();
        } catch ( Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Executing the asynctask for get pivotaltracker stories
     * @return boolean if getting the pivotaltracker stories are successful
     */
    public Boolean getPivotalStories() {
        try{
            Log.d("PivotalFunctionality", "getprojects");
            getStories task = new getStories();
            task.execute();
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

    /**
     * Async task to get the projects from pivotaltracker
     */
    private class getProjects extends AsyncTask<Void, Void, Boolean> {


        @Override
        protected Boolean doInBackground(Void... arg0) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet get = new HttpGet(urlProject);
                get.setHeader("X-TrackerToken", token);

                //Response
                HttpResponse resp = httpclient.execute(get);

                String responseString = new BasicResponseHandler().handleResponse(resp);

                Log.d("PivotalFunctionality", "RESPONSE FROM GET PROJECTS" + responseString);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("PivotalFunctionality", "Login failed");
                return false;
            }
        }
    }

    /**
     * Async task to get the stories from pivotaltracker
     */
    private class getStories extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... arg0) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet get = new HttpGet(urlStories);
                get.setHeader("X-TrackerToken", token);

                //Response
                HttpResponse resp = httpclient.execute(get);
                String responseString = new BasicResponseHandler().handleResponse(resp);

                Log.d("PivotalFunctionality", "RESPONSE GETSTORIES " + responseString);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("PivotalFunctionality", "failed get stories");
                return false;
            }
        }
    }

}
