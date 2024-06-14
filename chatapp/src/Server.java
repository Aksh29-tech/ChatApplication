import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
public class Server {
    ServerSocket server;
    Socket socket;
    BufferedReader bufferedReader;
    PrintWriter printWriter;
   public Server() {
       try {
           server = new ServerSocket(7777);
           System.out.println("Server is ready to accept connection");
           System.out.println("waiting...");
           socket = server.accept();

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
                       System.out.println("Client terminated the chat");
                       socket.close();
                       break;
                   }
                   System.out.println("Client: " + message);
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
           } catch (Exception e) {
               e.printStackTrace();
           }
       };
       new Thread(r2).start();
   }

    public static void main(String[] args) {
        new Server();
    }
}
