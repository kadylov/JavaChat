// Kamil Adylov

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientApp {

    private static final String GREEN = "\u001B[32;40;1m";
    private static final String RESET = "\u001B[0m";

    private static ObjectInputStream fromServer;
    private static ObjectOutputStream toServer;
    private static Socket serverSocket;

    private static Client client;

    public static void main(String[] args) throws IOException {

        String hostname = "localhost";
        int portNumber = 8888;

        connect(hostname, portNumber);

    }

    public static void connect(String hostName, int portNumber) {
        try {
            serverSocket = new Socket(hostName, portNumber);
            fromServer = new ObjectInputStream(serverSocket.getInputStream());
            toServer = new ObjectOutputStream(serverSocket.getOutputStream());

            start(hostName, portNumber);

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            e.printStackTrace();
            System.exit(1);
        }
    }


    private static void start(String hostName, int portNumber) {


        try {
            promptAndValidateUsername();

            Message serverMsg;

            Thread writeThread = new ClientInputThread(serverSocket, toServer, client);

            writeThread.start();

//            System.out.println("Waiting server response");
            Client recipient = null;
            while ((serverMsg = (Message) fromServer.readObject()) != null) {
                recipient = serverMsg.getReceiver();

                if (serverMsg.getText().equalsIgnoreCase("#q")) {
                    System.out.println("server: bye");
                    break;
                } else if (recipient != null && serverMsg.getReceiver().compareTo(client) == 1) {

                    System.out.println(GREEN + serverMsg + RESET);

                } else

                    System.out.println(serverMsg);


            }


        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            e.printStackTrace();
            System.exit(1);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static boolean promptAndValidateUsername() {
        Scanner scanner =
                new Scanner(System.in);

        boolean isUsernameNotInUse = false;
        Message serverMsg;

        while (!isUsernameNotInUse) {
            System.out.print("Please enter username: ");
            String username1 = scanner.next();
            client = new Client(username1);
            try {
                toServer.writeObject(client);
                toServer.flush();


                serverMsg = (Message) fromServer.readObject();
                if (!serverMsg.getText().equals("ok"))
                    System.out.println("Username is in use. Please create another one.\n");
                else
                    isUsernameNotInUse = true;

            } catch (IOException e) {
                System.err.println("Couldn't get I/O for the connection to hostname");

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        }
        return isUsernameNotInUse;
    }

}
