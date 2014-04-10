package com.EDA397.Navigator.NaviGitator.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.EDA397.Navigator.NaviGitator.Fragments.MyProjectsFragment;
import com.EDA397.Navigator.NaviGitator.Fragments.NewsFragment;
import com.EDA397.Navigator.NaviGitator.Fragments.SettingsFragment;

/**
 * Created by QuattroX on 2014-04-09.
 */
public class MainTabsPagerAdapter extends FragmentPagerAdapter {

    public MainTabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // News fragment activity
                return new NewsFragment();
            case 1:
                // MyProjects fragment activity
                return new MyProjectsFragment();
            case 2:
                // Settings fragment activity
                return new SettingsFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}