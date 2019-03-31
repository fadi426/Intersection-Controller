package controller;

import model.TrafficSensor;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.*;

public class TrafficLightController extends Thread {
    private List<String> sensorList = new ArrayList<>(Arrays.asList("0", "0"));
    private TrafficSensorController trafficSensorController;
    private MqttClient mqttClient;
    private String mainTopic;

    private Timer timer = new Timer();
    private int switchTime = 3000;
    TrafficSensor lastGreen = null;

    public void run(){
        timer.schedule(task,0,switchTime);
//        timer.schedule(task2, 0, switchTime+1000);
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            try {
                changeLights(mainTopic);
            } catch (InterruptedException e) {
                System.out.println("Failed to send message");
            }
        }
    };

//    TimerTask task2 = new TimerTask() {
//        @Override
//        public void run() {
//            if (greenLightArr.size() > 0) {
//                for (TrafficSensor sensor : greenLightArr) {
//                    String publishMsg = mainTopic + "/" + sensor.getGroup() + "/" + sensor.getGroupId() + "/" + "light/" + sensor.getId();
//                    publishMessage(publishMsg, "1");
//                }
//            }
//        }
//    };


    public void changeLights(String mainTopic) throws InterruptedException {

        if (trafficSensorController == null){
            return;
        }
        List<TrafficSensor> trafficSensorList = trafficSensorController.getTrafficSensorList();
        List<TrafficSensor> greenLightArr = new ArrayList<>();
        List<TrafficSensor> redLightArr = new ArrayList<>();

        if (trafficSensorList.size() > 0 ) {
            for (TrafficSensor sensor : trafficSensorList){
                if (sensor.getState().equals("1")) {
                    greenLightArr.add(sensor);
                }
                else if (sensor.getState().equals("0")) {
                    redLightArr.add(sensor);
                }
            }
            if (lastGreen != null){
                Thread.sleep(500);
                String publishMsg = mainTopic + "/" + lastGreen.getGroup() + "/" + lastGreen.getGroupId() + "/" + "light/" + lastGreen.getId();
                publishMessage(publishMsg, "1");
                lastGreen = null;
            }
            if (greenLightArr.size() > 0) {
                Random randomGenerator = new Random();
                int randomInt = randomGenerator.nextInt(greenLightArr.size()) + 1;

                TrafficSensor sensor = greenLightArr.get(randomInt - 1);
                String publishMsg = mainTopic + "/" + sensor.getGroup() + "/" + sensor.getGroupId() + "/" + "light/" + sensor.getId();
                publishMessage(publishMsg, "2");
                lastGreen = sensor;
                for (TrafficSensor s : greenLightArr) {
                    if (s != sensor)
                        redLightArr.add(s);
                }
                for (TrafficSensor s : redLightArr){
                    if (greenLightArr.contains(s))
                        greenLightArr.remove(s);
                }
            }
            if (redLightArr.size() > 0) {
                for (TrafficSensor sensor : redLightArr) {
                    String publishMsg = mainTopic + "/" + sensor.getGroup() + "/" + sensor.getGroupId() + "/" + "light/" + sensor.getId();
                    publishMessage(publishMsg, "0");
                }
            }
            redLightArr.clear();
            greenLightArr.clear();
        }
    }

    public void publishMessage(String topic, String content){
        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(1);
        try {
            mqttClient.publish(topic, message);
            System.out.println("Published message: " + content + " Published topic = " + topic);
        } catch (MqttException e) {
            System.out.println("Failed to publish message");
        }
    }

    public void setInfoThread(TrafficSensorController trafficSensorController, MqttClient mqttClient, String mainTopic) {
        this.trafficSensorController = trafficSensorController;
        this.mqttClient = mqttClient;
        this.mainTopic = mainTopic;
    }
}
