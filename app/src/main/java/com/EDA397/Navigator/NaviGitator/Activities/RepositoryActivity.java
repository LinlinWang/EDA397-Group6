package com.EDA397.Navigator.NaviGitator.Activities;

import android.app.ActionBar;
import android.app.ActivityManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.EDA397.Navigator.NaviGitator.Adapters.RepoTabsPagerAdapter;
import com.EDA397.Navigator.NaviGitator.R;
import com.EDA397.Navigator.NaviGitator.Services.NotificationService;

/**
 * Created by QuattroX on 2014-04-10.
 */
public class RepositoryActivity extends FragmentActivity implements
        ActionBar.TabListener{
    public ViewPager viewPager;
    private RepoTabsPagerAdapter rAdapter;
    private ActionBar actionBar;

    // Tab titles
    private String[] tabs = { "Commits", "Branches", "Issues", "Comments", "Watched Files" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository);
        GitFunctionality git = GitFunctionality.getInstance();

        if (git.getUserName().equals("")){
            startActivity(new Intent("com.EDA397.Navigator.NaviGitator.Activities.LoginActivity"));
        }
        else {
            // Start Notification Service
//            if(!isServiceRunning()){
//                startService(new Intent(this, NotificationService.class));
//            }

            viewPager = (ViewPager) findViewById(R.id.viewpagerRepo);
            actionBar = getActionBar();
            rAdapter = new RepoTabsPagerAdapter(getSupportFragmentManager());

            viewPager.setAdapter(rAdapter);
//            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

            // Adding Tabs
            for (String tab_name : tabs) {
                actionBar.addTab(actionBar.newTab().setText(tab_name)
                        .setTabListener(this));
            }

            /**
             * on swiping the viewpager make respective tab selected
             * */
            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {
                    // on changing the page
                    // make respected tab selected
                    actionBar.setSelectedNavigationItem(position);
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                }

                @Override
                public void onPageScrollStateChanged(int arg0) {
                }
            });
        }
    }

    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (NotificationService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }
    public void onBackPressed() {
        GitFunctionality git = GitFunctionality.getInstance();
        git.setCurrentCommit(null);
        super.onBackPressed();
    }
}
