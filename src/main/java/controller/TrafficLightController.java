package controller;

import model.TrafficSensor;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TrafficLightController extends Thread {
    private List<String> messageQueue = new ArrayList<>();
    private List<String> topiceQueue = new ArrayList<>();
    private List<String> sensorList = new ArrayList<>(Arrays.asList("0", "0"));
    private TrafficSensorController trafficSensorController;
    private MqttClient mqttClient;
    private String mainTopic;
    private int listSize = 0;
    private int counter = 1;


    public void run(){
        while (true) {
            if (topiceQueue.size() > 0 ) {
                System.out.println("message = " + messageQueue.get(0) + " || topic = " + topiceQueue.get(0));
                sensorTopicRegex(topiceQueue.get(0), messageQueue.get(0));
                messageQueue.remove(0);
                topiceQueue.remove(0);
                try {
                    changeLights(mainTopic);
                } catch (InterruptedException e) {
                    System.out.println("Failed to send message");
                }
               // counter = listSize;
            }
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void sensorTopicRegex(String topic, String message) {
        String pattern = "(\\d+)\\/(\\w+)\\/(\\d+)\\/(\\w+)\\/(\\d+)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(topic);
        if (m.find( )) {
            if (m.group(4).contains("sensor"))
            trafficSensorController.changeTrafficSensor(m.group(2), m.group(3), m.group(5), message);
        } else {

            System.out.println(topic + " = NO MATCH");
        }
    }

    public void changeLights(String mainTopic) throws InterruptedException {

        List<TrafficSensor> trafficSensorList = trafficSensorController.getTrafficSensorList();

        boolean exsist = false;
        if (trafficSensorList.size() > 0) {
            for (TrafficSensor sensor : trafficSensorList){
                if (sensor.getState().equals("1") && !sensorList.get(Integer.parseInt(sensor.getGroupId())-1).equals(sensor.getState()) && exsist == false){
                    String publishMsg = mainTopic + "/" + sensor.getGroup() + "/" + sensor.getGroupId()  + "/" + "light/" + sensor.getId();
 //                   System.out.println(publishMsg);
                    publishMessage(publishMsg, "0");
                    Thread.sleep(1500);
                    System.out.println("listsize = " + trafficSensorList.size());
                    exsist = true;
                    sensorList.set(Integer.parseInt(sensor.getGroupId())-1,sensor.getState());
                }
                else {
                    if (!sensorList.get(Integer.parseInt(sensor.getGroupId())-1).equals(sensor.getState())) {
                        String publishMsg = mainTopic + "/" + sensor.getGroup() + "/" + sensor.getGroupId() + "/" + "light/" + sensor.getId();
                        publishMessage(publishMsg, "1");
                        Thread.sleep(500);
                        publishMessage(publishMsg, "2");
                        Thread.sleep(500);
                        sensorList.set(Integer.parseInt(sensor.getGroupId())-1, sensor.getState());
                    }
                }
            }
        }
    }

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

    public void addMessage(String message, String topic) {
        boolean exists = false;
        for (int i = 0; i < topiceQueue.size(); i++){
            if (messageQueue.get(i).equals(message.toString()) && topiceQueue.get(i).equals(topic)){
                exists = true;
            }
        }
        if (!exists) {
            messageQueue.add(message);
            topiceQueue.add(topic);
            listSize += 1;
        }
    }


    public List<String> getTopiceQueue() {
        return topiceQueue;
    }

    public void setInfoThread(TrafficSensorController trafficSensorController, MqttClient mqttClient, String mainTopic) {
        this.trafficSensorController = trafficSensorController;
        this.mqttClient = mqttClient;
        this.mainTopic = mainTopic;
    }
}
