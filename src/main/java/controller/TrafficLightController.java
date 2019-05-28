package controller;

import model.TrafficLight;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrafficLightController {

    private List<TrafficLight> trafficLights = new ArrayList<>();
    private List<List<TrafficLight>> groups = new ArrayList<>();
    private List<TrafficLight> bridgeGroup = new ArrayList<>();
    private List<TrafficLight> bridgeExcludedLights = new ArrayList<>();
    private List<List<TrafficLight>> footGroup = new ArrayList<>();
    private List<TrafficLight> gateGroup = new ArrayList<>();

    private TrafficLight MV1L1 = new TrafficLight("motor_vehicle", "1", "1", "0");
    private TrafficLight MV2L1 = new TrafficLight("motor_vehicle", "2", "1", "0");
    private TrafficLight MV3L1 = new TrafficLight("motor_vehicle", "3", "1", "0");
    private TrafficLight MV4L1 = new TrafficLight("motor_vehicle", "4", "1", "0");
    private TrafficLight MV5L1 = new TrafficLight("motor_vehicle", "5", "1", "0");
    private TrafficLight MV5L2 = new TrafficLight("motor_vehicle", "5", "2", "0");
    private TrafficLight MV6L1 = new TrafficLight("motor_vehicle", "6", "1", "0");
    private TrafficLight MV7L1 = new TrafficLight("motor_vehicle", "7", "1", "0");
    private TrafficLight MV7L2 = new TrafficLight("motor_vehicle", "7", "2", "0");
    private TrafficLight MV8L1 = new TrafficLight("motor_vehicle", "8", "1", "0");
    private TrafficLight MV9L1 = new TrafficLight("motor_vehicle", "9", "1", "0");
    private TrafficLight MV10L1 = new TrafficLight("motor_vehicle", "10", "1", "0");
    private TrafficLight MV10L2 = new TrafficLight("motor_vehicle", "10", "2", "0");
    private TrafficLight MV11L1 = new TrafficLight("motor_vehicle", "11", "1", "0");
    private TrafficLight MV12L1 = new TrafficLight("motor_vehicle", "12", "1", "0");
    private TrafficLight MV13L1 = new TrafficLight("motor_vehicle", "13", "1", "0");

    private TrafficLight C1L1 = new TrafficLight("cycle", "1", "1", "0");
    private TrafficLight C2L1 = new TrafficLight("cycle", "2", "1", "0");
    private TrafficLight C3L1 = new TrafficLight("cycle", "3", "1", "0");
    private TrafficLight C4L1 = new TrafficLight("cycle", "4", "1", "0");


    private TrafficLight F1L1 = new TrafficLight("foot", "1", "1", "0");
    private TrafficLight F1L2 = new TrafficLight("foot", "1", "2", "0");
    private TrafficLight F2L1 = new TrafficLight("foot", "2", "1", "0");
    private TrafficLight F2L2 = new TrafficLight("foot", "2", "2", "0");
    private TrafficLight F3L1 = new TrafficLight("foot", "3", "1", "0");
    private TrafficLight F3L2 = new TrafficLight("foot", "3", "2", "0");
    private TrafficLight F4L1 = new TrafficLight("foot", "4", "1", "0");
    private TrafficLight F4L2 = new TrafficLight("foot", "4", "2", "0");
    private TrafficLight F5L1 = new TrafficLight("foot", "5", "1", "0");
    private TrafficLight F5L2 = new TrafficLight("foot", "5", "2", "0");
    private TrafficLight F6L1 = new TrafficLight("foot", "6", "1", "0");
    private TrafficLight F6L2 = new TrafficLight("foot", "6", "2", "0");
    private TrafficLight F7L1 = new TrafficLight("foot", "7", "1", "0");
    private TrafficLight F7L2 = new TrafficLight("foot", "7", "2", "0");
    private TrafficLight F8L1 = new TrafficLight("foot", "8", "1", "0");
    private TrafficLight F8L2 = new TrafficLight("foot", "8", "2", "0");

    private TrafficLight V1L1 = new TrafficLight("vessel", "1", "1", "0");
    private TrafficLight V2L1 = new TrafficLight("vessel", "2", "1", "0");

    private TrafficLight B1L1 = new TrafficLight("bridge", "1", "1", "0");

    private TrafficLight B1G1 = new TrafficLight("bridge", "1", "1", "0");
    private TrafficLight B1G2 = new TrafficLight("bridge", "1", "2", "0");

    public TrafficLightController(){

        trafficLights.add(MV1L1);
        trafficLights.add(MV2L1);
        trafficLights.add(MV3L1);
        trafficLights.add(MV4L1);
        trafficLights.add(MV5L1);
        trafficLights.add(MV5L2);
        trafficLights.add(MV6L1);
        trafficLights.add(MV7L1);
        trafficLights.add(MV7L2);
        trafficLights.add(MV8L1);
        trafficLights.add(MV9L1);
        trafficLights.add(MV10L1);
        trafficLights.add(MV10L2);
        trafficLights.add(MV11L1);
        trafficLights.add(MV12L1);

        trafficLights.add(C1L1);
        trafficLights.add(C2L1);
        trafficLights.add(C3L1);
        trafficLights.add(C4L1);

        trafficLights.add(F1L1);
        trafficLights.add(F1L2);
        trafficLights.add(F2L1);
        trafficLights.add(F2L2);
        trafficLights.add(F3L1);
        trafficLights.add(F3L2);
        trafficLights.add(F4L1);
        trafficLights.add(F4L2);
        trafficLights.add(F5L1);
        trafficLights.add(F5L2);
        trafficLights.add(F6L1);
        trafficLights.add(F6L2);
        trafficLights.add(F7L1);
        trafficLights.add(F7L2);
        trafficLights.add(F8L1);
        trafficLights.add(F8L2);

        trafficLights.add(V1L1);
        trafficLights.add(V2L1);


        List<TrafficLight> MV1 = new ArrayList<>(Arrays.asList(MV5L1, MV5L2, C1L1, C4L1, F1L1, F1L2, F2L2, F7L1, F8L1, F8L2));
        List<TrafficLight> MV2 = new ArrayList<>(Arrays.asList(MV5L1, MV5L2, MV6L1, MV8L1, MV9L1, MV10L1, MV10L2, MV11L1, MV12L1, C1L1, C3L1, F1L1, F1L2, F2L2, F5L1, F6L1, F6L2));
        List<TrafficLight> MV3 = new ArrayList<>(Arrays.asList(MV5L1, MV5L2, MV6L1, MV7L1, MV7L2, MV8L1, MV10L1, MV10L2, MV11L1,MV13L1, C1L1, C2L1, F1L1, F1L2, F2L2, F3L1, F4L1, F4L2));
        List<TrafficLight> MV4 = new ArrayList<>(Arrays.asList(MV8L1, MV11L1, C1L1, C2L1, F1L1, F2L1, F2L2, F3L1, F3L2, F4L2));
        List<TrafficLight> MV5 = new ArrayList<>(Arrays.asList(MV1L1, MV2L1, MV3L1, MV8L1, MV11L1, C2L1, C4L1, F3L1, F3L2, F4L2, F7L1, F8L1, F8L2));
        List<TrafficLight> MV5_2 = new ArrayList<>(Arrays.asList(MV1L1, MV2L1, MV3L1, MV8L1, MV11L1, C2L1, C4L1, F3L1, F3L2, F4L2, F7L1, F8L1, F8L2));
        List<TrafficLight> MV6 = new ArrayList<>(Arrays.asList(MV2L1, MV3L1, MV8L1, MV9L1, MV10L1, MV10L2, MV12L1, C2L1, C3L1, F3L1, F3L2, F4L2, F5L1, F6L1, F6L2));
        List<TrafficLight> MV7 = new ArrayList<>(Arrays.asList(MV3L1, MV10L1, MV10L2, MV12L1,MV13L1, C2L1, F3L1, F4L1, F4L2, F5L1, F5L2, F6L2, C3L1));
        List<TrafficLight> MV7_2 = new ArrayList<>(Arrays.asList(MV3L1, MV10L1, MV10L2, MV12L1,MV13L1, C2L1, F3L1, F4L1, F4L2, F5L1, F5L2, F6L2, C3L1));
        List<TrafficLight> MV8 = new ArrayList<>(Arrays.asList(MV1L1, MV2L1, MV3L1, MV4L1, MV5L1, MV5L2, MV6L1, MV10L1, MV10L2, MV11L1, MV12L1, C1L1, C3L1, C4L1, F1L1, F2L1, F2L2, F5L1, F5L2, F6L2, F7L1, F8L1, F8L2));
        List<TrafficLight> MV9 = new ArrayList<>(Arrays.asList(MV2L1, MV6L1, MV12L1, C3L1, C4L1, F4L2, F5L1, F6L1, F6L2, F7L1, F7L2, F8L2));
        List<TrafficLight> MV10 = new ArrayList<>(Arrays.asList(MV2L1, MV3L1, MV6L1, MV7L1, MV7L2, MV8L1, MV13L1, C2L1, C4L1, F3L1, F4L1, F7L1, F7L2, F8L2));
        List<TrafficLight> MV10_2 = new ArrayList<>(Arrays.asList(MV2L1, MV3L1, MV6L1, MV7L1, MV7L2, MV8L1, MV13L1, C2L1, C4L1, F3L1, F4L1, F7L1, F7L2, F8L2));
        List<TrafficLight> MV11 = new ArrayList<>(Arrays.asList(MV2L1, MV3L1, MV4L1, MV5L1, MV5L2, MV8L1, C1L1, C4L1, F1L1, F2L1, F2L2, F7L1, F7L2, F8L2));
        List<TrafficLight> MV12 = new ArrayList<>(Arrays.asList(MV2L1, MV6L1, MV7L1, MV7L2, MV8L1, MV9L1));

        List<TrafficLight> C1 = new ArrayList<>(Arrays.asList(MV4L1, MV8L1, MV11L1, MV1L1, MV2L1, MV3L1));
        List<TrafficLight> C2 = new ArrayList<>(Arrays.asList(MV3L1, MV4L1, MV5L1, MV5L2, MV6L1, MV7L1, MV7L2, MV10L1, MV10L2));
        List<TrafficLight> C3 = new ArrayList<>(Arrays.asList(MV2L1, MV6L1, MV9L1, MV7L1, MV7L2, MV8L1));
        List<TrafficLight> C4 = new ArrayList<>(Arrays.asList(MV1L1, MV5L1, MV5L2, MV8L1, MV9L1, MV10L1, MV10L2, MV11L1));

        List<TrafficLight> F1_1 = new ArrayList<>(Arrays.asList(MV1L1, MV2L1, MV3L1, MV4L1, MV8L1, MV11L1));
        List<TrafficLight> F1_2 = new ArrayList<>(Arrays.asList(MV1L1, MV2L1, MV3L1));
        List<TrafficLight> F2_1 = new ArrayList<>(Arrays.asList(MV4L1, MV8L1, MV11L1));
        List<TrafficLight> F2_2 = new ArrayList<>(Arrays.asList(MV1L1, MV2L1, MV3L1, MV4L1, MV8L1, MV11L1));
        List<TrafficLight> F3_1 = new ArrayList<>(Arrays.asList(MV3L1, MV4L1, MV6L1, MV7L1, MV7L2, MV5L1, MV5L2, MV10L1, MV10L2));
        List<TrafficLight> F3_2 = new ArrayList<>(Arrays.asList(MV4L1, MV5L1, MV5L2, MV6L1));
        List<TrafficLight> F4_1 = new ArrayList<>(Arrays.asList(MV3L1, MV7L1, MV7L2, MV10L1, MV10L2));
        List<TrafficLight> F4_2 = new ArrayList<>(Arrays.asList(MV3L1, MV4L1, MV6L1, MV7L1, MV7L2, MV5L1, MV5L2, MV10L1, MV10L2));
        List<TrafficLight> F5_1 = new ArrayList<>(Arrays.asList(MV2L1, MV6L1, MV7L1, MV7L2, MV8L1, MV9L1));
        List<TrafficLight> F5_2 = new ArrayList<>(Arrays.asList(MV7L1, MV7L2, MV8L1));
        List<TrafficLight> F6_1 = new ArrayList<>(Arrays.asList(MV2L1, MV6L1, MV9L1));
        List<TrafficLight> F6_2 = new ArrayList<>(Arrays.asList(MV2L1, MV6L1, MV7L1, MV7L2, MV8L1, MV9L1));
        List<TrafficLight> F7_1 = new ArrayList<>(Arrays.asList(MV1L1, MV5L1, MV5L2, MV8L1, MV9L1, MV10L1, MV10L2, MV11L1));
        List<TrafficLight> F7_2 = new ArrayList<>(Arrays.asList(MV9L1, MV10L1, MV10L2, MV11L1));
        List<TrafficLight> F8_1 = new ArrayList<>(Arrays.asList(MV1L1, MV5L1, MV5L2, MV8L1));
        List<TrafficLight> F8_2 = new ArrayList<>(Arrays.asList(MV1L1, MV5L1, MV5L2, MV8L1, MV9L1, MV10L1, MV10L2, MV11L1));


        List<TrafficLight> V1 = new ArrayList<>(Arrays.asList(V2L1));
        List<TrafficLight> V2 = new ArrayList<>(Arrays.asList(V1L1));

        groups.add(MV1);
        groups.add(MV2);
        groups.add(MV3);
        groups.add(MV4);
        groups.add(MV5);
        groups.add(MV5_2);
        groups.add(MV6);
        groups.add(MV7);
        groups.add(MV7_2);
        groups.add(MV8);
        groups.add(MV9);
        groups.add(MV10);
        groups.add(MV10_2);
        groups.add(MV11);
        groups.add(MV12);

        groups.add(C1);
        groups.add(C2);
        groups.add(C3);
        groups.add(C4);

        groups.add(F1_1);
        groups.add(F1_2);
        groups.add(F2_1);
        groups.add(F2_2);
        groups.add(F3_1);
        groups.add(F3_2);
        groups.add(F4_1);
        groups.add(F4_2);
        groups.add(F5_1);
        groups.add(F5_2);
        groups.add(F6_1);
        groups.add(F6_2);
        groups.add(F7_1);
        groups.add(F7_2);
        groups.add(F8_1);
        groups.add(F8_2);

        groups.add(V1);
        groups.add(V2);

        bridgeGroup.add(B1L1);

        gateGroup.add(B1G1);
        gateGroup.add(B1G2);

        bridgeExcludedLights.add(MV3L1);
        bridgeExcludedLights.add(MV7L1);
        bridgeExcludedLights.add(MV7L2);
        bridgeExcludedLights.add(MV10L1);
        bridgeExcludedLights.add(MV10L2);

        footGroup.add(new ArrayList<>(Arrays.asList(MV5L1,MV5L2)));
        footGroup.add(new ArrayList<>(Arrays.asList(MV7L1,MV7L2)));
        footGroup.add(new ArrayList<>(Arrays.asList(MV10L1,MV10L2)));
        footGroup.add(new ArrayList<>(Arrays.asList(F1L1,F2L1,F1L2,F2L2)));
        footGroup.add(new ArrayList<>(Arrays.asList(F3L1,F4L1,F3L2,F4L2)));
        footGroup.add(new ArrayList<>(Arrays.asList(F5L1,F6L1,F5L2,F6L2)));
        footGroup.add(new ArrayList<>(Arrays.asList(F7L1,F8L1,F7L2,F8L2)));
    }

    public List<List<TrafficLight>> getGroups() {
        return groups;
    }

    public List<TrafficLight> getTrafficLights() {
        return trafficLights;
    }

    public List<TrafficLight> getBridgeGroup() { return bridgeGroup; }

    public List<TrafficLight> getBridgeExcludedLights(){
        return bridgeExcludedLights;
    }

    public List<List<TrafficLight>> getFootGroup() {
        return footGroup;
    }

    public List<TrafficLight> getGateGroup() {
        return gateGroup;
    }
}
