package com.EDA397.Navigator.NaviGitator.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.EDA397.Navigator.NaviGitator.SupportFunctions.GitFunctionality;
import com.EDA397.Navigator.NaviGitator.R;

import java.util.ArrayList;

/**
 * Created by QuattroX on 2014-04-09.
 * Fragment for displaying events received by the logged-in user,
 * covering all repos the user has access to.
 */
public class NewsFragment extends Fragment implements AdapterView.OnItemClickListener{

    private ListView listView;
    private GitFunctionality git;
    private View view;
    private ArrayList<String> newsItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_news, container, false);
        git = GitFunctionality.getInstance();
        newsItems = git.getUserEvents();
        listView = (ListView) view.findViewById(R.id.news_list);
        listView.setClickable(true);
        listView.setOnItemClickListener(this);
        listView.setAdapter(new ArrayAdapter<String>(view.getContext(),
                            android.R.layout.simple_list_item_1, newsItems));

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        //Redirection logic
    }
}