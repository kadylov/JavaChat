import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientInputThread extends Thread {

    private Socket serverSocket;

    private boolean isRunning;
    private ObjectOutputStream toServer;
    private Client sender;


    public ClientInputThread(Socket socket, ObjectOutputStream oos, Client client) {
        this.serverSocket = socket;
        this.toServer = oos;
        sender = client;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        Message message=null;
        isRunning = true;
        while (isRunning) {

            if(serverSocket.isClosed()) {
                System.out.println("from " + sender + ": server socket is closet");

                break;
            }

            try {
                message=new Message(sender, scanner.nextLine());
                toServer.writeObject(message);
                toServer.flush();

                System.out.println(message);

                if (message.getText().equalsIgnoreCase("#q")) {
                    isRunning = false;
                }

            } catch (IOException e) {
                System.out.println("Error! Failed to sent message to server");
                e.printStackTrace();
            }


        }

    }

}
