package com.EDA397.Navigator.NaviGitator.Tests;

import android.test.InstrumentationTestCase;

import com.EDA397.Navigator.NaviGitator.Datatypes.PivotalStory;
import com.EDA397.Navigator.NaviGitator.SupportFunctions.PivotalFunctionality;
import com.EDA397.Navigator.NaviGitator.Datatypes.PivotalProject;

import java.util.List;

/**
 * Created by sajfer on 2014-05-01.
 */
public class PivotalFunctionalityTest extends InstrumentationTestCase {
    private static PivotalFunctionality pivotal;

    protected void setUp() throws Exception{
        pivotal = PivotalFunctionality.getInstance();
        if(pivotal.returnToken().equals("")) {
            pivotal.pivotalLogin("username", "password");
        }
    }

    /**
     * Test if the pivotalTracker login works
     * @throws Exception If the login fails
     */
    public void testLogin() throws Exception {
        String username = "username";
        String password = "password";
        boolean result;
        result = pivotal.pivotalLogin(username, password);
        assertTrue(result);
    }

    /**
     * Test if the GetProjects function works
     * @throws Exception If the getProjects fails
     */
    public void testGetProjects() throws Exception {
        List<PivotalProject> projects = null;
        projects = pivotal.getPivotalProjects();
        assertFalse(projects.isEmpty());
    }

    /**
     * Test if the GetStories function works
     * @throws Exception If the getStories fails
     */
    public void testGetStories() throws Exception {
        List<PivotalStory> stories = null;
        stories = pivotal.getPivotalStories();
        assertFalse(stories.isEmpty());
    }
}