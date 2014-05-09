package com.EDA397.Navigator.NaviGitator.Datatypes;

/**
 * Created by sajfer on 2014-05-05.
 */
public class PivotalProject {
    private String name;
    private Integer id;

    public PivotalProject(String name, Integer id){
        this.name = name;
        this.id = id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
