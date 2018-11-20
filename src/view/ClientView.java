package view;

import interfaces.View;
import model.Client;
import javax.swing.*;
import java.awt.*;

public class ClientView implements View {

    private JFrame frame;
    private JTextField textField;
    private JTextArea textArea;
    private JButton sendButton, sendAllButton, loginButton;
    private JLabel IPtext, portText, onlineTitle;
    private JList<Client> onlineList;

    public ClientView(String title) {
        frame = new JFrame(title);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        createView();
    }
    @Override
    public void createView() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Top - IP, Port and Connect button
        JPanel northPanel = new JPanel(new BorderLayout());
        mainPanel.add(northPanel, BorderLayout.NORTH);


        // Bottom -  Text field and Send button
        JPanel southPanel = new JPanel(new BorderLayout());
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        JPanel inSouthPanel = new JPanel(new BorderLayout());
        southPanel.add(inSouthPanel, BorderLayout.EAST);

        // Left - Online clients list
        JPanel westPanel = new JPanel(new BorderLayout());
        westPanel.setPreferredSize(new Dimension(100,450));
        mainPanel.add(westPanel, BorderLayout.WEST);

        textField = new JTextField(30);
        textArea = new JTextArea();
        sendButton = new JButton("Send");
        sendAllButton = new JButton("Send All");
        loginButton = new JButton("Logout");
        IPtext = new JLabel("IP: ");
        portText = new JLabel("Port: ");
        onlineTitle = new JLabel("Online");
        onlineList = new JList<>();

        mainPanel.add(textArea);

        northPanel.add(IPtext, BorderLayout.WEST);
        northPanel.add(portText, BorderLayout.CENTER);
        northPanel.add(loginButton, BorderLayout.EAST);

        southPanel.add(textField, BorderLayout.WEST);

        inSouthPanel.add(sendButton, BorderLayout.WEST);
        inSouthPanel.add(sendAllButton, BorderLayout.EAST);

        westPanel.add(onlineTitle, BorderLayout.NORTH);
        westPanel.add(onlineList, BorderLayout.CENTER);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    public JTextField getTextField() {
        return textField;
    }

    public void setTextField(JTextField textField) {
        this.textField = textField;
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public void setTextArea(JTextArea textArea) {
        this.textArea = textArea;
    }

    public JButton getSendButton() {
        return sendButton;
    }

    public void setSendButton(JButton sendButton) {
        this.sendButton = sendButton;
    }

    public JButton getSendAllButton() {
        return sendAllButton;
    }

    public void setSendAllButton(JButton sendButton) {
        this.sendAllButton = sendButton;
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    public void setLoginButton(JButton loginButton) {
        this.loginButton = loginButton;
    }

    public JLabel getIPtext() {
        return IPtext;
    }

    public void setIPtext(JLabel IPtext) {
        this.IPtext = IPtext;
    }

    public JLabel getPortText() {
        return portText;
    }

    public void setPortText(JLabel portText) {
        this.portText = portText;
    }

    public JList<Client> getOnlineList() {
        return onlineList;
    }

    public void setOnlineList(JList<Client> onlineList) {
        this.onlineList = onlineList;
    }

    /*
    public static void main(String[] args) {
        new ClientView("Client");
    }
    */
}
