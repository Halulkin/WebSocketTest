package com.lyulya.websockettest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;


    public MessageAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    private List<JSONObject> messagesList = new ArrayList<>();

    @Override
    public int getCount() {
        return messagesList.size();
    }

    @Override
    public Object getItem(int i) {
        return messagesList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null)
            view = inflater.inflate(R.layout.message_list_item, viewGroup, false);

        TextView sentMessage = view.findViewById(R.id.sentMessage);
        TextView receivedMessage = view.findViewById(R.id.receivedMessage);

        JSONObject item = messagesList.get(i);

        try {

            if (item.getBoolean("byServer")) {

                receivedMessage.setVisibility(View.VISIBLE);
                receivedMessage.setText(item.getString("message"));

                sentMessage.setVisibility(View.INVISIBLE);

            } else {
                sentMessage.setVisibility(View.VISIBLE);
                sentMessage.setText(item.getString("message"));

                receivedMessage.setVisibility(View.INVISIBLE);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }

    void addItem(JSONObject item) {
        messagesList.add(item);
        notifyDataSetChanged();
    }
}