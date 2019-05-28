package scheduler;

import controller.TrafficController;
import controller.TrafficLightController;
import controller.TrafficSensorController;
import model.TrafficLight;
import model.TrafficSensor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class GreenLightScheduler {

    private TrafficController trafficController;
    private TrafficSensorController trafficSensorController;
    private List<TrafficSensor> trafficSensorList;
    private List<TrafficLight> trafficLightList;
    private List<TrafficLight> redLightArr;
    private List<TrafficLight> greenLightArr = new ArrayList<>();
    private TrafficLightController trafficLightController;

    private int vehicleRotationCounter;

    public GreenLightScheduler(TrafficController trafficController){
        this.trafficController = trafficController;
        this.trafficSensorController = trafficController.getTrafficSensorController();
        this.trafficLightController = trafficController.getTrafficLightController();
        this.trafficSensorList = trafficController.getTrafficSensorList();
        this.trafficLightList = trafficController.getTrafficLightList();
        this.redLightArr = trafficController.getRedLightArr();
        this.vehicleRotationCounter = trafficController.getVehicleRotationCounter();
    }

    public TimerTask scheduler = new TimerTask() {
        @Override
        public void run() {
            if (trafficSensorController == null) {
                return;
            }
            Date date = new Date();
            List<TrafficSensor> tempTrafficSensors = new ArrayList<>();
            tempTrafficSensors.addAll(trafficSensorController.getTrafficSensorList());
            trafficSensorList = tempTrafficSensors.stream()
                    .filter(sensor -> sensor.getState().equals("1"))
                    .collect(Collectors.toList());


            trafficLightList.clear();
            redLightArr.clear();
            for (TrafficSensor sensor : trafficSensorList) {
                for (TrafficLight light : trafficLightController.getTrafficLights()) {
                    if (sensor.getGroupId().equals(light.getGroupId()) && sensor.getGroup().equals(light.getGroup()) && sensor.getId().equals(light.getId())) {
                        trafficLightList.add(light);
                    }
                }
            }

            detectBridgeJam();
            addCorrespondingGroup(trafficLightList);

            List<TrafficLight> toDeleteGreenLights = new ArrayList<>();
            if (vehicleRotationCounter < 5) {
                for (TrafficLight light : trafficLightList) {
                    if (light.getGroup().equals("foot"))
                        toDeleteGreenLights.add(light);
                }
                trafficLightList.removeAll(toDeleteGreenLights);
            }
            else
                vehicleRotationCounter = 0;

            if (trafficLightList.size() > 0) {

                List<Long> lightTimes = new ArrayList<>();
                for (TrafficLight light : trafficLightList) {
                    long diff = date.getTime() - light.getDate().getTime();
                    long diffInSeconds = diff / 1000;
                    lightTimes.add(diffInSeconds);
                }

                List<TrafficLight> priorityGroup = new ArrayList<>();
                priorityGroup.add(trafficController.findPriorityLight(lightTimes));

                List<TrafficLight> sortedLightArr = trafficController.sortLightArr(lightTimes);
                for (TrafficLight light : sortedLightArr) {
                    trafficController.addAvailableLight(priorityGroup, light);
                }

                for (TrafficLight light : priorityGroup) {
                    greenLightArr.add(light);
                    light.resetDate();
                }
                priorityGroup.clear();

                for (TrafficLight light : greenLightArr) {
                    if (!light.getGroup().equals("vessel")) {
                        trafficController.sendMessage(light, "2");
                        redLightArr.add(light);
                    }
                }
                greenLightArr.clear();
                vehicleRotationCounter++;
            }
        }
    };

    public void resetScheduler(){
        greenLightArr.clear();
    }

    public void detectBridgeJam() {
        List<TrafficLight> lightsToExclude = new ArrayList<>();
        for (TrafficSensor sensor : trafficSensorList) {
            if (sensor.getGroup().equals("motor_vehicle") && sensor.getGroupId().equals("14")) {
                for (TrafficLight light : trafficLightController.getBridgeExcludedLights()) {
                    if (trafficLightList.contains(light))
                        lightsToExclude.add(light);
                }
            }
        }
        trafficLightList.removeAll(lightsToExclude);
    }

    public void addCorrespondingGroup(List<TrafficLight> lights){
        List<TrafficLight> toAddList = new ArrayList<>();
        for (TrafficLight light : lights){
            for (List<TrafficLight> groups : trafficLightController.getFootGroup()){
                if (groups.contains(light)){
                    for (TrafficLight groupLight : groups){
                        if (!lights.contains(groupLight))
                            toAddList.add(groupLight);
                    }
                }
            }
        }
        lights.addAll(toAddList);
    }
}
