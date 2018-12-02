package JUnit;


import org.junit.Before;
import org.junit.Test;


import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ClientControllerTest {

    MockClient clientMock;

    @Before
    public void mainTest() {
        final int PORT = 9191;

        String name = "Adam";
        String serverAddress = "192.168.1.2";

        clientMock = new MockClient(name,serverAddress,PORT);
    }

    @Test
    public void testPort() {
        int expecetd = 9191;

        assertEquals(expecetd,clientMock.getPort());
    }

    @Test
    public void testName() {
        String expecetd = "Adam";

        assertEquals(expecetd,clientMock.getName());
    }

    @Test
    public void testServerAddress() {
        String expected = "192.168.1.2";

        assertEquals(expected,clientMock.getServerAddress());
    }
}