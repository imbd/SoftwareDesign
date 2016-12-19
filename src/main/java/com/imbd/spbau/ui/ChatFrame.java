package com.imbd.spbau.ui;

import com.imbd.spbau.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Class for frame on which communication happens
 */
public class ChatFrame {

    private static final Dimension WRITE_MESSAGE_MAX_SIZE = new Dimension(400, 550);
    private static final Dimension WRITE_MESSAGE_PREF_SIZE = new Dimension(400, 200);
    private static final Dimension CHAT_FRAME_SIZE = new Dimension(400, 800);

    private JTextArea chatHistory = new JTextArea();
    private JTextArea writeMessage = new JTextArea();
    private JLabel firstLabel = new JLabel();
    private String companionName = "Unknown";
    private String type = "client";
    private JLabel textLabel1 = new JLabel("Enter your text below");
    private JLabel textLabel2 = new JLabel("  ");
    private JButton sendButton;

    ChatFrame(Controller controller, Runnable onOpening, Runnable onClosing) {

        if (controller.getType() == Controller.Type.SERVER) {
            type = "server";
        }
        fillFunctions(controller);
        new Thread(onOpening::run).start();

        JFrame frame = new JFrame("ChatFrame");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(CHAT_FRAME_SIZE);
        sendButton = new JButton("<html><b><font color=\"black\">Send message</font></b></html>");
        sendButton.addActionListener(e -> {
                    if (controller.sendMessage(new Message(Message.SIMPLE_MESSAGE, writeMessage.getText()))) {
                        chatHistory.append("You: " + writeMessage.getText() + '\n');
                        writeMessage.setText("");
                    } else {
                        JOptionPane.showMessageDialog(frame, "There is no connection");
                    }
                }
        );
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onClosing.run();
            }
        });
        firstLabel.setText("New chat(as " + type + ")");
        chatHistory.setEditable(false);
        writeMessage.setMaximumSize(WRITE_MESSAGE_MAX_SIZE);
        writeMessage.setPreferredSize(WRITE_MESSAGE_PREF_SIZE);

        setAlignment();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(firstLabel);
        panel.add(new JScrollPane(chatHistory));
        panel.add(textLabel1);
        panel.add(writeMessage);
        panel.add(textLabel2);
        panel.add(sendButton);
        frame.getContentPane().add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void setAlignment() {
        sendButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        firstLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        textLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);
        textLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
        chatHistory.setAlignmentX(Component.CENTER_ALIGNMENT);
        writeMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void fillFunctions(Controller controller) {
        controller.afterGettingMessage((Message message) -> {
            if (message.getType() == Message.CONNECTION_START) {
                companionName = message.getText();
                firstLabel.setText("Chat with " + companionName + "(as " + type + ")");
            } else {
                chatHistory.append(companionName + ": " + message.getText() + '\n');
            }
        });
    }
}
