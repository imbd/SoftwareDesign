package com.imbd.spbau.model;

/**
 * Singleton class for storing settings
 */
public final class Settings {

    private Settings() {
    }

    private static Settings instance;
    private static String userName = "Unknown user";
    private static byte[] serverIP = new byte[]{0, 0, 0, 0};
    private static int serverPort = 8080;
    private static int connectionPort = 8080;

    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    public String getUserName() {
        return userName;
    }

    public byte[] getServerIP() {
        return serverIP;
    }

    public int getServerPort() {
        return serverPort;
    }

    public int getConnectionPort() {
        return connectionPort;
    }

    public void setUserName(String userName) {
        Settings.userName = userName;
    }

    public void setServerIP(byte[] serverIP) {
        Settings.serverIP = serverIP;
    }

    public void setServerPort(int serverPort) {
        Settings.serverPort = serverPort;
    }

    public void setConnectionPort(int connectionPort) {
        Settings.connectionPort = connectionPort;
    }

}
