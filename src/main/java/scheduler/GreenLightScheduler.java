package scheduler;

import controller.TrafficController;
import controller.TrafficLightController;
import controller.TrafficSensorController;
import model.TrafficLight;
import model.TrafficSensor;

import java.util.*;
import java.util.stream.Collectors;

public class GreenLightScheduler {

    private TrafficController trafficController;
    private TrafficSensorController trafficSensorController;
    private List<TrafficSensor> trafficSensorList;
    private List<TrafficLight> trafficLightList;
    private List<TrafficLight> redLightArr;
    private List<TrafficLight> greenLightArr = new ArrayList<>();
    private TrafficLightController trafficLightController;

    public GreenLightScheduler(TrafficController trafficController){
        this.trafficController = trafficController;
        this.trafficSensorController = trafficController.getTrafficSensorController();
        this.trafficLightController = trafficController.getTrafficLightController();
        this.trafficSensorList = trafficController.getTrafficSensorList();
        this.trafficLightList = trafficController.getTrafficLightList();
        this.redLightArr = trafficController.getRedLightArr();
    }

    /**
     * A timertask that checks which one of the sensors are high, and turn lights green depending on the time and priority system.
     * The longer the light has been on red, the more priority it gets.
     * The more sensors of that light have been triggered, the more priority it gets
     */
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
                    if (sensor.getGroupId().equals(light.getGroupId()) && sensor.getGroup().equals(light.getGroup()) && light.getSensorIds().contains(sensor.getId())) {
                        for (TrafficLight l : trafficLightController.findDoubleLight(light)) {
                            if (trafficLightList.contains(l))
                                continue;
                            trafficLightList.add(l);
                        }
                    }
                }
            }

            detectBridgeJam();

            if (trafficLightList.size() > 0) {

                List<Long> lightTimes = new ArrayList<>();
                for (TrafficLight light : trafficLightList) {
                    long diff = date.getTime() - light.getDate().getTime();
                    long diffInSeconds = diff / 1000;
                    long extraPriorityTime = calculatePriorityPoints(light)*20;
                    lightTimes.add(diffInSeconds  + extraPriorityTime);
                }

                List<TrafficLight> priorityGroup = new ArrayList<>();

                TrafficLight priorityLight = findPriorityLight(lightTimes);
                trafficController.addLight(priorityLight, priorityGroup);

                List<TrafficLight> sortedLightArr = trafficController.sortLightArr(lightTimes);
                for (TrafficLight light : sortedLightArr) {
                    addAvailableLight(priorityGroup, light);
                }

                for (TrafficLight light : priorityGroup) {
                    greenLightArr.add(light);
                    light.resetDate();
                }
                priorityGroup.clear();

                for (TrafficLight light : greenLightArr) {
                    if (!light.getGroup().equals("vessel")) {
                        trafficController.sendTrafficCommand(light, "2");
                        redLightArr.add(light);
                    }
                }
                greenLightArr.clear();
            }
        }
    };

    /**
     * Resets this specific scheduler
     */
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

    /**
     * @param light is the light where the points will be given to
     * @return the amount of points(time) that will be added to the waiting time to increase the pickrate of that light by the timertask
     */
    public int calculatePriorityPoints(TrafficLight light) {
        int priorityPoints = 0;

        List<TrafficSensor> tempTrafficSensorList = new ArrayList<>();
        tempTrafficSensorList.addAll(trafficSensorList);
        for (TrafficSensor sensor : tempTrafficSensorList){
            if (sensor.getGroupId().equals(light.getGroupId()) && sensor.getGroup().equals(light.getGroup()) && light.getSensorIds().contains(sensor.getId()))
                priorityPoints++;
        }
        return priorityPoints;
    }

    /**
     * @param lights are the lights where other available lights will be added to, those sensors of the lights have to be in a high state and should not be in the excluded group of those lights
     * @param light is the light that has the possibillity to be added to the lights array.
     */
    public void addAvailableLight(List<TrafficLight> lights, TrafficLight light) {
        if (lights.contains(light))
            return;

        for (TrafficLight l : lights) {
            for (int i = 0; i < trafficLightController.getTrafficLights().size(); i++) {
                TrafficLight groupLight = trafficLightController.getTrafficLights().get(i);
                if (groupLight.getGroupId().equals(l.getGroupId()) && groupLight.getGroup().equals(l.getGroup()) && groupLight.getId().equals(l.getId())) {

                    if (trafficLightController.getExcludedGroups().get(i).contains(light))
                        return;

                    if (trafficLightController.getBridgeGroup().contains(light)) {
                        return;
                    }
                }
            }
        }
        trafficController.addLight(light, lights);
    }

    /**
     * @param lightTimes A list of waiting times of the lights
     * @return the light with the highest priority or waiting time
     */
    public TrafficLight findPriorityLight(List<Long> lightTimes) {
        List<TrafficLight> priorityGroups = new ArrayList<>();
        Long longestWaitingTime = trafficController.getMax(lightTimes);
        for (int i = 0; i < lightTimes.size(); i++) {
            Long time = lightTimes.get(i);
            if (time == longestWaitingTime) {
                priorityGroups.add(trafficLightList.get(i));
            }
        }
        int randomInt = new Random().nextInt(priorityGroups.size());
        TrafficLight priorityLight = priorityGroups.get(randomInt);
        return priorityLight;
    }
}
