package controller;

import model.TrafficSensor;
import sun.management.Sensor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TrafficSensorController {

    List<TrafficSensor> trafficSensorsList = new ArrayList<TrafficSensor>();

    public void changeTrafficSensor(int trafficSensorId) {
        Optional<TrafficSensor> trafficSensor = null;
        if (trafficSensorsList.size() > 0 ){
            trafficSensor = trafficSensorsList.stream().
                    filter(sensor -> sensor.getId() == trafficSensorId).
                    findFirst();

            if (trafficSensor != null){
                trafficSensor.get().setState(1);
            }
            else {
                trafficSensor = Optional.of(new TrafficSensor(trafficSensorId, 1));
                trafficSensorsList.add(trafficSensor.get());
            }
        }
        else {
            trafficSensor = Optional.of(new TrafficSensor(trafficSensorId, 1));
            trafficSensorsList.add(trafficSensor.get());
        }
        trafficSensorTimer(trafficSensor);
    }

    public void trafficSensorTimer(Optional<TrafficSensor> trafficSensor) {
        try {
            Thread.sleep(10000); // 10000ms = 10s
            trafficSensor.get().setState(0);
        } catch(InterruptedException ex){
            System.out.println("Failed to change the trafficSensor value");
        }
    }
}
