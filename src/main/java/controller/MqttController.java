package controller;

import model.TrafficLight;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MqttController implements MqttCallback {

	private static final String brokerUrl = "tcp://broker.0f.nl:1883";
	private static final String USERNAME = "ebfphetc";
	private static final String PASSWORD = "YZUvxaLPT5Nw";
	private static final String clientId = "Groep8C";
//	private static final String topic = "8/motor_vehicle/1/light/1";
	private static final int qos = 1;
	private static final MemoryPersistence persistence = new MemoryPersistence();
	private TrafficSensorController trafficSensorController = new TrafficSensorController();
	private TrafficLightController trafficLightController = new TrafficLightController();
	private MqttClient sampleClient = null;
	private String mainTopic;
	private String tempMessage;
	private String tempTopic;
	private MqttConnectOptions connOpts = new MqttConnectOptions();

	public void subscribe(String topic) {
		try {
			mainTopic = mainTopicRegex(topic);
			trafficLightController.start();
			sampleClient = new MqttClient(brokerUrl, clientId, persistence);
			connOpts.setCleanSession(true);
			System.out.println("checking");
			System.out.println("Mqtt Connecting to broker: " + brokerUrl);
			sampleClient.connect(connOpts);
			System.out.println("Mqtt Connected");

			trafficLightController.setInfoThread(trafficSensorController, sampleClient, mainTopic);
			onConnect();
			sampleClient.setCallback(this);
			sampleClient.subscribe(topic);

			System.out.println("Subscribed");
			System.out.println("Listening");
			//Scheduler(topic);
		} catch (MqttException me) {
			System.out.println("Mqtt reason " + me.getReasonCode());
			System.out.println("Mqtt msg " + me.getMessage());
			System.out.println("Mqtt loc " + me.getLocalizedMessage());
			System.out.println("Mqtt cause " + me.getCause());
			System.out.println("Mqtt excep " + me);
		}

	}

	public void connectionLost(Throwable arg0) {
		System.out.println("connection lost");
	}

	public void deliveryComplete(IMqttDeliveryToken arg0) {

	}

	public void onConnect(){
			String publishMsg = "";
			trafficLightController.publishMessage(mainTopic + "/" + "features" + "/" + "lifecycle" + "/" + "controller/" + "onconnect", publishMsg);
			for (TrafficLight light : trafficLightController.getInitTrafficCases().getBridgeGroup())

			trafficLightController.sendMessage(light, "2");

			for (TrafficLight light : trafficLightController.getInitTrafficCases().getGateGroup()){
				trafficLightController.sendMessage(light, "0");
			}
	}

	public void messageArrived(String topic, MqttMessage message) throws InterruptedException {
		if (topic.contains("sensor")&& message.toString() != (tempMessage) && topic != (tempTopic)) {
			System.out.println("Mqtt topic : " + topic);
			System.out.println("Mqtt msg : " + message.toString());
			sensorTopicRegex(topic, message.toString());
		}
		if (topic.contains("simulator/onconnect")&& message.toString() != (tempMessage) && topic != (tempTopic)) {
			System.out.println("Mqtt topic : " + topic);
			System.out.println("Mqtt msg : " + message.toString());
			resetController();
		}
	}

	private void resetController() {
		trafficLightController.resetThread();
	}

	public void sensorTopicRegex(String topic, String message) {
		String pattern = "(\\d+)\\/(\\w+)\\/(\\d+)\\/(\\w+)\\/(\\d+)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(topic);
		if (m.find( )) {
			if (m.group(4).contains("sensor"))
				trafficSensorController.changeTrafficSensor(m.group(2), m.group(3), m.group(5), message);
		} else {

			System.out.println(topic + " = NO MATCH");
		}
	}

	public String mainTopicRegex(String topic){
		String pattern = "(\\d+)(.+)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(topic);
		if (m.find()){
			return m.group(1);
		}
		System.out.println("failed to parse topic, redirecting to default 8");
		return "8";
	}


	public void disconnectMqtt(){
		try {
			sampleClient.disconnect();
			sampleClient = null;
			System.out.println("Disconnected");
		} catch (MqttException e) {
			System.out.println("Failed to disconnected");
		}
	}

	private static MqttConnectOptions setUpConnectionOptions(String username, String password) {
		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(true);
		connOpts.setUserName(username);
		connOpts.setPassword(password.toCharArray());
		return connOpts;
	}
}