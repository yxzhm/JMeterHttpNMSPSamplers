package com.yxzhm.jmeter;

import javax.swing.*;
import java.awt.*;

import static javax.swing.BoxLayout.X_AXIS;
import static javax.swing.BoxLayout.Y_AXIS;

public class OpenWebSocketSamplerGuiPanel extends JPanel {

    public OpenWebSocketSamplerGuiPanel() {
        initGui();
    }

    JComboBox<String> protocolSelector;
    JLabel serverLabel, portLabel, pathLabel;
    JTextField serverField, portField, pathField;


    private void initGui() {
        JPanel boxPanel = new JPanel();
        boxPanel.setLayout(new BoxLayout(boxPanel, Y_AXIS));
        boxPanel.setBorder(BorderFactory.createTitledBorder("Connection"));
        boxPanel.add(Box.createVerticalStrut(3));

        JPanel urlPanel = new JPanel();
        urlPanel.setLayout(new BoxLayout(urlPanel, X_AXIS));
        urlPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0),
                BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Server URL"), BorderFactory.createEmptyBorder(3, 5, 5, 0))));

        protocolSelector = new JComboBox<String>(new String[]{"ws", "wss"});
        urlPanel.add(protocolSelector);
        urlPanel.add(Box.createHorizontalStrut(10));
        serverLabel = new JLabel("Server name or IP:");
        urlPanel.add(serverLabel);
        serverField = new JTextField();
        serverField.setColumns(10);
        serverField.setMaximumSize(new Dimension(Integer.MAX_VALUE, serverField.getMinimumSize().height));
        urlPanel.add(serverField);
        portLabel = new JLabel("Port:");

        urlPanel.add(portLabel);
        portField = new JTextField();
        portField.setText("80");
//        addIntegerRangeCheck(portField, 1, 65535);
        portField.setColumns(5);
        portField.setMaximumSize(portField.getPreferredSize());
        urlPanel.add(portField);
        pathLabel = new JLabel("Path:");
        urlPanel.add(pathLabel);
        pathField = new JTextField();
        pathField.setColumns(20);
        pathField.setMaximumSize(new Dimension(Integer.MAX_VALUE, pathField.getMinimumSize().height));
        urlPanel.add(pathField);
        boxPanel.add(urlPanel);

        this.setLayout(new BorderLayout());
        add(boxPanel, BorderLayout.NORTH);

    }

    public boolean getTLS() {
        return protocolSelector.getSelectedItem().equals("wss");
    }

    public void setTLS(boolean tls) {
        if (tls) {
            protocolSelector.setSelectedIndex(1);
        } else {
            protocolSelector.setSelectedIndex(0);
        }

    }

    public String getServer() {
        return serverField.getText();
    }

    public void setServer(String server){
        serverField.setText(server);
    }

    public String getPort() {
        return portField.getText();

    }

    public void setPort(String port){
        portField.setText(port);
    }

    public String getPath() {
        return pathField.getText();
    }

    public void setPath(String path){
        pathField.setText(path);
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        frame.getContentPane().add(new OpenWebSocketSamplerGuiPanel());
        frame.setVisible(true);
    }
}
