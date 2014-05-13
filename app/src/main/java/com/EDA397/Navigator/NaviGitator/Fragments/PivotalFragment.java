package com.EDA397.Navigator.NaviGitator.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.EDA397.Navigator.NaviGitator.Adapters.ExpandableListAdapterPivo;
import com.EDA397.Navigator.NaviGitator.Datatypes.PivotalProject;
import com.EDA397.Navigator.NaviGitator.Datatypes.PivotalStory;
import com.EDA397.Navigator.NaviGitator.R;
import com.EDA397.Navigator.NaviGitator.SupportFunctions.PivotalFunctionality;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * Fragment for displaying list of pivotaltracker stories and project
 */

public class PivotalFragment extends Fragment implements AdapterView.OnItemClickListener{

    private View view;
    private List<String> projNames;
    private PivotalFunctionality pivotal;
    private HashMap<String,List<String>> storyNames;
    private ExpandableListAdapterPivo listAdapter;
    private ExpandableListView lv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_pivotal, container, false);
        pivoLogin();

        return view;
    }

    /**
     * Check if password is correct to pivotaltracker
     * If the login is correct, show an expandablelistview of the project and stories
     */
    private void pivoLogin(){
        Button logout = (Button) view.findViewById(R.id.pivoLogin);
        logout.setFocusableInTouchMode(false);
        logout.setFocusable(false);
        logout.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String name = ((EditText)view.findViewById(R.id.pivotalUsername)).getText().toString().trim().toLowerCase();
                        final String pw = ((EditText)view.findViewById(R.id.pivotalPassword)).getText().toString().trim();
                        lv = (ExpandableListView)view.findViewById(R.id.expandableListView);
                        pivotal = PivotalFunctionality.getInstance();

                        if(name.length() < 1 || pw.length() < 1){
                            //Error handling too short/blank field.
                            Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                                    "Field(s) too short/unfilled", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP| Gravity.LEFT, 0, 0);
                            toast.show();
                        }
                        if (pivotal.pivotalLogin(name, pw)) {
                            getPivoData();
                            listAdapter = new ExpandableListAdapterPivo(getActivity(), projNames, storyNames);
                            // setting list adapter
                            lv.setAdapter(listAdapter);
                            lv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                                @Override
                                public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                                    return false;
                                }
                            });

                        }
                        else{
                            Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                                    "Not a valid Pivotal Tracker account", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
                            toast.show();
                        }
                    }
                }
        );

    }

    /**
     * Get the pivotaltracker stories that belongs to the project
     *
     */
    private void getPivoData(){
        pivotal = PivotalFunctionality.getInstance();
        List<PivotalProject> projects = pivotal.getPivotalProjects();
        storyNames = new HashMap<String,List<String>>();
        projNames = new ArrayList<String>();

        for(PivotalProject proj : projects){
            List<PivotalStory> stories = pivotal.getPivotalStories(proj.getId());
            projNames.add(proj.getName());
            List<String> story = new ArrayList<String>();
            for(PivotalStory stor : stories){
                story.add(stor.getName());
            }
            storyNames.put(proj.getName(),story);
        }


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}