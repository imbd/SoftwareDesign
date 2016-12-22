package com.imbd.spbau;

import com.imbd.spbau.model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class TestChat {


    private static final int MS_TIMEOUT = 2500;
    private static final int SMALL_MS_TIMEOUT = 500;
    private static final MessageInterface MESSAGE_1 = new MessageInterface(MessageInterface.SIMPLE_MESSAGE, "Text1");
    private static final MessageInterface MESSAGE_2 = new MessageInterface(MessageInterface.SIMPLE_MESSAGE, "Text2");
    private static final MessageInterface MESSAGE_3 = new MessageInterface(MessageInterface.SIMPLE_MESSAGE, "Text3");
    private static final MessageInterface MESSAGE_4 = new MessageInterface(MessageInterface.SIMPLE_MESSAGE, "Text4");

    private static Controller serverController;
    private static Controller clientController;
    private static MessengerServer messengerServer;
    private static Client client;
    private static ArrayList<MessageInterface> clientMessageList = new ArrayList<>();
    private static ArrayList<MessageInterface> serverMessageList = new ArrayList<>();
    private static ArrayList<MessageInterface> typingNotificationList = new ArrayList<>();

    @Before
    public void initialize() throws InterruptedException {
        int SERVER_PORT = 8080;
        int CONNECTION_PORT = 8080;
        byte[] HOST = {0, 0, 0, 0};
        serverController = new Controller(Controller.Type.SERVER);
        clientController = new Controller(Controller.Type.CLIENT);
        clientController.afterGettingMessage((MessageInterface message) -> {
            if (message.getType() == MessageInterface.SIMPLE_MESSAGE)
                clientMessageList.add(message);
        });
        serverController.afterGettingMessage((MessageInterface message) -> {
            if (message.getType() == MessageInterface.SIMPLE_MESSAGE)
                serverMessageList.add(message);
        });
        clientController.afterGettingNotification(typingNotificationList::add);
        serverController.afterGettingNotification(typingNotificationList::add);
        messengerServer = new MessengerServer(serverController, CONNECTION_PORT);

        new Thread(messengerServer::start).start();
        Thread.sleep(SMALL_MS_TIMEOUT);
        client = new Client(clientController);

        new Thread(() ->
                client.connect(HOST, SERVER_PORT)).start();
        Thread.sleep(MS_TIMEOUT);


    }

    @Test
    public void TestSendingMessages() throws InterruptedException {
        clientMessageList.clear();
        serverMessageList.clear();
        typingNotificationList.clear();
        Thread.sleep(SMALL_MS_TIMEOUT);
        clientController.sendMessage(MESSAGE_1);
        Thread.sleep(SMALL_MS_TIMEOUT);
        clientController.sendMessage(MESSAGE_2);
        Thread.sleep(SMALL_MS_TIMEOUT);
        serverController.sendMessage(MESSAGE_3);
        Thread.sleep(SMALL_MS_TIMEOUT);
        serverController.sendMessage(MESSAGE_4);
        Thread.sleep(SMALL_MS_TIMEOUT);

        assertEquals(clientMessageList, Arrays.asList(MESSAGE_3, MESSAGE_4));
        assertEquals(serverMessageList, Arrays.asList(MESSAGE_1, MESSAGE_2));
        assertFalse(typingNotificationList.isEmpty());
    }

    @Test
    public void TestSendingMessagesAfterDisconnecting() throws InterruptedException {
        clientMessageList.clear();
        serverMessageList.clear();
        typingNotificationList.clear();
        Thread.sleep(SMALL_MS_TIMEOUT);
        clientController.sendMessage(MESSAGE_1);
        Thread.sleep(SMALL_MS_TIMEOUT);
        clientController.sendMessage(MESSAGE_2);
        Thread.sleep(SMALL_MS_TIMEOUT);
        serverController.sendMessage(MESSAGE_3);
        Thread.sleep(SMALL_MS_TIMEOUT);
        client.disconnect();
        Thread.sleep(3 * MS_TIMEOUT);
        serverController.sendMessage(MESSAGE_4);
        Thread.sleep(SMALL_MS_TIMEOUT);

        assertEquals(clientMessageList, Collections.singletonList(MESSAGE_3));
        assertEquals(serverMessageList, Arrays.asList(MESSAGE_1, MESSAGE_2));
    }

    @Test
    public void TestSendingMessagesAfterStop() throws InterruptedException {
        clientMessageList.clear();
        serverMessageList.clear();
        typingNotificationList.clear();
        Thread.sleep(SMALL_MS_TIMEOUT);
        clientController.sendMessage(MESSAGE_1);
        Thread.sleep(SMALL_MS_TIMEOUT);
        clientController.sendMessage(MESSAGE_2);
        Thread.sleep(SMALL_MS_TIMEOUT);
        serverController.sendMessage(MESSAGE_3);
        Thread.sleep(SMALL_MS_TIMEOUT);
        messengerServer.stop();
        Thread.sleep(3 * MS_TIMEOUT);
        serverController.sendMessage(MESSAGE_4);
        Thread.sleep(SMALL_MS_TIMEOUT);
        clientController.sendMessage(MESSAGE_4);
        Thread.sleep(SMALL_MS_TIMEOUT);

        assertEquals(clientMessageList, Collections.singletonList(MESSAGE_3));
        assertEquals(serverMessageList, Arrays.asList(MESSAGE_1, MESSAGE_2));
    }

    @After
    public void close() {
        client.disconnect();
        messengerServer.stop();
    }
}
