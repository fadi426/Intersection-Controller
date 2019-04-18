package controller;

import model.TrafficLight;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InitTrafficCases {

    private List<TrafficLight> trafficLights = new ArrayList<>();
    private List<List<TrafficLight>> groups = new ArrayList<>();
    private List<List<TrafficLight>> cycleGroups = new ArrayList<>();

    private TrafficLight MV1L1 = new TrafficLight("motor_vehicle", "1", "1", "0", 0);
    private TrafficLight MV2L1 = new TrafficLight("motor_vehicle", "2", "1", "0", 0);
    private TrafficLight MV3L1 = new TrafficLight("motor_vehicle", "3", "1", "0", 0);
    private TrafficLight MV4L1 = new TrafficLight("motor_vehicle", "4", "1", "0", 0);
    private TrafficLight MV5L1 = new TrafficLight("motor_vehicle", "5", "1", "0", 0);
    private TrafficLight MV6L1 = new TrafficLight("motor_vehicle", "6", "1", "0", 0);
    private TrafficLight MV7L1 = new TrafficLight("motor_vehicle", "7", "1", "0", 0);
    private TrafficLight MV8L1 = new TrafficLight("motor_vehicle", "8", "1", "0", 0);
    private TrafficLight MV9L1 = new TrafficLight("motor_vehicle", "9", "1", "0", 0);
    private TrafficLight MV10L1 = new TrafficLight("motor_vehicle", "10", "1", "0", 0);
    private TrafficLight MV11L1 = new TrafficLight("motor_vehicle", "11", "1", "0", 0);

    private TrafficLight C1L1 = new TrafficLight("cycle", "1", "1", "0", 0);
    private TrafficLight C2L1 = new TrafficLight("cycle", "2", "1", "0", 0);
    private TrafficLight C3L1 = new TrafficLight("cycle", "3", "1", "0", 0);
    private TrafficLight C4L1 = new TrafficLight("cycle", "4", "1", "0", 0);

    private TrafficLight F1L1 = new TrafficLight("foot", "1", "1", "0", 0);
    private TrafficLight F2L1 = new TrafficLight("foot", "2", "1", "0", 0);
    private TrafficLight F3L1 = new TrafficLight("foot", "3", "1", "0", 0);
    private TrafficLight F4L1 = new TrafficLight("foot", "4", "1", "0", 0);

    
    public InitTrafficCases(){

        trafficLights.add(MV1L1);
        trafficLights.add(MV2L1);
        trafficLights.add(MV3L1);
        trafficLights.add(MV4L1);
        trafficLights.add(MV5L1);
        trafficLights.add(MV6L1);
        trafficLights.add(MV7L1);
        trafficLights.add(MV8L1);
        trafficLights.add(MV9L1);
        trafficLights.add(MV10L1);
        trafficLights.add(MV11L1);
        trafficLights.add(C1L1);
        trafficLights.add(C2L1);
        trafficLights.add(C3L1);
        trafficLights.add(C4L1);
        trafficLights.add(F1L1);
        trafficLights.add(F2L1);
        trafficLights.add(F3L1);
        trafficLights.add(F4L1);


        List<TrafficLight> MV1 = new ArrayList<>(Arrays.asList(MV5L1, C1L1, C4L1));
        List<TrafficLight> MV2 = new ArrayList<>(Arrays.asList(MV5L1, MV6L1, MV8L1, MV9L1, MV10L1, MV11L1, C1L1, C3L1));
        List<TrafficLight> MV3 = new ArrayList<>(Arrays.asList(MV5L1, MV6L1, MV7L1, MV8L1, MV10L1, MV11L1, C1L1, C2L1));
        List<TrafficLight> MV4 = new ArrayList<>(Arrays.asList(MV8L1, MV11L1, C1L1, C2L1));
        List<TrafficLight> MV5 = new ArrayList<>(Arrays.asList(MV1L1, MV2L1, MV3L1, MV8L1, MV11L1, C2L1, C4L1));
        List<TrafficLight> MV6 = new ArrayList<>(Arrays.asList(MV2L1, MV3L1, MV8L1, MV9L1, MV10L1, MV11L1, C2L1, C3L1));
        List<TrafficLight> MV7 = new ArrayList<>(Arrays.asList(MV3L1, MV10L1, C2L1));
        List<TrafficLight> MV8 = new ArrayList<>(Arrays.asList(MV2L1, MV3L1, MV4L1, MV5L1, MV6L1, MV10L1, MV11L1, C1L1, C4L1));
        List<TrafficLight> MV9 = new ArrayList<>(Arrays.asList(MV2L1, MV6L1, C3L1, C4L1));
        List<TrafficLight> MV10 = new ArrayList<>(Arrays.asList(MV2L1, MV3L1, MV6L1, MV7L1, MV8L1, C2L1, C4L1));
        List<TrafficLight> MV11 = new ArrayList<>(Arrays.asList(MV2L1, MV3L1, MV4L1, MV5L1, MV8L1,MV6L1, C1L1, C4L1));

        List<TrafficLight> C1 = new ArrayList<>(Arrays.asList(MV4L1, MV8L1, MV11L1, MV1L1, MV2L1, MV3L1));
        List<TrafficLight> C2 = new ArrayList<>(Arrays.asList(MV3L1, MV4L1, MV5L1, MV6L1, MV7L1, MV10L1));
        List<TrafficLight> C3 = new ArrayList<>(Arrays.asList(MV2L1, MV6L1, MV9L1));
        List<TrafficLight> C4 = new ArrayList<>(Arrays.asList(MV1L1, MV5L1, MV8L1, MV9L1, MV10L1, MV11L1));

        List<TrafficLight> F1 = new ArrayList<>(Arrays.asList(MV4L1, MV8L1, MV11L1, MV1L1, MV2L1, MV3L1));
        List<TrafficLight> F2 = new ArrayList<>(Arrays.asList(MV3L1, MV4L1, MV5L1, MV6L1, MV7L1, MV10L1));
        List<TrafficLight> F3 = new ArrayList<>(Arrays.asList(MV2L1, MV6L1, MV9L1));
        List<TrafficLight> F4 = new ArrayList<>(Arrays.asList(MV1L1, MV5L1, MV8L1, MV9L1, MV10L1, MV11L1));

        groups.add(MV1);
        groups.add(MV2);
        groups.add(MV3);
        groups.add(MV4);
        groups.add(MV5);
        groups.add(MV6);
        groups.add(MV7);
        groups.add(MV8);
        groups.add(MV9);
        groups.add(MV10);
        groups.add(MV11);

        groups.add(C1);
        groups.add(C2);
        groups.add(C3);
        groups.add(C4);

        groups.add(F1);
        groups.add(F2);
        groups.add(F3);
        groups.add(F4);
    }

    public List<List<TrafficLight>> getGroups() {
        return groups;
    }

    public List<List<TrafficLight>> getCyleGroups() {
        return cycleGroups;
    }

    public List<TrafficLight> getTrafficLights() {
        return trafficLights;
    }
}
