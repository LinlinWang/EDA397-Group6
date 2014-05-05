package com.EDA397.Navigator.NaviGitator.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.EDA397.Navigator.NaviGitator.Activities.GitFunctionality;
import com.EDA397.Navigator.NaviGitator.R;
import com.EDA397.Navigator.NaviGitator.Services.NotificationService;

/**
 * Created by QuattroX on 2014-04-09.
 * Fragment displaying user information and additional options.
 */
public class SettingsFragment extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_settings, container, false);
        GitFunctionality git = GitFunctionality.getInstance();
        TextView t = (TextView) view.findViewById(R.id.acc_name);
        t.setText("Account Name: " + git.getUserName());
        Button logout = (Button) view.findViewById(R.id.logout_button);
        logout.setFocusableInTouchMode(false);
        logout.setFocusable(false);
        logout.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /**
                         if(isServiceRunning()){
                         getActivity().stopService(new Intent(getActivity().getApplicationContext(),
                         NotificationService.class));
                         }
                         **/
                        startActivity(new Intent("com.EDA397.Navigator.NaviGitator.Activities.LoginActivity"));
                    }
                }
        );
        Button stopNTF = (Button) view.findViewById(R.id.stopNTF_button);
        stopNTF.setFocusableInTouchMode(false);
        stopNTF.setFocusable(false);
        stopNTF.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /**
                         if(isServiceRunning()){
                         getActivity().stopService(new Intent(getActivity().getApplicationContext(),
                         NotificationService.class));
                         }
                         **/
                    }
                }
        );
        return view;
    }
}