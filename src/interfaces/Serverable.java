package interfaces;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Interaface that implemented in the ServerController class
 */
public interface Serverable {

    /**
     * Calls new thread for every server instance.
     * @param firstRun find if view is already built
     * @throws IOException if connection error occurs
     */
    void initServer (boolean firstRun) throws IOException;

    /**
     * Initialize the GUI that came from the constructor.
     */
    void initView() throws UnknownHostException;

    /**
     * Connect to myself will cause to exist from main loop by turning isServerRunng to false
     * @throws IOException when connection error occurs
     */
    void stopReceivingSockets() throws IOException;

    /**
     * Handles the sending of a unicast message to a specified user.
     * Search for the targeted client name, send message and breaking from loop.
     * @param name sender
     * @param message text to be send
     * @param toUser target
     * @throws IOException when connection error occurs
     */
    void unicast(String name, String message, String toUser) throws IOException;

    /**
     * Handles the sending of a broadcast message to all users.
     * Runs on every client thread and sends the message.
     * @param name sender
     * @param message text to be sent
     * @throws IOException when connection error occurs
     */
    void broadcast(String name, String message) throws IOException;

    /**
     * Send SERVERCLOSED message to every client, tell them them to disconnect, then the server
     * closes connection from his side in the while loop in every thread.
     * After the loop, server will stop recieving sockets by calling the stopReceivingSockets() method.
     * @throws IOException when connection error occurs
     */
    void stopServerClients() throws IOException;
}
