package com.EDA397.Navigator.NaviGitator.Activities;


/**
 * Created by Mei on 2014-03-31.
 * https://developer.github.com/v3/oauth/
 */

import android.os.AsyncTask;
import android.util.Log;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.SearchRepository;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.io.IOException;
import java.util.List;


public class GitLogin extends AsyncTask<Void, Void, Boolean> {


    GitHubClient client;
    private String username;
    private String password;

    @Override
    protected Boolean doInBackground(Void... voids) {
        this.username = "";
        this.password = "";
        this.client = new GitHubClient();
        this.client.setCredentials(this.username,this.password);
        this.getRepo();
        return true;
    }

    public void getRepo() {
       RepositoryService service = new RepositoryService();
        try{
            List<Repository> l = service.getRepositories(this.username);
            for (Repository repo : l) {
                Log.v("nav", repo.getName());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onPostExecute (Boolean result){
        Log.v("nav", result.toString());
    }
}

