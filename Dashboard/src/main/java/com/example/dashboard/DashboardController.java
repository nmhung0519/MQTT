package com.example.dashboard;

import com.example.dashboard.Action;
import com.example.dashboard.Common;
import com.example.dashboard.ViewProvider;
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

public class DashboardController {
    public void start(Socket socket) throws IOException {
        Connection cnn = new Connection(socket);
        cnn.start();
    }
}