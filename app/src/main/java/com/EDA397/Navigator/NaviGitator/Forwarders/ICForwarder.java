package com.EDA397.Navigator.NaviGitator.Forwarders;

import android.app.Activity;
import android.os.Bundle;

import com.EDA397.Navigator.NaviGitator.Services.NotificationVariables;

/**
 * Created by QuattroX on 2014-05-08.
 */
public class ICForwarder extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationVariables.nrIssuesComments = 0;
        finish();
    }
}
