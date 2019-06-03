package scheduler;

import controller.TrafficController;
import model.TrafficLight;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class RedLightScheduler {

    private TrafficController trafficController;
    private List<TrafficLight> redLightArr;

    public RedLightScheduler(TrafficController trafficController){
        this.trafficController = trafficController;
        this.redLightArr = trafficController.getRedLightArr();
    }

    public TimerTask footScheduler = new TimerTask() {
        @Override
        public void run() {
            changeLightToRed("foot");
        }
    };
    public TimerTask cycleScheduler = new TimerTask() {
        @Override
        public void run() {
            changeLightToRed("cycle");
        }
    };
    public TimerTask vehicleScheduler = new TimerTask() {
        @Override
        public void run() {
            changeLightToRed("motor_vehicle");
        }
    };

    /**
     * Turns the lights in the redLightArray to red
     * @param trafficUser is the group of the light
     */
    public void changeLightToRed(String trafficUser){
        List<TrafficLight> orangeList = new ArrayList<>();
        if (redLightArr.size() > 0) {
            for (TrafficLight light : redLightArr) {
                if (light.getGroup().equals(trafficUser))
                    orangeList.add(light);
            }
            for (TrafficLight light : orangeList) {
                trafficController.sendTrafficCommand(light, "0");
            }
        }
    }
}
