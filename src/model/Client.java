package model;

import java.io.*;
import java.net.Socket;

/**
 * Simple POJO class that defines server a Client
 */
public class Client implements Serializable{

    protected static final long serialVersionUID = 2112122200L;

    private Socket socket;
    private String name, serverAddress;
    private int port;

    /**
     * Client side constructor. Initializes when new Client enters the chat.
     * @param name user wants to use for chat
     * @param serverAddress the target to connect
     * @param port of server
     * @throws IOException
     */
    public Client(String name, String serverAddress, int port) throws IOException {
        this.serverAddress = serverAddress;
        this.port = port;
        this.name = name;

        socket = new Socket(serverAddress, port);
    }

    /**
     * Server side constructor. Initializes when Client is connected to server.
     * @param name of user
     * @param socket of current user
     */
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

    /**
     * Input stream for user socket.
     * @return Stream to get server messages
     * @throws IOException
     */
    public ObjectInputStream in() throws IOException {
        return new ObjectInputStream(socket.getInputStream());
    }

    /**
     * Output stream for user socket.
     * @return Stream to write to server
     * @throws IOException
     */
    public ObjectOutputStream out() throws IOException {
        return new ObjectOutputStream(socket.getOutputStream());
    }

}
