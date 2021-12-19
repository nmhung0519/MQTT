package com.example.broker;

import java.util.ArrayList;
import java.util.List;

public class Topic {
    private String name;
    private List<String> messages;
    private boolean flag;

    public Topic(String _name) {
        name = _name;
        messages = new ArrayList<String>();
        flag = false;
    }

    public String getName() {
        return name;
    }

    public void addMessage(String message) {
        messages.add(message);
        checkMessage();
    }

    public void checkMessage() {
        System.out.println("When start check [" + name + "]: " + messages);
        if (messages.stream().count() == 0) return;
        flag = !BrokerController.hasSubcriber(name);
        if (flag) return;
        while (messages.stream().count() > 0) {
            BrokerController.sendMessageToSubcribe(name, messages.get(0));
            messages.remove(0);
        };
    }
}
