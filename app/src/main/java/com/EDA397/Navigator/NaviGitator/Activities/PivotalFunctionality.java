package com.EDA397.Navigator.NaviGitator.Activities;

import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sajfer on 2014-05-01.
 */
public class PivotalFunctionality {

    private static PivotalFunctionality instance;


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
     * Function to login to pivotalTracker
     * @param userName The username to be logged in
     * @param password The users password
     * @return the result of the login
     */
    public boolean pivotalLogin(String userName, String password) {
        try {
            URL url = new URL("https://www.pivotaltracker.com/services/v5/projects/");
            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getProjects() {
        return "";
    }

    public String getStories() {
        return "";
    }
}
