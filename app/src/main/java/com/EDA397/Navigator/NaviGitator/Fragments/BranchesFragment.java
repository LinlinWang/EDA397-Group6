package com.EDA397.Navigator.NaviGitator.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.EDA397.Navigator.NaviGitator.Activities.RepositoryActivity;
import com.EDA397.Navigator.NaviGitator.SupportFunctions.GitFunctionality;
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
    private List<RepositoryBranch> branches;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_branches, container, false);
        git = GitFunctionality.getInstance();
        TextView tv = (TextView) view.findViewById(R.id.branch_label);
        if(git.getCurrentBranch() != null){
            String [] temp = git.getCurrentBranch().getName().split("/");
            tv.setText("Currrent Branch: " +  temp[temp.length-1]);
        }
        else{
            tv.setText("Currrent Branch: master");
        }
        branches = git.getBranches();
        ArrayList<String> branchMsg = new ArrayList<String>();
        for(RepositoryBranch repBr : branches){
            String [] temp = repBr.getName().split("/");
            branchMsg.add(temp[temp.length-1]);
        }

        //Add the branch names into the listview
        ListView listView = (ListView) view.findViewById(R.id.branch_list);
        listView.setClickable(true);
        listView.setOnItemClickListener(this);
        listView.setAdapter(new ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1,branchMsg));
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        git = GitFunctionality.getInstance();
        git.setCurrentBranch(branches.get(position));
        TextView tv = (TextView) this.view.findViewById(R.id.branch_label);
        String [] temp = branches.get(position).getName().split("/");
        tv.setText("Currrent Branch: " +  temp[temp.length-1]);
        Fragment frg = null;
        frg = getActivity().getSupportFragmentManager().getFragments().get(0);
        final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
        RepositoryActivity r = (RepositoryActivity) getActivity();
        r.viewPager.setCurrentItem(0);
    }
}
