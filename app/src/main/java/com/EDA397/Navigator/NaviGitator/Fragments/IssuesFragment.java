package com.EDA397.Navigator.NaviGitator.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.EDA397.Navigator.NaviGitator.Activities.GitFunctionality;
import com.EDA397.Navigator.NaviGitator.Adapters.ExpandableListAdapter;
import com.EDA397.Navigator.NaviGitator.R;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.Issue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by QuattroX on 2014-04-10.
 */
public class IssuesFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ListView listView;
    private GitFunctionality git;
    private List<Issue> repoIssues;
    private List<Comment> issueComment;
    private View view;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_issues, container, false);
        // get the listview
        expListView =(ExpandableListView) view.findViewById(R.id.lvExp);
        // preparing list data
        prepareListData();
        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return false;
            }
        });
        return view;
    }
    private void prepareListData() {
        //list of issue(header)
        git = GitFunctionality.getInstance();
        repoIssues = git.getRepoIssues();
        listDataChild = new HashMap<String, List<String>>();
        listDataHeader = new ArrayList<String>();

        for(Issue is : repoIssues) {
            // git.setCurrentIssue(i);
            listDataHeader.add(is.getTitle());

            //list comments(child) of the issue
            git = GitFunctionality.getInstance();
            issueComment = new ArrayList<Comment>();
            issueComment.addAll(git.getIssueComments(is));
            List<String> comment = new ArrayList<String>();
            comment.add(is.getBody());
            for (Comment c : issueComment) {
                comment.add(c.getBody());
            }
            //Header, child data
            listDataChild.put(is.getTitle(), comment);
        }

    }

    @Override
    /**
     * Navigates to comments tab after a issue has been selected.
     */
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

    }
}
