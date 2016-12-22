package com.imbd.spbau.ui;


import com.imbd.spbau.model.Settings;
import com.imbd.spbau.model.*;

import javax.swing.*;
import java.awt.*;

/**
 * Class for first frame of chat on which you can choose client/server mode or change settings
 */
public class StartFrame {

    private static final Dimension START_FRAME_SIZE = new Dimension(500, 500);

    public StartFrame() {
        JFrame frame = new JFrame("StartFrame");
        frame.setBackground(Color.white);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(START_FRAME_SIZE);

        JButton serverButton = new JButton("<html><b><font color=\"red\">Server</font></b></html>");
        serverButton.addActionListener(e -> {
            Controller controller = new Controller(Controller.Type.SERVER);
            MessengerServer messengerServer = new MessengerServer(controller, Settings.getInstance().getConnectionPort());
            new ChatFrame(controller, messengerServer::start, messengerServer::stop);
        });
        JButton clientButton = new JButton("<html><b><font color=\"blue\">Client</font></b></html>");
        clientButton.addActionListener(e -> {
            Controller controller = new Controller(Controller.Type.CLIENT);
            Client client = new Client(controller);
            new ChatFrame(controller, () -> client.connect(Settings.getInstance().getServerIP(), Settings.getInstance().getServerPort()), client::disconnect);
        });
        JButton settingsButton = new JButton("<html><b><font color=\"black\">Settings</font></b></html>");
        settingsButton.addActionListener(e -> new SettingsFrame(Settings.getInstance()));

        JPanel panel = new JPanel();
        panel.add(serverButton);
        panel.add(clientButton);
        panel.add(settingsButton);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }
}
