package com.EDA397.Navigator.NaviGitator.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ViewSwitcher;

import com.EDA397.Navigator.NaviGitator.R;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private ViewSwitcher switcher;
    private ArrayList<Repository> repos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        repos = new ArrayList<Repository>();
        switcher = (ViewSwitcher) findViewById(R.id.listSwitcher);
        GitFunctionality git = GitFunctionality.getInstance();

        // Instanciate GitFunctionality
        //GitFunctionality.initInstance();

        if (git.getUserName().equals("")){
            startActivity(new Intent("com.EDA397.Navigator.NaviGitator.Activities.LoginActivity"));
        }
        else{
            repos.addAll(git.getRepos());
            Log.d("MainActivity", "number of repos: " + repos.size());
            listView = (ListView) findViewById(R.id.repo_list);
            listView.setClickable(true);
            listView.setOnItemClickListener(this);
            listView.setAdapter(new RepoAdapter(getApplicationContext(),R.layout.repo_item,
                    R.id.repo_text, repos));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.repo_list:
                Log.d("onItemClick", "RepoListItem: " + position);
                GitFunctionality git = GitFunctionality.getInstance();
                List<RepositoryCommit> repoCommits = git.getRepoCommits(repos.get(position));
                ArrayList<String> commitMsg = new ArrayList<String>();

                for(RepositoryCommit repComm: repoCommits){
                    commitMsg.add("Author: " /*+ repComm.getCommitter().getLogin()**/ + " Message: " + repComm.getCommit().getMessage());
                }

                listView = (ListView) findViewById(R.id.repoComment_list);
                listView.setClickable(true);
                listView.setOnItemClickListener(this);
                //Consider making a custom adapter for commits (if we want to extend the ListItems to hold multiple things).
                listView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, commitMsg.toArray()));
                switcher.showNext();
                break;
            case R.id.repoComment_list:
                Log.d("onItemClick", "RepoCommentListItem: " + position);
                switcher.showPrevious();
                break;
            case R.id.repoIssues_list:
                Log.d("onItemClick", "RepoIssuesListItem: " + position);
                switcher.showPrevious();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
