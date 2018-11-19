package interfaces;

import java.io.IOException;

public interface Clientable {

    void initClient() throws IOException, ClassNotFoundException;

    void initView();

    void sendMessage();

    void login();

    void logout();

    String welcomeMessage(String name);

}
