package com.sildian.go4lunch.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.bumptech.glide.RequestManager;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.sildian.go4lunch.R;
import com.sildian.go4lunch.model.Message;
import com.sildian.go4lunch.model.Workmate;

/************************************************************************************************
 * MessageAdapter
 * Monitors the message data within a recycler view
 ***********************************************************************************************/

public class MessageAdapter extends FirestoreRecyclerAdapter<Message, MessageViewHolder> {

    /**Data**/

    private Workmate currentUser;                           //The current user
    private RequestManager glide;                           //Glide manager to display the images

    /**Constructor**/

    public MessageAdapter(@NonNull FirestoreRecyclerOptions<Message> options, Workmate currentUser, RequestManager glide) {
        super(options);
        this.currentUser=currentUser;
        this.glide = glide;
    }

    /**Adapter methods**/

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.list_message_item, parent, false);
        return new MessageViewHolder(view, this.currentUser);
    }

    @Override
    protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull Message message) {
        holder.update(message, this.glide);
    }
}
