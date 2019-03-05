import controller.MqttController;
import org.eclipse.paho.client.mqttv3.MqttException;

public class Launcher {
    public static void main(String[] args) {
        System.out.println("Hello, World!");


        try {
            MqttController.mqttConnection();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
