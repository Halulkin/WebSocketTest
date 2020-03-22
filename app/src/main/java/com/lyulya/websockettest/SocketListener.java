package com.lyulya.websockettest;

import android.graphics.Color;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class SocketListener extends WebSocketListener {

    private MainActivity activity;
    private MessageAdapter adapter;

    SocketListener(MainActivity activity, MessageAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.tvStatus.setText("Connection Established!");
                activity.tvStatus.setTextColor(Color.GREEN);
            }
        });
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull final String text) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("message", text);
                    jsonObject.put("byServer", true);
                    adapter.addItem(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
        super.onMessage(webSocket, bytes);
    }

    @Override
    public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        super.onClosing(webSocket, code, reason);
        activity.tvStatus.setText("Connection clothing!");
        activity.tvStatus.setTextColor(Color.GREEN);

    }

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        super.onClosed(webSocket, code, reason);
        activity.tvStatus.setText("Connection Closed");
        activity.tvStatus.setTextColor(Color.GREEN);
    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull final Throwable t, @Nullable final Response response) {
        super.onFailure(webSocket, t, response);
        activity.tvStatus.setText("Connection Failure!" + "Code is " + String.valueOf(t) + " Reason is " + response );
        activity.tvStatus.setTextColor(Color.GREEN);
    }
}