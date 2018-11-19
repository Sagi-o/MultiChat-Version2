package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {

    private ServerSocket serverSocket;
    private int port;
    private boolean isRunning;

    private List<Client> clients;

    public Server(int port) throws IOException {
        this.port = port;
        this.isRunning = true;

        serverSocket = new ServerSocket(port);
        clients = Collections.synchronizedList(new ArrayList<>());
    }

    public Socket recieveSocketStreams() throws IOException {
        return serverSocket.accept();
    }

    public int getPort() {
        return port;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    // TODO: Check
    public void shutdown() throws IOException {
        if (isRunning) {
            isRunning = false;
            serverSocket.close();

            for (Client c : clients) {
                c.in().close();
                c.out().close();
                c.getSocket().close();

                clients.clear();
            }
        }
    }

    public void start() throws IOException {
        if (!isRunning) {
            isRunning = true;
            recieveSocketStreams();
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void addClient(Client client) {
        clients.add(client);
    }

    public void removeClient(Client client) throws IOException {
        client.closeSocket();
        clients.remove(client);
    }

    public List<Client> getActiveClients() {
        return clients;
    }

    public ObjectInputStream in(Socket socket) throws IOException {
        return new ObjectInputStream(socket.getInputStream());
    }

    public ObjectOutputStream out(Socket socket) throws IOException {
        return new ObjectOutputStream(socket.getOutputStream());
    }
}
