import controller.ClientController;
import model.Client;
import view.ClientView;

import javax.swing.*;
import java.io.IOException;

public class ClientApplication {

    final static JFrame frame = new JFrame();
    final static int PORT = 9191;

    static private String serverAddress;
    static private String name;

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        serverAddress = getServerAddress();
        name = getName();

        Client client = new Client(name, serverAddress, PORT);
        ClientView clientView = new ClientView(name + " | Connected");
        ClientController clientController = new ClientController(client, clientView);
        clientController.initClient();
    }

    private static String getServerAddress() {
        return JOptionPane.showInputDialog(
                frame,
                "Enter IP Address of the Server:",
                "Welcome to the Chatter",
                JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * Prompt for and return the desired screen name.
     */
    private static String getName() {
        return JOptionPane.showInputDialog(
                frame,
                "Choose a screen name:",
                "Screen name selection",
                JOptionPane.PLAIN_MESSAGE);
    }
}
