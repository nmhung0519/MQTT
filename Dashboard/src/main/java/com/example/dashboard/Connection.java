package com.example.dashboard;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketOption;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Connection extends Thread {
    private Socket _socket;
    private DashboardController _controller;
    private InputStream _is;
    private OutputStream _os;
    private Thread thread;
    public Connection(Socket socket, DashboardController controller) throws IOException {
        _socket = socket;
        _controller = controller;
        _is = socket.getInputStream();
        _os = socket.getOutputStream();
    }
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
                _controller.updateMessage(message);
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
}
