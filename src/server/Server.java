package server;

import client.ClientToServerMessage;
import client.Hello;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    void handleClient(Socket socket) throws Exception {
        try (var is = socket.getInputStream();
             var os = socket.getOutputStream()) {

            var oos = new ObjectOutputStream(os);
            var ois = new ObjectInputStream(is);

            while (true) {
                var message = (ClientToServerMessage) ois.readObject();

                if (message instanceof Hello(String name)) {
                    oos.writeObject(new Acknowledged("Hello " + name));
                }
                else {
                    System.err.println("Unknown message from client");
                }
            }
        }
    }

    void run() throws Exception {
        System.out.println("Server starting...");
        try (var serverSocket = new ServerSocket(5000)) {
            System.out.println("Server started.");
            while (true) {
                try {
                    var socket = serverSocket.accept();
                    Thread.startVirtualThread(() -> {
                        try {
                            handleClient(socket);
                        } catch (Exception e) {
                            e.printStackTrace(System.err);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new Server().run();
    }
}
