package controller;

import model.TrafficLight;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrafficLightController {

    private List<TrafficLight> trafficLights = new ArrayList<>();
    private List<List<TrafficLight>> excludedGroups = new ArrayList<>();
    private List<TrafficLight> bridgeExcludedLights = new ArrayList<>();
    private List<List<TrafficLight>> footPairs = new ArrayList<>();
    private List<TrafficLight> gateGroup = new ArrayList<>();
    private List<TrafficLight> firstFootLight = new ArrayList<>();

    private TrafficLight MV1L1 = new TrafficLight("motor_vehicle", "1", "1", "0", new ArrayList<>(Arrays.asList("1", "2")));
    private TrafficLight MV2L1 = new TrafficLight("motor_vehicle", "2", "1", "0", new ArrayList<>(Arrays.asList("1", "2")));
    private TrafficLight MV3L1 = new TrafficLight("motor_vehicle", "3", "1", "0", new ArrayList<>(Arrays.asList("1", "2")));
    private TrafficLight MV4L1 = new TrafficLight("motor_vehicle", "4", "1", "0", new ArrayList<>(Arrays.asList("1", "2", "3")));
    private TrafficLight MV5L1 = new TrafficLight("motor_vehicle", "5", "1", "0", new ArrayList<>(Arrays.asList("1", "2", "3")));
    private TrafficLight MV5L2 = new TrafficLight("motor_vehicle", "5", "2", "0", new ArrayList<>(Arrays.asList("4", "5", "6")));
    private TrafficLight MV6L1 = new TrafficLight("motor_vehicle", "6", "1", "0", new ArrayList<>(Arrays.asList("1", "2", "3")));
    private TrafficLight MV7L1 = new TrafficLight("motor_vehicle", "7", "1", "0", new ArrayList<>(Arrays.asList("1", "2")));
    private TrafficLight MV7L2 = new TrafficLight("motor_vehicle", "7", "2", "0", new ArrayList<>(Arrays.asList("3", "4")));
    private TrafficLight MV8L1 = new TrafficLight("motor_vehicle", "8", "1", "0", new ArrayList<>(Arrays.asList("1", "2")));
    private TrafficLight MV9L1 = new TrafficLight("motor_vehicle", "9", "1", "0", new ArrayList<>(Arrays.asList("1", "2")));
    private TrafficLight MV10L1 = new TrafficLight("motor_vehicle", "10", "1", "0", new ArrayList<>(Arrays.asList("1", "2", "3")));
    private TrafficLight MV10L2 = new TrafficLight("motor_vehicle", "10", "2", "0", new ArrayList<>(Arrays.asList("4", "5", "6")));
    private TrafficLight MV11L1 = new TrafficLight("motor_vehicle", "11", "1", "0", new ArrayList<>(Arrays.asList("1", "2")));
    private TrafficLight MV12L1 = new TrafficLight("motor_vehicle", "12", "1", "0", new ArrayList<>(Arrays.asList("1")));

    private TrafficLight C1L1 = new TrafficLight("cycle", "1", "1", "0", new ArrayList<>(Arrays.asList("1")));
    private TrafficLight C2L1 = new TrafficLight("cycle", "2", "1", "0", new ArrayList<>(Arrays.asList("1")));
    private TrafficLight C3L1 = new TrafficLight("cycle", "3", "1", "0", new ArrayList<>(Arrays.asList("1")));
    private TrafficLight C4L1 = new TrafficLight("cycle", "4", "1", "0", new ArrayList<>(Arrays.asList("1")));


    private TrafficLight F1L1 = new TrafficLight("foot", "1", "1", "0", new ArrayList<>(Arrays.asList("1")));
    private TrafficLight F1L2 = new TrafficLight("foot", "1", "2", "0", new ArrayList<>(Arrays.asList("2")));
    private TrafficLight F2L1 = new TrafficLight("foot", "2", "1", "0", new ArrayList<>(Arrays.asList("1")));
    private TrafficLight F2L2 = new TrafficLight("foot", "2", "2", "0", new ArrayList<>(Arrays.asList("2")));
    private TrafficLight F3L1 = new TrafficLight("foot", "3", "1", "0", new ArrayList<>(Arrays.asList("1")));
    private TrafficLight F3L2 = new TrafficLight("foot", "3", "2", "0", new ArrayList<>(Arrays.asList("2")));
    private TrafficLight F4L1 = new TrafficLight("foot", "4", "1", "0", new ArrayList<>(Arrays.asList("1")));
    private TrafficLight F4L2 = new TrafficLight("foot", "4", "2", "0", new ArrayList<>(Arrays.asList("2")));
    private TrafficLight F5L1 = new TrafficLight("foot", "5", "1", "0", new ArrayList<>(Arrays.asList("1")));
    private TrafficLight F5L2 = new TrafficLight("foot", "5", "2", "0", new ArrayList<>(Arrays.asList("2")));
    private TrafficLight F6L1 = new TrafficLight("foot", "6", "1", "0", new ArrayList<>(Arrays.asList("1")));
    private TrafficLight F6L2 = new TrafficLight("foot", "6", "2", "0", new ArrayList<>(Arrays.asList("2")));
    private TrafficLight F7L1 = new TrafficLight("foot", "7", "1", "0", new ArrayList<>(Arrays.asList("1")));
    private TrafficLight F7L2 = new TrafficLight("foot", "7", "2", "0", new ArrayList<>(Arrays.asList("2")));
    private TrafficLight F8L1 = new TrafficLight("foot", "8", "1", "0", new ArrayList<>(Arrays.asList("1")));
    private TrafficLight F8L2 = new TrafficLight("foot", "8", "2", "0", new ArrayList<>(Arrays.asList("2")));

    private TrafficLight V1L1 = new TrafficLight("vessel", "1", "1", "0", new ArrayList<>(Arrays.asList("1")));
    private TrafficLight V2L1 = new TrafficLight("vessel", "2", "1", "0", new ArrayList<>(Arrays.asList("1")));

    private TrafficLight B1L1 = new TrafficLight("bridge", "1", "1", "0", new ArrayList<>(Arrays.asList("1")));

    private TrafficLight B1G1 = new TrafficLight("bridge", "1", "1", "0", new ArrayList<>(Arrays.asList("1")));
    private TrafficLight B1G2 = new TrafficLight("bridge", "1", "2", "0", new ArrayList<>(Arrays.asList("2")));

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

        trafficLights.add(B1L1);


        List<TrafficLight> MV1 = new ArrayList<>(Arrays.asList(MV5L1, MV5L2, C1L1, C4L1, F1L1, F1L2, F2L2, F7L1, F8L1, F8L2));
        List<TrafficLight> MV2 = new ArrayList<>(Arrays.asList(MV5L1, MV5L2, MV6L1, MV8L1, MV9L1, MV10L1, MV10L2, MV11L1, MV12L1, C1L1, C3L1, F1L1, F1L2, F2L2, F5L1, F6L1, F6L2));
        List<TrafficLight> MV3 = new ArrayList<>(Arrays.asList(MV5L1, MV5L2, MV6L1, MV7L1, MV7L2, MV8L1, MV10L1, MV10L2, MV11L1, C1L1, C2L1, F1L1, F1L2, F2L2, F3L1, F4L1, F4L2));
        List<TrafficLight> MV4 = new ArrayList<>(Arrays.asList(MV8L1, MV11L1, C1L1, C2L1, F1L1, F2L1, F2L2, F3L1, F3L2, F4L2));
        List<TrafficLight> MV5 = new ArrayList<>(Arrays.asList(MV1L1, MV2L1, MV3L1, MV8L1, MV11L1, C2L1, C4L1, F3L1, F3L2, F4L2, F7L1, F8L1, F8L2));
        List<TrafficLight> MV5_2 = new ArrayList<>(Arrays.asList(MV1L1, MV2L1, MV3L1, MV8L1, MV11L1, C2L1, C4L1, F3L1, F3L2, F4L2, F7L1, F8L1, F8L2));
        List<TrafficLight> MV6 = new ArrayList<>(Arrays.asList(MV2L1, MV3L1, MV8L1, MV9L1, MV10L1, MV10L2, MV12L1, C2L1, C3L1, F3L1, F3L2, F4L2, F5L1, F6L1, F6L2));
        List<TrafficLight> MV7 = new ArrayList<>(Arrays.asList(MV3L1, MV10L1, MV10L2, MV12L1, C2L1, F3L1, F4L1, F4L2, F5L1, F5L2, F6L2, C3L1));
        List<TrafficLight> MV7_2 = new ArrayList<>(Arrays.asList(MV3L1, MV10L1, MV10L2, MV12L1, C2L1, F3L1, F4L1, F4L2, F5L1, F5L2, F6L2, C3L1));
        List<TrafficLight> MV8 = new ArrayList<>(Arrays.asList(MV1L1, MV2L1, MV3L1, MV4L1, MV5L1, MV5L2, MV6L1, MV10L1, MV10L2, MV11L1, MV12L1, C1L1, C3L1, C4L1, F1L1, F2L1, F2L2, F5L1, F5L2, F6L2, F7L1, F8L1, F8L2));
        List<TrafficLight> MV9 = new ArrayList<>(Arrays.asList(MV2L1, MV6L1, MV12L1, C3L1, C4L1, F5L1, F6L1, F6L2, F7L1, F7L2, F8L2));
        List<TrafficLight> MV10 = new ArrayList<>(Arrays.asList(MV2L1, MV3L1, MV6L1, MV7L1, MV7L2, MV8L1, C2L1, C4L1, F3L1, F4L1, F4L2, F7L1, F7L2, F8L2));
        List<TrafficLight> MV10_2 = new ArrayList<>(Arrays.asList(MV2L1, MV3L1, MV6L1, MV7L1, MV7L2, MV8L1, C2L1, C4L1, F3L1, F4L1, F4L2, F7L1, F7L2, F8L2));
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

        List<TrafficLight> B1_1 = new ArrayList<>(Arrays.asList());

        excludedGroups.add(MV1);
        excludedGroups.add(MV2);
        excludedGroups.add(MV3);
        excludedGroups.add(MV4);
        excludedGroups.add(MV5);
        excludedGroups.add(MV5_2);
        excludedGroups.add(MV6);
        excludedGroups.add(MV7);
        excludedGroups.add(MV7_2);
        excludedGroups.add(MV8);
        excludedGroups.add(MV9);
        excludedGroups.add(MV10);
        excludedGroups.add(MV10_2);
        excludedGroups.add(MV11);
        excludedGroups.add(MV12);

        excludedGroups.add(C1);
        excludedGroups.add(C2);
        excludedGroups.add(C3);
        excludedGroups.add(C4);

        excludedGroups.add(F1_1);
        excludedGroups.add(F1_2);
        excludedGroups.add(F2_1);
        excludedGroups.add(F2_2);
        excludedGroups.add(F3_1);
        excludedGroups.add(F3_2);
        excludedGroups.add(F4_1);
        excludedGroups.add(F4_2);
        excludedGroups.add(F5_1);
        excludedGroups.add(F5_2);
        excludedGroups.add(F6_1);
        excludedGroups.add(F6_2);
        excludedGroups.add(F7_1);
        excludedGroups.add(F7_2);
        excludedGroups.add(F8_1);
        excludedGroups.add(F8_2);

        excludedGroups.add(V1);
        excludedGroups.add(V2);
        excludedGroups.add(B1_1);

        gateGroup.add(B1G1);
        gateGroup.add(B1G2);

        bridgeExcludedLights.add(MV3L1);
        bridgeExcludedLights.add(MV7L1);
        bridgeExcludedLights.add(MV7L2);
        bridgeExcludedLights.add(MV10L1);
        bridgeExcludedLights.add(MV10L2);

        firstFootLight.add(F1L1);
        firstFootLight.add(F2L2);
        firstFootLight.add(F3L1);
        firstFootLight.add(F4L2);
        firstFootLight.add(F5L1);
        firstFootLight.add(F6L2);
        firstFootLight.add(F7L1);
        firstFootLight.add(F8L2);

        footPairs.add(new ArrayList<>(Arrays.asList(F1L1, F1L2, F2L1, F2L2)));
        footPairs.add(new ArrayList<>(Arrays.asList(F1L1, F1L2, F2L1, F2L2)));
        footPairs.add(new ArrayList<>(Arrays.asList(F3L1, F3L2, F4L1, F4L2)));
        footPairs.add(new ArrayList<>(Arrays.asList(F3L1, F3L2, F4L1, F4L2)));
        footPairs.add(new ArrayList<>(Arrays.asList(F5L1, F5L2, F6L1, F6L2)));
        footPairs.add(new ArrayList<>(Arrays.asList(F5L1, F5L2, F6L1, F6L2)));
        footPairs.add(new ArrayList<>(Arrays.asList(F7L1, F7L2, F8L1, F8L2)));
        footPairs.add(new ArrayList<>(Arrays.asList(F7L1, F7L2, F8L1, F8L2)));
    }

    public List<List<TrafficLight>> getExcludedGroups() {
        return excludedGroups;
    }

    public List<TrafficLight> getTrafficLights() {
        return trafficLights;
    }

    public List<TrafficLight> getBridgeGroup() {
        List<TrafficLight> bridgeGroup = new ArrayList<>();
        for (TrafficLight light : trafficLights){
            if (light.getGroup().equals("bridge"))
                bridgeGroup.add(light);
        }
        return bridgeGroup;
    }

    /**
     * @return a list of the lights that can not be on green at the same time
     */
    public List<TrafficLight> getBridgeExcludedLights(){
        return bridgeExcludedLights;
    }

    /**
     * @return a list of lights of the pairs of the foot traffic users that are needed to make a foot user walk from 1 side of the road to the other side
     */
    public List<List<TrafficLight>> getFootPairs() {
        return footPairs;
    }

    /**
     * @return a list of the lights of the gates of the bridge
     */
    public List<TrafficLight> getGateGroup() {
        return gateGroup;
    }

    /**
     * @return a list of the first lights that a foot user will hit when wanting to walk from 1 side to the other
     */
    public List<TrafficLight> getFirstFootLight() { return firstFootLight; }

    /**
     * @param light is the to be added light to the trafficLightList
     * @return a list of all the lights with the same group and groupID
     */
    public List<TrafficLight> findDoubleLight(TrafficLight light) {
        List<TrafficLight> lights = new ArrayList<>();
        for (TrafficLight l : trafficLights){
            if (light.getGroup().equals(l.getGroup()) && light.getGroupId().equals(l.getGroupId()))
                lights.add(l);
        }
        return lights;
    }
}
