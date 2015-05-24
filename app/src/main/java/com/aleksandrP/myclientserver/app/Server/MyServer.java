package com.aleksandrP.myclientserver.app.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Aleksandr on 21.05.2015.
 */
public class MyServer {

    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to Server side");
        BufferedReader in = null;
        PrintWriter out= null;

        while (true) {

            ServerSocket servers = null;
            Socket connect = null;

            // create server socket
            try {
                servers = new ServerSocket(5555, 5);
            } catch (IOException e) {
                System.out.println("Couldn't listen to port 4444");
                System.exit(-1);
            }

            try {
                System.out.print("Waiting for a client...");
                connect = servers.accept();
                System.out.println("Client  "+connect.toString()+"  is connected");
                System.out.println(connect.toString());
            } catch (IOException e) {
                System.out.println("Can't accept");
                System.exit(-1);
            }

            in = new BufferedReader(new
                    InputStreamReader(connect.getInputStream()));
            out = new PrintWriter(connect.getOutputStream(), true);
            String input, output;

            System.out.println("Wait for messages");
            while ((input = in.readLine()) != null) {
                if (input.equalsIgnoreCase("exit")) break;
                out.println("S ::: " + input);
                System.out.println(input);
            }
            out.close();
            in.close();
            connect.close();
            servers.close();
        }
    }


//    private ServerSocket serverSocket;
//    private Socket socket;
//    private ObjectInputStream input;
//    private ObjectOutputStream output;
//
//    public static void main(String[] args) {
//        MyServer myServer = new MyServer();
//        myServer.startServer();
//    }
//
//    private void startServer() {
//        try {
//            while (true) {
//                serverSocket = new ServerSocket(4444, 10);
//                System.out.println("Server is create");
//                socket = serverSocket.accept();
//                System.out.println("Connect is: "+ socket.toString());
//                output = new ObjectOutputStream(socket.getOutputStream());
//                input = new ObjectInputStream(socket.getInputStream());
//                System.out.println("Your message is: " + (String) input.readObject());
//                output.writeObject("Your message is: " + (String) input.readObject());
//            }
//        } catch (IOException e) {
//            System.out.println("Error I/O ");
//        } catch (ClassNotFoundException e) {
//            System.out.println("Error Stream");
//        }finally {
//            try {
//                input.close();
//                output.close();
//            } catch (IOException e) {
//
//            }
//        }
//    }
}
