package com.EDA397.Navigator.NaviGitator.Fragments;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.EDA397.Navigator.NaviGitator.Datatypes.PivotalProject;
import com.EDA397.Navigator.NaviGitator.Services.NotificationService;
import com.EDA397.Navigator.NaviGitator.R;
import com.EDA397.Navigator.NaviGitator.SupportFunctions.PivotalFunctionality;

import java.util.ArrayList;
import java.util.List;

public class PivotalFragment extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_pivotal, container, false);

        Button logout = (Button) view.findViewById(R.id.pivoLogin);
        logout.setFocusableInTouchMode(false);
        logout.setFocusable(false);
        logout.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String name = ((EditText)view.findViewById(R.id.username)).getText().toString().trim().toLowerCase();
                        final String pw = ((EditText)view.findViewById(R.id.password)).getText().toString().trim();
                        ListView lv = (ListView)view.findViewById(R.id.result);
                        PivotalFunctionality pivotal = PivotalFunctionality.getInstance();

                        if(name.length() < 1 || pw.length() < 1){
                            //Error handling too short/blank field.
                            //Toast toast = Toast.makeText(getApplicationContext(),
                            //        "Field(s) too short/unfilled", Toast.LENGTH_SHORT);
                            //toast.setGravity(Gravity.TOP| Gravity.LEFT, 0, 0);
                            //toast.show();
                        }
                        if (pivotal.pivotalLogin(name, pw)) {
                            List<PivotalProject> projects = pivotal.getPivotalProjects();
                            List<String> projNames = new ArrayList<String>();
                            for(PivotalProject proj : projects){
                                projNames.add(proj.getName());
                            }
                            lv.setAdapter(new ArrayAdapter<String>(view.getContext(),
                                    android.R.layout.simple_list_item_1, projNames));
                        }
                        else{
                            //Invalid GitHub account.
                            //Toast toast = Toast.makeText(getApplicationContext(),
                            //        "Not a valid GitHub account", Toast.LENGTH_SHORT);
                            //toast.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
                            //toast.show();
                        }
                    }
                }
        );
        return view;
    }

    private boolean isServiceRunning() {
        ActivityManager manager =
                (ActivityManager) getActivity().getSystemService(getActivity().ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service :
                manager.getRunningServices(Integer.MAX_VALUE)) {
            if (NotificationService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}