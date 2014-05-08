package com.EDA397.Navigator.NaviGitator.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.EDA397.Navigator.NaviGitator.Services.NotificationVariables;

/**
 * Created by Quattro on 2014-05-08.
 */
public class NotificationForwarder extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        switch(getIntent().getExtras().getInt("notiID")){
            case 1:
                NotificationVariables.nrPushes = 0;
                break;
            case 2:
                NotificationVariables.nrCommitComments = 0;
                break;
            case 3:
                NotificationVariables.nrIssues = 0;
                break;
            case 4:
                NotificationVariables.nrIssuesComments = 0;
                break;
        }

        finish();
    }

}
