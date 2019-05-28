package scheduler;

import controller.TrafficController;
import controller.TrafficLightController;
import controller.TrafficSensorController;
import model.TrafficLight;
import model.TrafficSensor;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class BridgeScheduler {

    TrafficController trafficController;
    TrafficLightController trafficLightController;
    TrafficSensorController trafficSensorController;
    List<TrafficLight> trafficLightList;
    String mainTopic;
    private List<TrafficLight> vessels = new ArrayList<>();
    private boolean waitingVessel = false;
    private boolean bridgeOpen = false;
    private boolean clearBridge = false;
    private boolean clearWater = false;
    private int bridgeCounter = 0;
    private int bridgeOpenCounter = 0;
    private int bridgeCloseCounter = 0;
    private int closeCounter = 0;

    public BridgeScheduler(TrafficController trafficController){
        this.trafficController = trafficController;
        this.mainTopic = trafficController.getMainTopic();
        this.trafficLightController = trafficController.getTrafficLightController();
        this.trafficSensorController = trafficController.getTrafficSensorController();
        this.trafficLightList = trafficController.getTrafficLightList();
    }

    public TimerTask closeBridge = new TimerTask() {
        @Override
        public void run() {

            if (!bridgeOpen || !clearWater)
                return;

            closeCounter++;

            if (closeCounter < 2)
                return;

            if (bridgeCloseCounter == 0) {
                for (TrafficLight light : vessels) {
                    trafficController.sendMessage(light, "0");
                }
            }

            if (bridgeCloseCounter == 1) {
                String publishMsg = mainTopic + "/" + "bridge" + "/" + "1" + "/" + "deck/" + "1";
                trafficController.publishMessage(publishMsg, "1");
            }

            if (bridgeCloseCounter == 6) {
                for (TrafficLight light : trafficLightController.getGateGroup()) {
                    trafficController.sendMessage(light, "0");
                }
            }

            if (bridgeCloseCounter == 8) {
                for (TrafficLight light : trafficLightController.getBridgeGroup()) {
                    trafficController.sendMessage(light, "2");
                }
                bridgeOpen = false;
                vessels.clear();
                bridgeCounter = 0;
                closeCounter = 0;
                return;
            }
            bridgeCloseCounter++;

        }
    };

    public TimerTask openBridge = new TimerTask() {
        @Override
        public void run() {

            if (!waitingVessel || !clearBridge)
                return;

            if (bridgeOpenCounter == 0) {
                for (TrafficLight light : trafficLightController.getBridgeGroup()) {
                    trafficController.sendMessage(light, "0");
                }
            }

            if (bridgeOpenCounter == 2) {
                for (TrafficLight light : trafficLightController.getGateGroup()) {
                    trafficController.sendMessage(light, "1");
                }
            }

            if (bridgeOpenCounter == 6) {
                String publishMsg = mainTopic + "/" + "bridge" + "/" + "1" + "/" + "deck/" + "1";
                trafficController.publishMessage(publishMsg, "0");
            }

            if (bridgeOpenCounter == 16) {
                for (TrafficLight light : vessels) {
                    trafficController.sendMessage(light, "2");
                }
                waitingVessel = false;
                bridgeOpen = true;
                return;
            }
            bridgeOpenCounter++;
        }
    };


    public TimerTask bridgeRegulator = new TimerTask() {
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
    public void resetScheduler(){
        vessels.clear();
        waitingVessel = false;
        bridgeOpen = false;
        clearBridge = false;
        clearWater = false;
        waitingVessel = false;
        bridgeCounter = 0;
        bridgeOpenCounter = 0;
        bridgeCloseCounter = 0;
    }
}
