package com.example.dashboard;

import com.example.dashboard.Action;
import com.example.dashboard.Common;
import com.example.dashboard.ViewProvider;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

public class DashboardController {
    @FXML
    private Label lbMessage;
    private Socket _socket;
    private InputStream _is;
    private OutputStream _os;
    Thread _thread;
    public void setSocket(Socket socket) {
        _socket = socket;
    }
    public void start() throws IOException {
        _is = _socket.getInputStream();
        _os = _socket.getOutputStream();
        _thread = new Thread() {
            private Thread thread;
            public void run() {
                try {
                    thread = new Thread() {
                        private Timer _tm;
                        public void run() {
                            _tm = new Timer();
                            _tm.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    try {
                                        _os.write("1".getBytes(StandardCharsets.UTF_8));
                                        _os.flush();
                                        System.out.println("Client: 1");
                                    }
                                    catch (Exception ex) {
                                        System.out.println("[Error while check alive] - " + ex);
                                    }
                                }
                            }, 0 , 1000);
                        }

                        @Override
                        public void interrupt() {
                            _tm.cancel();
                            super.interrupt();
                        }
                    };
                    thread.start();
                    do {
                        byte[] buffer = new byte[Common.BUFFER_SIZE];
                        int len = _is.read(buffer);
                        String message = new String(buffer, 0, len, StandardCharsets.UTF_8);
                        updateMessage(message);
                        System.out.println("SERVER: " + message);
                    } while (true);
                } catch (Exception ex) {
                    System.out.println(ex);
                    if (_socket != null && _socket.isConnected()) try { _socket.close(); } catch (Exception exc) {}
                }
            }
            @Override
            public void interrupt() {
                try {
                    thread.interrupt();
                    if (_socket != null && _socket.isConnected()) {
                        _socket.close();
                        System.out.println("Close connection!");
                    }
                }
                catch (Exception ex) {
                    System.out.println("[Error while close connection] - " + ex);
                }
                super.interrupt();
            }
        };
        _thread.start();
    }
    @FXML
    protected void stop(ActionEvent actionEvent) {
        stopDashboard();
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
    public void stopDashboard() {
        try
        {
            _thread.interrupt();
            _thread = null;
            if (_socket != null && _socket.isConnected()) {
                _socket.close();
                System.out.println("Close connection!");
            }
        }
        catch (Exception ex) {
            System.out.println("[Error while close connection] - " + ex);
        }

    }
    public void updateMessage(String message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lbMessage.setText(message);
            }
        });
    }
}