package com.imbd.spbau.model;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * Class that provides connection between UI and Model
 */
public class Controller {

    public enum Type {
        CLIENT,
        SERVER
    }

    private static final Logger logger = Logger.getLogger(Controller.class.getName());
    private static final int TIMEOUT_MS = 50;

    private Consumer<Message> handleGettingMessage;
    private Socket socket;
    private Type type;
    private boolean disconnected = false;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    public void setDisconnected(boolean value) {
        disconnected = value;
    }

    public Controller(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    /**
     * Getting socket's streams and performing connection with 'companion'
     *
     * @param socket controller's socket
     */
    public void run(Socket socket) {
        logger.info("Controller is running");
        disconnected = false;
        this.socket = socket;
        try {
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            logger.info("Couldn't get streams");
            return;
        }
        sendMessage(new Message(Message.CONNECTION_START, Settings.getInstance().getUserName()));
        while (!disconnected) {
            try {
                getMessage();
            } catch (Exception e) {
                //logger.info("Couldn't get message: repeated operation that will clog logs");
            }
        }
    }

    private Message getMessage() throws IOException {
        //logger.info("Try to get message: repeated operation that will clog logs");
        Message message = Message.read(inputStream);
        handleGettingMessage.accept(message);
        return message;
    }

    /**
     * Sending message
     *
     * @param message Message that we want to send
     * @return true if was successfully sent else false
     */
    public boolean sendMessage(Message message) {

        try {
            socket.setSoTimeout(TIMEOUT_MS);
            if (inputStream.read() == -1) {
                return false;
            }
        } catch (Exception e) {
            logger.info("Time is out in connection check");
        }
        logger.info("Sending message");
        try {
            message.write(outputStream);
        } catch (Exception e) {
            logger.info("Couldn't send message");
            return false;
        }
        return true;
    }

    /**
     * Specify operation that runs after receiving message
     *
     * @param handleGettingMessage operation that will be got from UI
     */
    public void afterGettingMessage(Consumer<Message> handleGettingMessage) {
        logger.info("Handle getting message");
        this.handleGettingMessage = handleGettingMessage;
    }

}
