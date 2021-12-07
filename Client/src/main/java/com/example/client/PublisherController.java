package com.example.client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class PublisherController {
    @FXML
    private Button btnStop;
    private Socket _socket = null;
    private InputStream _is = null;
    private OutputStream _os = null;

    @FXML
    protected void stop() {
        try {
            if (_socket != null && _socket.isConnected()) {
                _socket.close();
                System.out.println("Close connection!");
            }
        }
        catch (Exception ex) {
            System.out.println("[Error while close connection] - " + ex);
        }
        try {
            Stage stage = new Stage();
            ViewProvider.getLogin(stage);
            stage.show();
        }
        catch (Exception ex) {
            System.out.println("[Error while open login window] - " + ex);
        }
    }

    public void setSocket(Socket socket) throws IOException {
        _socket = socket;
        _is = socket.getInputStream();
        _os = socket.getOutputStream();
    }
}
