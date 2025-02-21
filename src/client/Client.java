package client;

import server.Acknowledged;
import server.ServerToClientMessage;

import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws Exception {
        System.out.println("Client starting...");
        var scanner = new Scanner(System.in);

        try (var socket = new Socket("0.0.0.0", 5000);
             var is = socket.getInputStream();
             var os = socket.getOutputStream()) {
            var oos = new ObjectOutputStream(os);
            var ois = new ObjectInputStream(is);

            System.out.println("Client started");

            while (true) {
                try {
                    System.out.print("What is your name?: ");
                    var name = scanner.nextLine();

                    oos.writeObject(new Hello(name));

                    var response = (ServerToClientMessage) ois.readObject();
                    if (response instanceof Acknowledged(String message)) {
                        System.out.println(message);
                    }
                    else {
                        System.out.println("Got an unknown message from the server");
                    }
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
            }
        }
    }
}