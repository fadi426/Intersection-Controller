package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TrafficLight implements ComponentType{

    private String group;
    private String groupId;
    private String id;
    private String state;
    private Date date;
    private List<String> sensorIds = new ArrayList<>();

    public TrafficLight(String group, String groupId, String id, String state, List<String> sensorIds) {
        this.id = id;
        this.state = state;
        this.groupId = groupId;
        this.group = group;
        this.date = new Date();
        this.sensorIds = sensorIds;
    }

    public String getId() {
        return id;
    }

    public String getGroupId() { return groupId; }

    public String getGroup() {
        return group;
    }

    public String getState() {
        return state;
    }

    public void setId(String id) { this.id = id; }

    public void setGroup(String group) { this.group = group; }

    public void setGroupId(String groupId) { this.groupId = groupId; }

    public void setState(String state) {
        this.state = state;
    }

    public void resetDate(){
        this.date = new Date();
    }

    public Date getDate() {
        return date;
    }

    public List<String> getSensorIds() {
        return sensorIds;
    }
}
