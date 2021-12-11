package com.example.broker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Listener extends Thread {
    ServerSocket listener = null;
    Socket socket;
    ConnectionType _type;
    public Listener(ConnectionType type) {
        _type = type;
    }
    public void run() {
        System.out.println("Starting listener...");
        try {
            switch (_type) {
                case PUB:
                    listener = new ServerSocket(Common.PUBLISH_PORT);
                    break;
                case SUB:
                    listener = new ServerSocket(Common.SUBCRIBE_PORT);
                    break;
                default:
                    throw new Exception("Not Exists Connection Type");
            }
        }
        catch (Exception ex) {
            System.out.println("[Error while start listener] - " + ex);
            return;
        }
        System.out.println("Started listener");
        do {
            try {
                socket = listener.accept();
                Connection connection = new Connection(socket, _type);
                connection.run();
            }
            catch (Exception ex) {
                System.out.println("[Error while accept connection] - " + ex);
            }
        } while (true);
    }
}
