import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerReceiverThread extends Thread {

    private ObjectInputStream fromClient;
    private Socket clientSocket;


    public ServerReceiverThread(Socket socket) {

        this.clientSocket = socket;


        try {
            fromClient = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("Failed to create ObjectInputStream in ServerReceiverThread construction");
            e.printStackTrace();
        }


    }


    @Override
    public void run() {

        String message;

        try {
            while ((message = fromClient.readUTF()) != null) {


            }

        }catch (IOException e){
            System.out.println("Failed to receive a client message");
            e.printStackTrace();
        }

    }


}
