package JUnit;

import controller.ServerController;
import model.Server;
import org.junit.Before;
import org.junit.Test;
import view.ServerView;

import static org.junit.Assert.*;

public class ServerControllerTest {

    ServerController serverController;
    Server server;


    @Before
    public void mainTest() throws Exception {
        final int PORT = 9191;

        server = new Server(PORT);
        ServerView serverView = new ServerView("Server");
        serverController = new ServerController(server, serverView);
    }

    @Test
    public void testPort() {
        int expecetd = 9191;

        assertEquals(expecetd,serverController.server.getPort());
    }
}