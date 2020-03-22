package com.lyulya.websockettest;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

public class MainActivity extends AppCompatActivity {

    private WebSocket webSocket;
    public MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView messageList = findViewById(R.id.messageList);
        final EditText messageBox = findViewById(R.id.messageBox);
        TextView send = findViewById(R.id.send);

        instantiateWebSocket();

        adapter = new MessageAdapter(getApplicationContext());
        messageList.setAdapter(adapter);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = messageBox.getText().toString();
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
        });
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
}
