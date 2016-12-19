package com.imbd.spbau.model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Class for server and its operations
 */
public class Server {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private int port;
    private Controller controller;
    private static final Logger logger = Logger.getLogger(Server.class.getName());

    public Server(Controller controller, int port) {
        this.port = port;
        this.controller = controller;
    }

    /**
     * Starting server and running controller
     */
    public void start() {
        logger.info("Starting server");
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            logger.info("Couldn't start server");
        }
        try {
            logger.info("Try to accept client");
            clientSocket = serverSocket.accept();
            controller.run(clientSocket);
        } catch (IOException e) {
            logger.info("Couldn't accept client");
        }
    }

    /**
     * Stopping server and closing sockets
     */
    public void stop() {
        logger.info("Stopping server");
        controller.setDisconnected(true);
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            logger.info("Couldn't close server socket");
        }
        try {
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            logger.info("Couldn't close client socket");
        }
    }
}











