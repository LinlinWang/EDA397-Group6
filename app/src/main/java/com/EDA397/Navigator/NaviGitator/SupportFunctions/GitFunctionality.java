package com.EDA397.Navigator.NaviGitator.SupportFunctions;

import android.os.AsyncTask;
import android.util.Log;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.CommitComment;
import org.eclipse.egit.github.core.CommitFile;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryBranch;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryContents;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.PageIterator;
import org.eclipse.egit.github.core.event.Event;
import org.eclipse.egit.github.core.event.EventPayload;
import org.eclipse.egit.github.core.event.PushPayload;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.ContentsService;
import org.eclipse.egit.github.core.service.EventService;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.OAuthService;
import org.eclipse.egit.github.core.service.OrganizationService;
import org.eclipse.egit.github.core.service.RepositoryService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 *  Class containing functionality to communicate with GitHub
 */
public class GitFunctionality {

    private static GitFunctionality instance;

    private static GitHubClient client;
    private static String username;
    private static Repository currentRepo;
    private static RepositoryCommit currentCommit;
    private static Issue currentIssue;
    private static RepositoryBranch currentBranch;

    

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
    public String getUserName(){ return username; }

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
    public Repository getCurrentRepo(){ return currentRepo; }
    public void setCurrentRepo(Repository r){ currentRepo = r; }
    public RepositoryCommit getCurrentCommit(){ return currentCommit; }
    public void setCurrentCommit(RepositoryCommit r){ currentCommit = r; }
    public Issue getCurrentIssues(){ return currentIssue; }
    public void setCurrentIssue(Issue r){ currentIssue = r; }
    public RepositoryBranch getCurrentBranch(){ return currentBranch; }
    public void setCurrentBranch(RepositoryBranch r){ currentBranch = r; }

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
            task.executeOnExecutor(task.THREAD_POOL_EXECUTOR,userName, password);
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
            task.executeOnExecutor(task.THREAD_POOL_EXECUTOR);
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
    public List<RepositoryBranch> getBranches() {
        try{
            Log.d("GitFunctionality", "Branches");
            GetRepoBranches task = new GetRepoBranches();
            task.executeOnExecutor(task.THREAD_POOL_EXECUTOR, currentRepo);
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
            GetRepoCommits task = new GetRepoCommits();
            task.executeOnExecutor(task.THREAD_POOL_EXECUTOR, currentRepo);
            return task.get();
        } catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Get the contents of the specified directory (from the current repository).
     * @param dir The directory to retrieve.
     * @return A list of RepositoryContents representing the folders and files in the directory.
     */
    public ArrayList<RepositoryContents> getDirContents(String dir) {
        try{
            Log.d("GitFunctionality", "All FileNames");
            GetDirContents task = new GetDirContents();
            task.executeOnExecutor(task.THREAD_POOL_EXECUTOR, dir);
            return task.get();
        } catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get all filenames for the currently chosen commit
     * @return A list of filenames
     */
    public ArrayList<String> getCommitFileNames() {
        try{
            Log.d("GitFunctionality", "Commit FileNames");
            GetCommitFileNames task = new GetCommitFileNames();
            task.executeOnExecutor(task.THREAD_POOL_EXECUTOR, currentCommit);
            return task.get();
        } catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Get all comments for the currently chosen commit
     * @return A list of comments
     */
    public ArrayList<String> getCommitComments() {
        try{
            Log.d("GitFunctionality", "CommitComments");
            GetCommitComments task = new GetCommitComments();
            task.executeOnExecutor(task.THREAD_POOL_EXECUTOR, currentCommit);
            return task.get();
        } catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Returns the comments for the currently chosen issue
     * @return
     */
    public List<Comment> getIssueComments(Issue is){
        try{
            Log.d("GitFunctionality", "IssueComments");
            GetIssueComments task = new GetIssueComments();
            task.executeOnExecutor(task.THREAD_POOL_EXECUTOR,is);
            return task.get();
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Add a comment for a commit
     * @param s The comment to add
     */
    public void addCommitComment(String s) {
     try{
            Log.d("GitFunctionality", "CommitComments");
            AddCommitComment task = new AddCommitComment();
            task.executeOnExecutor(task.THREAD_POOL_EXECUTOR, s);
            task.get();
        } catch ( Exception e) {
            e.printStackTrace();
        }
    }

    public Void addIssueComment(String s){
        try{
            Log.d("GitFunctionality", "IssueComments");
            AddIssueComment task = new AddIssueComment();
            task.executeOnExecutor(task.THREAD_POOL_EXECUTOR,s);
            return task.get();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns up to the 30 latest events targeting the logged-in user
     * @return A list of events
     */
    public ArrayList<String> getUserEvents() {
        try{
            Log.d("GitFunctionality", "UserEvents");
            GetUserEvents task = new GetUserEvents();
            task.executeOnExecutor(task.THREAD_POOL_EXECUTOR);
            return task.get();
        } catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Returns up to the 30 latest pushevents on the current repo
     * @return A list of events
     */
    public ArrayList<PushPayload> getRepoEvents() {
        try{
            Log.d("GitFunctionality", "RepoEvents");
            GetRepoEvents task = new GetRepoEvents();
            task.executeOnExecutor(task.THREAD_POOL_EXECUTOR);
            return task.get();
        } catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Event> getRepoEvents2() {
        try{
            Log.d("GitFunctionality", "RepoEvents");
            GetRepoEvents2 task = new GetRepoEvents2();
            task.executeOnExecutor(task.THREAD_POOL_EXECUTOR);
            return task.get();
        } catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Method for checking for filename conflicts between a commit and the user's watched files.
     * @return
     */
    public ArrayList<String> checkConflicts(Set<String> w, Commit c) {
        try{
            Log.d("GitFunctionality", "Conflict Check");
            CheckConflicts task = new CheckConflicts(w);
            task.executeOnExecutor(task.THREAD_POOL_EXECUTOR, c);
            return task.get();
        } catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Method for getting the issues of a repository
     * @return
 */
    public List<Issue> getRepoIssues() {
        try{
            Log.d("GitFunctionality", "RepoIssues");
            GetRepoIssues task = new GetRepoIssues();
            task.executeOnExecutor(task.THREAD_POOL_EXECUTOR, currentRepo);
            return task.get();
        } catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method which makes it possible for a user using the app to add an issue
     * @return
     */
    public Void addIssues(){
        return null;
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
    private class GetRepos extends AsyncTask<Void, Void, List<Repository>> {
        @Override
        protected List<Repository> doInBackground(Void... arg0) {
            try {
                Log.d("GitFunctionality", "Repo thread");
                GitFunctionality git = GitFunctionality.getInstance();
                RepositoryService repoService = new RepositoryService(git.getClient());
                /***try {
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
                catch (Exception e){**/
                    //repos user owns/is member of.
                    List<Repository> repos = repoService.getRepositories();

                    for (Repository repo : repos) {
                        Log.d("GitFunctionality", repo.getName());
                    }
                    return repos;
                //}
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
    private class GetRepoCommits extends AsyncTask<Repository, Void, List<RepositoryCommit>> {
        @Override
        protected List<RepositoryCommit> doInBackground(Repository... repo) {
            try {
                Log.d("GitFunctionality", "Commit thread");
                GitFunctionality git = GitFunctionality.getInstance();
                CommitService commitService = new CommitService(git.getClient());
                List<RepositoryCommit> commits;
                if(currentBranch == null) {
                    commits = (List<RepositoryCommit>) commitService.pageCommits(repo[0],20).next();
                }
                else{
                    commits = (List<RepositoryCommit>) commitService.pageCommits(repo[0], currentBranch.getCommit().getSha(), null,20).next();
                }
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
                RepositoryService repService = new RepositoryService(git.getClient());

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
    }
    /**
     * Async task to get the contents of a specific repo directory (sorted by listing directories
     * first).
     */
    private class GetDirContents extends AsyncTask<String, Void, ArrayList<RepositoryContents>> {
        @Override
        protected ArrayList<RepositoryContents> doInBackground(String... dir) {
            try {
                Log.d("GitFunctionality", "FileName thread");
                GitFunctionality git = GitFunctionality.getInstance();
                ContentsService conService = new ContentsService(git.getClient());
                ArrayList<RepositoryContents> unsorted = new ArrayList<RepositoryContents>();
                ArrayList<RepositoryContents> sorted = new ArrayList<RepositoryContents>();
                if(currentBranch == null) {
                    unsorted.addAll(conService.getContents(currentRepo, dir[0]));
                }
                else{
                    unsorted.addAll(conService.getContents(currentRepo, dir[0], currentBranch.getName()));
                }
                for(int i = 0; i < unsorted.size(); i++){
                    Log.d("GitFunctionality", unsorted.get(i).getPath());
                    if(unsorted.get(i).getType().equals("dir")){
                        sorted.add(unsorted.get(i));
                        unsorted.remove(i);
                        i--;
                    }
                }
                sorted.addAll(unsorted);
                return sorted;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * Async task to get the names of all files changed in a selected commit
     */
    private class GetCommitFileNames extends AsyncTask<RepositoryCommit, Void, ArrayList<String>> {
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
    /**
     * Async task to get all the comments on a selected commit
     */
    private class GetCommitComments extends AsyncTask<RepositoryCommit, Void, ArrayList<String>> {
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

    private class AddCommitComment extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... s) {
            try {
                Log.d("GitFunctionality", "add CommitComments thread");
                GitFunctionality git = GitFunctionality.getInstance();
                CommitService commitService = new CommitService(git.getClient());
                User u = new User();
                u.setLogin(username);
                CommitComment comment = new CommitComment();
                comment.setBody(s[0]);
                comment.setUser(u);
                commitService.addComment(currentRepo, currentCommit.getSha(), comment);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class AddIssueComment extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... s) {
            try{
                Log.d("GitFunctionality", "add IssueComments thread");
                GitFunctionality git = GitFunctionality.getInstance();
                IssueService issueService = new IssueService(git.getClient());
                String issueId = String.valueOf(currentIssue.getId());
                User user = new User();
                user.setLogin(username);
                Comment comment = new Comment();
                comment.setBody(s[0]);
                String c = comment.getBody();
                //comment.setUser(user);
                issueService.createComment(username,currentRepo.getName(),issueId,c);

            }catch(Exception e){
                e.printStackTrace();
            }
            return  null;
        }
    }

    /**
     * Async task to retrieve events received by the logged-in user (currently only retrieving
     * the latest events, using the default maximum of 30).
     */
    private class GetUserEvents extends AsyncTask<Void, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(Void... v) {
            try {
                Log.d("GitFunctionality", "Events thread");
                GitFunctionality git = GitFunctionality.getInstance();
                EventService evService = new EventService(git.getClient());
                PageIterator<Event> events = evService.pageUserReceivedEvents(username,false,20);
                ArrayList<String> news = new ArrayList<String>();
                for (Event e : events.next()) {
                    String s = e.getType() + " in " + e.getRepo().getName() + "\nBy: " +
                               e.getActor().getLogin() + "\n" + e.getCreatedAt().toString();
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

    /**
     * Async task to get all the issues for a repository
     * Class which gets the 30 latest events covering all the current repo's branches
     * (filtering out any non-push events).
     */
    private class GetRepoEvents extends AsyncTask<Void, Void, ArrayList<PushPayload>> {
        @Override
        protected ArrayList<PushPayload> doInBackground(Void... v) {
            try {
                Log.d("GitFunctionality", "Events thread");
                GitFunctionality git = GitFunctionality.getInstance();
                EventService evService = new EventService(git.getClient());
                PageIterator<Event> events = evService.pageEvents(currentRepo);
                ArrayList <PushPayload> pushes = new ArrayList<PushPayload>();
                for(Event e : events.next()){
                    if(e.getType().equals("PushEvent")) {
                        PushPayload p = (PushPayload) e.getPayload();
                        String[] branch = p.getRef().split("/");
                        Log.d("GitFunctionality", e.getActor().getLogin() + " pushed to " + branch[branch.length-1]);
                        pushes.add(p);
                    }
                }
                return pushes;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private class GetRepoEvents2 extends AsyncTask<Void, Void, ArrayList<Event>> {
        @Override
        protected ArrayList<Event> doInBackground(Void... v) {
            try {
                Log.d("GitFunctionality", "Events thread");
                GitFunctionality git = GitFunctionality.getInstance();
                EventService evService = new EventService(git.getClient());
                PageIterator<Event> evIter = evService.pageEvents(currentRepo);
                ArrayList <Event> events = new ArrayList<Event>();
                events.addAll(evIter.next());

                return events;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * Class which checks if the files changed in the commit are amongst the ones
     * the user is watching for changes
     */
    private class CheckConflicts extends AsyncTask<Commit, Void, ArrayList<String>> {
        private Set<String> watched;

        public CheckConflicts(Set<String> w) {
            this.watched = w;
        }
        @Override
        protected ArrayList<String> doInBackground(Commit... comm) {
            try {
                Log.d("GitFunctionality", "Conflicts thread");
                    RepositoryCommit temp = new RepositoryCommit();
                    temp.setSha(comm[0].getSha());
                    GetCommitFileNames task = new GetCommitFileNames();
                    ArrayList<String> fileNames = new ArrayList<String>();
                    task.executeOnExecutor(THREAD_POOL_EXECUTOR, temp);
                    for (String f : task.get()) {
                        if (watched.contains(f)) {
                            Log.d("Possible Conflict", f);
                            fileNames.add(f);
                        }
                    }
                return fileNames;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * Class which gets all issues for the current repository.
     */
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
    /**
     * Class which returns the comments on the selected issue
     */
    private class GetIssueComments extends AsyncTask<Issue,Void,List<Comment>> {
        @Override
        protected List<Comment> doInBackground(Issue... issue) {
            try {
                Log.e("GitFunctionality", "IssueComments thread");
                GitFunctionality git = GitFunctionality.getInstance();
                IssueService issueService = new IssueService(git.getClient());
                List<Comment> commentList = new ArrayList<Comment>() ;

                   String issueNumber = String.valueOf(issue[0].getNumber());
                   commentList.addAll(issueService.getComments(currentRepo, issueNumber));
                   Log.e("GitFunctionality","issueNumber"+issueNumber);

                for (Comment i: commentList){
                    Log.e("GitFunctionality", " get issue comments: " + i.getBody()) ;
                }
                return commentList;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }
    }
}