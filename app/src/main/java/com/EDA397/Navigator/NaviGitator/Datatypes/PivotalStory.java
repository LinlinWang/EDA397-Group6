package com.EDA397.Navigator.NaviGitator.Datatypes;

/**
 * Created by sajfer on 2014-05-05.
 */
public class PivotalStory {
    private String name;
    private Integer id;
    private String description;
    private Integer owner;

    public PivotalStory(String name, Integer id, String description, Integer owner){
        this.name = name;
        this.id = id;
        this.description = description;
        this.owner = owner;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
    }

    public Integer getOwner() {
        return owner;
    }
}
