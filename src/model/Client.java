package model;

import java.io.*;
import java.net.Socket;

public class Client {

    private Socket socket;
    private String name, serverAddress;
    private int port;

    // Client side constructor
    public Client(String name, String serverAddress, int port) throws IOException {
        this.serverAddress = serverAddress;
        this.port = port;
        this.name = name;

        socket = new Socket(serverAddress, port);
    }

    // Server side constructor
    public Client(String name, Socket socket) {
        this.name = name;
        this.socket = socket;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public int getPort() {
        return port;
    }

    public Socket getSocket() {
        return socket;
    }

    public void closeSocket() throws IOException {
        socket.close();
    }

    public ObjectInputStream in() throws IOException {
        return new ObjectInputStream(socket.getInputStream());
    }

    public ObjectOutputStream out() throws IOException {
        return new ObjectOutputStream(socket.getOutputStream());
    }
}
