import java.io.*;
import java.net.*;

class Server {

    ServerSocket server;
    Socket socket;

    BufferedReader br; // for reading
    PrintWriter out; // for writing

    public Server() {

        try {

            server = new ServerSocket(7777);
            System.out.println("Server is ready to accept connection");
            System.out.println("Waiting...");
            socket = server.accept();
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startReading() {
        // threading for reading data

        Runnable r1 = () -> {
            System.out.println("Reader started...");

            try {

                while (true) {

                    String msg = br.readLine();
                    if (msg.equals("Exit")) {
                        System.out.println("Client terminated the chat");
                        socket.close();
                        break;
                    }

                    System.out.println("Client : " + msg);
                }
            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println("Connection is closed");

            }
        };

        new Thread(r1).start();
    }

    public void startWriting() {
        // threading - takes data from user and sends it to client

        Runnable r2 = () -> {
            System.out.println("Writer started.....");
            try {
                while (true && !socket.isClosed()) {

                    BufferedReader br1 = new BufferedReader(
                            new InputStreamReader(System.in));

                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                    if (content.equals("Exit")) {
                        socket.close();
                        break;
                    }
                }
            } catch (Exception e) {
                // e.printStackTrace();

                System.out.println("Connection is closed");

            }
        };

        new Thread(r2).start();
    }

    public static void main(String[] args) {

        System.out.println("This is server");
        new Server();
    }
}
