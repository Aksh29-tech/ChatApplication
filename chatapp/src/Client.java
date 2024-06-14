import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    Socket socket;
    BufferedReader bufferedReader;
    PrintWriter printWriter;
    public Client() {
        try {
            System.out.println("Sending request to Server");
            socket = new Socket("127.0.0.1",7777);
            System.out.println("connection done.");

            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                        socket.close();
                        break;
                    }
                    System.out.println("Server: " + message);
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
