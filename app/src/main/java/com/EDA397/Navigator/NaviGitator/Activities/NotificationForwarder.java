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
        Log.d("NOTI", "----VALUE: " + NotificationVariables.nrPushes);
        NotificationVariables.nrPushes = 0;
    }

}
