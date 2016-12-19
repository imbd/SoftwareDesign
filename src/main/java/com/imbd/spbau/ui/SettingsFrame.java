package com.imbd.spbau.ui;


import com.imbd.spbau.model.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Class for frame with editable settings
 */
public class SettingsFrame {

    private static final String settingsArray[] = {"User name", "Server IP", "Server port", "Connection port"};
    private static final String[] SEPARATOR = {".", ".", ".", ""};
    private static final int USER_NAME_NUMBER = 0;
    private static final int SERVER_IP_NUMBER = 1;
    private static final int SERVER_PORT_NUMBER = 2;
    private static final int CONNECTION_PORT_NUMBER = 3;
    private static final Dimension SETTINGS_FRAME_SIZE = new Dimension(400, 150);
    private static final int DISTANCE_IN_LAYOUT = 10;


    SettingsFrame(Settings settings) {
        JTextField[] settingsValue = new JTextField[settingsArray.length];

        JFrame frame = new JFrame("SettingsFrame");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                settings.setUserName(settingsValue[USER_NAME_NUMBER].getText());
                try {
                    InetAddress ip = InetAddress.getByName(settingsValue[SERVER_IP_NUMBER].getText());
                    byte[] bytes = ip.getAddress();
                    settings.setServerIP(bytes);
                } catch (UnknownHostException e1) {
                    e1.printStackTrace();
                }
                settings.setServerPort(Integer.valueOf(settingsValue[SERVER_PORT_NUMBER].getText()));
                settings.setConnectionPort(Integer.valueOf(settingsValue[CONNECTION_PORT_NUMBER].getText()));
                frame.setVisible(false);
                frame.dispose();
            }
        });

        frame.setSize(SETTINGS_FRAME_SIZE);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(settingsArray.length, 0, DISTANCE_IN_LAYOUT, DISTANCE_IN_LAYOUT));

        settingsValue[USER_NAME_NUMBER] = new JTextField(settings.getUserName());
        settingsValue[SERVER_IP_NUMBER] = new JTextField();
        int index = 0;
        for (byte b : settings.getServerIP()) {
            settingsValue[SERVER_IP_NUMBER].setText(settingsValue[SERVER_IP_NUMBER].getText() + String.valueOf(b & 0xFF) + SEPARATOR[index]);
            index++;
        }
        settingsValue[SERVER_PORT_NUMBER] = new JTextField(String.valueOf(settings.getServerPort()));
        settingsValue[CONNECTION_PORT_NUMBER] = new JTextField(String.valueOf(settings.getConnectionPort()));
        for (int i = 0; i < settingsArray.length; i++) {
            JLabel label = new JLabel(settingsArray[i]);
            panel.add(label);
            panel.add(settingsValue[i]);
        }

        frame.getContentPane().add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
