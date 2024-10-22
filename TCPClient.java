
import java.io.*;
import java.net.*;

public class TCPClient {

    public static void main(String argv[]) throws Exception {
        String sentence = "";
        String reply;

        int port = 6789;

        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        Socket clientSocket = new Socket("localhost", port); //localhost = 127.0.0.1

        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        System.out.println("The TCP client is on. Please enter your input:");

        sentence = inFromUser.readLine();
        while (!sentence.equals("exit")) {

            outToServer.writeBytes(sentence + '\n');

            reply = inFromServer.readLine();

            System.out.println("FROM SERVER: " + reply);
            sentence = inFromUser.readLine();
        }
        outToServer.writeBytes(sentence + '\n');
        Thread.sleep(2000); // waits for 2 seconds
        clientSocket.close();

    }
}
