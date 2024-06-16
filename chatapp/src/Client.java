import com.sun.nio.sctp.MessageInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends JFrame{
    Socket socket;
    BufferedReader bufferedReader;
    PrintWriter printWriter;

    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    public Client() {
        try {
            System.out.println("Sending request to Server");
            socket = new Socket("127.0.0.1",7777);
            System.out.println("connection done.");

            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvents();
            startReading();
//            startWriting();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                 if(e.getKeyCode() == 10) {
                    String contentToSend = messageInput.getText();
                    messageArea.append("Me: " + contentToSend + "\n");
                    printWriter.println(contentToSend);
                    printWriter.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();

                 }
            }
        });
    }

    private void createGUI() {
        this.setTitle("Client Messenger");
        this.setSize(600,700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //coding for component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

        ImageIcon originalIcon = new ImageIcon("logo_Chat_Application.png");
        Image resizedImage = originalIcon.getImage().getScaledInstance(50,50,Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        heading.setIcon(resizedIcon);

        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        //frame ka layout set karenge
        this.setLayout(new BorderLayout());

        //adding components to frame
        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    public void startReading() {
        // thread - read karke deta rahega
        Runnable r1 = () -> {
            System.out.println("reader started..");

            try {
                while(true) {
                    String message = null;
                    message = bufferedReader.readLine();
                    if(message.equals("exit")) {
                        System.out.println("Server terminated the chat");
                        JOptionPane.showMessageDialog(this, "Server Terminated the Chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
//                    System.out.println("Server: " + message);
                    messageArea.append("Server: " + message + "\n");
                }
            } catch (Exception e) {
                System.out.println("connection closed");
            }

        };
        new Thread(r1).start();
    }
    public void startWriting() {
        // thread - data user lega and send karega client tak
        Runnable r2 = () -> {
            System.out.println("writer started..");

            try {
                while(!socket.isClosed()) {
                    BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = bufferedReader1.readLine();

                    printWriter.println(content);
                    printWriter.flush();

                    if(content.equals("exit")) {
                        socket.close();
                        break;
                    }
                }
                System.out.println("connection closed");
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        new Thread(r2).start();
    }
    public static void main(String[] args) {
        new Client();
    }
}
