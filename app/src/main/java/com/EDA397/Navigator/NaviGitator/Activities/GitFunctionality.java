package com.EDA397.Navigator.NaviGitator.Activities;

import android.os.AsyncTask;
import android.util.Log;

import org.eclipse.egit.github.core.CommitComment;
import org.eclipse.egit.github.core.CommitFile;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryBranch;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryCommitCompare;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.PageIterator;
import org.eclipse.egit.github.core.event.Event;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.EventService;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.OAuthService;
import org.eclipse.egit.github.core.service.OrganizationService;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.util.ArrayList;
import java.util.List;

/**
 *  Class containing functionality to communicate with GitHub
 */
public class GitFunctionality {

    private static GitFunctionality instance;

    private static GitHubClient client;
    private static String username;
    private static Repository currentRepo;
    private static RepositoryCommit currentCommit;

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
    //Used in current solution, cannot be protected since fragments are not
    //in same folder anymore.
    protected Repository getCurrentRepo(){ return currentRepo; }
    public void setCurrentRepo(Repository r){ currentRepo = r; }
    public RepositoryCommit getCurrentCommit(){ return currentCommit; }
    public void setCurrentCommit(RepositoryCommit r){ currentCommit = r; }

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
            getRepos task = new getRepos();
            task.execute();
            return task.get();
        } catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Function to get a list of all the branches connected to the current user.
     * @return A list with the branches connected to the current user.
     */
    public List<RepositoryBranch> getBranches(Repository repo) {
        try{
            Log.d("GitFunctionality", "Branches");
            GetRepoBranches task = new GetRepoBranches();
            task.execute(repo);
            return task.get();
        } catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Get all commits for a repository
     * @return A list of all commits for the selected repository
     */
    public List<RepositoryCommit> getRepoCommits() {
        try{
            Log.d("GitFunctionality", "RepoCommits");
            getRepoCommits task = new getRepoCommits();
            task.execute(currentRepo);
            return task.get();
        } catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public ArrayList<String> getFileNames() {
        try{
            Log.d("GitFunctionality", "FileNames");
            getFileNames task = new getFileNames();
            task.execute(currentCommit);
            return task.get();
        } catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public ArrayList<String> getCommitComments() {
        try{
            Log.d("GitFunctionality", "CommitComments");
            getCommitComments task = new getCommitComments();
            task.execute(currentCommit);
            return task.get();
        } catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public ArrayList<String> getUserEvents() {
        try{
            Log.d("GitFunctionality", "UserEvents");
            getUserEvents task = new getUserEvents();
            task.execute();
            return task.get();
        } catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public ArrayList<String> getRepoEvents() {
        try{
            Log.d("GitFunctionality", "RepoEvents");
            getRepoEvents task = new getRepoEvents();
            task.execute();
            return task.get();
        } catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public List<Issue> getRepoIssues() {
        try{
            Log.d("GitFunctionality", "RepoIssues");
            GetRepoIssues task = new GetRepoIssues();
            task.execute(currentRepo);
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
        @Override
        protected Boolean doInBackground(String... str) {
            try {
                getClient().setCredentials(str[0], str[1]);
                OAuthService oAuth = new OAuthService(client);
                oAuth.getAuthorizations();
                Log.d("GitFunctionality", "User logged in");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("GitFunctionality", "Login failed");
                return false;
            }
        }
    }

    /**
     * Async task to get all the repositories for the current user
     */
    private class getRepos extends AsyncTask<Void, Void, List<Repository>> {
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

    /**
     * Async task to get all the commits for a selected repository
     */
    private class getRepoCommits extends AsyncTask<Repository, Void, List<RepositoryCommit>> {
        @Override
        protected List<RepositoryCommit> doInBackground(Repository... repo) {
            try {
                Log.d("GitFunctionality", "Commit thread");
                GitFunctionality git = GitFunctionality.getInstance();
                CommitService commitService = new CommitService(git.getClient());

                List<RepositoryCommit> commits = commitService.getCommits(repo[0]);
                for (RepositoryCommit comm : commits) {
                    Log.d("GitFunctionality", comm.getCommit().getAuthor().getName() + " : " + comm.getCommit().getMessage());
                }
                return commits;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            }
    }


    /**
     * Async task to get all the branches that belongs to the repository
     */
    //https://github.com/eclipse/egit-github/blob/master/org.eclipse.egit.github.core/src/org/eclipse/egit/github/core/service/RepositoryService.java
    private class GetRepoBranches extends AsyncTask<Repository, Void, List<RepositoryBranch>> {
        @Override
        protected List<RepositoryBranch> doInBackground(Repository... repo) {
            try {
                Log.d("GitFunctionality", "Branch thread");
                GitFunctionality git = GitFunctionality.getInstance();
                RepositoryService repService = new RepositoryService();

                List<RepositoryBranch> bList = repService.getBranches(repo[0]);
                for (RepositoryBranch b : bList) {
                    Log.d("GitFunctionality", b.getName());
                }
                return bList;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        private class getFileNames extends AsyncTask<RepositoryCommit, Void, ArrayList<String>> {
            @Override
            protected ArrayList<String> doInBackground(RepositoryCommit... r) {
                try {
                    Log.d("GitFunctionality", "FileName thread");
                    GitFunctionality git = GitFunctionality.getInstance();
                    CommitService commitService = new CommitService(git.getClient());

                    ArrayList<String> names = new ArrayList<String>();
                    for (CommitFile cf : commitService.getCommit(currentRepo, r[0].getSha()).getFiles()) {
                        names.add(cf.getFilename());
                        Log.d("GitFunctionality", cf.getFilename());
                    }
                    return names;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

        private class getCommitComments extends AsyncTask<RepositoryCommit, Void, ArrayList<String>> {
            @Override
            protected ArrayList<String> doInBackground(RepositoryCommit... r) {
                try {
                    Log.d("GitFunctionality", "CommitComments thread");
                    GitFunctionality git = GitFunctionality.getInstance();
                    CommitService commitService = new CommitService(git.getClient());

                    ArrayList<String> comments = new ArrayList<String>();
                    for (CommitComment cc : commitService.getComments(currentRepo, r[0].getSha())) {
                        comments.add(cc.getUser().getLogin() + ":\n" + cc.getBody());
                        Log.d("GitFunctionality", cc.getUser().getLogin());
                    }
                    return comments;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

        private class getUserEvents extends AsyncTask<Void, Void, ArrayList<String>> {
            @Override
            protected ArrayList<String> doInBackground(Void... v) {
                try {
                    Log.d("GitFunctionality", "Events thread");
                    GitFunctionality git = GitFunctionality.getInstance();
                    EventService evService = new EventService(git.getClient());
                    PageIterator<Event> events = evService.pageUserReceivedEvents(username);
                    ArrayList<String> news = new ArrayList<String>();
                    for (Event e : events.next()) {
                        String s = e.getActor().getLogin() + " " + e.getType() + " " + e.getRepo().getName();
                        Log.d("GitFunctionality", s);
                        news.add(s);
                    }
                    return news;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

        private class getRepoEvents extends AsyncTask<Void, Void, ArrayList<String>> {
            @Override
            protected ArrayList<String> doInBackground(Void... v) {
                try {
                    Log.d("GitFunctionality", "Events thread");
                    GitFunctionality git = GitFunctionality.getInstance();
                    EventService evService = new EventService(git.getClient());
                    PageIterator<Event> events = evService.pageEvents(currentRepo);
                    ArrayList<String> news = new ArrayList<String>();
                    for (Event e : events.next()) {
                        String s = e.getActor().getLogin() + " " + e.getType();
                        Log.d("GitFunctionality", s);
                        news.add(s);
                    }
                    return news;

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
                        Log.d("GitFunctionality", " : " + i.getTitle());  //get the titles of the issues for the current repository
                    }

                    return issues;


                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }