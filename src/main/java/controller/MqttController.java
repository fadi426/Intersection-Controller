package controller;

import model.TrafficSensor;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import javax.swing.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MqttController implements MqttCallback {

	private static final String brokerUrl = "tcp://broker.0f.nl:1883";
	private static final String clientId = "Groep8C";
	private static final MemoryPersistence persistence = new MemoryPersistence();
	private TrafficSensorController trafficSensorController;
	private TrafficController trafficController;
	private MqttClient mqttClient = null;
	private String mainTopic;
	private boolean simulatorConnectionLost = false;
	JTextArea jTextArea;
	private MqttConnectOptions connOpts = new MqttConnectOptions();

	public MqttController(String topic, JTextArea jTextArea, TrafficController trafficController, TrafficSensorController trafficSensorController) {
		try {
			this.trafficController = trafficController;
			this.trafficSensorController = trafficSensorController;
			mainTopic = topic;
			setLastWill();
			this.jTextArea = jTextArea;
			mqttClient = new MqttClient(brokerUrl, clientId, persistence);
			connOpts.setCleanSession(true);
			System.out.println("checking");
			System.out.println("Mqtt Connecting to broker: " + brokerUrl);
			mqttClient.connect(connOpts);
			System.out.println("Mqtt Connected");

			onConnect();
			mqttClient.setCallback(this);
			mqttClient.subscribe(topic + "/#");

			System.out.println("Subscribed");
			System.out.println("Listening");
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

	/**
	 * Resets the scheduler threads and sends the initial states of the intersection together with the "onconnect" message
	 */
	public void onConnect(){
            trafficController.setMqttController(this);
			trafficController.resetThread();
			String publishMsg = "";
			publishMessage(mainTopic + "/" + "features" + "/" + "lifecycle" + "/" + "controller/" + "onconnect", publishMsg);
			trafficController.initState();
	}

	/**
	 * @param topic is the main topic of the channel that is getting used
	 * @param content is the message that has to be sent to the broker
	 */
    public void publishMessage(String topic, String content) {
	    if (mqttClient == null || simulatorConnectionLost == true || trafficController.getMqttController() == null)
	        return;
        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(1);
        try {
            mqttClient.publish(topic, message);
        } catch (MqttException e) {
            System.out.println("Failed to publish message");
        }
    }

	/**
	 * @param topic the main topic of the channel of the received message
	 * @param message the message content that has been received
	 * @throws InterruptedException when the mqtt client stops working
	 */
	public void messageArrived(String topic, MqttMessage message) throws InterruptedException {
		if (topic.contains("sensor")) {
			sensorTopicRegex(topic, message.toString());
//			System.out.println("Mqtt topic : " + topic);
//			System.out.println("Mqtt msg : " + message.toString());
		}
		if (topic.contains("simulator/onconnect")) {
			System.out.println("Mqtt topic : " + topic);
			System.out.println("Mqtt msg : " + message.toString());
			trafficController.resetThread();
			trafficController.initState();
			simulatorConnectionLost = false;
		}
		if (topic.contains("simulator/ondisconnect")) {
			System.out.println("Mqtt topic : " + topic);
			System.out.println("Mqtt msg : " + message.toString());
			simulatorConnectionLost = true;
		}

		String connected = "MQTT not Connected";
		if (mqttClient.isConnected())
			connected = "MQTT Connected";
		jTextArea.setText(connected + "\n");
		jTextArea.append("Group		GroupID		SensorID		State" + "\n");
		for (TrafficSensor sensor : trafficSensorController.getTrafficSensorList()){
			String sensorString = sensor.getGroup() + "		" + sensor.getGroupId() + "		" + sensor.getId() + "		" + sensor.getState() +"\n";
			jTextArea.append(sensorString);
		}
	}

	/**
	 * @param topic is the topic of the sensor
	 * @param message is the state of the given sensor
	 */
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

	/**
	 *  Disconnects the mqtt clients and sends a last will to let the simulator know that the connection has been lost
	 */
	public void disconnectMqtt(){
		try {
            sendLastWill();
			mqttClient.disconnect();
			mqttClient = null;
			System.out.println("Disconnected");
		} catch (MqttException e) {
			System.out.println("Failed to disconnect");
		}
	}

	/**
	 * Sets the last will on the broker, so even when the network connection has been lost, the "ondisconnect" message will be sent to the simulator
	 */
	public void setLastWill(){
		String topic = mainTopic + "/features/lifecycle/controller/ondisconnect";
		connOpts.setWill(topic, "Controller offline".getBytes(), 1, false);
	}

	/**
	 * Sends the last will message
	 */
	public void sendLastWill(){
        String topic = mainTopic + "/features/lifecycle/controller/ondisconnect";
	    publishMessage(topic, "");
    }
}