package com.EDA397.Navigator.NaviGitator.Services;

import org.eclipse.egit.github.core.event.PushPayload;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Quattro on 2014-05-08.
 */
public class NotificationVariables {
    public static int nrPushes;
    public static int nrCommitComments;
    public static int nrIssues;
    public static int nrIssuesComments;
    public static int nrConflicts;
    public static ArrayList<PushPayload> pushes;
    public static HashMap<String,String> conflictFiles;
}
