package controller;

import view.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainFrameController {

    private MainFrame mainFrame;
    private JButton connectBtn;
    private JButton disconnectBtn;
    private JTextArea mqttConnectionTA;
    private JTextField topicTF;
    private Boolean mqttConnected = false;
    private MqttController mqttController;

    private TrafficController trafficController;
    private TrafficSensorController trafficSensorController = new TrafficSensorController();

    public MainFrameController() {
        initComponents();
        initListeners();
    }

    public void showMainFrameWindow() {
        mainFrame.setVisible(true);
    }

    private void initComponents() {
        mainFrame = new MainFrame();
        connectBtn = mainFrame.getConnectBtn();
        disconnectBtn = mainFrame.getDisconnectBtn();
        mqttConnectionTA = mainFrame.getMqttConnectionTA();
        topicTF = mainFrame.getTopicTF();
    }

    private void initListeners() {
        connectBtn.addActionListener(new connectBtnListener());
        disconnectBtn.addActionListener(new disconnectBtnListener());
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (mqttController == null)
                    return;
                mqttController.sendLastWill();
            }
        });
    }

    private class connectBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (mqttConnected == false) {
                String mainTopic = mainTopicRegex(topicTF.getText());
                trafficController = new TrafficController(trafficSensorController, mainTopic);
                mqttController = new MqttController(mainTopic, mqttConnectionTA, trafficController, trafficSensorController);
                mqttConnected = true;
                mqttConnectionTA.append("MQTT Connected\n");
                trafficController.start();
                trafficController.initState();
            }
            else{
                mqttConnectionTA.append("MQTT not connected\n");;
            }
        }
    }
    private class disconnectBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            mqttController.disconnectMqtt();
            mqttConnected = false;
            mqttConnectionTA.append("MQTT not connected\n");
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
}
