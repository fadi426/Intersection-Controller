package controller;

import model.TrafficLight;
import model.TrafficSensor;

import java.util.ArrayList;
import java.util.List;

public class TrafficSensorController {

    private List<TrafficSensor> trafficSensorList = new ArrayList<TrafficSensor>();
    private List<TrafficLight> trafficLightsList = new ArrayList<TrafficLight>();

    public void changeTrafficSensor(String group, String groupId, String sensorId, String sensorValue) {
        if (trafficSensorList.size() > 0 ){

            for (TrafficSensor sensor : trafficSensorList){
                if (sensor.getId().equals(sensorId) && sensor.getGroupId().equals(groupId)){
                    sensor.setState(sensorValue);
                }
                else {
                    trafficSensorList.add(new TrafficSensor(group, groupId, sensorId, sensorValue));
                }
            }
        }
        else {
            trafficSensorList.add(new TrafficSensor(group, groupId, sensorId, sensorValue));
        }
    }
    public List<TrafficSensor> getTrafficSensorList() {
        return trafficSensorList;
    }
}
