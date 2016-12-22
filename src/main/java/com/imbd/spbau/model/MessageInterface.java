package com.imbd.spbau.model;


/**
 * Class for message interface
 */
public class MessageInterface {

    private int type;
    private String text;
    public static final int CONNECTION_START = 1;
    public static final int SIMPLE_MESSAGE = 2;
    public static final int TYPING_NOTIFICATION = 3;
    public static final int CHECK_CONNECTION = 4;

    public MessageInterface(int type, String text) {
        this.text = text;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof MessageInterface && ((MessageInterface) o).getText().equals(this.getText()) && ((MessageInterface) o).getType() == (this.getType());
    }
}
