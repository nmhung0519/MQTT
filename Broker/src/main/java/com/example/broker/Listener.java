package com.example.broker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Listener extends Thread {

    ServerSocket listener = null;
    Socket socket;

    public void run() {
        System.out.println("Starting listener...");
        try {
            listener = new ServerSocket(Common.SERVER_PORT);
        }
        catch (Exception ex) {
            System.out.println("[Error while start listener] - " + ex);
            return;
        }
        System.out.println("Started listener");
        do {
            try {
                socket = listener.accept();
                Connection connection = new Connection(socket);
                connection.run();
            }
            catch (Exception ex) {
                System.out.println("[Error while accept connection] - " + ex);
            }
        } while (true);
    }
}
