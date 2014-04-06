package com.EDA397.Navigator.NaviGitator.Activities;


/**
 * Created by Mei on 2014-03-31.
 * https://developer.github.com/v3/oauth/
 */

import android.os.AsyncTask;
import android.util.Log;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.util.ArrayList;
import java.util.List;


public class GitFunctionality {

    private static GitFunctionality instance;

    private static GitHubClient client;
    private String username;

    public GitFunctionality() {
        super();
    }

    public static void initInstance() {
        if (instance == null) {
            instance = new GitFunctionality();
            client = new GitHubClient();
            Log.d("GitFunctionality", "Instance Created");
        }
    }

    protected GitHubClient getClient(){
        return client;
    }

    public static GitFunctionality getInstance() {
        Log.d("GitFunctionality", "Instance Returned");
        return instance;
    }

    public Boolean gitLogin(String userName, String password) {
        try {
            this.client.setCredentials(userName, password);
            this.username = userName;

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("GitFunctionality", "Login failed");
            return false;
        }
        Log.d("GitFunctionality", "User logged in");
        return true;
    }

    public ArrayList<String> getRepos() {
        getRepos task = new getRepos();
        task.execute();
        return task.getRepos();
    }
    private class getRepos extends AsyncTask<Void, Void, ArrayList<String>> {
        ArrayList<String> repoList = new ArrayList<String>();
        @Override
        protected ArrayList<String> doInBackground(Void... arg0) {
            try {
                GitFunctionality git = GitFunctionality.getInstance();
                RepositoryService repoService = new RepositoryService(git.getClient());
                List<Repository> repos = repoService.getRepositories(username);
                for (Repository repo : repos) {
                    repoList.add(repo.getName());
                    Log.d("GitFunctionality", repo.getName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return repoList;
        }
        public ArrayList<String> getRepos(){ return repoList; }
    }
}