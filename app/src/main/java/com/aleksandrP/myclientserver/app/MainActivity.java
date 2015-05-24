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


public class MainActivity extends Activity {

    private final int PORT = 5555;
    private static final String TAG = "myLog";

    private EditText editText;
    private Button btnConnect;
    private Button btnSend;
    private TextView textView;
    private TextView textInfo;
    private EditText textId;
    private EditText textPort;

    private Socket connect=null;

    private String textIP;
    private String outText;
    private String text;
    private int port;

    private  PrintWriter printWriter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        editText = (EditText) findViewById(R.id.editText);
        btnConnect = (Button) findViewById(R.id.btnConnect);
        btnSend = (Button) findViewById(R.id.btnSend);
        textView = (TextView) findViewById(R.id.textView);
        textInfo = (TextView) findViewById(R.id.textInfo);
        textId = (EditText) findViewById(R.id.textId);
        textPort = (EditText) findViewById(R.id.textPort);
    }

    public void clickButton(View view) {
        switch (view.getId()) {
            case R.id.btnConnect:
//                Toast.makeText(this, "Select button Connect", Toast.LENGTH_SHORT).show();
                if (textId.getText().length() > 0 && textPort.getText().length()>0 ) {
                    textIP = textId.getText().toString();
                    port = Integer.parseInt(textPort.getText().toString());
//                    Toast.makeText(this, "Connect to " + textIP+" via "+port+" port", Toast.LENGTH_SHORT).show();
                }else {
                    textIP = "192.168.0.66";
                    port = PORT;
//                    Toast.makeText(this, "IP adres : " +textIP+ "\nPort : "+port, Toast.LENGTH_SHORT).show();
                }
                new CreateConnect().execute(textIP, port);
                break;
            case R.id.btnSend:
//                Toast.makeText(this, "Select button Send", Toast.LENGTH_SHORT).show();
//                Toast.makeText(this, "Send messages ...", Toast.LENGTH_SHORT).show();
//                outText = editText.getText().toString();
                new SendDate().execute(editText.getText().toString());
//                Toast.makeText(this, "All messages send to client", Toast.LENGTH_SHORT).show();
//                textView.setText("\n" + text);
//                editText.setText("");
                break;
        }
    }

    private class CreateConnect extends AsyncTask {
        @Override
        protected String doInBackground(Object[] params) {
                try {
                    Log.i(TAG, "START create socket IP = " + textIP + "\n Port = " + port);
                    connect = new Socket(textIP, port);
                    Log.i(TAG, "Good create socket");
                } catch (IOException e) {
                    e.printStackTrace();
                    return text ="Error in creare Connect "+ e.toString();
                }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (connect != null) {
                textInfo.setText("Port is: "+ port+ "\nIP adress: "+ textIP +" \n"+connect.toString());
            } else {
                Toast.makeText(getApplicationContext(), "Socket not valid", Toast.LENGTH_SHORT).show();
                textInfo.setText("Port is: "+ port+ "\nIP adress: "+ textIP);
            }
        }
    }

    private class SendDate extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            outText = editText.getText().toString();
        }

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

        @Override
        protected void onPostExecute(String o) {
            super.onPostExecute(o);
            Toast.makeText(getApplicationContext(), "All messages send to client", Toast.LENGTH_SHORT).show();
            textView.setText("\n" + text);
            editText.setText("");
        }

        void getDatafromServer() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean w = true;
                    while (w) {
                        try {
                            new CreateConnect().execute();
                            InputStream inputStream = connect.getInputStream();
                            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                            text = br.readLine();
                            if (inputStream != null)
                                inputStream.close();
                            if (br != null)
                                br.close();
                            w = false;
                        } catch (IOException e) {
                            e.printStackTrace();
                            text = "Error in the Run method "+ e.toString();
                        }
                    }
                }
            }).start();
        }
    }
}
