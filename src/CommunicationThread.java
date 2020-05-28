import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommunicationThread extends Thread {


    private Socket clientSocket;

    private ObjectInputStream fromClient;
    private ObjectOutputStream toClient;

    private Server mainServer;

    private Client client;


    public CommunicationThread(String username, Socket socket, Server server) {
        super(username);
        this.clientSocket = socket;

        this.mainServer = server;

        try {
            toClient = new ObjectOutputStream(socket.getOutputStream());
            fromClient = new ObjectInputStream(socket.getInputStream());

        } catch (IOException e) {
            System.out.println("Error in serverIO constructor");
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        Message newMessage = null;

        // validate username
        try {

            System.out.println("validating username");
            boolean created = false;
            while (!created) {


                // verify client first
                while ((client = (Client) fromClient.readObject()) != null) {

                    if (mainServer.addClient(client) == true) {
                        sendMessage(new Message("ok"));
                        System.out.println(client + " was added to the list");
                        created = true;
                        break;
                    } else {
                        sendMessage(new Message("no"));
                    }

                }
            }

            if (client == null)
                System.out.println("Client is null");
            else if (toClient == null)
                System.out.println("toClient is null");

            mainServer.addToOutputStreamList(client, toClient);

            newMessage = new Message(new Client("server"), client + " has connected...");


//            sendMessage(newMessage);
            notifyAll(newMessage);

            sendMessage(new Message("\n\nWelcome to JavaChat"));
            sendHelpInfo();

            // for debugging purpose
            System.out.println("Reading user newMessage...");

            // wait for client response
            while ((newMessage = (Message) fromClient.readObject()) != null) {

                String txt = newMessage.getText();

                if (txt.equalsIgnoreCase("#q")) {
                    sendMessage(new Message(client, null, "#q"));

                    notifyAll(new Message(null, new Client("server"), client + " disconnected..."));
                    mainServer.removeClient(client);
                    mainServer.removefromObjectOutputStreamList(client, toClient);

                    break;
                } else if (txt.equalsIgnoreCase("#help")) {

                    sendHelpInfo();

                } else if (txt.equalsIgnoreCase("#list")) {
                    sendListofConnectedClients();

                } else if (txt.startsWith("@")) {

                    sendToSelectedRecipient(newMessage);


                } else {

                    System.out.println(newMessage);
                    notifyAll(newMessage);
                }

            }

            fromClient.close();
            toClient.close();
            clientSocket.close();
            System.out.println(client + "  disconnected...");


        } catch (IOException e) {
            System.out.println("Failed to read " + client + "'s message");
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }


    public void sendHelpInfo() {
        String helpInfo = "\n\n#q    <-------- quit from the chat\n";
        helpInfo += "#list <-------- print list of connected users\n";
        helpInfo += "#help <-------- list of commands\n";
        helpInfo += "@uname <------ send private message to user whose username is 'uname'\n";
        sendMessage(new Message(helpInfo));
    }

    public void sendListofConnectedClients() {


        try {


            Map<String, Client> clientMap = mainServer.getListOfClients();
            List<Client> list = new ArrayList<Client>(clientMap.values());


            Message message = new Message("\n\n" + list.toString());
            toClient.writeObject(message);
        } catch (IOException e) {
            System.out.println("Unable to send list of users");
            e.printStackTrace();
        }
    }

    public void sendMessage(Message message) {
        try {
            toClient.writeObject(message);
            toClient.flush();

        } catch (IOException e) {
            System.out.println("Fail to send a message");
            e.printStackTrace();
        }
    }


    public void sendToSelectedRecipient(Message message) throws IOException {

        Map<Client, ObjectOutputStream> map = mainServer.getClientOutputStreamList();

        ObjectOutputStream oos;

        Matcher matcher = Pattern.compile("@s*(\\w+)").matcher(message.getText());
        while (matcher.find()) {

            String recipientUsername = matcher.group(1);
            Client client = mainServer.getListOfClients().get(recipientUsername);
            System.out.println("\nFinding user: " + client);

            if (map.containsKey(client)) {
                oos = map.get(client);
                System.out.println("Sending private to " + recipientUsername);
                message.setReceiver(new Client(recipientUsername));
                message.setText(separateMessage(message));
                oos.writeObject(message);
                oos.flush();

            }

        }


    }

    private String separateMessage(Message msg) {
        Client recipient = msg.getReceiver();
        String newMsg = msg.getText().replace("@" + recipient.getUsername(), "");
        return newMsg;
    }

    // notify all connected clients
    public void notifyAll(Message message) throws IOException {
        if (message == null)
            return;

        Map<Client, ObjectOutputStream> clientOutputStreamList = mainServer.getClientOutputStreamList();

        if (clientOutputStreamList.isEmpty())
            return;

        else {
            for (Map.Entry<Client, ObjectOutputStream> entry : clientOutputStreamList.entrySet()) {

                // use if statement to ensure the current user is not notified
                if (!entry.getKey().equals(client)) {
                    ObjectOutputStream oos = entry.getValue();
                    oos.writeObject(message);
                    oos.flush();

                }
            }

        }
    }


}
