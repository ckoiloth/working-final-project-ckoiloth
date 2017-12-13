package com.example.chiru.authentication;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Chiru on 12/12/2017.
 */

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ChatMessageViewHolder> {
    private ArrayList<ChatMessage> messageList;

    public ChatMessageAdapter(ArrayList<ChatMessage> messages){
        messageList = messages;
    }

    @Override
    public ChatMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_message_bubble, parent, false);

        return new ChatMessageViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ChatMessageViewHolder viewHolder, int position){
        final ChatMessage CHAT = messageList.get(position);

        viewHolder.author.setText(CHAT.userSent);
        viewHolder.message.setText(CHAT.message);
    }

    @Override
    public int getItemCount(){return messageList.size();}

    public class ChatMessageViewHolder extends RecyclerView.ViewHolder{
        public View item;
        public TextView author;
        public EditText message;

        public ChatMessageViewHolder(View itemView){
            super(itemView);
            item = itemView;
            author = (TextView)item.findViewById(R.id.author);
            message = (EditText)item.findViewById(R.id.messageContent);
            message.setFocusable(false);
        }

    }
}
