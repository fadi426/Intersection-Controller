package model;

public class TrafficLight {

    private int id;
    private int state;

    public TrafficLight(int id, int state) {
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
