package com.EDA397.Navigator.NaviGitator.Tests;

import android.test.InstrumentationTestCase;

import com.EDA397.Navigator.NaviGitator.Activities.GitFunctionality;
import com.EDA397.Navigator.NaviGitator.Activities.PivotalFunctionality;

/**
 * Created by sajfer on 2014-05-01.
 */
public class PivotalFunctionalityTest extends InstrumentationTestCase {
    private static PivotalFunctionality pivotal;

    protected void setUp() throws Exception{
        pivotal = PivotalFunctionality.getInstance();
        //if(git.getUserName().equals("")) {
        //    git.gitLogin("navigitator", "navi123");
        //    git.setCurrentRepo(git.getRepos().get(0));
        //    git.setCurrentCommit(git.getRepoCommits().get(0));
        //}
    }

    public void testLogin() throws Exception {
        String username = "";
        String password = "";
        boolean result;
        result = pivotal.pivotalLogin(username, password);
        assertTrue(result);
    }

    public void testGetProjects() throws Exception {
        String projects = "";
        //projects = pivotal.getProjects();
        assertFalse(projects.equals(""));
    }

    public void testGetStories() throws Exception {
        String stories = "";
        //stories = pivotal.getStories();
        assertFalse(stories.equals(""));
    }
}