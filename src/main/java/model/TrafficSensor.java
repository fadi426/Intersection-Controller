package model;

public class TrafficSensor {

    private int id;
    private int state;

    public TrafficSensor(int id, int state) {
        this.id = id;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public int getState() {
        return state;
    }
}
