package model;

import java.util.Date;

public class TrafficLight implements ComponentType{

    private String group;
    private String groupId;
    private String id;
    private String state;
    private Date date;

    public TrafficLight(String group, String groupId, String id, String state) {
        this.id = id;
        this.state = state;
        this.groupId = groupId;
        this.group = group;
        this.date = new Date();
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

    public void resetDate(){
        this.date = new Date();
    }

    public Date getDate() {
        return date;
    }
}
