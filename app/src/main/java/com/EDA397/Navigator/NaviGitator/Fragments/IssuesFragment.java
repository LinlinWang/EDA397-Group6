package com.EDA397.Navigator.NaviGitator.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.EDA397.Navigator.NaviGitator.SupportFunctions.GitFunctionality;
import com.EDA397.Navigator.NaviGitator.Adapters.ExpandableListAdapter;
import com.EDA397.Navigator.NaviGitator.R;

import org.eclipse.egit.github.core.Comment;
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

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
               // Toast.makeText(getActivity(),listDataHeader.get(groupPosition)+":"+listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition),Toast.LENGTH_SHORT).show();
                AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
                //set title
                ad.setTitle(listDataHeader.get(groupPosition));
                //set dialog message
                ad.setMessage(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition));
                //close the dialog box and do nothing
                ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        //Toast.makeText(getActivity(),"Cancel clicked",Toast.LENGTH_SHORT);
                        Log.d("IssuesFragment", "Cancel Button clicked");
                        dialog.cancel();
                    }
                });
                //Edit the current comment of the issue
                ad.setNeutralButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("IssuesFragment", "Edit Button clicked");
                        //do nothing now
                    }
                });

                AlertDialog alertDialog = ad.create();
                alertDialog.show();
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
