package com.EDA397.Navigator.NaviGitator.Activities;


/**
 * Created by Mei on 2014-03-31.
 * https://developer.github.com/v3/oauth/
 */

import android.os.AsyncTask;
import android.util.Log;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryIssue;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.OAuthService;
import org.eclipse.egit.github.core.service.OrganizationService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.IssueService;

import java.util.ArrayList;
import java.util.List;

/**
 *  Class containing functionality to communicate with GitHub
 */
public class GitFunctionality {

    private static GitFunctionality instance;

    private static GitHubClient client;
    private static String username;

    private GitFunctionality() {
            client = new GitHubClient();
            username = "";
    }

    /**
     * Function to return the GitHub client
     * @return The GitHub client
     */
    protected GitHubClient getClient(){ return client; }

    /**
     * Function to return the username
     * @return The current username
     */
    protected String getUserName(){ return username; }

    /**
     * Return the current instance of GitFunctionality
     * @return The current instance of GitFunctionality
     */
    public static GitFunctionality getInstance() {
        if (instance == null) {
            instance = new GitFunctionality();
            Log.d("GitFunctionality", "Instance Created");
        }
        Log.d("GitFunctionality", "Instance Returned");
        return instance;
    }

    /**
     * Function to login to GitHub
     * @param userName The username to be logged in
     * @param password The users password
     * @return the result of the login
     */
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

    /**
     * Function to get a list of all the repositories connected to the current user.
     * @return A list with the repositories connected to the current user.
     */
    public List<Repository> getRepos() {
        try{
            Log.d("GitFunctionality", "Repos");
            GetRepos task = new GetRepos();
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
            GetRepoCommits task = new GetRepoCommits();
            task.execute(repo);
            return task.get();
        } catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Issue> getRepoIssues(Repository repo) {
        try{
            Log.d("GitFunctionality", "RepoIssues");
            GetRepoIssues task = new GetRepoIssues();
            task.execute(repo);
            return task.get();
        } catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Async task to Authenticate a user against GitHub
     */
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

    /**
     * Async task to get all the repositories for the current user
     */
    private class GetRepos extends AsyncTask<Void, Void, List<Repository>> {
        @Override
        protected List<Repository> doInBackground(Void... arg0) {
            try {
                Log.d("GitFunctionality", "Repo thread");
                GitFunctionality git = GitFunctionality.getInstance();
                RepositoryService repoService = new RepositoryService(git.getClient());
                try {
                    OrganizationService org = new OrganizationService(git.getClient());
                    //repos user owns/is member of.
                    List<Repository> repos = repoService.getRepositories();
                    //repos owned by organizations this user is a member of.
                    List<User> organisations = org.getOrganizations();

                    for (User orgz : organisations) {
                        repos.addAll(repoService.getOrgRepositories(orgz.getLogin()));
                    }
                    for (Repository repo : repos) {
                        Log.d("GitFunctionality", repo.getName());
                    }
                    return repos;
                }
                catch (Exception e){
                    //repos user owns/is member of.
                    List<Repository> repos = repoService.getRepositories();

                    for (Repository repo : repos) {
                        Log.d("GitFunctionality", repo.getName());
                    }
                    return repos;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private class GetRepoCommits extends AsyncTask<Repository, Void, List<RepositoryCommit>> {
        protected List<RepositoryCommit> doInBackground(Repository... repo) {
            try {
                Log.d("GitFunctionality", "Commit thread");
                GitFunctionality git = GitFunctionality.getInstance();
                CommitService commitService = new CommitService(git.getClient());

                List<RepositoryCommit> commits = commitService.getCommits(repo[0]);
                for (RepositoryCommit comm : commits) {
                    Log.d("GitFunctionality", /*comm.getCommitter().getName() +**/ " : " + comm.getCommit().getMessage());
                }
                return commits;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            }
        }


    private class GetRepoIssues extends AsyncTask<Repository, Void, List<Issue>> {
        protected List<Issue> doInBackground(Repository... repo) {
            try {
                Log.d("GitFunctionality", "Issues thread");
                GitFunctionality git = GitFunctionality.getInstance();
                IssueService issueService = new IssueService(git.getClient());
                List<Issue> issues = issueService.getIssues(repo[0], null); //get all issues for the repository

                for (Issue i : issues) {
                    Log.d("GitFunctionality",  " : " + i.getTitle());  //get the titles of the issues for the current repository
                }

                return issues;


            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
    }