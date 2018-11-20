import controller.ServerController;
import model.Server;
import view.ServerView;

import java.io.IOException;

public class ServerApplication {

    final static int PORT = 9191;

    public static void main(String[] args) throws IOException {

        Server server = new Server(PORT);
        ServerView serverView = new ServerView("Server");
        ServerController serverController = new ServerController(server, serverView);
        serverController.initServer();
    }
}
