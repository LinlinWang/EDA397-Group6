package com.EDA397.Navigator.Navigator;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.appcompat.*;
import android.support.v7.appcompat.R;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Mei on 2014-03-31.
 * https://developer.github.com/v3/oauth/
 */


import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.SearchRepository;
import org.eclipse.egit.github.core.service.RepositoryService;




public class GitLogin{

 /*   public static String OAUTH_URL = "https://github.com/login/oauth/authorize";
    public static String OAUTH_ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";

    public static String CLIENT_ID = "f0998780dd28c2d78122";
    public static String CLIENT_SECRET = "7a320bd177e2708b55bbbd07336ad96ffe4eea83";
    public static String CALLBACK_URL = "http://localhost";*/


    GitHubClient client = new GitHubClient();
    private static String TAG = "GitLogin";
    private String username;
    private String password;



    public void createAuth() {
        client.setCredentials("kohsuke","012345678");




    }

}

