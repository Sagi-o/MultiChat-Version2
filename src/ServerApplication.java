import controller.ServerController;
import model.Server;
import view.ServerView;

import java.io.IOException;

/**
 * Starting point of the Server side.
 */
public class ServerApplication {

    final static int PORT = 9191;

    /**
     * Constructs new server in a desired port. And initializes the rest
     * @param args
     * @throws IOException when connection error occurs
     */
    public static void main(String[] args) throws IOException {

        Server server = new Server(PORT);
        ServerView serverView = new ServerView("Server");
        new ServerController(server, serverView);
    }
}
