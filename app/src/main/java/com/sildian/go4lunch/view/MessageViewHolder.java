package com.sildian.go4lunch.view;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.sildian.go4lunch.R;
import com.sildian.go4lunch.model.Message;
import com.sildian.go4lunch.model.Workmate;

import butterknife.BindView;
import butterknife.ButterKnife;

/************************************************************************************************
 * MessageViewHolder
 * Displays the items related to a message in a RecyclerView
 ***********************************************************************************************/

public class MessageViewHolder extends RecyclerView.ViewHolder {

    /**UI components**/

    private View itemView;
    @BindView(R.id.list_message_item) LinearLayout messageLayout;
    @BindView(R.id.list_message_item_text) TextView messageText;
    @BindView(R.id.list_message_item_sender_image) ImageView senderImage;
    @BindView(R.id.list_message_item_sender_name) TextView senderName;
    @BindView(R.id.list_message_item_date) TextView messageDate;

    /**Data**/

    private Workmate currentUser;               //The current user

    /**Constructor**/

    public MessageViewHolder(View itemView, Workmate currentUser){
        super(itemView);
        this.itemView=itemView;
        ButterKnife.bind(this, itemView);
        this.currentUser=currentUser;
    }

    /**Updates with a message
     * @param message : the message
     * @param glide : glide manager to display the image
     */

    public void update(Message message, RequestManager glide){

        /*If the message's send is the current user, then applies a specific background and gravity to the message layout*/

        if(message.getWorkmate().getFirebaseId().equals(this.currentUser.getFirebaseId())){
            this.messageText.setBackground(this.itemView.getResources().getDrawable(R.drawable.background_message_user));
            this.messageLayout.setGravity(Gravity.END);
        }

        /*Updates the other components*/

        this.messageText.setText(message.getText());
        if(message.getWorkmate().getImageUrl()!=null){
            glide.load(message.getWorkmate().getImageUrl()).apply(RequestOptions.circleCropTransform()).into(this.senderImage);
        }
        this.senderName.setText(message.getWorkmate().getName());
        this.messageDate.setText(message.getDate().toString());
    }
}
