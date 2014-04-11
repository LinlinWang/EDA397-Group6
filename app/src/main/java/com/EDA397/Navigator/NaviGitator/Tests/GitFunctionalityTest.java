package com.EDA397.Navigator.NaviGitator.Tests;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.EDA397.Navigator.NaviGitator.Activities.GitFunctionality;

import org.eclipse.egit.github.core.Repository;

import java.util.List;


/**
 * Created by linlinwang on 07/04/14.
 */
public class GitFunctionalityTest extends InstrumentationTestCase {
    private GitFunctionality git;

    protected void setUp() throws Exception{
        super.setUp();
        GitFunctionality.getInstance();
        git = GitFunctionality.getInstance();
    }

    public void testGitLogin() throws Exception{
        assertTrue(git.gitLogin("navigitator","navi123"));
    }

    public void testGetRepos() throws Exception{
        String s = "";
        List<Repository> repos = git.getRepos();
        if (repos != null) {
            for (Repository repo : repos) {
                s += repo.getName() + ",";
            }
        }
        System.out.println(s);
        Log.d("Navigitator", s);
        assertNotSame("", s);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
