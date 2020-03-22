package com.lyulya.websockettest;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kusu.loadingbutton.LoadingButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

public class MainActivity extends AppCompatActivity {

    public WebSocket webSocket;
    public MessageAdapter adapter;
    public ListView messageList;

    public EditText messageBox;
    public TextView tvStatus;
    public LoadingButton btnBomb;
    public Button btnSendMessage;

    public String message;

    public long endTime;
    public long startTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageList = findViewById(R.id.messageList);
        messageBox = findViewById(R.id.messageBox);
        btnSendMessage = findViewById(R.id.btnSendMessage);
        btnBomb = findViewById(R.id.btnBomb);
        tvStatus = findViewById(R.id.tvStatus);

        adapter = new MessageAdapter(getApplicationContext());
        messageList.setAdapter(adapter);


        instantiateWebSocket();
    }

    private void instantiateWebSocket() {
        OkHttpClient client = new OkHttpClient();
        //replace x.x.x.x with your machine's IP Address
        // Work ip
//        Request request = new Request.Builder().url("ws://192.168.2.181:8080").build();
        // Dormitory ip
        Request request = new Request.Builder().url("ws://146.102.205.133:8080").build();

        SocketListener socketListener = new SocketListener(this, adapter);
        webSocket = client.newWebSocket(request, socketListener);
    }

    public void btnBombOnClick(View view) {

        btnBomb.showLoading();

        Runnable mSomeTask = new Runnable() {
            public void run() {

                List<Integer> list = new ArrayList<>();
                for (int i = 0; i < 4000; i++) {
                    int rand = ((int) (Math.random() * 40)) + 1;
                    list.add(rand);
                }

                startTime = System.currentTimeMillis();

                for (int i = 0; i < 4000; i++) {

                    message = String.valueOf(list.get(i));

                    webSocket.send(message);
                    messageBox.setText("");

                    JSONObject jsonObject = new JSONObject();

                    try {
                        jsonObject.put("message", message);
                        jsonObject.put("byServer", false);

                        adapter.addItem(jsonObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        mSomeTask.run();
        endTime = System.currentTimeMillis();
        btnBomb.hideLoading();
        btnBomb.setText("Total execution time: " + (endTime - startTime));
    }

    public void btnSendMessage(View view) {
        message = messageBox.getText().toString();
        if (!message.isEmpty()) {
            webSocket.send(message);
            messageBox.setText("");

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("message", message);
                jsonObject.put("byServer", false);

                adapter.addItem(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
