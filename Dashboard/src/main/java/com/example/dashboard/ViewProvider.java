package com.example.dashboard;

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
        FXMLLoader fxmlLoader = new FXMLLoader(Dashboard.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 320);
        stage.setTitle("Client");
        stage.setScene(scene);
    }

    public static void getDashboard(Stage stage, Socket socket) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Dashboard.class.getResource("dashboard-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        DashboardController controller = fxmlLoader.<DashboardController>getController();
        stage.setTitle("Dashboard");
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                Platform.exit();
                controller.stopDashboard();
            }
        });
        controller.setSocket(socket);
        controller.start();
        stage.setScene(scene);
    }
}
