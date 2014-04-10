package com.EDA397.Navigator.NaviGitator.Fragments;

import android.content.Intent;
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
import java.util.List;

import com.EDA397.Navigator.NaviGitator.Activities.GitFunctionality;
import com.EDA397.Navigator.NaviGitator.Activities.RepoAdapter;
import com.EDA397.Navigator.NaviGitator.R;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;

/**
 * Created by QuattroX on 2014-04-09.
 */
public class MyProjectsFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView listView;
    private ArrayList<Repository> repos;
    private GitFunctionality git;
    private View view;
//    private ViewSwitcher switcher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_myprojects, container, false);

        git = GitFunctionality.getInstance();

        repos = new ArrayList<Repository>();
        repos.addAll(git.getRepos());
        Log.d("MyProjectsFragment", "number of repos: " + repos.size());
//        switcher = (ViewSwitcher) view.findViewById(R.id.listSwitcher);
        listView = (ListView) view.findViewById(R.id.repo_list);
        listView.setClickable(true);
        listView.setOnItemClickListener(this);
        listView.setAdapter(new RepoAdapter(getActivity().getApplicationContext(), R.layout.repo_item,
                R.id.repo_text, repos));

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        switch (parent.getId()) {
            case R.id.repo_list:
                Log.d("onItemClick", "RepoListItem: " + position);
                startActivity(new Intent("com.EDA397.Navigator.NaviGitator.Activities.RepositoryActivity"));
//                GitFunctionality git = GitFunctionality.getInstance();
//                List<RepositoryCommit> repoCommits = git.getRepoCommits(repos.get(position));
//                ArrayList<String> commitMsg = new ArrayList<String>();
//                String temp = "";
//                for(RepositoryCommit repComm: repoCommits){
//                    /*
//                    for (CommitFile f : repComm.getFiles()){
//                        temp += "\n" + f.getFilename();
//                    }**/
//                    commitMsg.add("Date: " + repComm.getCommit().getAuthor().getDate().toString() +
//                            "\nAuthor: " + repComm.getCommit().getAuthor().getName() +
//                            "\nMessage: " + "\n" + repComm.getCommit().getMessage() +
//                            "\nFiles: " + temp);
//                }
//
//                listView = (ListView) view.findViewById(R.id.repoComment_list);
//                listView.setClickable(true);
//                listView.setOnItemClickListener(this);
//                //Consider making a custom adapter for commits (if we want to extend the ListItems to hold multiple things).
//                listView.setAdapter(new ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1, commitMsg.toArray()));
//                switcher.showNext();
                break;
//            case R.id.repoComment_list:
//                Log.d("onItemClick", "RepoCommentListItem: " + position);
//                switcher.showPrevious();
//                break;
        }
    }
}