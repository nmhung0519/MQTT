package com.example.broker;
import java.util.*;
import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;

public class Connection extends Thread {
    ConnectionType _type;
    Socket _socket = null;
    InputStream is = null;
    OutputStream os = null;
    String topicName = null;
    Queue<String> messageQueue;
    boolean flag = true;
    byte[] buffer;
    int state = Common.CLIENT_STATE_PENDING;
    public Connection(Socket socket, ConnectionType type) {
        try {
            _socket = socket;
            is = socket.getInputStream();
            os = socket.getOutputStream();
            _type = type;
            messageQueue = new LinkedList<String>();
        }
        catch (Exception ex) {

        }
    }
    protected void finalize() throws Throwable {

    }
    public void run() {
        System.out.println("Start thread for connection");
        try {
            switch (verify()) {
                case PASSWORD_INCORRECT:
                    os.write(("501").getBytes(StandardCharsets.UTF_8));
                    os.flush();

                    System.out.println("Server: 501");
                    break;
                case TOPIC_NOT_EXITS:
                    os.write(("502").getBytes(StandardCharsets.UTF_8));
                    os.flush();
                    System.out.println("Server: 502");
                    break;
                case ERROR:
                    os.write(("502").getBytes(StandardCharsets.UTF_8));
                    os.flush();
                    System.out.println("Server: 503");
                    break;
                case SUCCESS:
                    os.write(("200").getBytes(StandardCharsets.UTF_8));
                    System.out.println("Server: 200");
                    state = Common.CLIENT_STATE_ACTIVE;
                    break;
            }
        }
        catch (Exception ex) {
            System.out.println("[Error while verify connection] - " + ex);
        }
        if (state == Common.CLIENT_STATE_ACTIVE) {
            if (_type == ConnectionType.PUB) {
                do {
                    try {
                        buffer = new byte[Common.BUFFER_SIZE];
                        int len = is.read(buffer);

                        String message = new String(buffer, 0, len, StandardCharsets.UTF_8);
                        System.out.println("Client: " + message);
                        BrokerController.pushMessage(topicName, message);
                    } catch (Exception ex) {
                        break;
                    }
                } while (true);
            }
            else if (_type == ConnectionType.SUB) {
                BrokerController.checkTopic(topicName);
                try {
                    _socket.setSoTimeout(3000);
                    do {
                        if (is.read() == -1) throw new Exception();
                        if (messageQueue.stream().count() > 0) {
                            try {
                                String message = messageQueue.poll();
                                os.write(message.getBytes(StandardCharsets.UTF_8));
                                os.flush();
                            } catch (Exception ex) {
                            }
                        }
                    } while (_socket.isConnected());
                }
                catch (Exception ex) {
                    try {
                        if (_socket != null && _socket.isConnected()) _socket.close();
                    }
                    catch (Exception exc) {}
                }
            }
        }
        if (_type == ConnectionType.PUB) {
            BrokerController.deletePublisher(this);
        }
        else if (_type == ConnectionType.SUB) {
            BrokerController.deleteSubriber(this);
        }
        System.out.println("Close connection with client!");
    }
    private VerifyResult verify() {
        try {
            int len;
            os.write(("USERNAME").getBytes(StandardCharsets.UTF_8));
            os.flush();
            System.out.println("Server: USERNAME");
            buffer = new byte[Common.BUFFER_SIZE];
            len = is.read(buffer);
            String username = new String(buffer, 0, len, StandardCharsets.UTF_8);
            System.out.println("Client: " + username);
            os.write(("PASSWORD").getBytes(StandardCharsets.UTF_8));
            os.flush();
            System.out.println("Server: PASSWORD");
            buffer = new byte[Common.BUFFER_SIZE];
            len = is.read(buffer);
            String password = new String(buffer, 0, len, StandardCharsets.UTF_8);
            System.out.println("Client: " + password);
            if (!login(username, password)) return VerifyResult.PASSWORD_INCORRECT;
            os.write(("TOPIC").getBytes(StandardCharsets.UTF_8));
            os.flush();
            System.out.println("Server: TOPIC");
            buffer = new byte[Common.BUFFER_SIZE];
            len = is.read(buffer);
            String topic = new String(buffer, 0, len, StandardCharsets.UTF_8);
            System.out.println("Client: " + topic);
            if (!BrokerController.checkExistsTopic(topic)) return VerifyResult.TOPIC_NOT_EXITS;
            topicName = topic;
            return VerifyResult.SUCCESS;
        }
        catch (Exception ex) {
            return VerifyResult.ERROR;
        }
    }
    private boolean login(String username, String password) {
        if (username.equals("admin") && password.equals("admin")) return true;
        return false;
    }
    public String getTopic() {
        return topicName;
    }
    public void sendMessage(String message) {
        messageQueue.add(message);
    }
    public boolean isActive() {
        if (state == Common.CLIENT_STATE_ACTIVE) return true;
        return false;
    }
}
