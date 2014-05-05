package com.EDA397.Navigator.NaviGitator.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.EDA397.Navigator.NaviGitator.Activities.GitFunctionality;
import com.EDA397.Navigator.NaviGitator.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by linlinwang on 05/05/14.
 */
public class IssuesCommentsFragment extends Fragment implements AdapterView.OnItemClickListener{

    private ListView listView;
    private GitFunctionality git;
    private Context context;
    private ArrayList<String> info;
    private View view;
    private SharedPreferences watched_files;
    Button commentButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_comments, container, false);
        git = GitFunctionality.getInstance();
        watched_files = getActivity().getApplicationContext().getSharedPreferences("WatchedFiles",
                getActivity().getApplicationContext().MODE_PRIVATE);
        Set<String> watched = new HashSet<String>();
        watched.addAll(watched_files.getStringSet(git.getUserName() +
                git.getCurrentRepo().getName(), new HashSet<String>()));
        addListenerOnButton();
        info = new ArrayList<String>();
        context = container.getContext();

        if(git.getCurrentIssues() != null) {
            info.add("Message: " + "\n" + git.getCurrentIssues().getBody());

            for (String f : git.getIssueComments()) {
                if(watched.contains(f)){
                    Log.d("Possible Conflict", f);
                }
                String[] temp = f.split("/");
                info.add("Modified File:\n" + temp[temp.length - 1]);
            }
            for (String s : git.getIssueComments()) {
                info.add("Comment by " + s);
            }

            listView = (ListView) view.findViewById(R.id.commitInfo_list);
            listView.setClickable(true);
            listView.setOnItemClickListener(this);
            listView.setAdapter(new ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1, info));
        }

        return view;
    }
    /**
     * Used to create the "add comment" button, as well as assigning it the necessary logic
     * to perform said function.
     */
    private void addListenerOnButton() {

        commentButton = (Button) view.findViewById(R.id.comment_button);
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View promptsView = li.inflate(R.layout.comment_prompt, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.repo_comment_input);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (userInput.getText() != null) {
                                            //git.addIssueComment (userInput.getText().toString());
                                            //git.addCommitComment(userInput.getText().toString());
                                            dialog.dismiss();
                                        } else {
                                            dialog.cancel();
                                        }

                                    }
                                }
                        )
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                }
                        );

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

        });
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
