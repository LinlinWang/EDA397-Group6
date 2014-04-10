package com.EDA397.Navigator.NaviGitator.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.EDA397.Navigator.NaviGitator.R;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryBranch;

import java.util.ArrayList;

/**
 * Created by Meiso on 2014-04-10.
 */
public class BranchFragment extends Fragment {
    private View view;
    private GitFunctionality git;
    private ArrayList<RepositoryBranch> bList;
    private ArrayList<Repository> repoList;

    public BranchFragment(GitFunctionality git) {
        this.git = git;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bList = new ArrayList<RepositoryBranch>();
        repoList = new ArrayList<Repository>();
        repoList.addAll(git.getRepos());
        bList.addAll(git.getBranches(repoList.get(0)));
        Log.d("BranchFragment", "number of branches:" + bList.size());

        view = inflater.inflate(R.layout.fragment_branch, container, false);

        TextView tv = (TextView) view.findViewById(R.id.branch_text);
        tv.setText("branches:" + bList.get(0));

        return view;
    }

}

