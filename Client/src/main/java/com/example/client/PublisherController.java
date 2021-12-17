package com.example.client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Timer;

public class PublisherController {
    @FXML
    private TextField txtMessage;
    @FXML
    private Button btnSend;

    @FXML
    private Button btnStop;

    Timer timer = new Timer();

    private Socket _socket = null;
    private InputStream _is = null;
    private OutputStream _os = null;

//    public void sendRandom() {
//
//        DataSender dataSender = new DataSender(_socket, _is, _os);
//
//        do {
//            dataSender.start();
//        } while (_socket != null);
//    }



    @FXML
    protected void send() throws IOException {
//        System.out.println(txtMessage.getText());
//        _os.write(txtMessage.getText().getBytes(StandardCharsets.UTF_8));
//        _os.flush();
//        System.out.println("Client: " + txtMessage.getText());
        DataSender dataSender = new DataSender(_socket, _is, _os);

        timer.schedule(dataSender,0,  3000);

        btnSend.setDisable(true);
    }

    @FXML
    protected void stop(ActionEvent actionEvent) {
        try {
            if (_socket != null && _socket.isConnected()) {
                _socket.close();
                timer.cancel();
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
            Node source = (Node) actionEvent.getSource();
            Stage curStage = (Stage) source.getScene().getWindow();
            curStage.close();
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
