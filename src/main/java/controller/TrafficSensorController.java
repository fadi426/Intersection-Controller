package controller;

import model.TrafficLight;
import model.TrafficSensor;

import java.util.*;

public class TrafficSensorController {

    private List<TrafficSensor> trafficSensorList = new ArrayList<TrafficSensor>();
    private List<TrafficSensor> tempTrafficSensorList = new ArrayList<TrafficSensor>();

    public void changeTrafficSensor(String group, String groupId, String sensorId, String sensorValue) {
        boolean exists = false;
        if (trafficSensorList.size() >0) {
                for (TrafficSensor sensor : trafficSensorList) {
                    if (sensor.getId().equals(sensorId) && sensor.getGroupId().equals(groupId)) {
                        sensor.setState(sensorValue);
                        exists = true;
                    }
                }
                if (!exists){
                    tempTrafficSensorList.add(new TrafficSensor(group, groupId, sensorId, sensorValue));
                }
            }
            else{
                tempTrafficSensorList.add(new TrafficSensor(group, groupId, sensorId, sensorValue));
            }
            trafficSensorList.addAll(tempTrafficSensorList);
            tempTrafficSensorList.clear();
    }
    public List<TrafficSensor> getTrafficSensorList() {
        return trafficSensorList;
    }

}
