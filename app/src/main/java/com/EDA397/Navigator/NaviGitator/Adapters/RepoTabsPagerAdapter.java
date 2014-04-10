package com.EDA397.Navigator.NaviGitator.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.EDA397.Navigator.NaviGitator.Fragments.BranchesFragment;
import com.EDA397.Navigator.NaviGitator.Fragments.CommentsFragment;
import com.EDA397.Navigator.NaviGitator.Fragments.CommitsFragment;
import com.EDA397.Navigator.NaviGitator.Fragments.IssuesFragment;

/**
 * Created by QuattroX on 2014-04-10.
 */
public class RepoTabsPagerAdapter extends FragmentPagerAdapter {

    public RepoTabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Commits fragment activity
                return new CommitsFragment();
            case 1:
                // Branches fragment activity
                return new BranchesFragment();
            case 2:
                // Issues fragment activity
                return new IssuesFragment();
            case 3:
                // Comments fragment activity
                return new CommentsFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 4;
    }
}
