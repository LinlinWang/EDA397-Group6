package com.EDA397.Navigator.Navigator;


/**
 * Created by Mei on 2014-03-31.
 * https://developer.github.com/v3/oauth/
 */

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.SearchRepository;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.io.IOException;


public class GitLogin{


    GitHubClient client = new GitHubClient();
    private String username;
    private String password;


    public void createAuth() {
        client.setCredentials("username","password");
    }

    public void getRepo() {
        RepositoryService service = new RepositoryService();
        try{
            for (Repository repo : service.getRepositories(username))
                repo.getName();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}

