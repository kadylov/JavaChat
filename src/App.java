//import java.net.InetAddress;
//import java.net.ServerSocket;
//import java.net.UnknownHostException;
//import java.util.ArrayList;
//import java.util.InputMismatchException;
//import java.util.List;
//import java.util.Scanner;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//public class App {
//    private static List<Server> serverList = new ArrayList<>();
//
//
//    public static void main(String[] args) {
//
//
////        client = new Client("nick", "alex");
////        client.connect(serverToConnect);
////
////        ExecutorService executorService = Executors.newCachedThreadPool();
////        executorService.execute(serverToConnect);
////        executorService.execute(client);
//
//
//    }
//
//    public static Server getServerToConnect() {
//        Server server = null;
//        Scanner sc = new Scanner(System.in);
//
//        if (!printListOfServers())       // prints all servers currently running to the screen
//            return null;
//
//        try {
//            server = serverList.get(sc.nextInt() - 1);
//
//        } catch (NumberFormatException e) {
//            System.out.println("\nError! invalid entry");
//            return null;
//        } catch (IndexOutOfBoundsException e) {
//            System.out.println("\nError! invalid entry");
//            return null;
//        }
//
//        return server;
//    }
//
//
//    public static void createServer() {
//
//        String name, address;
//        int port;
//
//
//        Scanner sc = new Scanner(System.in);
//
//        try {
//
//            System.out.print("Server name: ");
//            name = sc.next();
//            System.out.print("Server address: ");
//            address = sc.next();
//            InetAddress.getByName(address);      // will throw UnknownHostException if host name or ip is invalid
//
//            System.out.print("Server port: ");
//            port = sc.nextInt();
//
//            if (port < 0 || port > 65535) {
//                System.out.println("\nError! Port number is out of range");
//                return;
//            }
//
//            Server server = null; //= new Server(name, address, port);
////            if (!findServer(server)) {
////
////                serverList.add(server);
////                System.out.println("\nServer " + name + " has been created successfully");
////            } else {
////                System.out.println("\nServer with the same ip address and port number already exists in the system");
////                return;
////            }
//
//
//        } catch (InputMismatchException e) {
//            System.out.println("\nError! Invalid port number\n");
//            return;
//        } catch (UnknownHostException e) {
//            System.out.println("\nError! Invalid server address\n");
//            return;
//        }
//    }
//
//    public static boolean printListOfServers() {
//
//        boolean flag = true;
//        if (serverList.isEmpty()) {
//            System.out.println("\nThere is no running servers at this time. Please create a new server");
//            return flag = false;
//        } else {
//            int i = 1;
//            for (Server server : serverList) {
////                System.out.println(i + ") " + server.getName());
//                ++i;
//            }
//
//            System.out.print("\nPlease select server from the list:");
//        }
//
//        return flag;
//    }
//
////    public static boolean findServer(Server s) {
////
////        boolean found = false;
////
////        for (Server server : serverList) {
////            if (server.compareTo(s) == 0) {
////                found = true;
////                break;
////            }
////
////        }
////
////        return found;
////    }
//
//
//}
