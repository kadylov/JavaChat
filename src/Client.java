// Kamil Adylov

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client implements Comparable<Client>, Serializable {

    private String username;


    public Client(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }


    @Override
    public String toString() {
        return username;
    }

    @Override
    public int compareTo(Client o) {
        if (username.equals(o.username))
            return 1;
        else return 0;
    }
}
