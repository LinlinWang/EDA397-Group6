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
import java.util.HashSet;
import java.util.Set;

/**
 * Created by QuattroX on 2014-04-29.
 */
public class NotificationService extends Service{

    private Thread thread;
    private SharedPreferences watched_files;

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
                NotificationManager NotifyManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
                Notification notification = builder
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("This is the title")
                        .setContentText("This is the text")
                        .setAutoCancel(true)
                        .setContentIntent(PendingIntent.getActivity(NotificationService.this, 0, new Intent(), 0))
                        .build();
                GitFunctionality git = GitFunctionality.getInstance();
                ArrayList<PushPayload> pushes = new ArrayList<PushPayload>();
                ArrayList<Event> events = git.getRepoEvents2();
                int nrPushes = 0;
                int nrCommitComments = 0;
                int nrIssues = 0;
                int nrIssuesComments = 0;
                int pushId = 1;
                int commitCommentsId = 2;
                int issuesId = 3;
                int issueCommentId = 4;
                int conflictId = 5;
                for(Event e : events){
                    if(e.getType().equals("PushEvent")){
                        PushPayload p = (PushPayload) e.getPayload();
                        pushes.add(p);
                        String[] branch = p.getRef().split("/");
                        Notification pushNoti = builder
                                .setSmallIcon(R.drawable.ic_launcher)
                                .setContentTitle(++nrPushes + " new push(es)")
                                .setContentText("on branch " + branch[branch.length-1])
                                .setAutoCancel(true)
                                .setContentIntent(PendingIntent.getActivity(NotificationService.this, 0, new Intent(), 0))
                                .build();
                        NotifyManager.notify(pushId, pushNoti);
                    }
                    else if(e.getType().equals("CommitCommentEvent")){
                        CommitCommentPayload ccp = (CommitCommentPayload) e.getPayload();
                        Notification commitCommentNoti = builder
                                .setSmallIcon(R.drawable.ic_launcher)
                                .setContentTitle(++nrCommitComments + " new commit comment(s)")
                                .setContentText("on " + ccp.getComment().getCommitId())
                                .setAutoCancel(true)
                                .setContentIntent(PendingIntent.getActivity(NotificationService.this, 0, new Intent(), 0))
                                .build();
                        NotifyManager.notify(commitCommentsId, commitCommentNoti);
                    }
                    else if(e.getType().equals("IssuesEvent")){
//                        IssuesPayload ip = (IssuesPayload) e.getPayload();
                        Notification issueNoti = builder
                                .setSmallIcon(R.drawable.ic_launcher)
                                .setContentTitle(++nrIssues + " new issue(s)")
                                .setContentText("")
                                .setAutoCancel(true)
                                .setContentIntent(PendingIntent.getActivity(NotificationService.this, 0, new Intent(), 0))
                                .build();
                        NotifyManager.notify(issuesId, issueNoti);
                    }
                    else if(e.getType().equals("IssueCommentEvent")){
                        IssueCommentPayload icp = (IssueCommentPayload) e.getPayload();
                        Notification issueCommentNoti = builder
                                .setSmallIcon(R.drawable.ic_launcher)
                                .setContentTitle(++nrIssuesComments + " new issue comment(s)")
                                .setContentText("on " + icp.getIssue().getTitle())
                                .setAutoCancel(true)
                                .setContentIntent(PendingIntent.getActivity(NotificationService.this, 0, new Intent(), 0))
                                .build();
                        NotifyManager.notify(issueCommentId, issueCommentNoti);
                    }
                }
                watched_files = getSharedPreferences("WatchedFiles", MODE_PRIVATE);
                Set<String> watched = new HashSet<String>();
                watched.addAll(watched_files.getStringSet(git.getUserName() +
                        git.getCurrentRepo().getName(), new HashSet<String>()));
                ArrayList<String> conflictFiles = new ArrayList<String>();
                for(PushPayload p : pushes){
                    for (Commit c : p.getCommits()) {
                        for (String f : (git.checkConflicts(watched, c))){
                            if(!conflictFiles.contains(f)){
                                conflictFiles.add(f);
                            }
                        }
                    }
                }
                if(conflictFiles.size() > 0){
                    Notification conflictNoti = builder
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle(conflictFiles.size() + " possible file conflict(s)")
                            .setContentText("")
                            .setAutoCancel(true)
                            .setContentIntent(PendingIntent.getActivity(NotificationService.this, 0, new Intent(), 0))
                            .build();
                    NotifyManager.notify(conflictId, conflictNoti);
                }
                NotifyManager.notify(1111, notification);
            }
        };
        thread.setPriority(Thread.MIN_PRIORITY);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this,"Service destroyed", Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this,"Service started", Toast.LENGTH_LONG).show();
        thread.start();
        return super.onStartCommand(intent, flags, startId);
    }
}
