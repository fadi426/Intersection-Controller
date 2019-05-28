package controller;

import model.TrafficLight;
import model.TrafficSensor;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.*;
import java.util.stream.Collectors;

public class TrafficLightController extends Thread {
    List<TrafficLight> greenLightArr = new ArrayList<>();
    List<TrafficLight> redLightArr = new ArrayList<>();
    private TrafficSensorController trafficSensorController;
    private InitTrafficCases initTrafficCases = new InitTrafficCases();
    List<TrafficSensor> trafficSensorList = new ArrayList<>();
    List<TrafficLight> trafficLightList = new ArrayList<>();
    List<TrafficLight> vessels = new ArrayList<>();

    private MqttClient mqttClient;
    private String mainTopic;

    private Timer timer = new Timer();
    private int vehicleRotationCounter = 0;
    private boolean waitingVessel = false;
    private boolean bridgeOpen = false;
    private boolean clearBridge = false;
    private boolean clearWater = false;
    private int bridgeCounter = 0;
    private int bridgeOpenCounter = 0;
    private int bridgeCloseCounter = 0;

    public void run() {
        timer.schedule(greenLightScheduler, 0, 13000);
        timer.schedule(orangeFootScheduler, 6000, 13000);
        timer.schedule(orangeCycleScheduler, 8000, 13000);
        timer.schedule(orangeVehicleScheduler, 6000, 13000);
        timer.schedule(redFootScheduler, 12000, 13000);
        timer.schedule(redCycleScheduler, 10000, 13000);
        timer.schedule(redVehicleScheduler, 10000, 13000);
        timer.schedule(bridgeScheduler, 0, 1000);
        timer.schedule(openBridge, 0, 1000);
        timer.schedule(closeBridge, 0, 2000);
    }

    TimerTask greenLightScheduler = new TimerTask() {
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
                for (TrafficLight light : initTrafficCases.getTrafficLights()) {
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
                priorityGroup.add(findPriorityLight(lightTimes));

                List<TrafficLight> sortedLightArr = sortLightArr(lightTimes);
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
                        sendMessage(light, "2");
                        redLightArr.add(light);
                    }
                }
                greenLightArr.clear();
                vehicleRotationCounter++;
            }
        }
    };

    TimerTask orangeFootScheduler = new TimerTask() {
        @Override
        public void run() {
            List<TrafficLight> orangeList = new ArrayList<>();
            if (redLightArr.size() > 0) {
                for (TrafficLight light : redLightArr) {
                    if (light.getGroup().equals("foot"))
                        orangeList.add(light);
                }
                for (TrafficLight light : orangeList) {
                    sendMessage(light, "1");
                }
            }
        }
    };
    TimerTask orangeCycleScheduler = new TimerTask() {
        @Override
        public void run() {
            if (redLightArr.size() > 0) {
                for (TrafficLight light : redLightArr) {
                    if (light.getGroup().equals("cycle"))
                        sendMessage(light, "1");
                }
            }
        }
    };
    TimerTask orangeVehicleScheduler = new TimerTask() {
        @Override
        public void run() {
            if (redLightArr.size() > 0) {
                for (TrafficLight light : redLightArr) {
                    if (light.getGroup().equals("motor_vehicle"))
                        sendMessage(light, "1");
                }
            }
        }
    };

    TimerTask redFootScheduler = new TimerTask() {
        @Override
        public void run() {
            if (redLightArr.size() > 0) {
                for (TrafficLight light : redLightArr) {
                    if (light.getGroup().equals("foot"))
                        sendMessage(light, "0");
                }
            }
        }
    };
    TimerTask redCycleScheduler = new TimerTask() {
        @Override
        public void run() {
            if (redLightArr.size() > 0) {
                for (TrafficLight light : redLightArr) {
                    if (light.getGroup().equals("cycle"))
                        sendMessage(light, "0");
                }
            }
        }
    };
    TimerTask redVehicleScheduler = new TimerTask() {
        @Override
        public void run() {
            if (redLightArr.size() > 0) {
                for (TrafficLight light : redLightArr) {
                    if (light.getGroup().equals("motor_vehicle"))
                        sendMessage(light, "0");
                }
            }
        }
    };

    TimerTask closeBridge = new TimerTask() {
        @Override
        public void run() {

            if (!bridgeOpen || !clearWater)
                return;

            if (bridgeCloseCounter == 0) {
                for (TrafficLight light : vessels) {
                    sendMessage(light, "0");
                }
            }

            if (bridgeCloseCounter == 1) {
                String publishMsg = mainTopic + "/" + "bridge" + "/" + "1" + "/" + "deck/" + "1";
                publishMessage(publishMsg, "1");
            }

            if (bridgeCloseCounter == 6) {
                for (TrafficLight light : initTrafficCases.getGateGroup()) {
                    sendMessage(light, "0");
                }
            }

            if (bridgeCloseCounter == 8) {
                for (TrafficLight light : initTrafficCases.getBridgeGroup()) {
                    sendMessage(light, "2");
                }
                bridgeOpen = false;
                vessels.clear();
                bridgeCounter = 0;
                return;
            }
            bridgeCloseCounter++;

        }
    };

    TimerTask openBridge = new TimerTask() {
        @Override
        public void run() {

            if (!waitingVessel || !clearBridge)
                return;

            if (bridgeOpenCounter == 0) {
                for (TrafficLight light : initTrafficCases.getBridgeGroup()) {
                    sendMessage(light, "0");
                }
            }

            if (bridgeOpenCounter == 2) {
                for (TrafficLight light : initTrafficCases.getGateGroup()) {
                    sendMessage(light, "1");
                }
            }

            if (bridgeOpenCounter == 6) {
                String publishMsg = mainTopic + "/" + "bridge" + "/" + "1" + "/" + "deck/" + "1";
                publishMessage(publishMsg, "0");
            }

            if (bridgeOpenCounter == 16) {
                for (TrafficLight light : vessels) {
                    sendMessage(light, "2");
                }
                waitingVessel = false;
                bridgeOpen = true;
                return;
            }
            bridgeOpenCounter++;
        }
    };


    TimerTask bridgeScheduler = new TimerTask() {
        @Override
        public void run() {
            if (trafficSensorController == null)
                return;
            List<TrafficSensor> tempSensorList = new ArrayList<>();
            tempSensorList.addAll(trafficSensorController.getTrafficSensorList());
            for (TrafficSensor sensor : tempSensorList){
                if (sensor.getGroup().equals("bridge") && sensor.getState().equals("0"))
                    clearBridge = true;
                if (sensor.getGroup().equals("bridge") && sensor.getState().equals("1"))
                    clearBridge = false;
                if (sensor.getGroup().equals("vessel") && sensor.getGroupId().equals("3") && sensor.getState().equals("0"))
                    clearWater = true;
                if (sensor.getGroup().equals("vessel") && sensor.getGroupId().equals("3") && sensor.getState().equals("1"))
                    clearWater = false;
            }

            bridgeCounter++;

            if(bridgeCounter != 50)
                return;

            List<TrafficLight> tempTrafficLightList = new ArrayList<>();
            tempTrafficLightList.addAll(trafficLightList);
            if (tempTrafficLightList.size() <= 0)
                return;

            for (TrafficLight light : tempTrafficLightList) {
                if (light.getGroup().equals("vessel") && vessels.size() < 1) {
                    vessels.add(light);
                }
            }
            waitingVessel = true;
            bridgeOpenCounter = 0;
            bridgeCloseCounter = 0;
        }
    };

    public void publishMessage(String topic, String content) {
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
        if (initTrafficCases.getGateGroup().contains(light))
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
            for (int i = 0; i < initTrafficCases.getTrafficLights().size(); i++) {
                TrafficLight groupLight = initTrafficCases.getTrafficLights().get(i);
                if (groupLight.getGroupId().equals(l.getGroupId()) && groupLight.getGroup().equals(l.getGroup()) && groupLight.getId().equals(l.getId())) {

                    if (initTrafficCases.getGroups().get(i).contains(light))
                        return;

                    if (initTrafficCases.getBridgeGroup().contains(light)) {
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

    public void detectBridgeJam() {
        List<TrafficLight> lightsToExclude = new ArrayList<>();
        for (TrafficSensor sensor : trafficSensorList) {
            if (sensor.getGroup().equals("motor_vehicle") && sensor.getGroupId().equals("14")) {
                for (TrafficLight light : initTrafficCases.getBridgeExcludedLights()) {
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
            for (List<TrafficLight> groups : initTrafficCases.getFootGroup()){
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

    public InitTrafficCases getInitTrafficCases() {
        return initTrafficCases;
    }

    public void resetThread(){
        trafficSensorController.getTrafficSensorList().clear();
        greenLightArr.clear();
        redLightArr.clear();
        trafficSensorList.clear();
        trafficLightList .clear();
        vessels.clear();
        vehicleRotationCounter = 0;
        waitingVessel = false;
        bridgeOpen = false;
        clearBridge = false;
        clearWater = false;
        vehicleRotationCounter = 0;
        waitingVessel = false;
        bridgeCounter = 0;
        bridgeOpenCounter = 0;
        bridgeCloseCounter = 0;
    }

}
