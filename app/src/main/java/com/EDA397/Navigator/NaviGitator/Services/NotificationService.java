package com.EDA397.Navigator.NaviGitator.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.EDA397.Navigator.NaviGitator.Forwarders.CCForwarder;
import com.EDA397.Navigator.NaviGitator.Forwarders.ConflictForwarder;
import com.EDA397.Navigator.NaviGitator.Forwarders.ICForwarder;
import com.EDA397.Navigator.NaviGitator.Forwarders.IssueForwarder;
import com.EDA397.Navigator.NaviGitator.Forwarders.PushForwarder;
import com.EDA397.Navigator.NaviGitator.SupportFunctions.GitFunctionality;
import com.EDA397.Navigator.NaviGitator.R;

import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.event.Event;
import org.eclipse.egit.github.core.event.PushPayload;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
                initVariables();
                Date currentLoopDate = new Date();
                currentLoopDate.getTime();

                NotificationManager NotifyManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
                NotificationCompat.Builder builderConflict = new NotificationCompat.Builder(getApplicationContext());
                NotificationCompat.BigTextStyle big = new NotificationCompat.BigTextStyle();
                builderConflict.setStyle(big);

                Intent pushIntent = new Intent(getApplicationContext(), PushForwarder.class);
                Intent ccIntent = new Intent(getApplicationContext(), CCForwarder.class);
                Intent issueIntent = new Intent(getApplicationContext(), IssueForwarder.class);
                Intent icIntent = new Intent(getApplicationContext(), ICForwarder.class);
                Intent conflictIntent = new Intent(getApplicationContext(), ConflictForwarder.class);

                while(running){
                    ArrayList<Event> events = git.getRepoEvents2();
                    NotificationVariables.pushes = new ArrayList<PushPayload>();
                    for(Event e : events){
                        if(e.getCreatedAt().after(currentLoopDate)){
                            if(e.getType().equals("PushEvent")){
                                NotificationVariables.nrPushes++;
                                NotificationVariables.pushes.add((PushPayload)e.getPayload());
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

                    currentLoopDate.setTime(System.currentTimeMillis());

                    if(NotificationVariables.nrPushes > 0){
                        Notification pushNoti = builder
                                .setSmallIcon(R.drawable.minitator)
                                .setContentTitle(NotificationVariables.nrPushes + " new push(es)")
                                .setAutoCancel(true)
                                .setContentIntent(PendingIntent.getActivity(NotificationService.this, 0, pushIntent, 0))
                                .setDeleteIntent(PendingIntent.getActivity(NotificationService.this, 0, pushIntent, 0))
                                .build();

                        NotifyManager.notify(pushId, pushNoti);
                    }
                    if(NotificationVariables.nrCommitComments > 0){
                        Notification commitCommentNoti = builder
                                .setSmallIcon(R.drawable.minitator)
                                .setContentTitle(NotificationVariables.nrCommitComments + " commit comment(s)")
                                .setAutoCancel(true)
                                .setContentIntent(PendingIntent.getActivity(NotificationService.this, 0, ccIntent, 0))
                                .setDeleteIntent(PendingIntent.getActivity(NotificationService.this, 0, ccIntent, 0))
                                .build();

                        NotifyManager.notify(commitCommentsId, commitCommentNoti);
                    }
                    if(NotificationVariables.nrIssues > 0){
                        Notification issueNoti = builder
                                .setSmallIcon(R.drawable.minitator)
                                .setContentTitle(NotificationVariables.nrIssues + " new issue(s)")
                                .setAutoCancel(true)
                                .setContentIntent(PendingIntent.getActivity(NotificationService.this, 0, issueIntent, 0))
                                .setDeleteIntent(PendingIntent.getActivity(NotificationService.this, 0, issueIntent, 0))
                                .build();

                        NotifyManager.notify(issuesId, issueNoti);
                    }
                    if(NotificationVariables.nrIssuesComments > 0){
                        Notification issueCommentNoti = builder
                                .setSmallIcon(R.drawable.minitator)
                                .setContentTitle(NotificationVariables.nrIssuesComments + " issue comment(s)")
                                .setAutoCancel(true)
                                .setContentIntent(PendingIntent.getActivity(NotificationService.this, 0, icIntent, 0))
                                .setDeleteIntent(PendingIntent.getActivity(NotificationService.this, 0, icIntent, 0))
                                .build();

                        NotifyManager.notify(issueCommentId, issueCommentNoti);
                    }

                    conflictCheck();
                    if(NotificationVariables.conflictFiles.size()>0){
                        String body = "";
                        for(String f : NotificationVariables.conflictFiles.keySet()){
                           String [] temp = f.split("/");
                           body = NotificationVariables.conflictFiles.get(f);
                            builderConflict
                                    .setSmallIcon(R.drawable.minitator)
                                    .setContentTitle("Possible conflict")
                                    .setContentText(temp[temp.length - 1])
                                    .setAutoCancel(true)
                                    .setContentIntent(PendingIntent.getActivity(NotificationService.this, 0, conflictIntent, 0))
                                    .setDeleteIntent(PendingIntent.getActivity(NotificationService.this, 0, conflictIntent, 0));
                            big.bigText("In branches " + body);
                            big.setSummaryText(temp[temp.length - 1]);
                            big.setBigContentTitle("Possible conflict");
                            Notification conflictNoti = builderConflict.build();
                            NotifyManager.notify((int)watched_files.getLong(f + git.getUserName(), 0), conflictNoti);
                        }
                    }

                    try {
                        sleep(60000);
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
    private void initVariables(){
        NotificationVariables.nrPushes = 0;
        NotificationVariables.nrCommitComments = 0;
        NotificationVariables.nrIssues = 0;
        NotificationVariables.nrIssuesComments = 0;
        NotificationVariables.nrConflicts = 0;
        NotificationVariables.pushes = new ArrayList<PushPayload>();
        NotificationVariables.conflictFiles = new HashMap<String,String>();
    }
    private void conflictCheck(){
        GitFunctionality git = GitFunctionality.getInstance();
        watched_files = getSharedPreferences("WatchedFiles", MODE_PRIVATE);
        Set<String> watched = new HashSet<String>();
        watched.addAll(watched_files.getStringSet(git.getUserName() +
                git.getCurrentRepo().getName(), new HashSet<String>()));

        for(PushPayload p : NotificationVariables.pushes){
            String[] branch = p.getRef().split("/");
            for (Commit c : p.getCommits()) {
                for (String f : (git.checkConflicts(watched, c))){
                    String temp = NotificationVariables.conflictFiles.get(f);
                    if(temp == null){
                        NotificationVariables.conflictFiles.put(f,branch[branch.length-1]);
                        NotificationVariables.nrConflicts++;
                    }
                    else if(!temp.contains(branch[branch.length-1])){
                        NotificationVariables.conflictFiles.put(f, temp + ", " + branch[branch.length-1]);
                        NotificationVariables.nrConflicts++;
                    }
                }
            }
        }
    }
}
