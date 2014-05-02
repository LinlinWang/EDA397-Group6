package com.EDA397.Navigator.NaviGitator.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.EDA397.Navigator.NaviGitator.R;

import java.util.ArrayList;

/**
 * Adapter used to apply formatting to items in the CommitComment list, as well as to add
 * a "delete button" component to list items belonging to the currently logged in user.
 */
public class CommentAdapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> info;
    private GitFunctionality git;

    public CommentAdapter(Context c, int r, ArrayList<String> i) {
        super(c,r,i);
        this.context = c;
        this.info = i;
        git = git.getInstance();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.account_item, null);
        }

        final String s = info.get(position);
        TextView textView = (TextView) convertView.findViewById(R.id.comment_text);
        textView.setText(s);
        ImageButton deleteButton = (ImageButton) convertView.findViewById(R.id.delete_comment_button);
        String[] parts = s.split("Comment by ");

        if(parts[1].startsWith(git.getUserName())){
            deleteButton.setFocusableInTouchMode(false);
            deleteButton.setFocusable(false);
            deleteButton.setOnClickListener(
                    new ImageButton.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           // git.removeCommitComment(info.get(position), position);
                            info.remove(position);
                            remove(s);
                        }
                    }
            );
        } else {
            deleteButton.setVisibility(View.INVISIBLE);
        }

        return (convertView);
    }
}