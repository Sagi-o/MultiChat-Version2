package model;

import java.io.Serializable;
import java.util.List;

public class Message implements Serializable {

    protected static final long serialVersionUID = 1112122200L;

    public static final int GETUSERS = 0, BROADCAST = 1, UNICAST = 2, LOGOUT = 3;

    private int type;

    private String message;

    private Client toUser;

    private List<Client> onlineList;

    // Broadcast
    public Message(int type, String message) {
        this.type = type;
        this.message = message;
    }

    // Unicast
    public Message(int type, String message, Client toUser) {
        this.type = type;
        this.message = message;
        this.toUser = toUser;
    }

    // GETUSERS Message, from SERVER to Client, notifying clients changed
    public Message(int type, List<Client> clients) {
        this.type = type;
        this.onlineList = clients;
    }

    public Client getToUser() {
        return toUser;
    }

    public int getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public List<Client> getOnlineList() {
        return onlineList;
    }
}
