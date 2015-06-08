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
    /**
     *
     * @throws IOException - исключение ввода вывода
     */
    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to Server side");
        BufferedReader in = null;
        PrintWriter out= null;
/**
 * в бесконечном цикле проверяем соединение
 */
        while (true) {

            ServerSocket servers = null;
            Socket connect = null;

            /**
             * сreate server socket
             */

            try {
                /**
                 * порт 5555, количество одновременных клиентов - 5
                 */
                servers = new ServerSocket(5555, 5);
            } catch (IOException e) {
                /**
                 *  порт 5555 по умолчанию
                 */
                System.out.println("Couldn't listen to port 5555");
                System.exit(-1);
            }

            try {
                /**
                 *  сообщение о готовности
                 */
                System.out.print("Waiting for a client...");
                connect = servers.accept();
                /**
                 *  сooбщение о подключении коиента
                 */
                System.out.println("Client  "+connect.toString()+"  is connected");
                // сообщение о Socket
                System.out.println(connect.toString());
            } catch (IOException e) {
                System.out.println("Can't accept");
                System.exit(-1);
            }
            // создается буфер для входного потока
            in = new BufferedReader(new
                    InputStreamReader(connect.getInputStream()));
            out = new PrintWriter(connect.getOutputStream(), true);
            String input, output;
            // сообщение об ожидании сообщения
            System.out.println("Wait for messages");
            // в бесконечном цикле считуем информацию
            while ((input = in.readLine()) != null) {
                // при введении слова exit, сервер прекращает работу
                if (input.equalsIgnoreCase("exit")) break;
                // выходное сообщение с припиской
                out.println("S ::: " + input);
                // для проверки выводим сообщение в консоль
                System.out.println(input);
            }
            // закрывается выходной поток
            out.close();
            // закрывается входной поток
            in.close();
            // закрывается Socket
            connect.close();
            // закрывается Server
            servers.close();
        }
    }


}
