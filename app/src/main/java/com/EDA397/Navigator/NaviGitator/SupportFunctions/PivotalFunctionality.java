package com.EDA397.Navigator.NaviGitator.SupportFunctions;

import android.os.AsyncTask;
import android.util.Log;
import com.EDA397.Navigator.NaviGitator.Datatypes.PivotalProject;
import com.EDA397.Navigator.NaviGitator.Datatypes.PivotalStory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sajfer on 2014-05-01.
 */
public class PivotalFunctionality {

    private static PivotalFunctionality instance;
    private String url = "https://www.pivotaltracker.com/services/v5";
    private String token = "";

    private PivotalFunctionality() {}

    /**
     * Return the current instance of PivotalFunctionality
     *
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
     * Return the token
     * @return the current token
     */
    public String returnToken(){
        return token;
    }
    /**
     * Executing the asynctask for pivotal login
     * @param userName
     * @param password
     * @return boolean if user is logged into pivotaltracker
     */
    public Boolean pivotalLogin(String userName, String password) {
        try {
            Log.d("PivotalFunctionality", "Login");
            LoginPivotal task = new LoginPivotal();
            task.executeOnExecutor(task.THREAD_POOL_EXECUTOR,userName, password);
            return task.get();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Executing the asynctask for get pivotaltracker projects
     * @return boolean if getting the pivotaltracker projects are successful
     */
    public List<PivotalProject> getPivotalProjects() {
        try{
            Log.d("PivotalFunctionality", "getprojects");
            getProjects task = new getProjects();
            task.executeOnExecutor(task.THREAD_POOL_EXECUTOR);
            return task.get();
        } catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Executing the asynctask for get pivotaltracker stories
     * @return boolean if getting the pivotaltracker stories are successful
     */
    public List<PivotalStory> getPivotalStories(Integer project) {
        try{
            Log.d("PivotalFunctionality", "getstories");
            getStories task = new getStories();
            task.executeOnExecutor(task.THREAD_POOL_EXECUTOR,project);
            return task.get();
        } catch ( Exception e) {
            e.printStackTrace();
            return null;
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
                HttpGet get = new HttpGet(url + "/me");

                ((DefaultHttpClient) httpclient).getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY),
                        new UsernamePasswordCredentials(str[0], str[1]));

                //Response
                HttpResponse resp = httpclient.execute(get);

                String responseString = new BasicResponseHandler().handleResponse(resp);
                JSONObject jsonObject = new JSONObject(responseString);
                token = jsonObject.getString("api_token");
                if (resp.getStatusLine().getStatusCode() == 200) {
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
    private class getProjects extends AsyncTask<Void, Void, List<PivotalProject>> {


        @Override
        protected List<PivotalProject> doInBackground(Void... arg0) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet get = new HttpGet(url + "/projects");
                get.setHeader("X-TrackerToken", token);

                //Response
                HttpResponse resp = httpclient.execute(get);

                String responseString = new BasicResponseHandler().handleResponse(resp);
                JSONArray jsonArray = new JSONArray(responseString);

                List<PivotalProject> projects = new ArrayList<PivotalProject>();
                for(int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject json = jsonArray.getJSONObject(i);
                    projects.add(new PivotalProject(json.getString("name"), json.getInt("id")));
                }
                return projects;
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("PivotalFunctionality", "failed get projects");
                return null;
            }
        }
    }

//    /**
//     * Async task to get the stories from pivotaltracker
//     */
    private class getStories extends AsyncTask<Integer, Void, List<PivotalStory>> {

        @Override
        protected List<PivotalStory> doInBackground(Integer... arg0) {
            InputStream is = null;
            JSONObject jObj = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet get = new HttpGet(url + "/projects/"+arg0[0]+"/stories");
                get.setHeader("X-TrackerToken", token);

                //Response
                HttpResponse resp = httpclient.execute(get);
                String responseString = new BasicResponseHandler().handleResponse(resp);
                JSONArray jsonArray = new JSONArray(responseString);
                List<PivotalStory> stories = new ArrayList<PivotalStory>();
                for(int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject json = jsonArray.getJSONObject(i);
                    String name = "";
                    Integer id = 0;
                    String description = "";
                    Integer owner = 0;
                    PivotalStory.StoryStatus status = PivotalStory.StoryStatus.noStatus;

                    if (json.has("name"))
                        name = json.getString("name");
                    if (json.has("id"))
                        id = json.getInt("id");
                    if (json.has("description"))
                        description = json.getString("description");
                    if (json.has("owned_by_id"))
                        owner = json.getInt("owned_by_id");
                    if (json.has("current_state"))
                        status = statusToEnum(json.getString("current_state"));

                    stories.add(new PivotalStory(name, id, description, owner, status));
                }
                return stories;
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("PivotalFunctionality", "failed get stories");
                return null;
            }
        }
    }
    private PivotalStory.StoryStatus statusToEnum(String s){
        if(s.equals("unscheduled"))
            return PivotalStory.StoryStatus.unscheduled;
        if(s.equals("unstarted"))
            return PivotalStory.StoryStatus.unstarted;
        if(s.equals("started"))
            return PivotalStory.StoryStatus.started;
        if(s.equals("accepted"))
            return PivotalStory.StoryStatus.accepted;
        if(s.equals("finished"))
            return PivotalStory.StoryStatus.finished;
        if(s.equals("delivered"))
            return PivotalStory.StoryStatus.delivered;
        if(s.equals("rejected"))
            return PivotalStory.StoryStatus.rejected;
        return PivotalStory.StoryStatus.noStatus;
    }
}
