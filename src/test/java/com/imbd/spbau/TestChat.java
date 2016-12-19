package com.imbd.spbau;

import com.imbd.spbau.model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

public class TestChat {


    private static final int MS_TIMEOUT = 500;
    private static final int SMALL_MS_TIMEOUT = 100;
    private static final Message message1 = new Message(Message.SIMPLE_MESSAGE, "Text1");
    private static final Message message2 = new Message(Message.SIMPLE_MESSAGE, "Text2");
    private static final Message message3 = new Message(Message.SIMPLE_MESSAGE, "Text3");
    private static final Message message4 = new Message(Message.SIMPLE_MESSAGE, "Text4");

    private static Controller serverController;
    private static Controller clientController;
    private static Server server;
    private static Client client;
    private static ArrayList<Message> clientMessageList = new ArrayList<>();
    private static ArrayList<Message> serverMessageList = new ArrayList<>();

    @Before
    public void initialize() throws InterruptedException {
        int SERVER_PORT = 8888;
        int CONNECTION_PORT = 8888;
        byte[] HOST = {0, 0, 0, 0};
        serverController = new Controller(Controller.Type.SERVER);
        server = new Server(serverController, CONNECTION_PORT);
        clientController = new Controller(Controller.Type.CLIENT);
        client = new Client(clientController);
        new Thread(server::start).start();
        Thread.sleep(MS_TIMEOUT);
        new Thread(() ->
                client.connect(HOST, SERVER_PORT)).start();
        Thread.sleep(MS_TIMEOUT);
        clientController.afterGettingMessage(clientMessageList::add);
        serverController.afterGettingMessage(serverMessageList::add);
    }

    @Test
    public void TestSendingMessages() throws InterruptedException {
        clientMessageList.clear();
        serverMessageList.clear();
        clientController.sendMessage(message1);
        Thread.sleep(SMALL_MS_TIMEOUT);
        clientController.sendMessage(message2);
        Thread.sleep(SMALL_MS_TIMEOUT);
        serverController.sendMessage(message3);
        Thread.sleep(SMALL_MS_TIMEOUT);
        serverController.sendMessage(message4);
        Thread.sleep(SMALL_MS_TIMEOUT);

        assertEquals(clientMessageList, Arrays.asList(message3, message4));
        assertEquals(serverMessageList, Arrays.asList(message1, message2));
    }

    @Test
    public void TestSendingMessagesAfterDisconnecting() throws InterruptedException {
        clientMessageList.clear();
        serverMessageList.clear();
        clientController.sendMessage(message1);
        Thread.sleep(SMALL_MS_TIMEOUT);
        clientController.sendMessage(message2);
        Thread.sleep(SMALL_MS_TIMEOUT);
        serverController.sendMessage(message3);
        Thread.sleep(SMALL_MS_TIMEOUT);
        client.disconnect();
        Thread.sleep(MS_TIMEOUT);
        serverController.sendMessage(message4);
        Thread.sleep(SMALL_MS_TIMEOUT);

        assertEquals(clientMessageList, Arrays.asList(message3));
        assertEquals(serverMessageList, Arrays.asList(message1, message2));
    }

    @Test
    public void TestSendingMessagesAfterStop() throws InterruptedException {
        clientMessageList.clear();
        serverMessageList.clear();
        clientController.sendMessage(message1);
        Thread.sleep(SMALL_MS_TIMEOUT);
        clientController.sendMessage(message2);
        Thread.sleep(SMALL_MS_TIMEOUT);
        serverController.sendMessage(message3);
        Thread.sleep(SMALL_MS_TIMEOUT);
        server.stop();
        Thread.sleep(MS_TIMEOUT);
        serverController.sendMessage(message4);
        Thread.sleep(SMALL_MS_TIMEOUT);
        clientController.sendMessage(message4);
        Thread.sleep(SMALL_MS_TIMEOUT);

        assertEquals(clientMessageList, Arrays.asList(message3));
        assertEquals(serverMessageList, Arrays.asList(message1, message2));
    }

    @After
    public void close() {
        client.disconnect();
        server.stop();
    }
}
