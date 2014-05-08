package com.EDA397.Navigator.NaviGitator.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.Process;
import android.support.v4.app.NotificationCompat;
//import android.app.Notification;
import android.widget.Toast;

import com.EDA397.Navigator.NaviGitator.Activities.GitFunctionality;
import com.EDA397.Navigator.NaviGitator.Activities.NotificationForwarder;
import com.EDA397.Navigator.NaviGitator.R;

import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.event.CommitCommentPayload;
import org.eclipse.egit.github.core.event.Event;
import org.eclipse.egit.github.core.event.IssueCommentPayload;
import org.eclipse.egit.github.core.event.IssuesPayload;
import org.eclipse.egit.github.core.event.PushPayload;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by QuattroX on 2014-04-29.
 */
public class NotificationService extends Service{

    private Thread thread;
    private SharedPreferences watched_files;
    private boolean running = false;

    private final int pushId = 1;
    private final int commitCommentsId = 2;
    private final int issuesId = 3;
    private final int issueCommentId = 4;
    private final int conflictId = 5;
    private ArrayList<PushPayload> pushes;
    private ArrayList<String> conflictFiles;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this,"Service created", Toast.LENGTH_LONG).show();

        thread = new Thread(){
            @Override
            public void run() {
                GitFunctionality git = GitFunctionality.getInstance();
                reset();
                Date currentLoopDate = new Date();
                currentLoopDate.getTime();

                NotificationManager NotifyManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                //Using same builder for all is the cause of settings on some notifications
                //"leaking" into others?
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
                NotificationCompat.BigTextStyle big = new NotificationCompat.BigTextStyle();
                builder.setStyle(big);

                while(running){
                    ArrayList<Event> events = git.getRepoEvents2();
                    for(Event e : events){
                        if(e.getCreatedAt().before(currentLoopDate)){
                            if(e.getType().equals("PushEvent")){
                                NotificationVariables.nrPushes++;
                                pushes.add((PushPayload)e.getPayload());
                            }
                            else if(e.getType().equals("CommitCommentEvent")){
                                NotificationVariables.nrCommitComments++;
                            }
                            else if(e.getType().equals("IssuesEvent")){
                                NotificationVariables.nrIssues++;
                            }
                            else if(e.getType().equals("IssueCommentEvent")){
                                NotificationVariables.nrIssuesComments++;
                            }
                        }
                    }
                    //We will need sharedpreference, last notification time/date is unique per account,
                    //If another account logs in they should get notifications.
                    currentLoopDate.setTime(System.currentTimeMillis());

                    Intent notiIntent = new Intent(getApplicationContext(), NotificationForwarder.class);

                    if(NotificationVariables.nrPushes > 0){
                        builder
                                .setSmallIcon(R.drawable.ic_launcher)
                                .setContentTitle(NotificationVariables.nrPushes + " new push(es)")
                                .setContentText("")
                                .setAutoCancel(true)
                                .setContentIntent(PendingIntent.getActivity(NotificationService.this, 0, notiIntent, 0));
                        big.bigText("");
                        Notification pushNoti = builder.build();

                        NotifyManager.notify(pushId, pushNoti);
                    }
                    if(NotificationVariables.nrCommitComments > 0){
                        builder
                                .setSmallIcon(R.drawable.ic_launcher)
                                .setContentTitle(NotificationVariables.nrCommitComments + " commit comment(s)")
                                .setContentText("")
                                .setAutoCancel(true)
                                .setContentIntent(PendingIntent.getActivity(NotificationService.this, 0, new Intent(), 0));
                        big.bigText("");
                        Notification commitCommentNoti = builder.build();

                        NotifyManager.notify(commitCommentsId, commitCommentNoti);
                    }
                    if(NotificationVariables.nrIssues > 0){
                        builder
                                .setSmallIcon(R.drawable.ic_launcher)
                                .setContentTitle(NotificationVariables.nrIssues + " new issue(s)")
                                .setContentText("")
                                .setAutoCancel(true)
                                .setContentIntent(PendingIntent.getActivity(NotificationService.this, 0, new Intent(), 0));
                        big.bigText("");
                        Notification issueNoti = builder.build();

                        NotifyManager.notify(issuesId, issueNoti);
                    }
                    if(NotificationVariables.nrIssuesComments > 0){
                        builder
                                .setSmallIcon(R.drawable.ic_launcher)
                                .setContentTitle(NotificationVariables.nrIssuesComments + " issue comment(s)")
                                .setContentText("")
                                .setAutoCancel(true)
                                .setContentIntent(PendingIntent.getActivity(NotificationService.this, 0, new Intent(), 0));
                        big.bigText("");
                        Notification issueCommentNoti =  builder.build();

                        NotifyManager.notify(issueCommentId, issueCommentNoti);
                    }
                    conflictCheck();
                    if(NotificationVariables.nrConflicts > 0){
                        String body = "";
                        for(String f : conflictFiles){
                            String [] temp = f.split("/");
                            body += temp[temp.length-1] + "\n";
                        }
                        builder
                                .setSmallIcon(R.drawable.ic_launcher)
                                .setContentTitle(conflictFiles.size() + " file conflict(s)")
                                .setContentText(body)
                                .setAutoCancel(true)
                                .setContentIntent(PendingIntent.getActivity(NotificationService.this, 0, new Intent(), 0));
                        big.bigText(body);
                        Notification conflictNoti = builder.build();
                        NotifyManager.notify(conflictId, conflictNoti);
                    }

                    try {
                        sleep(60000);
//                        reset();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.setPriority(Thread.MIN_PRIORITY);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        running = false;
        Toast.makeText(this,"Service destroyed", Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this,"Service started", Toast.LENGTH_LONG).show();
        running = true;
        thread.start();
        return super.onStartCommand(intent, flags, startId);
    }
    private void reset(){
        NotificationVariables.nrPushes = 0;
        NotificationVariables.nrCommitComments = 0;
        NotificationVariables.nrIssues = 0;
        NotificationVariables.nrIssuesComments = 0;
        NotificationVariables.nrConflicts = 0;
        pushes = new ArrayList<PushPayload>();
        conflictFiles = new ArrayList<String>();
    }
    private void conflictCheck(){
        GitFunctionality git = GitFunctionality.getInstance();
        watched_files = getSharedPreferences("WatchedFiles", MODE_PRIVATE);
        Set<String> watched = new HashSet<String>();
        watched.addAll(watched_files.getStringSet(git.getUserName() +
                git.getCurrentRepo().getName(), new HashSet<String>()));

        for(PushPayload p : pushes){
            String[] branch = p.getRef().split("/");
            for (Commit c : p.getCommits()) {
                for (String f : (git.checkConflicts(watched, c))){
                    if(!conflictFiles.contains(f + " in branch " +
                            branch[branch.length-1])){
                        conflictFiles.add(f + " in branch " + branch[branch.length-1]);
                        NotificationVariables.nrConflicts++;
                    }
                }
            }
        }
    }
}
