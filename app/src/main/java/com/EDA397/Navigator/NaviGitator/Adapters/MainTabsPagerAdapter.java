package com.EDA397.Navigator.NaviGitator.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.EDA397.Navigator.NaviGitator.Activities.BranchFragment;
import com.EDA397.Navigator.NaviGitator.Activities.GitFunctionality;
import com.EDA397.Navigator.NaviGitator.Activities.MyProjectsFragment;
import com.EDA397.Navigator.NaviGitator.Activities.NewsFragment;
import com.EDA397.Navigator.NaviGitator.Activities.SettingsFragment;

/**
 * Created by QuattroX on 2014-04-09.
 */
public class MainTabsPagerAdapter  extends FragmentPagerAdapter {

    private GitFunctionality git;

    public MainTabsPagerAdapter(FragmentManager fm, GitFunctionality git) {
        super(fm);
        this.git = git;
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new NewsFragment();
            case 1:
                // Games fragment activity
                return new MyProjectsFragment(git);
            case 2:
                // Movies fragment activity
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