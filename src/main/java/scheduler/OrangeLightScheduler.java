package scheduler;

import controller.TrafficController;
import model.TrafficLight;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class OrangeLightScheduler {

    private TrafficController trafficController;
    private List<TrafficLight> redLightArr;


    public OrangeLightScheduler(TrafficController trafficController){
        this.trafficController = trafficController;
        this.redLightArr = trafficController.getRedLightArr();
    }

    public TimerTask footScheduler = new TimerTask() {
        @Override
        public void run() {
            changeLightToOrange("foot");
        }
    };
    public TimerTask cycleScheduler = new TimerTask() {
        @Override
        public void run() {
            changeLightToOrange("cycle");
        }
    };
    public TimerTask vehicleScheduler = new TimerTask() {
        @Override
        public void run() {
            changeLightToOrange("motor_vehicle");
        }
    };

    public void changeLightToOrange(String trafficUser){
        List<TrafficLight> orangeList = new ArrayList<>();
        if (redLightArr.size() > 0) {
            for (TrafficLight light : redLightArr) {
                if (light.getGroup().equals(trafficUser))
                    orangeList.add(light);
            }
            for (TrafficLight light : orangeList) {
                trafficController.sendMessage(light, "1");
            }
        }
    }
}
