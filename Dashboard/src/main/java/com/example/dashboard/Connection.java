package com.example.dashboard;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Connection extends Thread {
    private Socket _socket;
    private InputStream _is;
    private OutputStream _os;
    public Connection(Socket socket) throws IOException {
        _socket = socket;
        _is = socket.getInputStream();
        _os = socket.getOutputStream();
    }
    public void run() {
        try {
            do {
                byte[] buffer = new byte[Common.BUFFER_SIZE];
                int len = _is.read(buffer);
                String message = new String(buffer, 0, len, StandardCharsets.UTF_8);
                System.out.println("SERVER: " + message);
            } while (true);
        } catch (Exception ex) {

        }
    }
}
