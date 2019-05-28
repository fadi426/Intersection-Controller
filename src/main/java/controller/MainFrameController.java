package controller;

import view.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrameController {

    private MainFrame mainFrame;
    private JButton connectBtn;
    private JButton disconnectBtn;
    private JTextArea mqttConnectionTA;
    private JTextField topicTF;
    private Boolean mqttConnected = false;
    private MqttController mqttController;

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
    }

    private class connectBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (mqttConnected == false) {
                mqttController = new MqttController();
                mqttController.subscribe(topicTF.getText(), mqttConnectionTA);
                mqttConnected = true;
                mqttConnectionTA.append("MQTT Connected\n");
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
}
