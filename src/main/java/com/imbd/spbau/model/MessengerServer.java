package com.imbd.spbau.model;

import com.imbd.spbau.proto.MessengerGrpc;
import com.imbd.spbau.proto.ProtoMessage;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

import java.util.logging.Logger;

/**
 * Class for server and its operations
 */
public class MessengerServer {

    private Controller controller;
    private Server server;
    private static final Logger logger = Logger.getLogger(MessengerServer.class.getName());

    /**
     * Specifying stream observers
     */
    private class MessengerService extends MessengerGrpc.MessengerImplBase {

        @Override
        public StreamObserver<ProtoMessage> messagesExchange(StreamObserver<ProtoMessage> streamObserver) {
            return new StreamObserver<ProtoMessage>() {
                @Override
                public void onNext(ProtoMessage message) {
                    MessengerServer.this.controller.setOutputStreamObserver(streamObserver);
                    MessengerServer.this.controller.getMessage(new MessageInterface(message.getType(), message.getText()));

                }

                @Override
                public void onError(Throwable throwable) {
                    logger.info("Exception caught in service: " + throwable.getMessage());
                    stop();
                }

                @Override
                public void onCompleted() {
                    streamObserver.onCompleted();
                }
            };
        }

        @Override
        public StreamObserver<ProtoMessage> typingNotification(StreamObserver<ProtoMessage> streamObserver) {
            return new StreamObserver<ProtoMessage>() {
                @Override
                public void onNext(ProtoMessage message) {
                    MessengerServer.this.controller.getTypingNotification(new MessageInterface(message.getType(), message.getText()));
                    MessengerServer.this.controller.setTypingNotificationObserver(streamObserver);
                }

                @Override
                public void onError(Throwable throwable) {
                    logger.info("Exception caught in service: " + throwable.getMessage());
                    stop();
                }

                @Override
                public void onCompleted() {
                    streamObserver.onCompleted();
                }
            };
        }
    }

    public MessengerServer(Controller controller, int port) {
        this.controller = controller;
        server = ServerBuilder.forPort(port).addService(new MessengerService()).build();
    }

    /**
     * Starting server
     */
    public void start() {
        logger.info("Starting server");
        try {
            server.start();
            controller.run();
            server.awaitTermination();
        } catch (IOException e) {
            logger.info("Couldn't start server");
        } catch (InterruptedException e) {
            logger.info("Exception caught in a work process: " + e.getMessage());
        }

    }

    /**
     * Stopping server
     */
    public void stop() {
        logger.info("Stopping server");
        if (server != null && !server.isShutdown()) {
            server.shutdown();
            server = null;
        }
    }
}











