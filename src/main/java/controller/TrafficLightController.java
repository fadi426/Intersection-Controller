package controller;

import model.ComponentType;
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
    List<TrafficSensor> trafficSensorList = new ArrayList<>();
    List<TrafficLight> priorityGroup2 = new ArrayList<>();

    private MqttClient mqttClient;
    private String mainTopic;

    private Timer timer = new Timer();
    private int switchTime = 9000;

    public void run(){
        timer.schedule(orangeLightScheduler,7000,switchTime);
        timer.schedule(redLightScheduler,8000,switchTime);
        timer.schedule(greenLightScheduler,0,switchTime);
        timer.schedule(bridgeScheduler,0,switchTime*2 +250);
    }

    TimerTask greenLightScheduler = new TimerTask() {
        @Override
        public void run() {
            if (trafficSensorController == null){
                return;
            }
            trafficSensorList = trafficSensorController.getTrafficSensorList();
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
                    priorityGroup.addAll(priorityGroup2);
                    for (TrafficLight light : priorityGroups.get(randomInt)){
                        priorityGroup.add(light);
                    }

                    for (TrafficSensor sensor : highSensorArr) {
                        containsTL(priorityGroup, sensor);
                    }
                    priorityGroup.clear();

                    for (TrafficSensor sensor : greenLightArr){
                        if (!sensor.getGroup().equals("vessel")){
                            sendMessage(sensor, "2");
                            redLightArr.add(sensor);
                        }
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


    TimerTask bridgeScheduler = new TimerTask() {
        @Override
        public void run() {
            // TODO make a more generic way of executing the color changes for the exceptions
            List<ComponentType> tempGreenLightArr = new ArrayList<>();
            List<ComponentType> tempRedLightArr = new ArrayList<>();

            List<TrafficSensor> tempTrafficSensorList = new ArrayList<>();
            tempTrafficSensorList.addAll(trafficSensorList);
            for (TrafficSensor sensor : tempTrafficSensorList) {
                if (sensor.getGroup().equals("vessel") && sensor.getState().equals("1")) {
                    tempGreenLightArr.add(sensor);
                    if (tempRedLightArr.size() <1)
                        tempRedLightArr.addAll(initTrafficCases.getExceptionGroup());
                        priorityGroup2.clear();
                }
                else if (sensor.getGroup().equals("vessel") && sensor.getState().equals("0")) {
                    tempRedLightArr.add(sensor);
                    if (tempGreenLightArr.size() <1)
                        tempGreenLightArr.addAll(initTrafficCases.getExceptionGroup());
                        priorityGroup2.addAll(initTrafficCases.getExceptionGroup());
                }
            }

            if (tempGreenLightArr.size() < 1 || tempRedLightArr.size() < 1)
                return;

            for (ComponentType componentType : tempRedLightArr){
                sendMessage(componentType, "0");
            }

            for (ComponentType componentType : tempGreenLightArr){
                sendMessage(componentType, "2");
            }
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

    public void sendMessage(ComponentType componentType, String mode) {
        List<String> doubleLight = new ArrayList<>(Arrays.asList("5", "7", "10", "13"));

        if (doubleLight.contains(componentType.getGroupId()) || componentType.getGroup().equals("foot") || (componentType.getGroup().equals("motor_vehicle") && Integer.parseInt(componentType.getId()) > 1)) {
            String publishMsg = mainTopic + "/" + componentType.getGroup() + "/" + componentType.getGroupId() + "/" + "light/" + "1";
            publishMessage(publishMsg, mode);

            publishMsg = mainTopic + "/" + componentType.getGroup() + "/" + componentType.getGroupId() + "/" + "light/" + "2";
            publishMessage(publishMsg, mode);
        }
        else{
            String publishMsg = mainTopic + "/" + componentType.getGroup() + "/" + componentType.getGroupId() + "/" + "light/" + componentType.getId();
            publishMessage(publishMsg, mode);
        }

    }

    public void containsTL(List<TrafficLight> lights, TrafficSensor sensor) {
        for (TrafficLight light : lights) {
            if (sensor.getGroupId().equals(light.getGroupId()) && sensor.getGroup().equals(light.getGroup()) && sensor.getId().equals(light.getId())) {
                return;
            }
        }

        for (int i = 0; i < initTrafficCases.getTrafficLights().size(); i++) {
            TrafficLight light = initTrafficCases.getTrafficLights().get(i);
            if (sensor.getGroupId().equals(light.getGroupId()) && sensor.getGroup().equals(light.getGroup()) && sensor.getId().equals(light.getId())) {
                light.substractToScore(1);
                if (!initTrafficCases.getExceptionGroup().contains(light))
                    greenLightArr.add(sensor);

                for (TrafficLight tl : groups.get(i)) {
                    if (!lights.contains(tl))
                        lights.add(tl);
                }
            }
        }
    }
}
