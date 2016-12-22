package com.imbd.spbau.model;


import com.imbd.spbau.proto.ProtoMessage;
import io.grpc.stub.StreamObserver;

import java.util.Timer;
import java.util.TimerTask;
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
    private static final long DELAY = 500;
    private static final long TIMEOUT = 1000;

    private Consumer<MessageInterface> handleGettingMessage;
    private Consumer<MessageInterface> handleTypingNotification;
    private Type type;
    private long lastTypingTime;
    private long lastConnectionCheckTime;
    private String userName;

    private StreamObserver<ProtoMessage> outputStreamObserver;
    private StreamObserver<ProtoMessage> typingNotificationObserver;

    /**
     * Setting outputStreamObserver value
     *
     * @param outputStreamObserver value to set
     */
    public void setOutputStreamObserver(StreamObserver<ProtoMessage> outputStreamObserver) {
        this.outputStreamObserver = outputStreamObserver;
    }

    /**
     * Setting typingNotificationObserver value
     *
     * @param typingNotificationObserver value to set
     */
    public void setTypingNotificationObserver(StreamObserver<ProtoMessage> typingNotificationObserver) {
        this.typingNotificationObserver = typingNotificationObserver;
    }


    public Controller(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    /**
     * Getting message
     *
     * @param message message value
     */
    public void getMessage(MessageInterface message) {
        if (message.getType() == MessageInterface.CHECK_CONNECTION) {
            lastConnectionCheckTime = System.currentTimeMillis();
        } else {
            handleGettingMessage.accept(message);
        }
    }

    /**
     * Getting typing notification
     *
     * @param message message value
     */
    public void getTypingNotification(MessageInterface message) {
        logger.info("Getting typing notification");
        if (!message.getText().isEmpty()) {
            userName = message.getText();
            lastTypingTime = System.currentTimeMillis();
        }
    }

    /**
     * Sending name and starting TimerTasks
     */
    public void run() {
        logger.info("Controller is running");
        lastConnectionCheckTime = System.currentTimeMillis();
        sendMessage(new MessageInterface(MessageInterface.CONNECTION_START, Settings.getInstance().getUserName()));

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (System.currentTimeMillis() < lastTypingTime + TIMEOUT) {
                    handleTypingNotification.accept(new MessageInterface(MessageInterface.TYPING_NOTIFICATION, userName));
                } else {
                    handleTypingNotification.accept(new MessageInterface(MessageInterface.TYPING_NOTIFICATION, ""));
                }
            }
        }, DELAY, TIMEOUT);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                sendMessage(new MessageInterface(MessageInterface.CHECK_CONNECTION, ""));
            }
        }, DELAY, TIMEOUT);


    }

    /**
     * Sending message
     *
     * @param message Message that we want to send
     * @return true if was successfully sent else false
     */
    public boolean sendMessage(MessageInterface message) {

        if (outputStreamObserver == null || (lastConnectionCheckTime + TIMEOUT < System.currentTimeMillis())) {
            return false;
        }
        outputStreamObserver.onNext(ProtoMessage.newBuilder().setType(message.getType()).setText(message.getText()).build());
        return true;

    }

    /**
     * Sending typing notification
     *
     * @param message Message that we want to send
     * @return true if was successfully sent else false
     */
    public boolean sendTypingNotification(MessageInterface message) {

        if (typingNotificationObserver != null) {
            typingNotificationObserver.onNext(ProtoMessage.newBuilder().setType(message.getType()).setText(message.getText()).build());
            return true;
        }
        return false;
    }

    /**
     * Specify operation that runs after receiving message
     *
     * @param handleGettingMessage operation that will be got from UI
     */
    public void afterGettingMessage(Consumer<MessageInterface> handleGettingMessage) {
        logger.info("Handle getting message");
        this.handleGettingMessage = handleGettingMessage;
    }

    /**
     * Specify operation that runs after receiving typing notification
     *
     * @param handleTypingNotification operation that will be got from UI
     */
    public void afterGettingNotification(Consumer<MessageInterface> handleTypingNotification) {
        logger.info("Handle typing notification");
        this.handleTypingNotification = handleTypingNotification;
    }

}
