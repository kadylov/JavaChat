import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {


    private Map<Client, ObjectOutputStream> clientOutputStreamList;
    private List<String> chatHistory;
    private Map<String, Client> listOfClients;
    private int port;

    public Server(int port) {
        this.port=port;

        clientOutputStreamList = new HashMap<>();
        chatHistory = new ArrayList<>();
        listOfClients = new HashMap<>();
    }


    public boolean addClient(Client newClient) {
        boolean added = true;

        if (listOfClients.containsKey(newClient.getUsername())) {
            added = false;

        } else
            listOfClients.put(newClient.getUsername(), newClient);

        return added;
    }

    public void removeClient(Client client) {
        String username = client.getUsername();
        if (listOfClients.containsValue(client)) {

            listOfClients.remove(client.getUsername(), client);
        }

        if (listOfClients.containsKey(client))
            System.out.println("removeClient--Not Removed " + username);
        else
            System.out.println("removeClient--Removed " + username);

    }


    public static void main(String[] args) throws IOException {

        Server chatServer = new Server(8888);
        chatServer.start();


    }

    public void start() {

        boolean isRunning = true;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);

            while (isRunning) {

                System.out.println("Waiting for client connection....");
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client is connected: " + clientSocket);

                Thread thread = new CommunicationThread("", clientSocket, this);
                thread.start();

            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void addToOutputStreamList(Client client, ObjectOutputStream oos) {
        clientOutputStreamList.put(client, oos);

    }


    public void removefromObjectOutputStreamList(Client client, ObjectOutputStream oos) {
        if (clientOutputStreamList.containsValue(oos))
            clientOutputStreamList.remove(client);


        if (clientOutputStreamList.containsValue(oos))
            System.out.println("OOS was not removed");

    }

    public void addMessage(String msg) {
        chatHistory.add(msg);
    }


    public List<String> getChatHistory() {
        return chatHistory;
    }

    public Map<String, Client> getListOfClients() {
        return listOfClients;
    }


    public Map<Client, ObjectOutputStream> getClientOutputStreamList() {
        return clientOutputStreamList;
    }

}
