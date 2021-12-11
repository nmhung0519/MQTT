package com.example.broker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;

public class BrokerController {
    @FXML
    private Button btnStart;
    private Listener _listenerPublish = null;
    private Listener _listenerSubcribe = null;
    private static List<Topic> _topics = null;
    @FXML
    protected void startListener() {
        _topics = new ArrayList<Topic>();
        _listenerPublish = new Listener(ConnectionType.PUB);
        _listenerPublish.start();
        _listenerSubcribe = new Listener(ConnectionType.SUB);
        _listenerSubcribe.start();
        btnStart.setDisable(true);
    }

    public static boolean checkExistsTopic(String name) {
        for (Topic t : _topics) {
            if (t.getName().equals(name)) return true;
        }
        return false;
    }

    public static void pushMessage(String topic, String message) {
        for (Topic t : _topics) {
            if (t.getName().equals(topic)) t.addMessage(message);
        }
    }
}