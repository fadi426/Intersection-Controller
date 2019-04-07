package model;

import java.sql.Time;

public class TrafficLight {

    private String group;
    private String groupId;
    private String id;
    private String state;
    private int score;

    public TrafficLight(String group, String groupId, String id, String state, int score) {
        this.id = id;
        this.state = state;
        this.groupId = groupId;
        this.group = group;
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public String getGroupId() { return groupId; }

    public String getGroup() {
        return group;
    }

    public void substractToScore(int score){
        this.score = this.score - score;
    }

    public int getScore() {
        return score;
    }
}
