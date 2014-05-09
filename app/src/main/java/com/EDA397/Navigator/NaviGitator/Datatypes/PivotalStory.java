package com.EDA397.Navigator.NaviGitator.Datatypes;

/**
 * Created by sajfer on 2014-05-05.
 */
public class PivotalStory {

    public static enum StoryStatus
    {
        noStatus, unscheduled, unstarted, started, accepted, finished, delivered, rejected
    }

    private String name;
    private Integer id;
    private String description;
    private Integer owner;
    private StoryStatus status;

    public PivotalStory(String name, Integer id, String description, Integer owner, StoryStatus status){
        this.name = name;
        this.id = id;
        this.description = description;
        this.owner = owner;
        this.status = status;
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

    public void setStatus(StoryStatus status) {
        this.status = status;
    }

    public StoryStatus getStatus() {
        return status;
    }
}
