package com.EDA397.Navigator.Navigator;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Mei on 2014-03-31.
 * https://developer.github.com/v3/oauth/
 */

import com.jcabi.github.Github;
import com.jcabi.github.Issue;
import com.jcabi.github.Repo;
import com.jcabi.github.RtGithub;


public class GitLogin{

 /*   public static String OAUTH_URL = "https://github.com/login/oauth/authorize";
    public static String OAUTH_ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";

    public static String CLIENT_ID = "f0998780dd28c2d78122";
    public static String CLIENT_SECRET = "7a320bd177e2708b55bbbd07336ad96ffe4eea83";
    public static String CALLBACK_URL = "http://localhost";*/

    Github gh = new RtGithub("username","password");

    //Repo repo = gh.repo("jcabi/jcabi-github");
    //Issue issue = repo.issues().create("Hejhej");
    //issue.post("My first comment!");
    public void createAuth() {

//        String url = OAUTH_URL + "?client_id=" + CLIENT_ID;
//        //https://github.com/login/oauth/authorize?client_id=f0998780dd28c2d78122&client_secret=7a320bd177e2708b55bbbd07336ad96ffe4eea83&code=7e989154ca60baf7c0e4
//        //https://github.com/login/oauth/access_token?client_id=f0998780dd28c2d78122&client_secret=7a320bd177e2708b55bbbd07336ad96ffe4eea83&code=7e989154ca60baf7c0e4
//        //https://api.github.com/user?access_token=
//
//        String accessTokenFragment = "access_token=";
//        String accessCodeFragment = "code=";
//
//        // check if access token exist
//
//        if (url.contains(accessTokenFragment)) {
//            // the GET request contains directly the token
//
//            String accessToken = url.substring(url.indexOf(accessTokenFragment));
//
//        } else if(url.contains(accessCodeFragment)) {
//            // the GET request contains an authorization code
//            String accessCode = url.substring(url.indexOf(accessCodeFragment));
//
//            String query = "client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&code=" + accessCode;

        }

}

