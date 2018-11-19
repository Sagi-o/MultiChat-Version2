package controller;

import interfaces.Clientable;
import model.Client;
import view.ClientView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ClientController implements Clientable {

    private Client client;
    private ClientView clientView;

    ObjectInputStream in;
    ObjectOutputStream out;

    public ClientController(Client client, ClientView clientView) {
        this.client = client;
        this.clientView = clientView;
    }

    @Override
    public void initClient() throws IOException, ClassNotFoundException {

        // Log: user has connected succesfully

        // Get server input and output streams
        in = client.in();
        out = client.out();

        // Sends to server that new client connected
        out.writeObject(client.getName());

        initView();
        handleChat();
    }

    @Override
    public void initView() {
        clientView.getLoginButton().addActionListener(e -> {
            // Login method
        });

        clientView.getSendButton().addActionListener(e -> {
            // Send message method
            JTextArea textArea = clientView.getTextArea();
            if (!textArea.getText().trim().equals("")) {

            }
        });

        clientView.getOnlineList().addListSelectionListener(e -> {
            clientView.getOnlineList().getSelectedIndex();
            // Handle
        });


        clientView.getTextField().setText(welcomeMessage(client.getName()));
        clientView.getIPtext().setText("IP: " + client.getServerAddress());
        clientView.getPortText().setText("Port: " + client.getPort());
    }

    @Override
    public void sendMessage() {

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

    private void handleChat() {

    }


    private void log() {
        // Prints in GUI a message:
        // clientView.log()
    }
}
