package controller;

import java.util.Timer;
import java.util.TimerTask;

public class MainController {

    public static void MainController(){

        new MqttListenerController().subscribe();
        final String[] content = {"0"};
        class Scheduler extends TimerTask {
            public void run() {
                if (content[0].equals("0")){
                    content[0] = "2";
                }
                else {
                    content[0] = "0";
                }
                new MqttPublisherController().publish(content[0]);
            }
        }
        Timer timer = new Timer();
        timer.schedule(new Scheduler(), 0, 5000);
    }
}
