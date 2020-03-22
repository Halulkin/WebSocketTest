package com.lyulya.websockettest;

import android.widget.Toast;

import androidx.annotation.Nullable;

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
    public void onOpen(WebSocket webSocket, Response response) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, "Connection Established!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onMessage(WebSocket webSocket, final String text) {
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
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        super.onMessage(webSocket, bytes);
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        super.onClosing(webSocket, code, reason);
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, final Throwable t, @Nullable final Response response) {
        super.onFailure(webSocket, t, response);
    }
}