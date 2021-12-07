package com.example.broker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class BrokerController {
    @FXML
    private Button btnStart;
    private Listener _listener = null;
    @FXML
    protected void startListener() {
        _listener = new Listener();
        _listener.start();
        btnStart.setDisable(true);
    }
}