package model;

public class ComponentType {

    private String group;
    private String id;
    private String state;

    ComponentType() {

    }

    public ComponentType(String group, String sensorId, String sensorValue) {
        this.id = id;
        this.group = group;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public String getgroup() {
        return group;
    }

    public String getState() {
        return state;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setgroup(String group) {
        this.group = group;
    }

    public void setState(String state) {
        this.state = state;
    }
}
