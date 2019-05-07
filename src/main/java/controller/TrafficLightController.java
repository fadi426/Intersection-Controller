package controller;

import model.TrafficLight;
import model.TrafficSensor;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.*;

public class TrafficLightController extends Thread {
    List<TrafficSensor> greenLightArr = new ArrayList<>();
    List<TrafficSensor> redLightArr = new ArrayList<>();
    private TrafficSensorController trafficSensorController;
    private InitTrafficCases initTrafficCases = new InitTrafficCases();
    List<List<TrafficLight>> groups = initTrafficCases.getGroups();
    private MqttClient mqttClient;
    private String mainTopic;

    private Timer timer = new Timer();
    private int switchTime = 8000;

    public void run(){


        timer.schedule(orangeLightScheduler,6000,switchTime);
        timer.schedule(redLightScheduler,7000,switchTime);
        timer.schedule(greenLightScheduler,0,switchTime);
    }

    TimerTask greenLightScheduler = new TimerTask() {
        @Override
        public void run() {
            if (trafficSensorController == null){
                return;
            }
            List<TrafficSensor> trafficSensorList = trafficSensorController.getTrafficSensorList();
            List<TrafficSensor> highSensorArr = new ArrayList<>();
            List<List<TrafficLight>> availableSensorArr = new ArrayList<>();

            if (trafficSensorList.size() > 0 ) {
                for (TrafficSensor sensor : trafficSensorList) {
                    if (sensor.getState().equals("1"))
                        highSensorArr.add(sensor);
                }
                if (highSensorArr.size() > 0) {

                    List<Integer> lightScore = new ArrayList<>();
                    for (TrafficSensor sensor : highSensorArr){
                        for (int i = 0; i < initTrafficCases.getTrafficLights().size(); i++) {
                            TrafficLight light = initTrafficCases.getTrafficLights().get(i);
                            if (sensor.getGroupId().equals(light.getGroupId()) && sensor.getGroup().equals(light.getGroup()) && sensor.getId().equals(light.getId())){
                                int score = initTrafficCases.getTrafficLights().get(i).getScore();
                                lightScore.add(score);
                                availableSensorArr.add(groups.get(i));
                            }
                        }
                    }
                    List<List<TrafficLight>> priorityGroups = new ArrayList<>();

                    if (lightScore.size() == 0){
                        return;
                    }

                    int highestScore = getMax(lightScore);
                    for( int i = 0; i < lightScore.size(); i++) {
                        int score = lightScore.get(i);
                        if (score == highestScore) {
                            priorityGroups.add(availableSensorArr.get(i));
                        }
                    }
                    int randomInt = new Random().nextInt(priorityGroups.size());
                    List<TrafficLight> priorityGroup = new ArrayList<>();

                    for (TrafficLight light : priorityGroups.get(randomInt)){
                        priorityGroup.add(light);
                    }

                    for (TrafficSensor sensor : highSensorArr) {
                        containsTL(priorityGroup, sensor);
                    }

                    boolean vessel = false;
                    for (TrafficSensor s : greenLightArr){
                        if (s.getGroup().equals("vessel")){
                            vessel = true;
                        }
                    }
                    // TODO make a more generic way of executing the color changes for the exceptions
                    List<TrafficSensor> tempGreenLightArr = new ArrayList<>();
                    List<TrafficSensor> tempRedLightArr = new ArrayList<>();
                    if (!vessel){
                        for (TrafficLight light : initTrafficCases.getExceptionGroup()){
                            for (TrafficSensor sensor : trafficSensorList) {
                                if (sensor.getGroup().equals(light.getGroup()) && sensor.getGroupId().equals(light.getGroupId()) && sensor.getId().equals(light.getId())) {
                                    if (!greenLightArr.contains(sensor))
                                        tempGreenLightArr.add(sensor);
                                }
                            }
                        }
                    }
                    else {
                        for (TrafficLight light : initTrafficCases.getExceptionGroup()){
                            for (TrafficSensor sensor : trafficSensorList) {
                                if (sensor.getGroup().equals(light.getGroup()) && sensor.getGroupId().equals(light.getGroupId()) && sensor.getId().equals(light.getId())) {
                                    tempRedLightArr.add(sensor);
                                }
                            }
                        }
                    }
                    for (TrafficSensor sensor : tempGreenLightArr){
                        sendMessage(sensor, "2");
                    }

                    for (TrafficSensor sensor : tempRedLightArr){
                        sendMessage(sensor, "0");
                    }

                    for (TrafficSensor sensor : greenLightArr){
                        sendMessage(sensor, "2");
                        redLightArr.add(sensor);
                    }
                }
            }
            greenLightArr.clear();
        }
    };

    TimerTask orangeLightScheduler = new TimerTask() {
        @Override
        public void run() {
            if (redLightArr.size() > 0) {
                for (TrafficSensor sensor : redLightArr) {
                    if (!sensor.getGroup().equals("vessel"))
                        sendMessage(sensor, "1");
                }
            }
        }
    };

    TimerTask redLightScheduler = new TimerTask() {
        @Override
        public void run() {
            if (redLightArr.size() > 0) {
                for (TrafficSensor sensor : redLightArr) {
                    sendMessage(sensor, "0");
                }
            }
            redLightArr.clear();
        }
    };

    public void publishMessage(String topic, String content){
        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(1);
        try {
            mqttClient.publish(topic, message);
            //System.out.println("Published message: " + content + " Published topic = " + topic);
        } catch (MqttException e) {
            System.out.println("Failed to publish message");
        }
    }

    public void setInfoThread(TrafficSensorController trafficSensorController, MqttClient mqttClient, String mainTopic) {
        this.trafficSensorController = trafficSensorController;
        this.mqttClient = mqttClient;
        this.mainTopic = mainTopic;
        trafficSensorController.addExceptions(initTrafficCases.getExceptionGroup());
    }

    public static int getMax(List<Integer> inputArray){
        int maxValue = inputArray.get(0);
        for(int i=1;i < inputArray.size();i++){
            if(inputArray.get(i) > maxValue){
                maxValue = inputArray.get(i);
            }
        }
        return (maxValue);
    }

    public void sendMessage(TrafficSensor sensor, String mode) {
        List<String> doubleLight = new ArrayList<>(Arrays.asList("5", "7", "10", "13"));


        if (doubleLight.contains(sensor.getGroupId()) || sensor.getGroup().equals("foot")) {
            String publishMsg = mainTopic + "/" + sensor.getGroup() + "/" + sensor.getGroupId() + "/" + "light/" + "1";
            publishMessage(publishMsg, mode);

            publishMsg = mainTopic + "/" + sensor.getGroup() + "/" + sensor.getGroupId() + "/" + "light/" + "2";
            publishMessage(publishMsg, mode);
        }
        else{
            String publishMsg = mainTopic + "/" + sensor.getGroup() + "/" + sensor.getGroupId() + "/" + "light/" + sensor.getId();
            publishMessage(publishMsg, mode);
        }

    }

    public void containsTL(List<TrafficLight> lights, TrafficSensor sensor) {
        for (TrafficLight light : lights) {
            if (sensor.getGroupId().equals(light.getGroupId()) && sensor.getGroup().equals(light.getGroup()) && sensor.getId().equals(light.getId())) {
                return;
            }
        }


//        List<String> groupIdArr = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8"));
//        List<String> sensorIdArr = new ArrayList<>(Arrays.asList("1", "2", "1", "2", "1", "2", "1", "2"));
//        int[][] multi = new int[][]{
//                { 1, 2},
//                { 1, 2},
//                { 3, 4},
//                { 3, 4},
//                { 5, 6},
//                { 5, 6},
//                { 7, 8},
//                { 7, 8}
//        };
//
//        if (sensor.getGroup().equals("foot") && groupIdArr.contains(sensor.getGroupId()) && sensorIdArr.contains(sensor.getId())) {
//            for(int i = 0; i < groupIdArr.size(); i++){
//                if (groupIdArr.get(i).equals(sensor.getGroupId())){
//
//                }
//            }
//        }

        for (int i = 0; i < initTrafficCases.getTrafficLights().size(); i++) {
            TrafficLight light = initTrafficCases.getTrafficLights().get(i);
            if (sensor.getGroupId().equals(light.getGroupId()) && sensor.getGroup().equals(light.getGroup()) && sensor.getId().equals(light.getId())) {
                light.substractToScore(1);
                greenLightArr.add(sensor);

                for (TrafficLight tl : groups.get(i)) {
                    if (!lights.contains(tl))
                        lights.add(tl);
                }
            }
        }
    }
}
