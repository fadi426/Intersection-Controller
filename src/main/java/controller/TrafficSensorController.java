package controller;

import model.TrafficSensor;

import java.util.*;

public class TrafficSensorController {

    private List<TrafficSensor> trafficSensorList = new ArrayList<TrafficSensor>();
    private List<TrafficSensor> tempTrafficSensorList = new ArrayList<TrafficSensor>();

    /**
     * Checks if the sensor is already in the trafficSensorList. It will add it to the list if its not in there and update it when it is in there already.
     * @param group the group of the sensor
     * @param groupId the groupID of the sensor
     * @param sensorId the sensorID of the sensor
     * @param sensorValue the state of the sensor
     */
    public void changeTrafficSensor(String group, String groupId, String sensorId, String sensorValue) {
        boolean exists = false;
        if (trafficSensorList.size() >0) {
                for (TrafficSensor sensor : trafficSensorList) {
                    if (sensor.getGroup().equals(group) && sensor.getId().equals(sensorId) && sensor.getGroupId().equals(groupId)) {
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

    /**
     * @return a list of all the sensors that have been received from the simulator
     */
    public List<TrafficSensor> getTrafficSensorList() {
        return trafficSensorList;
    }
}
