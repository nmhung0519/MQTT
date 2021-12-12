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
    private static Listener _listenerPublish = null;
    private static Listener _listenerSubcribe = null;
    private static List<Topic> _topics = null;
    @FXML
    protected void startListener() {
        _topics = new ArrayList<Topic>();
        _topics.add(new Topic("A"));
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

    public static boolean sendMessageToSubcribe(String topic, String message) {
        return _listenerSubcribe.pushMessageToSubcribe(topic, message);
    }

    public static boolean hasSubcriber(String topic) {
        return _listenerSubcribe.hasConnectionInTopic(topic);
    }
    public static void checkTopic(String topic) {
        for (Topic t : _topics) {
            if (t.getName().equals(topic)) {
                t.checkMessage();
                break;
            }
        }
    }
    public static void deleteSubriber(Connection connection) {
        _listenerSubcribe.deleteConnection(connection);
    }
    public static void deletePublisher(Connection connection) {
        _listenerPublish.deleteConnection(connection);
    }
}