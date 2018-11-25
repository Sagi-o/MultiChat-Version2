package view;

import interfaces.View;
import model.Client;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

/**
 * GUI class that constructs a client side chatter
 */
public class ClientView implements View {

    private static JFrame frame;
    private JTextField textField;
    private JTextArea textArea;
    private JButton sendButton, sendAllButton, loginButton;
    private JLabel IPtext, portText, onlineTitle;
    private JList<String> onlineList;
    private DefaultListModel<String> listModel;


    /**
     * Initialize new frame.
     * @param title of the frame
     */
    public ClientView(String title) {
        frame = new JFrame(title);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        createView();
    }

    /**
     * Creates the main view using BorderLayout.
     */
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
        listModel = new DefaultListModel<>();
        onlineList = new JList<>(listModel);

        JScrollPane scrollList = new JScrollPane(onlineList);
        JScrollPane scrollText = new JScrollPane(textArea);

        onlineList.setAutoscrolls(true);
        textArea.setAutoscrolls(true);

        mainPanel.add(scrollText);

        northPanel.add(IPtext, BorderLayout.WEST);
        northPanel.add(portText, BorderLayout.CENTER);
        northPanel.add(loginButton, BorderLayout.EAST);

        southPanel.add(textField, BorderLayout.WEST);

        inSouthPanel.add(sendButton, BorderLayout.WEST);
        inSouthPanel.add(sendAllButton, BorderLayout.EAST);

        westPanel.add(onlineTitle, BorderLayout.NORTH);
        westPanel.add(scrollList, BorderLayout.CENTER);

        frame.add(mainPanel);
        frame.setVisible(true);
    }
    public JFrame getFrame() {
        return frame;
    }

    public JTextField getTextField() {
        return textField;
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public JButton getSendButton() {
        return sendButton;
    }

    public JButton getSendAllButton() {
        return sendAllButton;
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    public JLabel getIPtext() {
        return IPtext;
    }

    public JLabel getPortText() {
        return portText;
    }

    public DefaultListModel<String> getListModel() {
        return listModel;
    }

    public JList<String> getOnlineList() {
        return onlineList;
    }

    public void showMessagePrompt(String text) {
        JOptionPane.showMessageDialog(frame,
                text);
    }

    /**
     * Prompt user to enter a name.
     * @return the name that entered
     */
    public static String openNamePrompt() {
        return JOptionPane.showInputDialog(
                frame,
                "Choose a screen name:",
                "Chat name selection",
                JOptionPane.PLAIN_MESSAGE);
    }

}
