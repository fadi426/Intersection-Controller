package controller;

import model.TrafficSensor;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
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
	private MqttClient sampleClient = null;
	private String mainTopic;

	public void subscribe(String topic) {
		try {
			sampleClient = new MqttClient(brokerUrl, clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			System.out.println("checking");
			System.out.println("Mqtt Connecting to broker: " + brokerUrl);
			sampleClient.connect(connOpts);
			System.out.println("Mqtt Connected");

			mainTopic = mainTopicRegex(topic);

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

	}

	public void deliveryComplete(IMqttDeliveryToken arg0) {

	}

	public void messageArrived(String topic, MqttMessage message) throws InterruptedException {

		System.out.println("Mqtt topic : " + topic);
		System.out.println("Mqtt msg : " + message.toString());
		sensorTopicRegex(topic, message.toString());
		changeLights();


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

	public void sensorTopicRegex(String topic, String message) {
		String pattern = "(\\d+)\\/(\\w+)\\/(\\d+)\\/(\\w+)\\/(\\d+)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(topic);
		if (m.find( )) {
//			System.out.println("group: " + m.group(2) );
//			System.out.println("groupId: " + m.group(3) );
//			System.out.println("sensor Id: " + m.group(5) );
			trafficSensorController.changeTrafficSensor(m.group(2), m.group(3), m.group(5), message);
			System.out.println("DONE");
		} else {
			System.out.println("NO MATCH");
		}
	}

	public void changeLights() throws InterruptedException {

		List<TrafficSensor> trafficSensorList = trafficSensorController.getTrafficSensorList();
		if (trafficSensorList.size() > 0) {
			for (TrafficSensor sensor : trafficSensorList){
				if (sensor.getState().equals("1")){
					String publishMsg = mainTopic + "/" + sensor.getGroup() + "/" + sensor.getGroupId()  + "/" + "light/" + sensor.getId();
					System.out.println(publishMsg);
					publishMessage(publishMsg, "2");
					Thread.sleep(500);
//					publishMessage(publishMsg, "1");
//					Thread.sleep(500);
					publishMessage(publishMsg, "0");
					Thread.sleep(500);
					System.out.println(trafficSensorList.size());
				}
			}
		}
	}

	public void publishMessage(String topic, String content){
		System.out.println("Publishing message: " + content);
		MqttMessage message = new MqttMessage(content.getBytes());
		message.setQos(qos);
		try {
			sampleClient.publish(topic, message);
			//System.out.println("Message published");
		} catch (MqttException e) {
			System.out.println("Failed to publish message");
		}
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

	public void Scheduler(final String topic) {
		final String[] content = {"0"};
		class Scheduler extends TimerTask {
			public void run() {
				if (sampleClient != null) {
					if (content[0].equals("0")) {
						content[0] = "2";
					} else {
						content[0] = "0";
					}
					publishMessage(topic, content[0]);
				}
			}
		}
		Timer timer = new Timer();
		timer.schedule(new Scheduler(), 0, 5000);
	}

	private static MqttConnectOptions setUpConnectionOptions(String username, String password) {
		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(true);
		connOpts.setUserName(username);
		connOpts.setPassword(password.toCharArray());
		return connOpts;
	}

}