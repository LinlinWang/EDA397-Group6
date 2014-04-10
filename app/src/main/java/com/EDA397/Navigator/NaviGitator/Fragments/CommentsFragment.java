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

import com.EDA397.Navigator.NaviGitator.Activities.GitFunctionality;
import com.EDA397.Navigator.NaviGitator.R;

import org.eclipse.egit.github.core.RepositoryCommit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QuattroX on 2014-04-10.
 */
public class CommentsFragment extends Fragment implements AdapterView.OnItemClickListener{

    private ListView listView;
    private GitFunctionality git;
    private ArrayList<String> info;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_comments, container, false);
        git = GitFunctionality.getInstance();
        info = new ArrayList<String>();
        if(git.getCurrentCommit() != null) {
            info.add("Message: " + "\n" + git.getCurrentCommit().getCommit().getMessage());

            for (String s : git.getFileNames()) {
                String[] temp = s.split("/");
                info.add("Modified File:\n" + temp[temp.length - 1]);
            }
            for (String s : git.getCommitComments()) {
                info.add("Comment by " + s);
            }

            listView = (ListView) view.findViewById(R.id.commitInfo_list);
            listView.setClickable(true);
            listView.setOnItemClickListener(this);
            listView.setAdapter(new ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1, info));
        }
        return view;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        //Redirection logic
    }
}
