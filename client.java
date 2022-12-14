import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import javafx.scene.text.Font;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

public class client extends JFrame{
    Socket socket;
    BufferedReader br; // for reading
    PrintWriter out; // for writing

    //Declare Components
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", 20);



    //Constructor
    public client() {
        try {
            System.out.println("Sending request to server");
            socket = new Socket("192.168.1.3", 7777);
            System.out.println("Connection done");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

        createGUI();
        handleEvents();
            startReading();
            // startWriting();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    private void handleEvents() {

        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                if(e.getKeyCode() == 10) {
                    String contentToSend = messageInput.getText();
                    messageArea.append("Me :" + contentToSend+ "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
                
            }

        });
    }

    private void createGUI() {
        //gui.....

        this.setTitle("Client Message Window[Client End]");
        this.setSize(500,500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Coding for all components
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        // heading.setIcon(null);
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);



        //setting the frame layout
        this.setLayout(new BorderLayout());

        //addong the components to frame

        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);

         
        this.setVisible(true);

    }


    public void startReading() {

        // threading for reading data
        Runnable r1 = () -> {
            System.out.println("Reader started...");
            try {
                while (true) {

                    String msg = br.readLine();
                    if (msg.equals("Exit")) {
                        System.out.println("Server terminated the chat");
                        JOptionPane.showMessageDialog(this, "Server terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }

                    //System.out.println("Server : " + msg);
                    messageArea.append("Server : " + msg + "\n");
                }
            } catch (Exception e) {
                //e.printStackTrace();
                System.out.println("Connection is closed");
            }
        };

        new Thread(r1).start();
    }

    public void startWriting() {

        // threading - takes data from user and sends it to Client
        Runnable r2 = () -> {
            System.out.println("Writer started.....");
            try {
                while (true && !socket.isClosed()) {

                    BufferedReader br1 = new BufferedReader(
                            new InputStreamReader(System.in));

                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                    if(content.equals("Exit"))
                    {
                        socket.close();
                        break;
                    }

                }

                System.out.println("Connection is closed");

            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        new Thread(r2).start();
    }

    public static void main(String[] args) {

        System.out.println("This is Client");
        new client();
    }
}
