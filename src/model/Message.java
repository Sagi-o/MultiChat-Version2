package model;

import java.io.Serializable;

/**
 * Simple POJO class that defines Message object that sent between Server and Client:
 *
 * Server to Client messages:
 *      GETUSERS: Update users with new list of current active clients.
 *      SERVERCLOSED: Update users that server is closed, make them logout.
 *      VALID: Notify user that some action is valid.
 *      NOTVALID: Notify user that some action is not valid.
 *
 * Client to Server messages:
 *      BROADCAST: Client send a message to all active users.
 *      UNICAST: Client send message to specific user.
 *      LOGOUT: Client wants to log out and disconnect from server.
 *      GETNAME: Client ssend server a nickname to be validated.
 */
public class Message implements Serializable {

    protected static final long serialVersionUID = 1112122200L;

    public static final int GETUSERS = 0, BROADCAST = 1, UNICAST = 2, LOGOUT = 3, GETNAME = 4, SERVERCLOSED = 5, VALID = 6, NOTVALID = 7;

    private int type;

    private String message, toUser;

    private String[] onlineList;

    /**
     * Constructor that defines a broadcast message.
     * @param type should be BROADCAST
     * @param message text to send
     */
    public Message(int type, String message) {
        this.type = type;
        this.message = message;
    }

    /**
     * Constructor that defines a unicast message.
     * @param type should be UNICAST
     * @param message text to send
     */
    public Message(int type, String message, String toUser) {
        this.type = type;
        this.message = message;
        this.toUser = toUser;
    }

    /**
     * Constructor that defines GETUSERS message. This is a message from server to client.
     * When clients list changed, server will send message to active clients to notify changes.
     * @param type should be GETUSERS
     * @param clients list of strings with client names
     */
    public Message(int type, String[] clients) {
        this.type = type;
        this.onlineList = clients;
    }

    /**
     * Constructor that defines SEVERCLOSED message. This is a message from server to client.
     * When server gets the command to stop, it will update all clients that the server closed with this message.
     * @param type
     */
    public Message(int type) {
        this.type = type;
    }

    public String getToUser() {
        return toUser;
    }

    public int getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String[] getOnlineList() {
        return onlineList;
    }
}
