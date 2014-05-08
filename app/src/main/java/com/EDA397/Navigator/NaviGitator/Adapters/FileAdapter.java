package com.EDA397.Navigator.NaviGitator.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import com.EDA397.Navigator.NaviGitator.R;
import com.EDA397.Navigator.NaviGitator.SupportFunctions.GitFunctionality;

import org.eclipse.egit.github.core.RepositoryContents;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Eric on 2014-04-17.
 * Adapter used to apply formatting to items in the File Watcher list (depending on if item is a
 * folder or file), as well as to add logic for what to do when an file item's checkbox is
 * interacted with.
 */
public class FileAdapter extends ArrayAdapter<RepositoryContents> {

    private Context context;
    private SharedPreferences watched_files;
    private SharedPreferences.Editor watchEdit;
    private ArrayList<RepositoryContents> items;

    public FileAdapter(Context c, int r, int tv, ArrayList<RepositoryContents> l) {
        super(c,r,tv,l);
        this.context = c;
        this.items = l;
        watched_files = c.getSharedPreferences("WatchedFiles", c.MODE_PRIVATE);
        watchEdit = watched_files.edit();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final String p = items.get(position).getPath();
        final String n = items.get(position).getName();
        final GitFunctionality git = GitFunctionality.getInstance();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.file_item, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.pathitem_text);
        CheckBox watch = (CheckBox) convertView.findViewById(R.id.watch_box);
        watch.setFocusableInTouchMode(false);
        watch.setFocusable(false);
        if(items.get(position).getType().equals("dir")){
            watch.setEnabled(false);
            watch.setVisibility(convertView.GONE);
            if(items.get(position).getContent() != null &&
               items.get(position).getContent().equals("return")) {
                tv.setText("Back to " + n + "...");
            }
            else{
                tv.setText("Open dir " + n + "...");
            }
        }
        else {
            Set<String> watched = new HashSet<String>();
            watched.addAll(watched_files.getStringSet(git.getUserName() +
                    git.getCurrentRepo().getName(), new HashSet<String>()));
            tv.setText(n);
            watch.setEnabled(true);
            watch.setVisibility(convertView.VISIBLE);
            watch.setChecked(watched.contains(p));
            watch.setOnClickListener(
                    new CheckBox.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean checked = ((CheckBox) v).isChecked();
                            Set<String> watched = new HashSet<String>();
                            watched.addAll(watched_files.getStringSet(git.getUserName() +
                                    git.getCurrentRepo().getName(), new HashSet<String>()));
                            if (checked) {
                                watched.add(p);
                                watchEdit.putLong(p + git.getUserName(), System.currentTimeMillis());
                                watchEdit.putStringSet(git.getUserName() +
                                        git.getCurrentRepo().getName(), watched);
                                watchEdit.commit();
                            } else {
                                watched.remove(p);
                                watchEdit.remove(p + git.getUserName());
                                watchEdit.putStringSet(git.getUserName() +
                                        git.getCurrentRepo().getName(), watched);
                                watchEdit.commit();
                            }
                        }
                    }
            );
        }
        return (convertView);
    }
}