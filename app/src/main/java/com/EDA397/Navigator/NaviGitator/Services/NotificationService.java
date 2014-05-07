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
import android.widget.Toast;

import com.EDA397.Navigator.NaviGitator.Activities.GitFunctionality;
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

    public static int nrPushes = 0;
    public static int nrCommitComments = 0;
    public static int nrIssues = 0;
    public static int nrIssuesComments = 0;

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

                Date currentLoopDate = new Date();
                currentLoopDate.getTime();

                NotificationManager NotifyManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());

                while(running){
                    ArrayList<Event> events = git.getRepoEvents2();
                    for(Event e : events){
                        if(e.getCreatedAt().after(currentLoopDate)){
                            if(e.getType().equals("PushEvent")){
                                nrPushes++;
                            }
                            else if(e.getType().equals("CommitCommentEvent")){
                                nrCommitComments++;
                            }
                            else if(e.getType().equals("IssuesEvent")){
                                nrIssues++;
                            }
                            else if(e.getType().equals("IssueCommentEvent")){
                                nrIssuesComments++;
                            }
                        }
                    }

                    currentLoopDate.getTime();

                    if(nrPushes > 0){
                        Notification pushNoti = builder
                                .setSmallIcon(R.drawable.ic_launcher)
                                .setContentTitle(nrPushes + " new push(es)")
                                .setAutoCancel(true)
                                .setContentIntent(PendingIntent.getActivity(NotificationService.this, 0, new Intent(), 0))
                                .build();

                        NotifyManager.notify(pushId, pushNoti);
                    }
                    if(nrCommitComments > 0){
                        Notification commitCommentNoti = builder
                                .setSmallIcon(R.drawable.ic_launcher)
                                .setContentTitle(nrCommitComments + " new commit comment(s)")
                                .setAutoCancel(true)
                                .setContentIntent(PendingIntent.getActivity(NotificationService.this, 0, new Intent(), 0))
                                .build();

                        NotifyManager.notify(commitCommentsId, commitCommentNoti);
                    }
                    if(nrIssues > 0){
                        Notification issueNoti = builder
                                .setSmallIcon(R.drawable.ic_launcher)
                                .setContentTitle(nrIssues + " new issue(s)")
                                .setAutoCancel(true)
                                .setContentIntent(PendingIntent.getActivity(NotificationService.this, 0, new Intent(), 0))
                                .build();

                        NotifyManager.notify(issuesId, issueNoti);
                    }
                    if(nrIssuesComments > 0){
                        Notification issueCommentNoti = builder
                                .setSmallIcon(R.drawable.ic_launcher)
                                .setContentTitle(nrIssuesComments + " new issue comment(s)")
                                .setAutoCancel(true)
                                .setContentIntent(PendingIntent.getActivity(NotificationService.this, 0, new Intent(), 0))
                                .build();

                        NotifyManager.notify(issueCommentId, issueCommentNoti);
                    }

                    try {
                        sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


//                watched_files = getSharedPreferences("WatchedFiles", MODE_PRIVATE);
//                Set<String> watched = new HashSet<String>();
//                watched.addAll(watched_files.getStringSet(git.getUserName() +
//                        git.getCurrentRepo().getName(), new HashSet<String>()));
//                ArrayList<String> conflictFiles = new ArrayList<String>();
//                for(PushPayload p : pushes){
//                    for (Commit c : p.getCommits()) {
//                        for (String f : (git.checkConflicts(watched, c))){
//                            if(!conflictFiles.contains(f)){
//                                conflictFiles.add(f);
//                            }
//                        }
//                    }
//                }
//                if(conflictFiles.size() > 0){
//                    Notification conflictNoti = builder
//                            .setSmallIcon(R.drawable.ic_launcher)
//                            .setContentTitle(conflictFiles.size() + " possible file conflict(s)")
//                            .setContentText("")
//                            .setAutoCancel(true)
//                            .setContentIntent(PendingIntent.getActivity(NotificationService.this, 0, new Intent(), 0))
//                            .build();
//                    NotifyManager.notify(conflictId, conflictNoti);
//                }

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
}
