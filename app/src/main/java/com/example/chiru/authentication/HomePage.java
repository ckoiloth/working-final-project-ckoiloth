package com.example.chiru.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class HomePage extends AppCompatActivity {
    private Button addChat;
    private User user;
    private FirebaseDatabase database;
    public static final String USER = "USER";
    private ArrayList<Chat> listOfChats;
    private RecyclerView mRecyclerView;

    /**
     * This will add a value event listener to the changed users.
     */
    @Override
    public void onStart() {
        super.onStart();

        database.getReference("/Users/"+user.key).addValueEventListener(new UserValueEventListener());
    }

    /**
     * This will  hte homepage and then list out the chats available to the specific user.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        database = FirebaseDatabase.getInstance();
        createAddChatButton();
        user = getIntent().getParcelableExtra(MainActivity.USER);

    }

    /**
     * Button to add a new chat.
     */
    private void createAddChatButton() {
        addChat = (Button) findViewById(R.id.addNewChat);

        addChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toCreateNewChat = new Intent(HomePage.this, CreatingNewChat.class);
                toCreateNewChat.putExtra(USER, user);
                startActivity(toCreateNewChat);
            }
        });
    }

    /**
     * This will first check for all chats first available.
     */
    private class UserValueEventListener implements ValueEventListener{

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            user = dataSnapshot.getValue(User.class);
            listOfChats = (ArrayList<Chat>) user.getChats();


            ChatActivityViewAdapter chatActivityViewAdapter = new ChatActivityViewAdapter(listOfChats, user);
            mRecyclerView = (RecyclerView) findViewById(R.id.chatList);
            mRecyclerView.setAdapter(chatActivityViewAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(HomePage.this, LinearLayoutManager.VERTICAL, false));
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

}
