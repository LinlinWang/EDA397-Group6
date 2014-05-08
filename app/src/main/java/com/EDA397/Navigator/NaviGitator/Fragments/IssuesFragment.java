package com.EDA397.Navigator.NaviGitator.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.EDA397.Navigator.NaviGitator.SupportFunctions.GitFunctionality;
import com.EDA397.Navigator.NaviGitator.R;

import org.eclipse.egit.github.core.Issue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QuattroX on 2014-04-10.
 */
public class IssuesFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ListView listView;
    private GitFunctionality git;
    private List<Issue> repoIssues;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_issues, container, false);
        git = GitFunctionality.getInstance();
        repoIssues = git.getRepoIssues();
        ArrayList<String> issueMsg = new ArrayList<String>();

        for(Issue i : repoIssues) {
            issueMsg.add("Title: " + i.getTitle() + " Body: " + i.getBody());
        }
        listView = (ListView) view.findViewById(R.id.issues_list);
        listView.setClickable(true);
        listView.setOnItemClickListener(this);
        //Consider making a custom adapter for commits (if we want to extend the ListItems to hold multiple things).
        listView.setAdapter(new ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1, issueMsg.toArray()));

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
    // do nothing for now

    }
}
