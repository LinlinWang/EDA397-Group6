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

import com.EDA397.Navigator.NaviGitator.Activities.GitFunctionality;
import com.EDA397.Navigator.NaviGitator.R;

import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.event.Event;
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
//                watched_files = getSharedPreferences("WatchedFiles", MODE_PRIVATE);
//                Set<String> watched = new HashSet<String>();
//                watched.addAll(watched_files.getStringSet(git.getUserName() +
//                        git.getCurrentRepo().getName(), new HashSet<String>()));
//                ArrayList<PushPayload> pushes = git.getRepoEvents();
                /**for(PushPayload p : pushes){
                    for (Commit c : p.getCommits()) {
                        git.checkConflicts(watched, c);
                    }
                }**/
                ArrayList<Event> events = git.getRepoEvents2();
                int nrPushes = 0;
                int pushId = 1;
                for(Event e : events){
                    if(e.getType().equals("PushEvent")){
                        PushPayload p = (PushPayload) e.getPayload();
                        String[] branch = p.getRef().split("/");
                        Notification pushNoti = builder
                                .setSmallIcon(R.drawable.ic_launcher)
                                .setContentTitle(++nrPushes + " new push")
                                .setContentText("on branch " + branch[branch.length-1])
                                .setAutoCancel(true)
                                .setContentIntent(PendingIntent.getActivity(NotificationService.this, 0, new Intent(), 0))
                                .build();
                        NotifyManager.notify(pushId, pushNoti);
                    }
                    else if(e.getType().equals("CommitCommentEvent")){

                    }
                    else if(e.getType().equals("IssueEvent")){

                    }
                    else if(e.getType().equals("IssueCommentEvent")){

                    }
                }
                NotifyManager.notify(1111, notification);
            }
        };

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
