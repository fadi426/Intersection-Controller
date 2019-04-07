package controller;

import model.TrafficLight;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InitTrafficCases {
    private List<List<TrafficLight>> groups = new ArrayList<>();
    private List<List<TrafficLight>> cycleGroups = new ArrayList<>();

    private TrafficLight MV1L1 = new TrafficLight("motor_vehicle", "1", "1", "0", 0);
    private TrafficLight MV2L1 = new TrafficLight("motor_vehicle", "2", "1", "0", 0);
    private TrafficLight MV3L1 = new TrafficLight("motor_vehicle", "3", "1", "0", 0);
    private TrafficLight MV4L1 = new TrafficLight("motor_vehicle", "4", "1", "0", 0);
    private TrafficLight MV5L1 = new TrafficLight("motor_vehicle", "5", "1", "0", 0);
    private TrafficLight MV5L2 = new TrafficLight("motor_vehicle", "5", "2", "0", 0);
    private TrafficLight MV6L1 = new TrafficLight("motor_vehicle", "6", "1", "0", 0);
    private TrafficLight MV7L1 = new TrafficLight("motor_vehicle", "7", "1", "0", 0);
    private TrafficLight MV7L2 = new TrafficLight("motor_vehicle", "7", "2", "0", 0);
    private TrafficLight MV8L1 = new TrafficLight("motor_vehicle", "8", "1", "0", 0);
    private TrafficLight MV9L1 = new TrafficLight("motor_vehicle", "9", "1", "0", 0);
    private TrafficLight MV10L1 = new TrafficLight("motor_vehicle", "10", "1", "0", 0);
    private TrafficLight MV10L2 = new TrafficLight("motor_vehicle", "10", "2", "0", 0);
    private TrafficLight MV11L1 = new TrafficLight("motor_vehicle", "11", "1", "0", 0);

    private TrafficLight C1L1 = new TrafficLight("cycle", "1", "1", "0", 0);
    
    public InitTrafficCases(){
        List<TrafficLight> blue = new ArrayList<>(Arrays.asList(MV10L1, MV10L2, MV5L1, MV5L2)); // MV4L1, MV9L1
        List<TrafficLight> green = new ArrayList<>(Arrays.asList(MV10L1, MV10L2, MV1L1, MV4L1));
        List<TrafficLight> brown = new ArrayList<>(Arrays.asList(MV11L1, MV9L1, MV1L1, MV7L1, MV7L2));
        List<TrafficLight> brownLight = new ArrayList<>(Arrays.asList(MV6L1, MV1L1, MV7L2)); //, MV7L1, MV9L1
        List<TrafficLight> Violet = new ArrayList<>(Arrays.asList(MV9L1, MV5L1, MV5L2, MV7L1, MV7L2));
        //List<TrafficLight> pink = new ArrayList<>(Arrays.asList( MV1L1, MV2L1, MV3L1, MV4L1));
        List<TrafficLight> pinkLight = new ArrayList<>(Arrays.asList( MV1L1, MV9L1, MV3L1, MV4L1));
        List<TrafficLight> purple = new ArrayList<>(Arrays.asList(MV7L1, MV7L2, MV8L1)); //MV9L1
        List<TrafficLight> purpleLight = new ArrayList<>(Arrays.asList(MV9L1, MV7L1, MV7L2, MV11L1));
        List<TrafficLight> orange = new ArrayList<>(Arrays.asList(MV1L1, MV2L1, MV3L1, MV4L1));
        List<TrafficLight> orangeLight = new ArrayList<>(Arrays.asList(MV1L1, MV2L1, MV7L1, MV7L2, MV4L1));
        List<TrafficLight> grey = new ArrayList<>(Arrays.asList(MV4L1, MV6L1, MV7L1, MV7L2));

        groups.add(blue);
        groups.add(green);
        groups.add(brown);
        groups.add(Violet);
        //groups.add(pink);
        groups.add(purple);
        groups.add(orange);
        groups.add(grey);
        groups.add(brownLight);
        groups.add(pinkLight);
        groups.add(purpleLight);
        groups.add(orangeLight);

        List<TrafficLight> C1L1 = new ArrayList<>(Arrays.asList(MV4L1, MV8L1, MV11L1, MV1L1, MV2L1, MV3L1));
        cycleGroups.add(C1L1);
    }

    public List<List<TrafficLight>> getGroups() {
        return groups;
    }

    public List<List<TrafficLight>> getCyleGroups() {
        return cycleGroups;
    }


}
