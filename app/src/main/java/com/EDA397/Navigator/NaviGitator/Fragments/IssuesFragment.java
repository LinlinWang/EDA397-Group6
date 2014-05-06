package com.EDA397.Navigator.NaviGitator.Fragments;

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

import org.eclipse.egit.github.core.Issue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by QuattroX on 2014-04-10.
 */
public class IssuesFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ListView listView;
    private GitFunctionality git;
    private List<Issue> repoIssues;
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


       /* git = GitFunctionality.getInstance();
        repoIssues = git.getRepoIssues();
        ArrayList<String> issueMsg = new ArrayList<String>();
        for(Issue i : repoIssues) {
           // issueMsg.add("Title: " + i.getTitle() + " Body: " + i.getBody());
            issueMsg.add(i.getTitle());
        }
        listView = (ListView) view.findViewById(R.id.issues_list);
        listView.setClickable(true);
        listView.setOnItemClickListener(this);
        //Consider making a custom adapter for commits (if we want to extend the ListItems to hold multiple things).
        listView.setAdapter(new ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1, issueMsg.toArray()));
*/
        return view;
    }
    private void prepareListData() {


        listDataChild = new HashMap<String, List<String>>();

        //list of issue
        git = GitFunctionality.getInstance();
        repoIssues = git.getRepoIssues();
        listDataHeader = new ArrayList<String>();
        for(Issue i : repoIssues) {
            listDataHeader.add(i.getTitle());
        }
        //list comments of the issue
        ArrayList<String> issueComment = new ArrayList<String>();
         for(Issue i : repoIssues){
          issueComment.add(i.getBody());
        }
        //for(Issue i:repoIssues) {
          //  listDataChild.put(listDataHeader.get(i.getNumber()), issueComment);
        //}

    }

    @Override
    /**
     * Navigates to comments tab after a issue has been selected.
     */
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
       // git = GitFunctionality.getInstance();
       // git.setCurrentIssue(repoIssues.get(position));
       // RepositoryActivity r = (RepositoryActivity) getActivity();
       // r.viewPager.setCurrentItem(3);

    }
}
