package com.EDA397.Navigator.NaviGitator.Fragments;



import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.EDA397.Navigator.NaviGitator.Activities.FileAdapter;
import com.EDA397.Navigator.NaviGitator.Activities.GitFunctionality;
import com.EDA397.Navigator.NaviGitator.Activities.RepoAdapter;
import com.EDA397.Navigator.NaviGitator.R;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryContents;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class FileWatchFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView listView;
    private ArrayList<RepositoryContents> repoContents;
    private GitFunctionality git;
    private SharedPreferences watched_files;
    private SharedPreferences.Editor watchEdit;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_file_watch, container, false);
        git = GitFunctionality.getInstance();
        watched_files = getActivity().getApplicationContext().getSharedPreferences("WatchedFiles",
                getActivity().getApplicationContext().MODE_PRIVATE);
        watchEdit = watched_files.edit();
        repoContents = git.getAllFileNames("");
        listView = (ListView) view.findViewById(R.id.file_list);
        listView.setClickable(true);
        listView.setOnItemClickListener(this);
        listView.setAdapter(new FileAdapter(getActivity().getApplicationContext(), R.layout.file_item,
                R.id.pathitem_text, repoContents));
        Button watch = (Button) view.findViewById(R.id.watch_button);
        watch.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Set<String> temp = new HashSet<String>();
                        temp.addAll(watched_files.getStringSet(git.getUserName() +
                                git.getCurrentRepo().getName(), new HashSet<String>()));
                        for(RepositoryContents r : repoContents){
                            if(!r.getType().equals("dir") && !temp.contains(r.getName())){
                                temp.add(r.getName());
                            }
                        }
                        watchEdit.putStringSet(git.getUserName() + git.getCurrentRepo().getName(),
                                temp);
                        watchEdit.commit();
                        listView.setAdapter(new FileAdapter(getActivity().getApplicationContext(),
                                R.layout.file_item, R.id.pathitem_text, repoContents));
                    }
                }
        );
        Button unwatch = (Button) view.findViewById(R.id.unwatch_button);
        unwatch.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Set<String> temp = new HashSet<String>();
                        temp.addAll(watched_files.getStringSet(git.getUserName() +
                                git.getCurrentRepo().getName(), new HashSet<String>()));
                        for(RepositoryContents r : repoContents){
                            if(!r.getType().equals("dir") && temp.contains(r.getName())){
                                temp.remove(r.getName());
                            }
                        }
                        watchEdit.putStringSet(git.getUserName() + git.getCurrentRepo().getName(),
                                temp);
                        watchEdit.commit();
                        listView.setAdapter(new FileAdapter(getActivity().getApplicationContext(),
                                R.layout.file_item, R.id.pathitem_text, repoContents));
                    }
                }
        );
        return view;
    }

    /**
     * Overrides method in parent class, used to set the repo that was clicked as the currently
     * selected one, followed by navigating to the tabs which show repo-specific info.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        Log.d("onItemClick", "FileListItem: " + position);
        if(repoContents.get(position).getType().equals("dir")){
            String path = repoContents.get(position).getPath();
            String backPath = "";
            String[] levels = path.split("/");
            TextView tv = (TextView) view.findViewById(R.id.repo_path);
            tv.setText(path);
            repoContents = new ArrayList<RepositoryContents>();
            if(!path.equals("")){
                RepositoryContents rc = new RepositoryContents();
                rc.setType("dir");
                rc.setContent("return");
                rc.setName("Root");
                rc.setPath("");
                repoContents.add(rc);
            }
            for(int i = 0; i < levels.length-1; i++){
                RepositoryContents rc = new RepositoryContents();
                rc.setType("dir");
                rc.setContent("return");
                rc.setName(levels[i]);
                backPath += levels[i] + "/";
                rc.setPath(backPath);
                repoContents.add(rc);
            }
            repoContents.addAll(git.getAllFileNames(path));
            FileAdapter f = (FileAdapter) listView.getAdapter();
            f.clear();
            f.addAll(repoContents);
        }
    }
}