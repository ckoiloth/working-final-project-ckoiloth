package com.example.chiru.authentication;

import android.os.Bundle;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreatingNewChat extends AppCompatActivity {
    private User user;
    private EditText chatName;
    private DatabaseReference childReference;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private RecyclerView mRecyclerView;
    private ArrayList<User> listOfUsers = new ArrayList<>();
    private UserAdapter userAdapter;
    private Button createChatButton;

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating_new_chat);

        user = getIntent().getParcelableExtra(HomePage.USER);
        mRecyclerView = (RecyclerView) findViewById(R.id.userList);
        chatName = (EditText) findViewById(R.id.chatName);
        createChatButton = (Button)findViewById(R.id.createChatButton);

        CreateChatListener chatListener = new CreateChatListener();
        createChatButton.setOnClickListener(chatListener);
        final ArrayList<User> addedUsers = new ArrayList<>();
        chatListener.users = addedUsers;

        childReference = database.getReference("/Users");

        childReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot childDataSnapShot: dataSnapshot.getChildren()){
                    User userToAdd = childDataSnapShot.getValue(User.class);
                    if(!userToAdd.equals(user)) {
                        listOfUsers.add(childDataSnapShot.getValue(User.class));
                    }
                }

                userAdapter = new UserAdapter(listOfUsers, addedUsers);
                mRecyclerView.setAdapter(userAdapter);
                mRecyclerView.setLayoutManager
                        (new LinearLayoutManager(CreatingNewChat.this, LinearLayoutManager.VERTICAL, false));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private Chat createNewChat(String name, ArrayList<User> users) {
        Chat newChat = new Chat(name);
        for (User user : users) {
            user.addNewChat(newChat);
        }
        this.user.addNewChat(newChat);
        return newChat;
    }

    private class CreateChatListener implements View.OnClickListener{
        public ArrayList<User> users = new ArrayList<>();

        @Override
        public void onClick(View view) {
            boolean chatReady = true;
            if(chatName.getText().length() == 0) {
                Toast.makeText
                        (CreatingNewChat.this, "Chat needs a name", Toast.LENGTH_LONG).show();
                chatReady = false;
            }

            if(users.size() == 0){
                Toast.makeText
                        (CreatingNewChat.this, "Need more users", Toast.LENGTH_LONG).show();
                chatReady = false;
            }

            if(chatReady){
                createNewChat(chatName.getText().toString(), users);
                finish();
            }
        }
    }

}
