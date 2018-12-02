package JUnit;

/**
 * Class that mock real client;
 */
public class MockClient {

    private String name, serverAddress;
    private int port;

    public MockClient(String name, String serverAddress, int port) {
        this.name = name;
        this.serverAddress = serverAddress;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public int getPort() {
        return port;
    }
}
