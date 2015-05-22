package com.aleksandrP.myclientserver.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.aleksandrP.myclientserver.app.Client.MyClient;


public class MainActivity extends Activity {

    private EditText editText;
    private Button btnConnect;
    private Button btnSend;
    private TextView textView;
    private MyClient myClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        editText = (EditText) findViewById(R.id.editText);
        btnConnect = (Button) findViewById(R.id.btnConnect);
        btnSend = (Button) findViewById(R.id.btnSend);
        textView = (TextView) findViewById(R.id.textView);

    }

    public void clickButton(View view) {

        switch (view.getId()) {
            case R.id.btnConnect:
                myClient = new MyClient();
                myClient.clientConnect();
                break;
            case R.id.btnSend:
                myClient = new MyClient();
                myClient.sendDate(editText.getText().toString());
                textView.setText(myClient.getOutputText());
                break;
        }
    }

}
