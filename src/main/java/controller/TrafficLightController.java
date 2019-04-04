package controller;

import model.TrafficSensor;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.*;

public class TrafficLightController extends Thread {
    List<TrafficSensor> greenLightArr = new ArrayList<>();
    List<TrafficSensor> redLightArr = new ArrayList<>();
    List<List<String>> groups = new ArrayList<>();
    private TrafficSensorController trafficSensorController;
    private MqttClient mqttClient;
    private String mainTopic;

    private Timer timer = new Timer();
    private int switchTime = 5000;

    public void run(){
        trafficRegulator();
        timer.schedule(redLightScheduler,3000,switchTime);
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
                    Random randomGenerator = new Random();
                    int randomInt = randomGenerator.nextInt(groups.size());
                    List<String> randomGroup = groups.get(randomInt);

                    for (TrafficSensor sensor : greenLightArr) {
                        if (randomGroup.contains(sensor.getGroupId())) {
                            String publishMsg = mainTopic + "/" + sensor.getGroup() + "/" + sensor.getGroupId() + "/" + "light/" + sensor.getId();
                            publishMessage(publishMsg, "2");
                            redLightArr.add(sensor);
                        }
                    }
                }
            }
            greenLightArr.clear();
        }
    };

    TimerTask task2 = new TimerTask() {
        @Override
        public void run() {
            if (greenLightArr.size() > 0) {
                for (TrafficSensor sensor : greenLightArr) {
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

    public void trafficRegulator(){
        List<String> blue = new ArrayList<>(Arrays.asList("10", "5", "4", "9"));
        List<String> green = new ArrayList<>(Arrays.asList("10", "1", "4"));
        List<String> brown = new ArrayList<>(Arrays.asList("11", "9", "1", "7")); //6 ontbreekt hier, maar kan niet tegelijkertijd met 11
        List<String> purpleLight = new ArrayList<>(Arrays.asList("9", "5", "7"));
        List<String> pink = new ArrayList<>(Arrays.asList( "1", "2", "3", "4"));; //9 ontbreekt hier, maar kan niet tegelijkertijd met 2
        List<String> purple = new ArrayList<>(Arrays.asList("9", "7", "8"));    // 11 ontbreekt hier, maar kan niet tegelijkertijd met 8
        List<String> orange = new ArrayList<>(Arrays.asList("1", "2", "3", "4")); // 7 ontbreekt hier, maar kan niet tegelijkertijd met 3
        List<String> grey = new ArrayList<>(Arrays.asList( "4", "6", "7")); //1 ontbreekt hier, maar kan niet tegelijkertijd met 5

        groups.add(blue);
        groups.add(green);
        groups.add(brown);
        groups.add(purpleLight);
        groups.add(pink);
        groups.add(purple);
        groups.add(orange);
        groups.add(grey);


    }
}
