package com.example.client;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.TimerTask;

public class DataSender extends TimerTask {

    private Socket _socket = null;
    private InputStream _is = null;
    private OutputStream _os = null;

    DataSender(Socket socket, InputStream is, OutputStream os) {
        this._is = is;
        this._os = os;
        this._socket = socket;
    }

    public void run() {
        try {

            int temp = getRandomNumber(30, 40);
            String s = String.valueOf(temp);
            s = s + " do C";
            _os.write(s.getBytes(StandardCharsets.UTF_8));
            _os.flush();
        }
        catch (Exception e){
            System.err.println(e);
        }
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
