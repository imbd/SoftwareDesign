package com.imbd.spbau.model;

import io.grpc.ManagedChannel;
import com.imbd.spbau.proto.*;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

/**
 * Class for client and its operations
 */
public class Client {

    private static final Logger logger = Logger.getLogger(Client.class.getName());
    private Controller controller;
    private ManagedChannel channel;

    public Client(Controller controller) {
        this.controller = controller;
    }

    /**
     * Connecting to server
     *
     * @param ip   server ip
     * @param port server port
     */
    public void connect(byte[] ip, int port) {
        logger.info("Connecting to server");
        try {
            channel = ManagedChannelBuilder.forAddress(InetAddress.getByAddress(ip).getHostAddress(), port).usePlaintext(true).build();
            MessengerGrpc.MessengerStub stub = MessengerGrpc.newStub(channel);

            StreamObserver<ProtoMessage> outputStreamObserver = stub.messagesExchange(new StreamObserver<ProtoMessage>() {
                @Override
                public void onNext(ProtoMessage message) {
                    controller.getMessage(new MessageInterface(message.getType(), message.getText()));

                }

                @Override
                public void onError(Throwable throwable) {
                    logger.info("Exception caught in service: " + throwable.getMessage());
                }

                @Override
                public void onCompleted() {
                }
            });

            StreamObserver<ProtoMessage> typingNotificationObserver = stub.typingNotification(new StreamObserver<ProtoMessage>() {
                @Override
                public void onNext(ProtoMessage message) {
                    controller.getTypingNotification(new MessageInterface(message.getType(), message.getText()));
                }

                @Override
                public void onError(Throwable throwable) {
                    logger.info("Exception caught in service: " + throwable.getMessage());
                }

                @Override
                public void onCompleted() {
                }
            });
            controller.setOutputStreamObserver(outputStreamObserver);
            controller.setTypingNotificationObserver(typingNotificationObserver);
        } catch (UnknownHostException e) {
            logger.info("Unknown host exception");
        }
        controller.run();

    }

    /**
     * Disconnecting from server
     */
    public void disconnect() {
        logger.info("Client is disconnecting");
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
            channel = null;
        }

    }
}
