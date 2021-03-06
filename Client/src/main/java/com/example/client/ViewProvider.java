package com.example.client;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.Socket;

public class ViewProvider {
    public static void getLogin(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 320);
        stage.setTitle("Client");
        stage.setScene(scene);
    }

    public static void getPublisher(Stage stage, Socket socket) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("publisher-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        PublisherController controller = fxmlLoader.<PublisherController>getController();
        controller.setSocket(socket);
        stage.setTitle("Publisher");
        stage.setScene(scene);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                Platform.exit();
                controller.stopPublisher();
            }
        });
        controller.autoSendData();
    }
}
