import controller.ClientController;
import model.Client;
import view.ClientView;

import javax.swing.*;
import java.io.IOException;

/**
 * Starting point of the Client side.
 */
public class ClientApplication {

    final static JFrame frame = new JFrame();
    final static int PORT = 9191;

    static private String serverAddress;
    static private String name;

    /**
     * Ask for server address to connect to and initializes the rest.
     * @param args
     * @throws IOException when connection error occurs
     */
    public static void main(String[] args) throws IOException {

        name = null;
        serverAddress = getServerAddress();

        Client client = new Client(name, serverAddress, PORT);
        ClientView clientView = new ClientView("Disconnected");
        new ClientController(client, clientView);
    }

    /**
     * Prompt for server address
     * @return the server to be connected
     */
    private static String getServerAddress() {
        return JOptionPane.showInputDialog(
                frame,
                "Enter IP Address of the Server:",
                "Welcome to the Chatter",
                JOptionPane.QUESTION_MESSAGE);
    }
}
