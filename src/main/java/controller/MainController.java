package controller;

public class MainController {

    public static void MainController(){
        new MqttPublisherController().publish();
        new MqttListenerController().subscribe();
    }
}
