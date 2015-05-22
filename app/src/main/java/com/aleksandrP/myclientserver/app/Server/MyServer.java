package com.aleksandrP.myclientserver.app.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Aleksandr on 21.05.2015.
 */
public class MyServer {

    private ServerSocket serverSocket;
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public static void main(String[] args) {
        MyServer myServer = new MyServer();
        myServer.startServer();
    }

    private void startServer() {
        try {
            while (true) {
                serverSocket = new ServerSocket(5678, 10);
                System.out.println("Server is create");
                socket = serverSocket.accept();
                output = new ObjectOutputStream(socket.getOutputStream());
                input = new ObjectInputStream(socket.getInputStream());
                System.out.println("Your message is: " + (String) input.readObject());
                output.writeObject("Your message is: " + (String) input.readObject());
            }
        } catch (IOException e) {
            System.out.println("Error I/O ");
        } catch (ClassNotFoundException e) {
            System.out.println("Error Stream");
        }finally {
            try {
                input.close();
                output.close();
            } catch (IOException e) {

            }
        }
    }
}
