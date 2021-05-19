package sample;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;
    private int port;

    public Server() {
        this.port = 9999;
    }

    private void start() throws IOException {
        System.out.println("Server : \nStarting the socket server at port:" + port);
        serverSocket = new ServerSocket(port);
    }
}
