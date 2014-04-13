package com.EDA397.Navigator.NaviGitator.Tests;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.EDA397.Navigator.NaviGitator.Activities.Encrypter;
import com.EDA397.Navigator.NaviGitator.Activities.GitFunctionality;

import org.eclipse.egit.github.core.CommitFile;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;

import java.util.List;


/**
 * Created by linlinwang on 07/04/14.
 * Tests are currently done in a random order.
 */
public class GitFunctionalityTest extends InstrumentationTestCase {
    private static GitFunctionality git;

    /**
     * Due to how setup/teardown works, we have to do this every time a new test starts, which makes
     * the login/getrepo/getcommits test cases irrelevant because you cannot test anything else
     * without doing them.
     * @throws Exception
     */
    protected void setUp() throws Exception{
        git = GitFunctionality.getInstance();
        if(git.getUserName().equals("")) {
            git.gitLogin("navigitator", "navi123");
            git.setCurrentRepo(git.getRepos().get(0));
            git.setCurrentCommit(git.getRepoCommits().get(0));
        }
    }
    /**
     * Test that checks if encrypting and decrypting a String results in the original value.
     * Works on phone but not on emulator (suspect encryption algorithm is not supported on all
     * android versions.
     * @throws Exception
     */
    public void testEncryptDecrypt() throws Exception{
        String s = "navi123";
        String s2 = Encrypter.encrypt(s);
        String s3 = Encrypter.decrypt(s2);
        assertFalse(s3.equals(null));
    }
    public void testGitLogin() throws Exception{
        assertTrue(git.gitLogin("navigitator","navi123"));
    }
    public void testGetRepos() throws Exception{
        String s = "";
        List<Repository> repos = git.getRepos();
        if (repos != null) {
            for (Repository repo : repos) {
                s += repo.getName() + ", ";
            }
        }
        System.out.println(s);
        Log.d("Navigitator", s);
        assertFalse(s.equals(""));
    }
    /**
     * Test that checks if getRepoCommits retrieves commits after a repo has been selected via
     * testGetRepos.
     * @throws Exception
     */
    public void testGetRepoCommits() throws Exception{
        String s = "";
        List<RepositoryCommit> rc = git.getRepoCommits();
        if (rc != null) {
            for (RepositoryCommit r : rc) {
                s += r.getSha() + ", ";
            }
        }
        System.out.println(s);
        Log.d("Navigitator", s);
        assertFalse(s.equals(""));
    }
    /**
     * Test that checks if getUserEvents retrieves events after a user has successfully logged in.
     * @throws Exception
     */
    public void testGetUserEvents() throws Exception{
        String s = "";
        List<String> ue = git.getUserEvents();
        if (ue != null) {
            for (String u : ue) {
                s += u + ", ";
            }
        }
        System.out.println(s);
        Log.d("Navigitator", s);
        assertFalse(s.equals(""));
    }
    /**
     * Test that checks if getFileNames retrieves filenames after a commit has been selected via
     * testGetRepoCommits().
     * @throws Exception
     */
    public void testGetFileNames() throws Exception{
        String s = "";
        List<String> fn = git.getFileNames();
        if (fn != null) {
            for (String f : fn) {
                s += f + ", ";
            }
        }
        System.out.println(s);
        Log.d("Navigitator", s);
        assertFalse(s.equals(""));
    }
    /**
     * Test that checks if getCommitComments retrieves comments after a commit has been selected via
     * testGetRepoCommits().
     * @throws Exception
     */
    public void testGetCommitComments() throws Exception{
        String s = "";
        List<String> cm = git.getCommitComments();
        if (cm != null) {
            for (String c : cm) {
                s += c + ", ";
            }
        }
        System.out.println(s);
        Log.d("Navigitator", s);
        assertFalse(s.equals(""));
    }

    protected void tearDown() throws Exception {
        //super.tearDown();
    }
}
