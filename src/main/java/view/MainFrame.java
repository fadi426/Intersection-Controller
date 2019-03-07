package view;

import javax.swing.*;

public class MainFrame extends JFrame{

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 1000;
    private JButton connectBtn;
    private JButton disconnectBtn;
    private JTextArea mqttConnectionTA;
    private JPanel mainPanel;
    private JTextField topicTF;

    public MainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().add(mainPanel);
        setSize(WIDTH, HEIGHT);
        setLocation(550, 25);
        setVisible(true);
    }

    public JButton getConnectBtn() {
        return connectBtn;
    }

    public JButton getDisconnectBtn() {
        return disconnectBtn;
    }

    public JTextArea getMqttConnectionTA() {
        return mqttConnectionTA;
    }

    public JTextField getTopicTF() {
        return topicTF;
    }
}
