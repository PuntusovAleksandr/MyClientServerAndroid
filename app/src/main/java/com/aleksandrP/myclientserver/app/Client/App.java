package com.aleksandrP.myclientserver.app.Client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Александр on 28.01.2015.
 */
public class App implements Runnable {

    static private Socket conect;                   //соединение
    static private ObjectOutputStream output;             //поток выхода
    static private ObjectInputStream input;               //поток входа

    public static void main(String[] args) {
       new Thread(new App("Test Client Server")).start();
    }

    public App(String name) {
        Scanner sc = new Scanner(new InputStreamReader(System.in));
        sendDate(sc.nextLine());
    }
    @Override
    public void run() {
        try {
            while (true){
                System.out.println("1 client");
                conect = new Socket(InetAddress.getByName("127.0.0.1"), 5678);
                output=new ObjectOutputStream(conect.getOutputStream());
                input = new ObjectInputStream(conect.getInputStream());
                System.out.println(input.readObject());
                System.out.println("2 client");
            }
        } catch (IOException e) {
            System.out.println("1");
        } catch (ClassNotFoundException e) {
            System.out.println("2");
        }
    }

    private static void sendDate (Object obj)  {
        try {
//            output.flush();
            output.writeObject(obj);
        } catch (IOException e) {
            System.out.println("Ошибка передачи 2");
        }
    }
}
