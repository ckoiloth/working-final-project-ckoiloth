package com.example.chiru.authentication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chiru on 12/9/2017.
 */

public class ChatActivityViewAdapter extends RecyclerView.Adapter<ChatActivityViewAdapter.ChatViewHolder> {
    private List<Chat> restaurantList;
    public static final String MESSAGE_KEY = "Message_Key";
    private User user;
    public static final String USER_NAME = "Username";

    public ChatActivityViewAdapter(ArrayList<Chat> listOfChats, User user){
        restaurantList = listOfChats;
        this.user = user;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View chatItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_layout,parent, false);

        return new ChatViewHolder(chatItem);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position){
        final Chat CHAT = restaurantList.get(position);
        holder.chatName.setText(CHAT.getName());
        final Context context = holder.item.getContext();

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rerouteToMessages(context, CHAT);
            }
        });
    }

    public int getItemCount(){
        return restaurantList.size();
    }


    private void rerouteToMessages(Context chatContext, Chat clickedOn){
        Intent rerouteToMessages = new Intent(chatContext, ChatActivity.class);
        rerouteToMessages.putExtra(MESSAGE_KEY, clickedOn.getMessageKey());
        rerouteToMessages.putExtra(USER_NAME, user.name);
        chatContext.startActivity(rerouteToMessages);
    }



    public class ChatViewHolder extends RecyclerView.ViewHolder{
        public View item;
        public TextView chatName;

        public ChatViewHolder(View itemView){
            super(itemView);
            this.item = itemView;
            chatName = (TextView) item.findViewById(R.id.chatName);
        }

    }
}
