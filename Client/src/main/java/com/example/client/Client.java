package com.example.client;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Client extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        ViewProvider.getLogin(stage);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}