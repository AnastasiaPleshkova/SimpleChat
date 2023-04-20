import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class SimpleChatClient {
    JTextArea incoming;
    JTextField outgoing;
    BufferedReader reader;
    PrintWriter writer;
    Socket sock;

    public static void main(String[] args) {
        new SimpleChatClient().go();
    }

    public  void go() {
        JFrame frame = new JFrame("SimpleChat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel mainPanel = new JPanel();
        incoming = new JTextArea(15,50);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);
        JScrollPane qScroller = new JScrollPane(incoming);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        outgoing = new JTextField(30);
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    writer.println(outgoing.getText());
                    writer.flush();
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
                outgoing.setText("");
                outgoing.requestFocus();
            }
        });

        mainPanel.add(qScroller);
        mainPanel.add(outgoing);
        mainPanel.add(sendButton);

        setUpNetworking();

        Thread readerThread = new Thread(() -> {
            String message;
            try{
                while((message = reader.readLine()) != null){
                    System.out.println("read" + message);
                    incoming.append(message + "\n");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        readerThread.start();
        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(600, 400);
        frame.setVisible(true);
    }
    public void setUpNetworking()  {
        try
        {
            sock = new Socket("127.0.0.1",5000);
            InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
            reader = new BufferedReader(streamReader);
            writer = new PrintWriter(sock.getOutputStream());
            System.out.println("Networking established");
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
}
