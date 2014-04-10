package com.EDA397.Navigator.NaviGitator.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.EDA397.Navigator.NaviGitator.R;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryBranch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Meiso on 2014-04-10.
 */
public class BranchFragment extends Fragment {
    private View view;
    private GitFunctionality git;
    private ArrayList<Repository> repoList;

    public BranchFragment(GitFunctionality git) {
        this.git = git;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        repoList = new ArrayList<Repository>();
        repoList.addAll(git.getRepos());

        List<RepositoryBranch> b = git.getBranches(repoList.get(0));
        ArrayList<String> branchMsg = new ArrayList<String>();
        for(RepositoryBranch repBr : b){
            branchMsg.add("Branch Name:" + repBr.getName());
        }

        view = inflater.inflate(R.layout.fragment_branch, container, false);

        //Add the branch names into the listview
        ListView listView = (ListView) view.findViewById(R.id.listbranch);
        listView.setAdapter(new ArrayAdapter(view.getContext(), android.R.layout.simple_expandable_list_item_1,branchMsg.toArray()));
        return view;
    }

}

