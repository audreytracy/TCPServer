
import java.io.*;
import java.net.*;

public class TCPClient {

    public static void main(String argv[]) throws Exception {
        String message = ""; // message client is sending to server
        String reply; // reply from server to client

        int port = 6789;

        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in)); // read client messages

        Socket clientSocket = new Socket("localhost", port); // localhost = 127.0.0.1

        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream()); // send messages to server
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // get messages from server

        System.out.println("The TCP client is on. Please enter your input:");

        message = userInput.readLine(); // read message from user
        while (!message.equals("exit")) { // end client if user types "exit"

            out.writeBytes(message + '\n'); // send user message to server

            reply = in.readLine(); // read response from server

            System.out.println("FROM SERVER: " + reply); // print response from server
            message = userInput.readLine(); // read new message from user
        }
        out.writeBytes(message + '\n');
        Thread.sleep(2000); // wait for 2 seconds
        clientSocket.close();

    }
}
