package com.aleksandrP.myclientserver.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.*;
import java.net.Socket;

/**
 *
 */
public class MainActivity extends Activity {

    private final int PORT = 5555; // порт по умолчанию
    private static final String TAG = "myLog";

    private EditText editText;
    private Button btnConnect;
    private Button btnSend;
    private TextView textView;
    private TextView textInfo;
    private EditText textId;
    private EditText textPort;

    private Socket connect;

    private String textIP;
    private String outText;
    private String text;
    private int port;

    private  PrintWriter printWriter;

    /**
     *
     *  создание главного Activiy
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    //* оределение элементов
        editText = (EditText) findViewById(R.id.editText);
        btnConnect = (Button) findViewById(R.id.btnConnect);
        btnSend = (Button) findViewById(R.id.btnSend);
        textView = (TextView) findViewById(R.id.textView);
        textInfo = (TextView) findViewById(R.id.textInfo);
        textId = (EditText) findViewById(R.id.textId);
        textPort = (EditText) findViewById(R.id.textPort);
    }

    /**
     *
     *  слушатель на нажатие кнопок
     */
    public void clickButton(View view) {
        switch (view.getId()) {
            case R.id.btnConnect:   // при нажатии кнопки Connect
                if (textId.getText().length() > 0 && textPort.getText().length()>0 ) {      // проверка ввода данных
                    textIP = textId.getText().toString();
                    port = Integer.parseInt(textPort.getText().toString());
                }else {
                    textIP = "192.168.0.66"; // IP  по умолчанию
                    port = PORT;
                }
                new CreateConnect().execute(textIP, port);      // создаем новое соединение
                break;
            case R.id.btnSend:  // при нажатии на кнопку Send
                if (connect.isConnected()) {        // проверяется условеие соединения
                    new SendDate().execute(editText.getText().toString());      // считуется текст и передается на сервер
                }else new CreateConnect().execute(textIP, port);
                break;
        }
    }

    /**
     * внутренний класс для создания асинхронного потока
     */
    private class CreateConnect extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(TAG, "START create socket IP = " + textIP + "\n Port = " + port);
        }

        @Override
        protected String doInBackground(Object[] params) {      // основно метод созданя Socket
            try {
                    connect = new Socket(textIP, port); // получение Socket с IP и портом
                    Log.i(TAG, "Good create socket");
                } catch (IOException e) {
                    e.printStackTrace();
                    return text ="Error in creare Connect "+ e.toString();
                }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {        // после выполнения основного метода, запускается этот метод
            super.onPostExecute(o);
            if (connect != null) {
                textInfo.setText("Port is: "+ port+ "\nIP adress: "+ textIP +" \n"+connect.toString()); // вывод инфо о порте и  порт
            } else {
                Toast.makeText(getApplicationContext(), "Socket not valid", Toast.LENGTH_SHORT).show();
                textInfo.setText("Port is: "+ port+ "\nIP adress: "+ textIP);
            }
        }
    }

    /**
     * внутренний класс  для передачи данных
     */
    private class SendDate extends AsyncTask<String, String, String>{
        /**
         * метод установки текста на выходную еременную
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            outText = editText.getText().toString();
        }

        /**
         * метод выполнения основных действий
         */
        @Override
        protected String doInBackground(String[] params) {
                try {
                    getDatafromServer();
                    printWriter = new PrintWriter(new OutputStreamWriter(connect.getOutputStream()), true);
                    printWriter.println(outText);
//                    if (printWriter != null) {
//                        printWriter.close();
//                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return text = "Error in send Date "+e.toString();
                }
            return null;
        }


        /**
         * метод выполнения после основного
         */
        @Override
        protected void onPostExecute(String o) {
            super.onPostExecute(o);
            Toast.makeText(getApplicationContext(), "All messages send to client", Toast.LENGTH_SHORT).show();
            textView.setText("\n" + text);
            editText.setText("");
        }

        void getDatafromServer() {          // получение данных с сервера
            new Thread(new Runnable() {     // запусается новый поток
                @Override
                public void run() {
                    boolean w = true;
                    while (w) {             // в цикле, пока true
                        try {
                            new CreateConnect().execute();      // создается новое соединнение
                            InputStream inputStream = connect.getInputStream(); // создается входящий поток
                            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream)); // создается новый буффер
                            text = br.readLine();       // чтение из буффера
                            if (inputStream != null)        // проврка входящего потока на существование
                                inputStream.close();        // закрытие на входящий поток
                            if (br != null)             // проврка буффера на существование
                                br.close();             // закрытие буффера
                            w = false;
                        } catch (IOException e) {
                            e.printStackTrace();
                            text = "Error in the Run method "+ e.toString();
                        }
                    }
                }
            }).start();     // запуск потока
        }
    }
}
