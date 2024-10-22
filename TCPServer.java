
import java.io.*;
import java.net.*;

public class TCPServer {

    private static int totalMessages = 0;

    public static void main(String argv[]) throws Exception {

        int port = 6789;

        ServerSocket welcomeSocket = new ServerSocket(port);
        System.out.println("TCP server is listening on port " + port + "\n");

        while (true) {

            Socket connectionSocket = welcomeSocket.accept();
            System.out.println("[" + connectionSocket.getInetAddress().toString().substring(1) + ":" + connectionSocket.getPort() + "] CONNECTED");

            Runnable clientHandler = new ClientThread(connectionSocket);
            Thread thread = new Thread(clientHandler);
            thread.start();

        }

    }

    public static synchronized int messageIncrement() {
        return ++totalMessages;
    }
    
    
    static class ClientThread implements Runnable {

        private Socket clientSocket;
        private int messageCount = 0;

        public ClientThread(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {

            String clientID = "[" + clientSocket.getInetAddress().toString().substring(1) + ":" + clientSocket.getPort() + "]";

            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));  
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                String message;
                while ((message = in.readLine()) != null) {
                    if (message.equalsIgnoreCase("exit")) {
                        System.out.println(clientID + " DISCONNECTED");
                        break;
                    }

                    int totalMessages = TCPServer.messageIncrement();

                    System.out.println(clientID + " MESSAGE: " + message);
                    System.out.println("\tFrom User: " + ++messageCount);
                    System.out.println("\tTotal Messages: " + totalMessages);

                    out.println("Total Messages: " + totalMessages);
                }
                clientSocket.close();
            }
            catch (IOException e) {
                System.err.println(clientID + " " + e.getMessage());
            }
        }
    }

}
