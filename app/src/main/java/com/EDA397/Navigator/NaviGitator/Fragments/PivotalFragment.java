package com.EDA397.Navigator.NaviGitator.Fragments;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.EDA397.Navigator.NaviGitator.Datatypes.PivotalProject;
import com.EDA397.Navigator.NaviGitator.Datatypes.PivotalStory;
import com.EDA397.Navigator.NaviGitator.Services.NotificationService;
import com.EDA397.Navigator.NaviGitator.Adapters.ExpandableListAdapter;
import com.EDA397.Navigator.NaviGitator.R;
import com.EDA397.Navigator.NaviGitator.SupportFunctions.PivotalFunctionality;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PivotalFragment extends Fragment implements AdapterView.OnItemClickListener{

    private View view;
    private List<String> projNames;
    private PivotalFunctionality pivotal;
    private HashMap<String,List<String>> storyNames;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_pivotal, container, false);
        PivoLogin();

        return view;
    }

    private void PivoLogin(){
        Button logout = (Button) view.findViewById(R.id.pivoLogin);
        logout.setFocusableInTouchMode(false);
        logout.setFocusable(false);
        logout.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String name = ((EditText)view.findViewById(R.id.pivotalUsername)).getText().toString().trim().toLowerCase();
                        final String pw = ((EditText)view.findViewById(R.id.pivotalPassword)).getText().toString().trim();
                        ExpandableListView lv = (ExpandableListView)view.findViewById(R.id.expandableListView);
                        pivotal = PivotalFunctionality.getInstance();

                        if(name.length() < 1 || pw.length() < 1){
                            //Error handling too short/blank field.
                            Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                                    "Field(s) too short/unfilled", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP| Gravity.LEFT, 0, 0);
                            toast.show();
                        }
                        if (pivotal.pivotalLogin(name, pw)) {

                            lv.setAdapter(new ArrayAdapter<String>(view.getContext(),
                                    android.R.layout.simple_list_item_1, getPivoData()));

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

    private List<String> getPivoData(){
        pivotal = PivotalFunctionality.getInstance();
        List<PivotalProject> projects = pivotal.getPivotalProjects();

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

        return projNames;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}