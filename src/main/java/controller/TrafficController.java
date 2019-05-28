package controller;

import model.TrafficLight;
import model.TrafficSensor;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import scheduler.BridgeScheduler;
import scheduler.GreenLightScheduler;
import scheduler.OrangeLightScheduler;
import scheduler.RedLightScheduler;

import java.util.*;

public class TrafficController extends Thread {
    private List<TrafficLight> redLightArr = new ArrayList<>();
    private TrafficSensorController trafficSensorController;
    private TrafficLightController trafficLightController = new TrafficLightController();
    private List<TrafficSensor> trafficSensorList = new ArrayList<>();
    private List<TrafficLight> trafficLightList = new ArrayList<>();

    private MqttClient mqttClient;
    private String mainTopic;

    private Timer timer = new Timer();
    private int vehicleRotationCounter = 0;

    private GreenLightScheduler greenLightScheduler;
    private OrangeLightScheduler orangeLightScheduler;
    private RedLightScheduler redLightScheduler;
    private BridgeScheduler bridgeScheduler;

    TrafficController(TrafficSensorController trafficSensorController, MqttClient mqttClient, String mainTopic) {
        this.trafficSensorController = trafficSensorController;
        this.mqttClient = mqttClient;
        this.mainTopic = mainTopic;
        this.greenLightScheduler = new GreenLightScheduler(this);
        this.orangeLightScheduler = new OrangeLightScheduler(this);
        this.redLightScheduler = new RedLightScheduler(this);
        this.bridgeScheduler = new BridgeScheduler(this);
    }

    public void run() {
        timer.schedule(greenLightScheduler.scheduler, 0, 13000);
        timer.schedule(orangeLightScheduler.footScheduler, 6000, 13000);
        timer.schedule(orangeLightScheduler.cycleScheduler, 8000, 13000);
        timer.schedule(orangeLightScheduler.vehicleScheduler, 6000, 13000);
        timer.schedule(redLightScheduler.footScheduler, 12000, 13000);
        timer.schedule(redLightScheduler.cycleScheduler, 10000, 13000);
        timer.schedule(redLightScheduler.vehicleScheduler, 10000, 13000);
        timer.schedule(bridgeScheduler.bridgeRegulator, 0, 1000);
        timer.schedule(bridgeScheduler.openBridge, 0, 1000);
        timer.schedule(bridgeScheduler.closeBridge, 0, 2000);
    }

    public void publishMessage(String topic, String content) {
        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(1);
        try {
            mqttClient.publish(topic, message);
        } catch (MqttException e) {
            System.out.println("Failed to publish message");
        }
    }

    public static Long getMax(List<Long> inputArray) {
        Long maxValue = inputArray.get(0);
        for (int i = 1; i < inputArray.size(); i++) {
            if (inputArray.get(i) > maxValue) {
                maxValue = inputArray.get(i);
            }
        }
        return (maxValue);
    }

    public void sendMessage(TrafficLight light, String mode) {
        String publishMsg = "";
        if (trafficLightController.getGateGroup().contains(light))
            publishMsg = mainTopic + "/" + light.getGroup() + "/" + light.getGroupId() + "/" + "gate/" + light.getId();
        else
            publishMsg = mainTopic + "/" + light.getGroup() + "/" + light.getGroupId() + "/" + "light/" + light.getId();


        light.setState(mode);
        publishMessage(publishMsg, mode);
    }

    public void addAvailableLight(List<TrafficLight> lights, TrafficLight light) {
        if (lights.contains(light))
            return;

        for (TrafficLight l : lights) {
            for (int i = 0; i < trafficLightController.getTrafficLights().size(); i++) {
                TrafficLight groupLight = trafficLightController.getTrafficLights().get(i);
                if (groupLight.getGroupId().equals(l.getGroupId()) && groupLight.getGroup().equals(l.getGroup()) && groupLight.getId().equals(l.getId())) {

                    if (trafficLightController.getGroups().get(i).contains(light))
                        return;

                    if (trafficLightController.getBridgeGroup().contains(light)) {
                        return;
                    }
                }
            }
        }

        lights.add(light);
    }

    public TrafficLight findPriorityLight(List<Long> lightTimes) {
        List<TrafficLight> priorityGroups = new ArrayList<>();
        Long longestWaitingTime = getMax(lightTimes);
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

    public List<TrafficLight> sortLightArr(List<Long> times) {
        List<TrafficLight> sortedLights = new ArrayList<>();

        List<Long> tempTimes = new ArrayList<>();
        tempTimes.addAll(times);
        List<Long> temppTimes = new ArrayList<>();

        for (Long t : tempTimes) {
            Long longestTime = getMax(tempTimes);
            for (int i = 0; i < tempTimes.size(); i++) {
                Long time = tempTimes.get(i);
                if (time == longestTime) {
                    sortedLights.add(trafficLightList.get(i));
                    temppTimes.add(time);
                    tempTimes.set(i, 0L);
                    break;
                }
            }
        }

        return sortedLights;
    }

    public TrafficLightController getTrafficLightController() {
        return trafficLightController;
    }

    public TrafficSensorController getTrafficSensorController() {
        return trafficSensorController;
    }

    public List<TrafficSensor> getTrafficSensorList() {
        return trafficSensorList;
    }

    public List<TrafficLight> getTrafficLightList() {
        return trafficLightList;
    }

    public List<TrafficLight> getRedLightArr() {
        return redLightArr;
    }

    public int getVehicleRotationCounter() {
        return vehicleRotationCounter;
    }

    public String getMainTopic(){
        return mainTopic;
    }

    public void resetThread(){
        trafficSensorController.getTrafficSensorList().clear();
        redLightArr.clear();
        trafficSensorList.clear();
        trafficLightList .clear();
        vehicleRotationCounter = 0;
        greenLightScheduler.resetScheduler();
        bridgeScheduler.resetScheduler();

    }

    public void sendAllLightValues(){
        trafficLightController.getTrafficLights().stream()
                .forEach(light -> sendMessage(light, light.getState()));

        trafficLightController.getBridgeGroup().stream()
                .forEach(light -> sendMessage(light, light.getState()));

        trafficLightController.getGateGroup().stream()
                .forEach(light -> sendMessage(light, light.getState()));
    }
}
