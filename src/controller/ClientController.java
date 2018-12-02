package controller;

import interfaces.Clientable;
import model.Client;
import model.Message;
import view.ClientView;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * The controller of the client side.
 * Gets the Model and the View and handles them.
 */
public class ClientController implements Clientable {

    public Client client;
    private ClientView clientView;

    private ObjectInputStream in;
    private ObjectOutputStream out;

    private String toUser;
    private boolean isRunning;

    /**
     *
     * @param client model to handle
     * @param clientView GUI
     * @throws IOException when error occurs
     */
    public ClientController(Client client, ClientView clientView) throws IOException {
        this.client = client;
        this.clientView = clientView;
        initClient(true);
    }

    /**
     * Initialize the client from the constructor, then start a clientThread.
     * @param firstRun to know if view is already found
     * @throws IOException
     */
    @Override
    public void initClient(boolean firstRun) throws IOException {

        if (firstRun) initView();
        else {
            client = new Client(client.getName(), client.getServerAddress(), client.getPort());
        }

        ClientThread clientThread = new ClientThread();
        clientThread.start();
    }

    /**
     * Initialize the GUI that came from the constructor.
     */
    @Override
    public void initView() {

        clientView.getIPtext().setText("IP: " + client.getServerAddress() + "\t\t\t");
        clientView.getPortText().setText("Port: " + client.getPort());

        clientView.getSendAllButton().setEnabled(false);
        clientView.getSendButton().setEnabled(false);
        clientView.getLoginButton().setEnabled(false);

        // Unicast
        clientView.getSendButton().addActionListener(e -> {
            String message = clientView.getTextField().getText();

            if (!message.trim().equals("")) {
                if (toUser != null) {
                    try {
                        sendMessage(new Message(Message.UNICAST, message, toUser));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    // Clear textField
                    clientView.getTextField().setText("");
                } else {
                    clientView.showMessagePrompt("Please select an active client from the list, then hit 'Send'!");
                }
            }
        });

        // Broadcast
        clientView.getSendAllButton().addActionListener(e -> {
            String message = clientView.getTextField().getText();

            if (!message.trim().equals("")) {
                try {
                    sendMessage(new Message(Message.BROADCAST, message));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                // Clear textField
                clientView.getTextField().setText("");
            }
        });

        clientView.getOnlineList().addListSelectionListener(e -> {
            int nameIndex = clientView.getOnlineList().getSelectedIndex();

            try {
                toUser = clientView.getListModel().getElementAt(nameIndex);
            } catch (ArrayIndexOutOfBoundsException outOfBounds) {
                clientView.getOnlineList().clearSelection();
            }
        });

        clientView.getLoginButton().addActionListener(e -> {
            if (clientView.getLoginButton().getText().equals("Logout")) {
                try {
                    logout(false);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                clientView.getLoginButton().setText("Login");
                clientView.getFrame().setTitle(client.getName() + " | Disconnected");
                clientView.getTextField().setEditable(false);
            } else {
                try {
                    initClient(false);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                clientView.getLoginButton().setText("Logout");
                clientView.getTextField().setEditable(true);
            }
        });

        clientView.getFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    logout(false);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    /**
     * Gets a Message object and outputs it to server accordingly
     * @param message to handle
     * @throws IOException when connection error occurs
     */
    @Override
    public void sendMessage(Message message) throws IOException {
        out.writeObject(message);

        switch (message.getType()) {
            case (Message.BROADCAST):
                log("me: " + message.getMessage());
                break;
            case (Message.UNICAST):
                log("me: " + message.getMessage());
                break;
            case (Message.LOGOUT):
                log("Logging out from chat...  ");
                break;
        }
    }

    /**
     * When user want to logut, close connections, update UI, and turn isRunning to false to stop listening to server.
     * @param isServerClosed if true, don't make a LOGOUT message to logout. Clients already logging out because SERVERCLOSED message
     * @throws IOException when connection error occurs
     */
    @Override
    public void logout(boolean isServerClosed) throws IOException {
        // Server will get the LOGOUT message and will close the sockets.
        if (!isServerClosed) sendMessage(new Message(Message.LOGOUT, ""));
        else clientView.getLoginButton().setEnabled(false);

        clientView.getFrame().setTitle(client.getName() + " | Disconnected");
        clientView.getLoginButton().setText("Login");
        clientView.getSendAllButton().setEnabled(false);
        clientView.getSendButton().setEnabled(false);

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        isRunning = false;

        log("You are disconnected from chat...  ");
    }

    /**
     * Get list of active client names and update every client with updated list.
     * @param names to print to online list
     */
    private void updateOnlinelist(String[] names) {

        clientView.getListModel().clear();

        for (String name : names) {
            clientView.getListModel().addElement(name);
        }
    }

    /**
     * Prints to client GUI.
     * @param text to be printed
     */
    private void log(String text) {
        // Prints in client GUI a message:
        clientView.getTextArea().append(text + "\n");
    }

    /**
     * Client constructs in and out streams from and to server.
     * Then, every client will call a new thread when connected to server. Then every client will
     * listen to the the server on another thread.
     */
    public class ClientThread extends Thread {
        public void run() {
            try {
                out = client.out();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                in = client.in();
            } catch (IOException e) {
                e.printStackTrace();
            }

            new listenToServer(client).start();
        }
    }

    /**
     * The main class where the Client logic happens.
     * Firstly, client will send the chosen nickname to server. Then server will answer with VALID or NOTVALID message.
     * After name is validated, the client then listens to server and in parallel can send broadcast or unicast messages
     * to users who is active. Client can also logout and login.
     */
    class listenToServer extends Thread {

        private Client client;

        listenToServer(Client client) {
            this.client = client;
        }

        public void run() {

            log("Starting connection with server...");

            isRunning = false;

            try {
                // Callback from server that tells if name is valid
                while (true) {
                    // Sends server the user name that connected
                    client.setName(clientView.openNamePrompt());

                    sendMessage(new Message(Message.GETNAME, client.getName()));

                    // Ask for name enter
                    Message isValid = (Message) in.readObject();

                    // Name is valid, break. another wise will continue to ask for name
                    if (isValid.getType() == Message.VALID) {
                        log(isValid.getMessage());

                        // Now user can enter the chat
                        isRunning = true;

                        break;
                    } else if (isValid.getType() == Message.NOTVALID) {
                        log(isValid.getMessage());
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                log("Server is not valid, closing connection.");
                try {
                    client.closeSocket();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            if (isRunning) {
                log("Welcome, " + client.getName());

                // Update UI
                clientView.getFrame().setTitle(client.getName() + " | Connected");
                clientView.getSendAllButton().setEnabled(true);
                clientView.getSendButton().setEnabled(true);
                clientView.getLoginButton().setEnabled(true);
            }

            while (isRunning) {
                    try {
                        Message serverMessage = (Message) in.readObject();

                        // Gets from server an array of connected clients
                        if (serverMessage.getType() == Message.GETUSERS) {
                            updateOnlinelist(serverMessage.getOnlineList());
                            continue;
                        }

                        if (serverMessage.getType() == Message.SERVERCLOSED) {
                            logout(true);
                            break;
                        }

                        log(serverMessage.getMessage());

                    } catch (IOException | ClassNotFoundException e) {
                        log("Server connection has closed.");
                        break;
                    }
                }
        }
    }
}



