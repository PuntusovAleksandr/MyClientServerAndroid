package com.aleksandrP.myclientserver.app.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Aleksandr on 21.05.2015.
 */
public class MyClient {

    private Socket socket=null;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private String outputText = "";


    public void clientConnect() {
        try {
            while (true){
                if (InetAddress.getByName("localhost").isReachable(5)){
                socket = new Socket(InetAddress.getByName("localhost"), 5678);
                }else{
                    socket = new Socket(InetAddress.getByName("localhost"), 5678);
                }
//                socket = new Socket(java.net.InetAddress.getByName("192.168.15.17"),5678);
//                Socket newSocket = new Socket(InetAddress.getByName("localhost"), 5678);

                output=new ObjectOutputStream(socket.getOutputStream());
                input = new ObjectInputStream(socket.getInputStream());
                outputText = (String) input.readObject();
            }
        } catch (IOException e) {
            System.out.println("1");
            clientConnect();
        } catch (ClassNotFoundException e) {
            System.out.println("2");
        }finally {
            try {
                input.close();
                output.close();
            } catch (IOException e) {

            }
        }
    }

    public void sendDate (Object obj)  {
        try {
            output.flush();
            output.writeObject(obj);
        } catch (IOException e) {
            System.out.println("Error send text");
        }
    }

    public String getOutputText() {
        return outputText;
    }
}
