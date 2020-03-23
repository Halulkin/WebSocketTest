package com.lyulya.websockettest;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
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
import java.io.FileWriter;
import java.io.IOException;
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

    public JSONObject jsonObject;
    public int avr, excOld = 0, excNew = 0, counter = 0;

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

        generateNoteOnSD();
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

    public void sendFile() throws IOException {

    }

    public void generateNoteOnSD() {

        String FILENAME = "hello_file";
        String sample = "hello my friend!";

        try {
            // this will create a new name everytime and unique
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            // if external memory exists and folder with name Notes
            if (!root.exists()) {
                root.mkdirs(); // this will create folder.
            }
            File filepath = new File(root, FILENAME + ".txt");  // file path to save
            FileWriter writer = new FileWriter(filepath);
            writer.append(sample);
            writer.flush();
            writer.close();
            String m = "File generated with name " + FILENAME + ".txt";

             Toast.makeText(MainActivity.this, m, Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }



//        File file = new File(MainActivity.this.getFilesDir(), "text");
//        if (!file.exists()) {
//            file.mkdir();
//        }
//        try {
//            File gpxfile = new File(file, "sample");
//            FileWriter writer = new FileWriter(gpxfile);
//            writer.append(string);
//            writer.flush();
//            writer.close();
//            Toast.makeText(MainActivity.this, "Saved your text", Toast.LENGTH_LONG).show();
//        } catch (Exception e) { }
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


