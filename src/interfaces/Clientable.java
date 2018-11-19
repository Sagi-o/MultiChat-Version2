package interfaces;

import model.Client;
import model.Message;

import java.io.IOException;
import java.util.List;

public interface Clientable {

    void initClient() throws IOException, ClassNotFoundException;

    void initView();

    void sendMessage(Message message) throws IOException;

    void updateOnline(List<Client> clients);

    void login();

    void logout();

    String welcomeMessage(String name);

}
