package interfaces;

import model.Message;
import java.io.IOException;

/**
 * Interaface that implemented in the ClientController class
 */
public interface Clientable {

    /**
     * Initialize the client from the constructor, then start a clientThread.
     * @param firstRun to know if view is already found
     * @throws IOException
     */
    void initClient(boolean firstRun) throws IOException, ClassNotFoundException;

    /**
     * Initialize the GUI that came from the constructor.
     */
    void initView();

    /**
     * Gets a Message object and outputs it to server accordingly
     * @param message to handle
     * @throws IOException when connection error occurs
     */
    void sendMessage(Message message) throws IOException;

    /**
     * Get list of active client names and update every client with updated list.
     * @param names to print to online list
     */
    void logout(boolean isServerClosed) throws IOException;
}
