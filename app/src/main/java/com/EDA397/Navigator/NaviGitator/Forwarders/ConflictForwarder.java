package com.EDA397.Navigator.NaviGitator.Forwarders;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.EDA397.Navigator.NaviGitator.R;
import com.EDA397.Navigator.NaviGitator.Services.NotificationVariables;

import org.eclipse.egit.github.core.event.PushPayload;

import java.util.ArrayList;
import java.util.HashMap;

public class ConflictForwarder extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationVariables.nrConflicts = 0;
        NotificationVariables.pushes = new ArrayList<PushPayload>();
        NotificationVariables.conflictFiles = new HashMap<String,String>();
        finish();
    }
}