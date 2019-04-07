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
    List<List<TrafficLight>> cycleGroups = initTrafficCases.getCyleGroups();
    private MqttClient mqttClient;
    private String mainTopic;

    private Timer timer = new Timer();
    private int switchTime = 7000;

    public void run(){
        timer.schedule(orangeLightScheduler,4000,switchTime);
        timer.schedule(redLightScheduler,5000,switchTime);
        timer.schedule(greenLightScheduler,0,switchTime);

//        try {
//            Thread.sleep(switchTime);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        timer.schedule(task2, 0, switchTime);
    }

    TimerTask greenLightScheduler = new TimerTask() {
        @Override
        public void run() {
            if (trafficSensorController == null){
                return;
            }
            List<TrafficSensor> trafficSensorList = trafficSensorController.getTrafficSensorList();

            if (trafficSensorList.size() > 0 ) {
                for (TrafficSensor sensor : trafficSensorList) {
                    if (sensor.getState().equals("1"))
                        greenLightArr.add(sensor);
                }
                if (greenLightArr.size() > 0) {

                    List<Integer> groupScores = new ArrayList<>();

                    for (int i = 0; i < groups.size(); i++){
                        int score = 0;
                        for (TrafficLight light : groups.get(i)){
                            for (TrafficSensor sensor : greenLightArr) {
                                //if (sensor.getGroupId().equals(light.getGroupId()) && sensor.getGroup().equals(light.getGroup())) {
                                    score = score + light.getScore();
                                //}
                            }
                        }
                        groupScores.add(score);
                    }

                    List<List<TrafficLight>> priorityGroups = new ArrayList<>();
                    int highestScore = getMax(groupScores);
                    for (int score : groupScores){
                        if (score == highestScore){
                         priorityGroups.add(groups.get(groupScores.indexOf(score)));
                        }
                    }
                    int randomInt = new Random().nextInt(priorityGroups.size());
                    List<TrafficLight> priorityGroup = priorityGroups.get(randomInt);

                    for (TrafficSensor sensor : greenLightArr) {
                        for (TrafficLight light : priorityGroup){
                            if (sensor.getGroupId().equals(light.getGroupId()) && sensor.getGroup().equals(light.getGroup()) && sensor.getId().equals(light.getId())) {
                                String publishMsg = mainTopic + "/" + sensor.getGroup() + "/" + sensor.getGroupId() + "/" + "light/" + sensor.getId();
                                publishMessage(publishMsg, "2");
                                redLightArr.add(sensor);
                                light.substractToScore(1);
                            }
                        }
                    }
                    for (List<TrafficLight> group : cycleGroups) {
                        Boolean exsist = false;
                        TrafficSensor lightSensor = null;
                        TrafficLight C1L1 = new TrafficLight("cycle", "1", "1", "0", 0);
                        for (TrafficSensor sensor : redLightArr) {
                            for (TrafficLight light : group) {
                                if ( sensor.getGroup().equals(light.getGroup()) &&sensor.getGroupId().equals(light.getGroupId()) && sensor.getId().equals(light.getId())) {
                                    exsist = true;
                                }
                            }
                        }
                        for (TrafficSensor sensor : greenLightArr){
                            if ( sensor.getGroup().equals(C1L1.getGroup()) && sensor.getGroupId().equals(C1L1.getGroupId()) &&  sensor.getId().equals(C1L1.getId())) {
                                lightSensor = sensor;
                            }
                        }
                        if (!exsist && lightSensor != null){
                            if (lightSensor.getState().equals("1")) {
                                String publishMsg = mainTopic + "/" + lightSensor.getGroup() + "/" + lightSensor.getGroupId() + "/" + "light/" + lightSensor.getId();
                                publishMessage(publishMsg, "2");
                                redLightArr.add(lightSensor);
                            }
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
                    String publishMsg = mainTopic + "/" + sensor.getGroup() + "/" + sensor.getGroupId() + "/" + "light/" + sensor.getId();
                    publishMessage(publishMsg, "1");
                }
            }
        }
    };

    TimerTask redLightScheduler = new TimerTask() {
        @Override
        public void run() {
            if (redLightArr.size() > 0) {
                for (TrafficSensor sensor : redLightArr) {
                    String publishMsg = mainTopic + "/" + sensor.getGroup() + "/" + sensor.getGroupId() + "/" + "light/" + sensor.getId();
                    publishMessage(publishMsg, "0");
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
}
