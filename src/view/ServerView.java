package view;

import interfaces.View;
import model.Client;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ServerView implements View {

    private JFrame frame;
    private JTextArea textArea;
    private JButton startButton;
    private JLabel IPtext, portText, onlineTitle;
    private JList<Client> onlineList;

    public ServerView(String title) {

        frame = new JFrame(title);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 500);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        createView();
    }

    @Override
    public void createView() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel northPanel = new JPanel(new BorderLayout());
        mainPanel.add(northPanel, BorderLayout.NORTH);

        JPanel westPanel = new JPanel(new BorderLayout());
        westPanel.setPreferredSize(new Dimension(100,450));
        mainPanel.add(westPanel, BorderLayout.WEST);

        textArea = new JTextArea("Server is running");
        startButton = new JButton("Stop");
        IPtext = new JLabel("IP: ");
        portText = new JLabel("Port: ");
        onlineTitle = new JLabel("Online");
        onlineList = new JList<>();

        mainPanel.add(textArea, BorderLayout.CENTER);

        westPanel.add(onlineList, BorderLayout.CENTER);
        westPanel.add(onlineTitle, BorderLayout.NORTH);

        northPanel.add(IPtext,BorderLayout.WEST);
        northPanel.add(portText,BorderLayout.CENTER);
        northPanel.add(startButton,BorderLayout.EAST);

        //JScrollPane scrollFrame = new JScrollPane(mainPanel);
        //mainPanel.setAutoscrolls(true);

        frame.getContentPane().add(mainPanel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
         new ServerView("Server");
    }
}

