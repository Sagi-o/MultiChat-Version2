package view;

import interfaces.View;
import javax.swing.*;
import java.awt.*;

/**
 * GUI class that constructs a server side chatter
 */
public class ServerView implements View {

    private JFrame frame;
    private JTextArea textArea;
    private JButton startButton;
    private JLabel IPtext, portText, onlineTitle;
    private JList<String> onlineList;
    private DefaultListModel<String> listModel;

    /**
     * Initialize new frame.
     * @param title of the frame
     */
    public ServerView(String title) {
        frame = new JFrame(title);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        JPanel northPanel = new JPanel(new BorderLayout());
        mainPanel.add(northPanel, BorderLayout.NORTH);

        JPanel westPanel = new JPanel(new BorderLayout());
        westPanel.setPreferredSize(new Dimension(100,500));
        mainPanel.add(westPanel, BorderLayout.WEST);

        textArea = new JTextArea();
        startButton = new JButton("Stop");
        IPtext = new JLabel("IP: ");
        portText = new JLabel("Port: ");
        onlineTitle = new JLabel("Online");
        listModel = new DefaultListModel<>();
        onlineList = new JList<>(listModel);

        JScrollPane scrollList = new JScrollPane(onlineList);
        JScrollPane scrollText = new JScrollPane(textArea);

        onlineList.setAutoscrolls(true);
        textArea.setAutoscrolls(true);

        mainPanel.add(scrollText, BorderLayout.CENTER);

        westPanel.add(scrollList, BorderLayout.CENTER);
        westPanel.add(onlineTitle, BorderLayout.NORTH);

        northPanel.add(IPtext,BorderLayout.WEST);
        northPanel.add(portText,BorderLayout.CENTER);
        northPanel.add(startButton,BorderLayout.EAST);

        frame.setTitle("Server Online");
        frame.getContentPane().add(mainPanel);
        frame.setVisible(true);
    }
    public JFrame getFrame() {
        return frame;
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public JButton getStartButton() {
        return startButton;
    }

    public JLabel getIPtext() {
        return IPtext;
    }

    public JLabel getPortText() {
        return portText;
    }

    public DefaultListModel<String> getOnlineList() {
        return listModel;
    }
}

