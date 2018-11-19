package model;

import java.io.Serializable;

public class Message implements Serializable {

    public static final int GETUSERS = 0, BROADCAST = 1, UNICAST = 2, LOGOUT = 3;

    private int type;

    private String message, toUser;

    protected static final long serialVersionUID = 1112122200L;

    public Message(int type, String message) {
        this.type = type;
        this.message = message;
    }

    public Message(int type, String message, String toUser) {
        this.type = type;
        this.message = message;
        this.toUser = toUser;
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
}
