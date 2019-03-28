package model;

import java.sql.Time;

public class TrafficLight {

    private String id;
    private String state;
    private String groupId;
    private Time time;

    public TrafficLight(String id, String state, String groupId, Time time) {
        this.id = id;
        this.state = state;
        this.groupId = groupId;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public String getGroupId() { return groupId; }

    public Time getTime() { return time; }
}
