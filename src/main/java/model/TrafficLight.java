package model;

import java.sql.Time;

public class TrafficLight implements ComponentType{

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
    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getGroupId() { return groupId; }

    @Override
    public String getGroup() {
        return group;
    }

    public String getState() {
        return state;
    }

    @Override
    public void setId(String id) {

    }

    @Override
    public void setGroup(String group) {

    }

    @Override
    public void setGroupId(String groupId) {

    }

    public void substractToScore(int score){
        this.score = this.score - score;
    }

    public int getScore() {
        return score;
    }
}
