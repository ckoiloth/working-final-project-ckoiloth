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

    /**
     * Creates an adapter to handle a recycler view of messages in a specific chat.
     * @param messages The list of messages.
     */
    public ChatMessageAdapter(ArrayList<ChatMessage> messages){
        messageList = messages;
    }

    /**
     * Creates a viewholder.
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ChatMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_message_bubble, parent, false);

        return new ChatMessageViewHolder(view);
    }

    /**
     * Binds a viewholder and then will get the position.
     * @param viewHolder The viewholder to bind.
     * @param position The message to bind.
     */
    @Override
    public void onBindViewHolder(ChatMessageViewHolder viewHolder, int position){
        final ChatMessage CHAT = messageList.get(position);

        viewHolder.author.setText(CHAT.userSent);
        viewHolder.message.setText(CHAT.message);
    }

    /**
     *
     * @return The number of messges.
     */
    @Override
    public int getItemCount(){return messageList.size();}


    public class ChatMessageViewHolder extends RecyclerView.ViewHolder{
        public View item;
        public TextView author;
        public EditText message;

        /**
         *  The messages view holder.
         * @param itemView The item to hold.
         */
        public ChatMessageViewHolder(View itemView){
            super(itemView);
            item = itemView;
            author = (TextView)item.findViewById(R.id.author);
            message = (EditText)item.findViewById(R.id.messageContent);
            message.setFocusable(false);
        }

    }
}
