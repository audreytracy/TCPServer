
import java.io.*;
import java.net.*;

public class TCPServer {

    private static int totalMessages = 0;

    public static void main(String argv[]) throws Exception {

        int port = 6789;

        ServerSocket welcomeSocket = new ServerSocket(port); // listen for connections
        System.out.println("TCP server is listening on port " + port + "\n");

        while (true) {

            Socket connectionSocket = welcomeSocket.accept(); // accept new connection
            System.out.println("[" + connectionSocket.getInetAddress().toString().substring(1) + ":" + connectionSocket.getPort() + "] CONNECTED"); // print user IP & port address

            Runnable clientHandler = new ClientThread(connectionSocket);
            Thread thread = new Thread(clientHandler); // start new thread for client
            thread.start();

        }

    }

    public static synchronized int messageIncrement() {
        return ++totalMessages; // increment shared resource totalMessages within synchronized method to handle race conditions
    }
    
    
    static class ClientThread implements Runnable {

        private Socket clientSocket;
        private int messageCount = 0; // number of messages sent to this client

        public ClientThread(Socket socket) {
            this.clientSocket = socket; 
        }

        @Override
        public void run() {

            String clientID = "[" + clientSocket.getInetAddress().toString().substring(1) + ":" + clientSocket.getPort() + "]"; // IP and port number

            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));  // get messages from client
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true); // send messages to client
                String message; 
                while ((message = in.readLine()) != null) {
                    if (message.equalsIgnoreCase("exit")) { // end client if exit is typed (ends loop and closes socket)
                        System.out.println(clientID + " DISCONNECTED");
                        break;
                    }

                    int totalMessages = TCPServer.messageIncrement(); // increment shared resource totalMessages

                    System.out.println(clientID + " MESSAGE: " + message); // print client IP and port number, message, and number of messages
                    System.out.println("\tFrom User: " + ++messageCount);
                    System.out.println("\tTotal Messages: " + totalMessages);

                    out.println("Total Messages: " + totalMessages); // send message total to client
                }
                clientSocket.close();
            }
            catch (IOException e) {
                System.err.println(clientID + " " + e.getMessage());
            }
        }
    }

}
