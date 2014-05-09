package com.EDA397.Navigator.NaviGitator.Activities;

import android.app.ActionBar;
import android.app.ActivityManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;

import com.EDA397.Navigator.NaviGitator.Adapters.MainTabsPagerAdapter;
import com.EDA397.Navigator.NaviGitator.Datatypes.PivotalProject;
import com.EDA397.Navigator.NaviGitator.Datatypes.PivotalStory;
import com.EDA397.Navigator.NaviGitator.R;
import com.EDA397.Navigator.NaviGitator.Services.NotificationService;
import com.EDA397.Navigator.NaviGitator.SupportFunctions.GitFunctionality;
import com.EDA397.Navigator.NaviGitator.SupportFunctions.PivotalFunctionality;

import java.util.List;

public class MainActivity extends FragmentActivity implements
        ActionBar.TabListener {

    private ViewPager viewPager;
    private MainTabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    // Tab titles
    private String[] tabs = { "News", "My Projects", "Pivotal Tracker", "Settings"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GitFunctionality git = GitFunctionality.getInstance();
        PivotalFunctionality pv = PivotalFunctionality.getInstance();
        pv.pivotalLogin("username","password");
        List<PivotalProject> projs = pv.getPivotalProjects();
        List<PivotalStory> stories = pv.getPivotalStories(1043912);

        for(PivotalProject project : projs) {
            Log.d("MainActivity", "Project: " + project.getName());
            Log.d("MainActivity", "Project Id: " + project.getId());
        }

        for(PivotalStory story : stories) {
            Log.d("MainActivity", "Story: " + story.getName());
            Log.d("MainActivity", "Story Id: " + story.getId());
        }
        if (git.getUserName().equals("")){
            startActivity(new Intent("com.EDA397.Navigator.NaviGitator.Activities.LoginActivity"));
        }
        else{
            // Initilization
            viewPager = (ViewPager) findViewById(R.id.viewpager);
            actionBar = getActionBar();
            mAdapter = new MainTabsPagerAdapter(getSupportFragmentManager());

            viewPager.setAdapter(mAdapter);
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

        if(isServiceRunning()){
            stopService(new Intent(getApplicationContext(),
            NotificationService.class));
         }

        super.onBackPressed();
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
}
