package com.example.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

public class LoginController {
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private TextField txtTopic;
    @FXML
    private Button btnStart;
    @FXML
    private Text txtError;

    private Socket socket;
    private OutputStream os;
    private InputStream is;
    byte[] buffer;

    @FXML
    protected void start(ActionEvent actionEvent) throws IOException {
        btnStart.setDisable(true);
        txtError.setVisible(false);
        try {
            socket = new Socket(Common.SERVER_ADDRESS, Common.SERVER_PORT);
            os = socket.getOutputStream();
            is = socket.getInputStream();
            Node source = (Node) actionEvent.getSource();
            Stage curStage = (Stage) source.getScene().getWindow();
            Action action = new Action() {
                @Override
                void start() throws IOException {
                    try {
                        System.out.println("Connect successfully!");
                        Stage stage = new Stage();
                        ViewProvider.getPublisher(stage, socket);
                        stage.show();
                        curStage.close();
                    }
                    catch (Exception ex) {
                        System.out.println("[Error while start publisher] - " + ex);

                        if (socket.isConnected()) {
                            socket.close();
                            System.out.println("Close connection!");
                        }
                        btnStart.setDisable(false);
                    }
                }
            };
            CompletableFuture<Void> completableFuture = new CompletableFuture<Void>();
            completableFuture.complete(login(action));

        }
        catch (Exception ex) {
            System.out.println("[Error while connect to server]" + ex);
            if(socket == null) {

                txtError.setText("Error when connecting to server. Please try again");
                txtError.setVisible(true);
                btnStart.setDisable(false);
                return;
            }
            if (socket.isConnected()) {
                socket.close();
                System.out.println("Close connection!");
            }
            btnStart.setDisable(false);
        }
    }

    private Void login(Action action) {
        try {
            do {
                buffer = new byte[Common.BUFFER_SIZE];
                int len = is.read(buffer);
                String message = new String(buffer, 0, len, StandardCharsets.UTF_8);
                System.out.println("SERVER: " + message);
                if (message.equals("501")) {
                    System.out.println("Password incorrect!");
                    txtError.setVisible(true);
                    txtError.setText("Password incorrect");
                    break;
                }
                if (message.equals("502")) {
                    System.out.println("Topic not exists");
                    txtError.setVisible(true);
                    txtError.setText("Topic not exists");
                    break;
                }
                if (message.equals("200")) {
                    txtError.setVisible(false);
                    action.start();
                    return null;
                }
                if (message.equals("USERNAME")) {
                    if(txtUsername.getText().isEmpty()) {
                        txtError.setText("Username cannot be null");
                        txtError.setVisible(true);
                        btnStart.setDisable(false);
                        socket.close();
                        return null;
                    }
                    os.write(txtUsername.getText().getBytes(StandardCharsets.UTF_8));
                    os.flush();
                    System.out.println("Client: " + txtUsername.getText());
                }
                if (message.equals("PASSWORD")) {
                    if(txtPassword.getText().isEmpty()) {
                        txtError.setText("Password cannot be null");
                        txtError.setVisible(true);
                        socket.close();
                        btnStart.setDisable(false);
                        return null;
                    }
                    os.write(txtPassword.getText().getBytes(StandardCharsets.UTF_8));
                    os.flush();
                    System.out.println("Client: " + txtPassword.getText());
                }
                if (message.equals("TOPIC")) {
                    if(txtTopic.getText().isEmpty()) {
                        txtError.setText("Topics cannot be null");
                        txtError.setVisible(true);
                        btnStart.setDisable(false);
                        socket.close();
                        return null;
                    }
                    os.write(txtTopic.getText().getBytes(StandardCharsets.UTF_8));
                    os.flush();
                    System.out.println("Client: " + txtTopic.getText());
                }
            } while (true);
            if (socket.isConnected()) socket.close();
            System.out.println("Close connection!");
        } catch (Exception ex) {
            System.out.println("[Error while connect to server] - " + ex);
        }
        btnStart.setDisable(false);
        return null;
    }
}