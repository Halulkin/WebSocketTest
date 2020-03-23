package com.lyulya.websockettest;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kusu.loadingbutton.LoadingButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okio.ByteString;

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

    public JSONObject jsonObject;
    public int avr, excOld = 0, excNew = 0, counter = 0;
    public File path;

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
        Request request = new Request.Builder().url("ws://192.168.2.181:8080").build();
        // Dormitory ip
//        Request request = new Request.Builder().url("ws://146.102.205.133:8080").build();

        SocketListener socketListener = new SocketListener(this, adapter);
        webSocket = client.newWebSocket(request, socketListener);
    }

    public void btnBombOnClick(View view) {
        counter++;

//        myTask = new MyTask(this);
//        myTask.execute();
//        makeBomb();


        sendFile();
    }

    private void makeBomb() {
//        btnBomb.showLoading();
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            int rand = ((int) (Math.random() * 65000)) + 1;
            list.add(rand);
        }

//        messageBox.setText("");

        startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            message = String.valueOf(list.get(i));
            webSocket.send(message);
            jsonObject = new JSONObject();

            try {
                jsonObject.put("message", message);
                jsonObject.put("byServer", false);

                adapter.addItem(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        endTime = System.currentTimeMillis();
//        btnBomb.hideLoading();
        excNew = (int) (endTime - startTime);

        avr = (excOld + excNew) / counter;
        excOld = excOld + excNew;
        tvStatus.setText("Last execution time: " + (excNew) + "\n" + " Average - " + avr);
        btnBomb.setText(String.valueOf(counter));

    }

    public void sendFile() {

        generateNoteOnSD();

        File file = new File(path + "/sample.txt");

//        File file = new File(Environment.getExternalStorageDirectory(), "Notes");

        //init array with file length
        byte[] bytesArray = new byte[(int) file.length()];

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fis.read(bytesArray); //read file into bytes[]
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteString byteString = new ByteString(bytesArray);

        webSocket.send(byteString);
    }

    public void generateNoteOnSD() {

        String FILENAME = "sample";
        String sample = "This is bits of my heart";

        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            int rand = ((int) (Math.random() * 65000)) + 1;
            list.add(rand);
        }

        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                root.mkdir();
            }
            File filepath = new File(root, FILENAME + ".txt");
            FileWriter writer = new FileWriter(filepath);
            writer.append(sample);
            writer.flush();
            writer.close();
            String m = "File generated with name " + FILENAME + ".txt";

            Toast.makeText(MainActivity.this, m, Toast.LENGTH_LONG).show();
            path = root;

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void btnSendMessage(View view) {
        message = messageBox.getText().toString();
        if (!message.isEmpty()) {
            webSocket.send(message);
            messageBox.setText("");

            jsonObject = new JSONObject();

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


