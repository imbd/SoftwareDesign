package com.imbd.spbau.model;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Class for message interface
 */
public class Message {

    private int type;
    private String text;
    public static final int CONNECTION_START = 0;
    public static final int SIMPLE_MESSAGE = 1;

    public Message(int type, String text) {
        this.text = text;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    /**
     * Writing message to output stream
     *
     * @param dataOutputStream output stream
     */
    public void write(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeInt(type);
        dataOutputStream.writeUTF(text);
    }

    /**
     * Reading message from input stream
     *
     * @param dataInputStream input stream
     * @return received message
     */
    public static Message read(DataInputStream dataInputStream) throws IOException {
        return new Message(dataInputStream.readInt(), dataInputStream.readUTF());
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Message && ((Message) o).getText().equals(this.getText()) && ((Message) o).getType() == (this.getType());
    }
}
