package controller;

import model.TrafficLight;
import model.TrafficSensor;
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

    private MqttController mqttController;
    private String mainTopic;

    private Timer timer = new Timer();

    private GreenLightScheduler greenLightScheduler;
    private OrangeLightScheduler orangeLightScheduler;
    private RedLightScheduler redLightScheduler;
    private BridgeScheduler bridgeScheduler;

    TrafficController(TrafficSensorController trafficSensorController, String mainTopic) {
        this.trafficSensorController = trafficSensorController;
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



    public static Long getMax(List<Long> inputArray) {
        Long maxValue = inputArray.get(0);
        for (int i = 1; i < inputArray.size(); i++) {
            if (inputArray.get(i) > maxValue) {
                maxValue = inputArray.get(i);
            }
        }
        return (maxValue);
    }

    public void sendTrafficCommand(TrafficLight light, String mode) {
        if (mqttController == null)
            return;
        String publishMsg = "";
        if (trafficLightController.getGateGroup().contains(light))
            publishMsg = mainTopic + "/" + light.getGroup() + "/" + light.getGroupId() + "/" + "gate/" + light.getId();
        else
            publishMsg = mainTopic + "/" + light.getGroup() + "/" + light.getGroupId() + "/" + "light/" + light.getId();


        light.setState(mode);
        mqttController.publishMessage(publishMsg, mode);
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

    public void initState(){
        trafficLightController.getTrafficLights().stream()
                .forEach(light -> light.setState("0"));

        trafficLightController.getTrafficLights().stream()
                .forEach(light -> sendTrafficCommand(light, light.getState()));

        trafficLightController.getBridgeGroup().stream()
                .forEach(light -> sendTrafficCommand(light, "2"));

        trafficLightController.getGateGroup().stream()
                .forEach(light -> sendTrafficCommand(light, "0"));
    }

    public void addLight(TrafficLight light, List<TrafficLight> list){
        if (light.getGroup().equals("foot")){
            if (trafficLightController.getFirstFootLight().contains(light)){
                int positionOfLight = trafficLightController.getFirstFootLight().indexOf(light);
                for (TrafficLight l :trafficLightController.getFootPairs().get(positionOfLight)){
                    if (list.contains(l))
                        continue;
                    list.add(l);
                }
                return;
            }
            else {
                list.add(light);
                return;
            }
        }
        list.add(light);
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

    public String getMainTopic(){
        return mainTopic;
    }

    public MqttController getMqttController() {
        return mqttController;
    }

    public void setMqttController(MqttController mqttController){
        this.mqttController = mqttController;
    }

    public void resetThread(){
        trafficSensorController.getTrafficSensorList().clear();
        redLightArr.clear();
        trafficSensorList.clear();
        trafficLightList .clear();
        greenLightScheduler.resetScheduler();
        bridgeScheduler.resetScheduler();
    }
}
