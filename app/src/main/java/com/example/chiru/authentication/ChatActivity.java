package com.example.chiru.authentication;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    private String messageKey;
    private DatabaseReference mRef;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private ArrayList<ChatMessage> chatMessages = new ArrayList<>();
    private RecyclerView mRecycleView;
    private ChatMessageAdapter adapter;
    private String userName;
    private ImageView sendMessageButton;
    private EditText messageToSend;

    /**
     * If there are more than a certain messages, then the messages will destruct. And the
     * adapter resets.
     */
    @Override
    public void onPause(){
        super.onPause();
        if(chatMessages.size() > 10){
            destructMessages();
        }
    }

    /**
     * Will create the chat chat activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        messageKey = getIntent().getStringExtra(ChatActivityViewAdapter.MESSAGE_KEY);
        userName = getIntent().getStringExtra(ChatActivityViewAdapter.USER_NAME);

        mRef = database.getReference("/Messages/" + messageKey);
        mRecycleView = (RecyclerView)findViewById(R.id.chatMessageList);


        mRef.addChildEventListener(new MessageLoaderListener());

        messageToSend = (EditText)findViewById(R.id.messageArea);
        sendMessageButton = (ImageView)findViewById(R.id.sendButton);
        sendMessageButton.setOnClickListener(new SendMessageListener());
    }

    /**
     * Sends the message to the database and the other users.
     * @param message The message to send.
     */
    private void sendMessage(String message){
        ChatMessage messageToSend = new ChatMessage();
        messageToSend.message = message;
        messageToSend.userSent = userName;

        mRef.push().setValue(messageToSend);
    }

    /**
     * Will destroy the messages.
     */
    private void destructMessages(){
        mRef.removeValue();
    }

    /**
     * This is a a class that listens to the messages and adds it to the firebase recycler view.
     */
    private class MessageLoaderListener implements ChildEventListener{

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            chatMessages.add(dataSnapshot.getValue(ChatMessage.class));
            resetAdapter();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            chatMessages = new ArrayList<>();
            resetAdapter();
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

    /**
     * When button to send message is click, sends the message.
     */
    private class SendMessageListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(messageToSend.getText().length() != 0){
                sendMessage(messageToSend.getText().toString());
                messageToSend.setText("");
            }
        }
    }

    /**
     * Resets the adapter to handle the changes.
     */
    private void resetAdapter(){
        adapter = new ChatMessageAdapter(chatMessages);
        mRecycleView.setAdapter(adapter);
        mRecycleView.setLayoutManager
                (new LinearLayoutManager(ChatActivity.this, LinearLayoutManager.VERTICAL, false));
    }
}
