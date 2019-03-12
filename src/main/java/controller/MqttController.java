package controller;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Timer;
import java.util.TimerTask;

public class MqttController implements MqttCallback {

	private static final String brokerUrl = "tcp://broker.0f.nl:1883";
	private static final String USERNAME = "ebfphetc";
	private static final String PASSWORD = "YZUvxaLPT5Nw";
	private static final String clientId = "JavaSample";
//	private static final String topic = "8/motor_vehicle/1/light/1";
	private static final int qos = 2;
	private static final MemoryPersistence persistence = new MemoryPersistence();
	private TrafficSensorController trafficSensorController = new TrafficSensorController();
	private MqttClient sampleClient = null;

	public void subscribe(String topic) {
		try {
			sampleClient = new MqttClient(brokerUrl, clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			System.out.println("checking");
			System.out.println("Mqtt Connecting to broker: " + brokerUrl);
			sampleClient.connect(connOpts);
			System.out.println("Mqtt Connected");

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

	public void messageArrived(String topic, MqttMessage message) throws Exception {

		System.out.println("Mqtt topic : " + topic);
		System.out.println("Mqtt msg : " + message.toString());
	}

	public void publishMessage(String topic, String content){
		System.out.println("Publishing message: " + content);
		MqttMessage message = new MqttMessage(content.getBytes());
		message.setQos(qos);
		try {
			sampleClient.publish(topic, message);
			System.out.println("Message published");
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