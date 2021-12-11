package com.example.broker;

import java.util.ArrayList;
import java.util.List;

public class Topic {
    private String name;
    private List<String> messages;

    public Topic(String _name) {
        name = _name;
        messages = new ArrayList<String>();
    }

    public String getName() {
        return name;
    }

    public void addMessage(String message) {
        messages.add(message);
    }
}
