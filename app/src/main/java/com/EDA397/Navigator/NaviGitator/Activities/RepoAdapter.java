package com.EDA397.Navigator.NaviGitator.Activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.EDA397.Navigator.NaviGitator.R;

import org.eclipse.egit.github.core.Repository;

import java.util.ArrayList;

/**
 * Created by Eric on 2014-04-05.
 * Adapter used to apply formatting to the items in the repo list.
 */
public class RepoAdapter extends ArrayAdapter<Repository> {

    private Context context;
    private ArrayList<Repository> repos;

    public RepoAdapter(Context c, int r, int tv, ArrayList<Repository> l) {
        super(c,r,tv,l);
        this.context = c;
        this.repos = l;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final String s = repos.get(position).getName() + "\nLast Updated:\n" +
                         repos.get(position).getPushedAt();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.repo_item, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.repo_text);
        tv.setText(s);
        return (convertView);
    }
}