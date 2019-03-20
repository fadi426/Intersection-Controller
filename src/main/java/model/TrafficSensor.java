package model;

public class TrafficSensor {

    private String group;
    private String groupId;
    private String id;
    private String state;

    public TrafficSensor(String group, String groupId, String id, String state) {
        this.group = group;
        this.groupId = groupId;
        this.id = id;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public String getGroup() {
        return group;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
