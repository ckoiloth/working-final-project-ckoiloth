package com.example.chiru.authentication;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Chiru on 12/9/2017.
 */

public class Chat implements Parcelable {
    public String messageKey;
    public String name;
    public String chatKey;

    public Chat(){

    }

    /**
     * This will create a new chat, create a message key, and push both of these to the
     * firebase database.
     * @param name The name of the chat to add.
     */

    public Chat(String name){
        this.name = name;
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference messages = database.getReference("/Messages");
        messageKey = messages.push().getKey();
    }

    /**
     * Sets The name of the chat.
     * @param name
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Gets the name of the chat.
     * @return The name of the chat.
     */
    public String getName(){
        return name;
    }

    /**
     *
     * @return The key of this chats messages.
     */
    public String getMessageKey(){
        return messageKey;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.messageKey);
        dest.writeString(this.name);
        dest.writeString(this.chatKey);
    }

    protected Chat(Parcel in) {
        this.messageKey = in.readString();
        this.name = in.readString();
        this.chatKey = in.readString();
    }

    public static final Creator<Chat> CREATOR = new Creator<Chat>() {
        @Override
        public Chat createFromParcel(Parcel source) {
            return new Chat(source);
        }

        @Override
        public Chat[] newArray(int size) {
            return new Chat[size];
        }
    };
}
