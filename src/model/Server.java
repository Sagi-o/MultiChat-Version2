package model;

import controller.ServerController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Simple POJO class that defines a Server
 */
public class Server {

    private ServerSocket serverSocket;
    private int port;
    private List<ServerController.ClientThread> clientThreads;

    /**
     * Takes port and initialize new server socket, with empty list of client threads.
     * @param port to open server in
     * @throws IOException when error occurs
     */
    public Server(int port) throws IOException {
        this.port = port;

        serverSocket = new ServerSocket(port);
        clientThreads = new ArrayList<>();
    }

    public int getPort() {
        return port;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public List<ServerController.ClientThread> getClientThreads() {
        return clientThreads;
    }

    /**
     * Add client thread to the array list.
     * @param clientThread to be added
     */
    public synchronized void addClientThread(ServerController.ClientThread clientThread) {
        clientThreads.add(clientThread);
    }

    /**
     * Remove client thread from the array list.
     * @param clientThread to be removed
     */
    public synchronized void removeClientThread(ServerController.ClientThread clientThread) {
        clientThreads.remove(clientThread);
    }

    /**
     * Returns the input stream of client socket.
     * @param socket of client
     * @return Client stream to read from
     * @throws IOException when error occurs
     */
    public ObjectInputStream in(Socket socket) throws IOException {
        return new ObjectInputStream(socket.getInputStream());
    }

    /**
     * Returns the output stream of client socket.
     * @param socket of client
     * @return Client stream to write to
     * @throws IOException when error occurs
     */
    public ObjectOutputStream out(Socket socket) throws IOException {
        return new ObjectOutputStream(socket.getOutputStream());
    }
}
