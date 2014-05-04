package com.EDA397.Navigator.NaviGitator.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.EDA397.Navigator.NaviGitator.Activities.GitFunctionality;
import com.EDA397.Navigator.NaviGitator.Activities.RepoAdapter;
import com.EDA397.Navigator.NaviGitator.R;

import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.event.PushPayload;

/**
 * Created by QuattroX on 2014-04-09.
 * Fragment for displaying the repos the logged-in user has access to,
 * along with the date they were last updated.
 */
public class MyProjectsFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView listView;
    private ArrayList<Repository> repos;
    private GitFunctionality git;
    private View view;
    private SharedPreferences watched_files;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_myprojects, container, false);

        git = GitFunctionality.getInstance();
        repos = new ArrayList<Repository>();
        repos.addAll(git.getRepos());
        Log.d("MyProjectsFragment", "number of repos: " + repos.size());
        listView = (ListView) view.findViewById(R.id.repo_list);
        listView.setClickable(true);
        listView.setOnItemClickListener(this);
        listView.setAdapter(new RepoAdapter(getActivity().getApplicationContext(), R.layout.repo_item,
                R.id.repo_text, repos));

        return view;
    }

    /**
     * Overrides method in parent class, used to set the repo that was clicked as the currently
     * selected one, followed by navigating to the tabs which show repo-specific info.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        Log.d("onItemClick", "RepoListItem: " + position);
        git.setCurrentRepo(repos.get(position));
        git.setCurrentBranch(null);
        watched_files = getActivity().getApplicationContext().getSharedPreferences("WatchedFiles",
                getActivity().getApplicationContext().MODE_PRIVATE);
        Set<String> watched = new HashSet<String>();
        watched.addAll(watched_files.getStringSet(git.getUserName() +
                git.getCurrentRepo().getName(), new HashSet<String>()));
        ArrayList <PushPayload> pushes = git.getRepoEvents();
 /**       for(PushPayload p : pushes){
            for (Commit c : p.getCommits()) {
                git.checkConflicts(watched, c);
            }
        }**/

        startActivity(new Intent("com.EDA397.Navigator.NaviGitator.Activities.RepositoryActivity"));
    }
}