package com.example.chiru.authentication;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Chiru on 12/11/2017.
 */


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private ArrayList<User> userList;
    private ArrayList<User> selectedUsers;

    /**
     * This will create a user adapter that loads the users.
     * @param userList The list of user.
     * @param selectedUsers  The users that are have checks next to them .
     */
    public UserAdapter(ArrayList<User> userList, ArrayList<User> selectedUsers){
        this.userList = userList;
        this.selectedUsers = selectedUsers;
    }

    /**
     * Creates the view holder to hold the user.
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View userItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.check_box_user_layout, parent, false);

        return new ViewHolder(userItem);
    }

    /**
     * Binds the viewholder and then binds a view to it.
     * @param viewHolder The view holder to bind the view to.
     * @param position The current position.
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position){
        final User currentUser = userList.get(position);
        viewHolder.userName.setText(currentUser.name);
        viewHolder.addUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(viewHolder.addUser.isChecked()){
                    selectedUsers.add(currentUser);
                }else{
                    selectedUsers.remove(currentUser);
                }
            }
        });
    }

    /**
     * The amount of users needed to load.
     * @return
     */
    @Override
    public int getItemCount() {
        return userList.size();
    }

    /**
     * The viewholder for each user.
     */
    public class ViewHolder extends RecyclerView.ViewHolder{
        public View item;
        public TextView userName;
        public CheckBox addUser;

        public ViewHolder(View itemView){
            super(itemView);
            item = itemView;
            userName = (TextView) itemView.findViewById(R.id.userName);
            addUser = (CheckBox) item.findViewById(R.id.addUserButton);
        }
    }

}
