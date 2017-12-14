package com.example.chiru.authentication;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chiru on 11/28/2017.
 */

public class User implements Parcelable {
    public String name;
    public String key;
    public  List<Chat> chats;


    public User(){

    }

    /**
     *
     * @return The list of the users chats.
     */
    public List<Chat> getChats(){
        if(chats == null) {
            chats = new ArrayList<>();
        }
        return chats;
    }


    public User(String name, String key, ArrayList<Chat> chats){
        this.name = name;
        this.key = key;
        this.chats = chats;
    }

    /**
     * Adds a new chat.
     * @param chatToAdd The chat to add.
     */
    public void addNewChat(Chat chatToAdd){
        if(chats == null){
            chats = new ArrayList<>();
        }
        chats.add(chatToAdd);
        pushNewChatToDb(chatToAdd);
    }

    /**
     * Adds the new chat to the database.
     * @param newChat The new chat to add.
     */
    private void pushNewChatToDb(Chat newChat){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference =
                database.getReference("/Users/" + key+"/chats/"+Integer.toString(chats.size() - 1));

        reference.setValue(newChat);
    }

    /**
     * Checks if two users are the same by using their database key.
     * @param user
     * @return
     */
    @Override
    public boolean equals(Object user){
        if(user instanceof User) {
            User userToCheck = (User) user;
            return this.key.equals(userToCheck.key);
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.key);
        dest.writeTypedList(this.chats);
    }

    protected User(Parcel in) {
        this.name = in.readString();
        this.key = in.readString();
        this.chats = in.createTypedArrayList(Chat.CREATOR);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}