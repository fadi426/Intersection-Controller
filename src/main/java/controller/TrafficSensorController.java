package controller;

import model.TrafficLight;
import model.TrafficSensor;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class TrafficSensorController {

    private List<TrafficSensor> trafficSensorList = new ArrayList<TrafficSensor>();
    private List<TrafficSensor> tempTrafficSensorList = new ArrayList<TrafficSensor>();
    private List<TrafficLight> trafficLightsList = new ArrayList<TrafficLight>();

    public void changeTrafficSensor(String group, String groupId, String sensorId, String sensorValue) {
        boolean exists = false;
        if (trafficSensorList.size() >0) {
                for (TrafficSensor sensor : trafficSensorList) {
                    if (sensor.getId().equals(sensorId) && sensor.getGroupId().equals(groupId)) {
                        sensor.setState(sensorValue);
                        exists = true;
                    }
                }
                if (!exists){
                    tempTrafficSensorList.add(new TrafficSensor(group, groupId, sensorId, sensorValue));
                }
            }
            else{
                tempTrafficSensorList.add(new TrafficSensor(group, groupId, sensorId, sensorValue));
                System.out.println("????????????????????????????????");
            }
            trafficSensorList.addAll(tempTrafficSensorList);
            tempTrafficSensorList.clear();
            exists = false;
    }
    public List<TrafficSensor> getTrafficSensorList() {
        return trafficSensorList;
    }

    public List<TrafficSensor> getListFromIterator(ListIterator<TrafficSensor> iterator)
    {
        List<TrafficSensor> list = new ArrayList<>();

        // Create an empty list
        //ArrayList<String> names = new ArrayList<String>( Arrays.asList("alex", "brian", "charles") );
//        Iterator<Integer> iteraator = Arrays.asList(1, 2, 3, 4, 5).listIterator();
        // Add each element of iterator to the List
//        while(iterator.hasNext()) {
//            //list.add(iterator.previous());
//            System.out.println(iterator.next() + "<------------------------");
//        }
        //List<TrafficSensor> iterable  = (List<TrafficSensor>) iterator;
//        List<TrafficSensor> mutableList = StreamSupport
//                .stream(iterable.spliterator(), false)
//                .collect(Collectors.toList());
        //iteraator.forEachRemaining(list::add);

        // Return the List
        return list;
    }

}
