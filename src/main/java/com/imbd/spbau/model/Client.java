package com.imbd.spbau.model;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Class for client and its operations
 */
public class Client {

    private static final Logger logger = Logger.getLogger(Client.class.getName());
    private Socket socket;
    private Controller controller;

    public Client(Controller controller) {
        this.controller = controller;
    }

    /**
     * Connecting to server and running controller
     *
     * @param ip   server ip
     * @param port server port
     */
    public void connect(byte[] ip, int port) {
        try {
            logger.info("Connecting to server");
            socket = new Socket(InetAddress.getByAddress(ip), port);
        } catch (IOException e) {
            logger.info("Couldn't connect to server");
            return;
        }
        controller.run(socket);
    }

    /**
     * Disconnecting from server
     */
    public void disconnect() {
        controller.setDisconnected(true);
        logger.info("Client is disconnecting");
        if (socket == null) {
            return;
        }
        try {
            if (!socket.isClosed()) {
                logger.info("Closing socket");
                socket.close();
                socket = null;
            }
        } catch (IOException e) {
            logger.info("Couldn't close socket");
        }
    }
}
