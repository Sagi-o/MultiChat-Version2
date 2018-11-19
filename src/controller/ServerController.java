package controller;

import interfaces.Serverable;
import model.Client;
import model.Message;
import model.Server;
import view.ServerView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ServerController implements Serverable {

    private Server server;
    private ServerView serverView;

    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ServerController(Server server, ServerView serverView) {
        this.server = server;
        this.serverView = serverView;
    }


    @Override
    public void initServer() throws IOException {

        while (server.isRunning()) {
            Socket socket = server.recieveSocketStreams();

            Thread clientThread = new Thread(new ClientRunnable(socket));
            clientThread.start();
        }
        server.shutdown();
    }

    @Override
    public void initView() {

    }

    public class ClientRunnable implements Runnable {

        Socket socket;
        Client client;
        String name;
        List<Client> clients;

        Message message;

        ClientRunnable(Socket socket) {
            this.socket = socket;
            login(socket, name);
        }

        @Override
        public void run() {

            while (server.isRunning()) {
                try {
                    message = (Message) in.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

                String messageText = message.getMessage();
                clients = server.getActiveClients();

                switch (message.getType()) {
                    case Message.GETUSERS: // 0
                        for (Client c : clients) {
                            if (!c.getName().equals(name)) {
                                // out.writeObject(c.getName());
                            }
                        }
                        break;
                    case Message.BROADCAST: // 1
                        try {
                            broadcast(name, messageText);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Message.UNICAST: // 2
                        try {
                            unicast(name, messageText, message.getToUser());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Message.LOGOUT: // 3
                        try {
                            server.removeClient(client);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        }

        private void login(Socket socket, String name) {
            // Initialize socket in and out streams
            try {
                in = server.in(socket);
                out = server.out(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Add client to server user database
            try {
                name = (String) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            clients = server.getActiveClients();

            synchronized (clients) {
                for (Client c : clients) {
                    if (c.getName().equals(name)) {
                        try {
                            out.writeObject(name + " is already connected, try another user name");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        // model.show name prompt

                    } else {
                        // log welcome on view
                    }
                }
            }

            client = new Client(name, socket);
            server.addClient(client);
        }

        private void broadcast(String name, String message) throws IOException {
            for (Client c : clients) {
                if (!c.getName().equals(name)) c.out().writeObject(name + ": " + message);
            }
            client.out().writeObject("me: " + message);
        }

        private void unicast(String name, String message, String toUser) throws IOException {
            for (Client c : clients) {
                if (c.getName().equals(toUser)) {
                    c.out().writeObject(name + ": " + message);
                    client.out().writeObject("me: " + message);
                    break;
                }
            }
        }
    }
}
