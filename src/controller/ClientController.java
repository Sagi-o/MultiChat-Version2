package controller;

import interfaces.Clientable;
import model.Client;
import model.Message;
import view.ClientView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class ClientController implements Clientable {

    private Client client;
    private ClientView clientView;

    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ClientController(Client client, ClientView clientView) {
        this.client = client;
        this.clientView = clientView;
        initView();
    }

    @Override
    public void initClient() throws IOException, ClassNotFoundException {

        // Log: user has connected succesfully

        // Get server input and output streams
        in = client.in();
        out = client.out();

        // Sends to server the name of user that connected
        out.writeObject(client.getName());

        listenFromServer();
    }

    @Override
    public void initView() {

        clientView.getLoginButton().addActionListener(e -> {
            // Login method
        });

        // Unicast
        clientView.getSendButton().addActionListener(e -> {
            String message = clientView.getTextField().getText();

            if (!message.trim().equals("")) {

                //TODO : clientView.getOnlineList().getSelectedValue(), if not selected
                Message msg = new Message(Message.UNICAST, message, clientView.getOnlineList().getSelectedValue());

                try {
                    sendMessage(msg);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                // Clear textField
                clientView.getTextField().setText("");
            }
        });

        // Broadcast
        clientView.getSendAllButton().addActionListener(e -> {
            String message = clientView.getTextField().getText();

            if (!message.trim().equals("")) {

                Message msg = new Message(Message.BROADCAST, message);

                try {
                    sendMessage(msg);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                // Clear textField
                clientView.getTextField().setText("");
            }
        });

        clientView.getLoginButton().addActionListener(e -> {
            if (clientView.getLoginButton().getText().equals("Logout")) {
                logout();
            } else {
                login();
            }
        });

        clientView.getTextField().setText(welcomeMessage(client.getName()));
        clientView.getIPtext().setText("IP: " + client.getServerAddress());
        clientView.getPortText().setText("Port: " + client.getPort());

                /*
        clientView.getOnlineList().addListSelectionListener(e -> {
            //clientView.getOnlineList().getSelectedIndex();
            // Handle
            //clientView.getTextArea().append();
        });

        //clientView.getOnlineList().setListData();
        */
    }

    private void listenFromServer() throws IOException, ClassNotFoundException {
        // Handle case that message is GETUSERS

        while (true) {
            Message message = (Message) in.readObject();

            if (message.getType() == Message.GETUSERS) {
                updateOnline(message.getOnlineList());
            } else {
                clientView.getTextArea().append(message.getMessage() + "\n");
            }

        }
    }

    @Override
    public void sendMessage(Message message) throws IOException {
        out.writeObject(message);
    }

    // If message from server is GETUSERS type, update online list
    @Override
    public void updateOnline(List<Client> clients) {

        // int i to change

        Client[] onlineList = new Client[clients.size()];

        for (int i = 0; i< clients.size() ; i++) {
            onlineList[i] = clients.get(i);
        }
        clientView.getOnlineList().setListData(onlineList);
    }

    @Override
    public void login() {
    }

    @Override
    public void logout() {
    }

    @Override
    public String welcomeMessage(String name) {
        return "Welcome, " + name;
    }


    private void log() {
        // Prints in GUI a message:
        // clientView.log()
    }
}
