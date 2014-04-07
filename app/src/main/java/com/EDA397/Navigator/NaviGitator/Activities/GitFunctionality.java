package com.EDA397.Navigator.NaviGitator.Activities;


/**
 * Created by Mei on 2014-03-31.
 * https://developer.github.com/v3/oauth/
 */

import android.os.AsyncTask;
import android.util.Log;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.OAuthService;
import org.eclipse.egit.github.core.service.OrganizationService;
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

    protected GitHubClient getClient(){ return client; }
    protected String getUserName(){ return username; }

    public static GitFunctionality getInstance() {
        Log.d("GitFunctionality", "Instance Returned");
        return instance;
    }

    public Boolean gitLogin(String userName, String password) {
        try{
            Log.d("GitFunctionality", "Login");
            username = userName;
            Authenticate task = new Authenticate();
            task.execute(userName, password);
            return task.get();
        } catch ( Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Repository> getRepos() {
        try{
            Log.d("GitFunctionality", "Repos");
            getRepos task = new getRepos();
            task.execute();
            return task.get();
        } catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<RepositoryCommit> getRepoCommits(Repository repo) {
        try{
            Log.d("GitFunctionality", "RepoCommits");
            getRepoCommits task = new getRepoCommits();
            task.execute(repo);
            return task.get();
        } catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private class Authenticate extends AsyncTask<String, Void, Boolean> {
        Boolean authenticate = false;
        @Override
        protected Boolean doInBackground(String... str) {
            try {
                getClient().setCredentials(str[0], str[1]);
                OAuthService oAuth = new OAuthService(client);
                oAuth.getAuthorizations();
                Log.d("GitFunctionality", "User logged in");
                authenticate = true;
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("GitFunctionality", "Login failed");
                authenticate = false;
                return false;
            }
        }
    }

    private class getRepos extends AsyncTask<Void, Void, List<Repository>> {
        List<Repository> repos = new ArrayList<Repository>();
        @Override
        protected List<Repository> doInBackground(Void... arg0) {
            try {
                Log.d("GitFunctionality", "Repo thread");
                GitFunctionality git = GitFunctionality.getInstance();
                RepositoryService repoService = new RepositoryService(git.getClient());

                List<Repository> repos = repoService.getRepositories();
                for (Repository repo : repos) {
                    Log.d("GitFunctionality", repo.getName());
                }
                return repos;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private class getRepoCommits extends AsyncTask<Repository, Void, List<RepositoryCommit>> {
        List<Repository> commits = new ArrayList<Repository>();
        protected List<RepositoryCommit> doInBackground(Repository... repo) {
            try {
                Log.d("GitFunctionality", "Commit thread");
                GitFunctionality git = GitFunctionality.getInstance();
                CommitService commitService = new CommitService(git.getClient());

                List<RepositoryCommit> commits = commitService.getCommits(repo[0]);
                for (RepositoryCommit comm : commits) {
                    Log.d("GitFunctionality", comm.getCommitter().getName() + " : " + comm.getCommit().getMessage());
                }
                return commits;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            }
        }
    }