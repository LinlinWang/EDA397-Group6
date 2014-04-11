package com.EDA397.Navigator.NaviGitator.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.EDA397.Navigator.NaviGitator.Activities.GitFunctionality;
import com.EDA397.Navigator.NaviGitator.R;

import org.eclipse.egit.github.core.RepositoryBranch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QuattroX on 2014-04-10.
 */
public class BranchesFragment extends Fragment implements AdapterView.OnItemClickListener{
    private View view;
    private GitFunctionality git;
    private List<RepositoryBranch> b;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_branches, container, false);
        git = GitFunctionality.getInstance();
        b = git.getBranches();
        ArrayList<String> branchMsg = new ArrayList<String>();
        for(RepositoryBranch repBr : b){
            branchMsg.add("Branch Name:" + repBr.getName());
        }

        //Add the branch names into the listview
        ListView listView = (ListView) view.findViewById(R.id.branch_list);
        listView.setClickable(true);
        listView.setOnItemClickListener(this);
        listView.setAdapter(new ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1,branchMsg));
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
