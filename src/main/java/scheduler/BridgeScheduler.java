package scheduler;

import controller.TrafficController;
import controller.TrafficLightController;
import controller.TrafficSensorController;
import model.TrafficLight;
import model.TrafficSensor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
                    trafficController.sendTrafficCommand(light, "0");
                }

                List<TrafficLight> tempTrafficLightList = new ArrayList<>();
                tempTrafficLightList.addAll(trafficLightList);
                TrafficLight oppositeVesselLight = null;
                for (TrafficLight light : tempTrafficLightList) {
                    if (light.getGroup().equals("vessel") && light != vessels.get(0)) {
                        trafficController.sendTrafficCommand(light, "2");
                        oppositeVesselLight = light;
                    }
                }
                vessels.clear();
                if (oppositeVesselLight != null)
                    vessels.add(oppositeVesselLight);
            }

            if (bridgeCloseCounter == 2) {
                for (TrafficLight light : vessels) {
                    trafficController.sendTrafficCommand(light, "0");
                }
                String publishMsg = mainTopic + "/" + "bridge" + "/" + "1" + "/" + "deck/" + "1";
                trafficController.getMqttController().publishMessage(publishMsg, "1");
            }

            if (bridgeCloseCounter == 8) {
                for (TrafficLight light : trafficLightController.getGateGroup()) {
                    trafficController.sendTrafficCommand(light, "0");
                }
            }

            if (bridgeCloseCounter == 10) {
                for (TrafficLight light : trafficLightController.getBridgeGroup()) {
                    trafficController.sendTrafficCommand(light, "2");
                }
                bridgeOpen = false;
                vessels.clear();
                bridgeCounter = 0;
                closeCounter = 0;
                bridgeOpenCounter = 0;
                waitingVessel = false;
                return;
            }
            bridgeCloseCounter++;

        }
    };

    public TimerTask openBridge = new TimerTask() {
        @Override
        public void run() {

            if (!waitingVessel)
                return;

            if (bridgeOpenCounter == 0) {
                for (TrafficLight light : trafficLightController.getBridgeGroup()) {
                    trafficController.sendTrafficCommand(light, "0");
                }
            }

            if (!clearBridge){
                return;
            }
            if (bridgeOpenCounter == 2) {
                trafficController.sendTrafficCommand(trafficLightController.getGateGroup().get(0), "1");
            }

            if (bridgeOpenCounter == 3) {
                trafficController.sendTrafficCommand(trafficLightController.getGateGroup().get(1), "1");
            }

            if (bridgeOpenCounter == 7) {
                String publishMsg = mainTopic + "/" + "bridge" + "/" + "1" + "/" + "deck/" + "1";
                trafficController.getMqttController().publishMessage(publishMsg, "0");
            }

            if (bridgeOpenCounter == 17) {
                for (TrafficLight light : vessels) {
                    trafficController.sendTrafficCommand(light, "2");
                }
                bridgeOpenCounter++;
                bridgeCloseCounter = 0;
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

            if(bridgeCounter < 50)
                return;

            for (TrafficSensor sensor : tempSensorList){
                if (sensor.getGroup().equals("vessel")){
                    for (TrafficLight light : trafficLightController.getTrafficLights()) {
                        if (sensor.getGroupId().equals(light.getGroupId()) && sensor.getGroup().equals(light.getGroup()) && light.getSensorIds().contains(sensor.getId())) {
                            if (trafficLightList.contains(light) && vessels.size() < 1)
                                vessels.add(light);
                        }
                    }
                }
            }
            if (vessels.size() > 0){
                waitingVessel = true;
            }

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
