package controller;

import interfaces.Serverable;
import model.Client;
import model.Message;
import model.Server;
import view.ServerView;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * The controller of the server side.
 * Gets the Model and the View and handles them.
 */
public class ServerController implements Serverable {

    private Server server;
    private ServerView serverView;
    private boolean isServerRunning;

    /**
     * Constructs the server and server GUI, then initializes the server.
     * @param server
     * @param serverView
     * @throws IOException
     */
    public ServerController(Server server, ServerView serverView) throws IOException {
        this.server = server;
        this.serverView = serverView;
        initServer(true);
    }


    /**
     * Calls new thread for every server instance.
     * @param firstRun find if view is already built
     * @throws IOException if connection error occurs
     */
    @Override
    public void initServer(boolean firstRun) throws IOException {

        if (firstRun) initView();

        ServerThread serverThread = new ServerThread();
        serverThread.start();
    }

    /**
     * Server thread will listen to every socket that asks to connect, he will accept it while the server is running.
     * Every client that connects will get a new ClientThread.
     */
    public class ServerThread extends Thread {
        public void run() {
            isServerRunning = true;

            log("Server is running...");

            while (isServerRunning) {
                Socket socket = null;
                try {
                    socket = server.getServerSocket().accept();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (!isServerRunning) break;

                Thread clientThread = new Thread(new ClientThread(socket));
                log("Starting new thread...");

                clientThread.start();
            }
            log("Server not accepting any sockets.");
        }
    }

    /**
     * Initialize the GUI that came from the constructor.
     */
    @Override
    public void initView() throws UnknownHostException {
        serverView.getIPtext().setText("IP: " + InetAddress.getLocalHost().getHostAddress() + "\t\t\t");
        serverView.getPortText().setText("Port: " + server.getPort());

        serverView.getStartButton().addActionListener(e -> {
            if (isServerRunning) {
                try {
                    stopServerClients();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                serverView.getOnlineList().clear();
                serverView.getFrame().setTitle("Server Offline");
                serverView.getStartButton().setText("Start");
            } else {
                try {
                    int port = server.getPort();

                    server = new Server(port);

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try {
                    initServer(false);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                serverView.getFrame().setTitle("Server Online");
                serverView.getStartButton().setText("Stop");
            }
        });
    }

    /**
     * The main class where the main client handling happens.
     * Firstly, constructs in an out streams with the connected socket. Then, server will listen to a GETNAME message
     * from client, he will validate the the entered name is not overrides existing client name. Then server will listen to
     * every client message and handle it accordingly.
     */
    public class ClientThread implements Runnable {

        private ObjectInputStream in;
        private ObjectOutputStream out;
        private boolean isRunning;

        Socket socket;
        Client client;
        String name, messageText;

        Message message;

        /**
         * Gets the socket for the current thread.
         * @param socket to handle
         */
        ClientThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {

            log("Starting connection process with new user...");

            // Initialize socket in and out streams
            try {
                in = server.in(socket);
                out = server.out(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }

            boolean isNameValid = false;

            // Get user name from client thread, ask for it while name is not validated
            while (!isNameValid) {
                try {
                    // Suppose name is valid, when there is a problem: make false and continue loop
                    isNameValid = true;

                    Message toServerMessage = (Message) in.readObject();

                    if (toServerMessage.getType() == Message.GETNAME) {
                        name = toServerMessage.getMessage();

                        if (name == null) {
                            log("Null name sent. Closing connection with client.");
                            closeConnection(false);
                            break;
                        }
                    } else {
                        isNameValid = false;
                        continue;
                    }
                } catch (IOException | ClassNotFoundException e) {
                    //e.printStackTrace();
                    log("Client is not valid, closing connection.");
                    try {
                        closeConnection(false);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    isNameValid = false;
                    continue;
                }

                synchronized (server.getClientThreads()) {
                    for (ClientThread ct : server.getClientThreads()) {
                        if (ct.client.getName().equals(name)) {
                            try {
                                isNameValid = false;
                                out.writeObject(new Message(Message.NOTVALID, "\'" + name + "\'" + " is already connected, please choose another user name."));
                                log("\'" + name + "\'" + " is already used, warned user to choose a different name.");
                                break;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            // Assert name is non-null then proceed
            if (name != null) {
                // Name accepted, update user and start login
                try {
                    out.writeObject(new Message(Message.VALID, "Server: " + name + " Connected succesfully"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Set client to future get from another thread
                client = new Client(name, socket);

                // Add to active client threads list
                server.addClientThread(this);

                // Update UI
                serverView.getOnlineList().addElement(name);

                // Update client
                try {
                    out.writeObject(new Message(Message.VALID,""));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                log(name + " Connected successfully.");

                try {
                    notifyClientsListChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                isRunning = true;

                while (isRunning) {
                    try {
                        message = (Message) in.readObject();
                        messageText = message.getMessage();
                    } catch (IOException | ClassNotFoundException e) {
                        log("Connection with client name: " + name + " have disconnected, closing connection from server...");
                        try {
                            closeConnection(true);
                            isRunning = false;
                        } catch (IOException e1) {
                            isRunning = false;
                            log("Error closing connection with client " + name + ". " + e);
                        }
                        break;
                    }

                    switch (message.getType()) {
                        case Message.BROADCAST: // 1
                            try {
                                log("(Broadcast) " + name + ": " + messageText);
                                broadcast(name, messageText);
                            } catch (IOException e) {
                                log("Error sending broadcast message. " + e);
                            }
                            break;
                        case Message.UNICAST: // 2
                            try {
                                log("(Unicast) " + name + " to " + message.getToUser() + ": " + messageText);
                                unicast(name, messageText, message.getToUser());
                            } catch (IOException e) {
                                log("Error sending unicast message. " + e);
                            }
                            break;
                        case Message.LOGOUT: // 3
                            log("(Logout) User " + name + " has logged out.");
                            // When user logged out and loop ends | Enters also when server stops
                            try {
                                closeConnection(false);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            isRunning = false;
                            break;
                    }
                }

            } else {
                log("User not connected.");
            }

        }
        /**
         * Gets a Message object and outputs it to client
         * @param message to handle
         * @throws IOException when connection error occurs
         */
        private void sendMessage(Message message) throws IOException {
            out.writeObject(message);
        }

        /**
         * Close connection and streams when: 1. User has logout, 2. Server is stopped
         * @param isServerClosed if true, ther is no need to notify clients with current active clients,
         *                       because every user is logged out automatically.
         * @throws IOException when connection error occurs
         */
        private void closeConnection(boolean isServerClosed) throws IOException {

            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }

            if (socket != null && name != null) {
                client.closeSocket();
            }

            server.removeClientThread(this);

            log("Removed client thread: \'" + name + "\' .Threads alive: " + server.getClientThreads().size());

            // If server is running, notify other clients that a client leaved the chat
            if (!isServerClosed) {
                try {
                    serverView.getOnlineList().removeElement(name);
                    notifyClientsListChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Connect to myself will cause to exist from main loop by turning isServerRunng to false
     * @throws IOException when connection error occurs
     */
    public void stopReceivingSockets() throws IOException {

        isServerRunning = false;

        new Socket("127.0.0.1", server.getPort());

        // After server is not accepting sockets, close socket
        server.getServerSocket().close();
    }

    /**
     * Handles the sending of a unicast message to a specified user.
     * Search for the targeted client name, send message and breaking from loop.
     * @param name sender
     * @param message text to be send
     * @param toUser target
     * @throws IOException when connection error occurs
     */
    @Override
    public synchronized void unicast(String name, String message, String toUser) throws IOException {

        for (ClientThread clientThread : server.getClientThreads()) {
            if (clientThread.client.getName().equals(toUser)) {
                //log("Sending to client: " + clientThread.client.getName());
                clientThread.sendMessage(new Message(Message.UNICAST, name + ": " + message));
            }
        }
    }

    /**
     * Handles the sending of a broadcast message to all users.
     * Runs on every client thread and sends the message.
     * @param name sender
     * @param message text to be sent
     * @throws IOException when connection error occurs
     */
    @Override
    public synchronized void broadcast(String name, String message) throws IOException {

        for (ClientThread clientThread : server.getClientThreads()) {
            if (!clientThread.client.getName().equals(name)) {
                //log("Sending to client: " + clientThread.client.getName());
                clientThread.sendMessage(new Message(Message.BROADCAST, name + ": " + message));
            }
        }
    }

    // Send SERVERCLOSED message to every client, tell them them to disconnect, then the server
    // closes connection from his side in the while loop in every thread.
    // After for loop, server will stop recieving sockets.

    /**
     * Send SERVERCLOSED message to every client, tell them them to disconnect, then the server
     * closes connection from his side in the while loop in every thread.
     * After the loop, server will stop recieving sockets by calling the stopReceivingSockets() method.
     * @throws IOException when connection error occurs
     */
    @Override
    public void stopServerClients() throws IOException {

        log("Starting to stop server...");
        log("Number of Threads alive: " + server.getClientThreads().size());

        synchronized (server.getClientThreads()) {
            for (ClientThread clientThread : server.getClientThreads()) {

                log("Stopping client name: " + clientThread.client.getName());
                clientThread.sendMessage(new Message(Message.UNICAST, "Server: Server has stopped."));
                clientThread.sendMessage(new Message(Message.SERVERCLOSED));
            }
        }
        stopReceivingSockets();
    }

    /**
     * Prints to client GUI.
     * @param text to be printed
     */
    private void log(String text) {
        serverView.getTextArea().append(text + "\n");
    }

    /**
     * Send GETUSERS message to every client. Calls when client list have changed.
     * @throws IOException whne connection error occurs
     */
    private void notifyClientsListChanged() throws IOException {

        for (ClientThread clientThread : server.getClientThreads()) {
            clientThread.sendMessage(new Message(Message.GETUSERS, getActiveClientNames()));
        }
    }

    /**
     * Outputs the name field of every client thread to a String array.
     * @return array of current active client names
     */
    private String[] getActiveClientNames() {
        String[] names = new String[server.getClientThreads().size()];

        int i = 0;

        for (ClientThread clientThread : server.getClientThreads()) {
            names[i] = clientThread.client.getName();
            i++;
        }
        return names;
    }
}
