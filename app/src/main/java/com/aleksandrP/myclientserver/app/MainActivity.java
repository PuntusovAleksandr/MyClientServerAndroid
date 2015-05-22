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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class MainActivity extends Activity {

    private EditText editText;
    private Button btnConnect;
    private Button btnSend;
    private TextView textView;
    private TextView textInfo;
    private EditText textId;
    private EditText textPort;

    private Socket connect=null;
    private ObjectInputStream oIn;
    private ObjectOutputStream oOut;


    private String textIP = null;
    private int port = 0;

    private Log myLog;

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
                textIP = textId.getText().toString();
                port = Integer.parseInt(textPort.getText().toString());
                Toast.makeText(this, "Select button Connect", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Connect to " + textIP+" via "+port+" port", Toast.LENGTH_SHORT).show();
                new CreateConnect().execute();
                if (connect != null) {
                    textInfo.setText("Port is: "+ port+ "\nIP adress: "+ textIP +" \n"+connect.toString());
                } else {
                    Toast.makeText(this, "Socket not valid", Toast.LENGTH_SHORT).show();
                    textInfo.setText("Port is: "+ port+ "\nIP adress: "+ textIP);
                }
                break;
            case R.id.btnSend:
                Toast.makeText(this, "Select button Send", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Send messages ", Toast.LENGTH_SHORT).show();
                new SendDate().execute();
                Toast.makeText(this, "All messages send to client", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private class CreateConnect extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            try {
                connect = new Socket("localhost", port);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class SendDate extends AsyncTask{
        @Override
        protected Object doInBackground(Object[] params) {
            try {
                oIn = new ObjectInputStream(connect.getInputStream());
                oOut = new ObjectOutputStream(connect.getOutputStream());
                textView.setText((String) oIn.readObject());

                oOut.flush();
                oOut.writeObject(editText.getText().toString());

                if (oIn != null){
                    oIn.close();
                }
                if (oOut != null){
                    oOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
