package com.sildian.go4lunch.controller.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.firestore.Query;
import com.sildian.go4lunch.R;
import com.sildian.go4lunch.controller.activities.MainActivity;
import com.sildian.go4lunch.model.Message;
import com.sildian.go4lunch.model.Restaurant;
import com.sildian.go4lunch.model.Workmate;
import com.sildian.go4lunch.utils.DateUtilities;
import com.sildian.go4lunch.utils.firebase.FirebaseQueriesLunch;
import com.sildian.go4lunch.utils.firebase.FirebaseQueriesMessage;
import com.sildian.go4lunch.view.MessageAdapter;
import com.sildian.go4lunch.view.WorkmateAdapter;
import com.sildian.go4lunch.view.WorkmateViewHolder;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**************************************************************************************************
 * ChatFragment
 * Allow the workmates to chat together
 *************************************************************************************************/

public class ChatFragment extends BaseFragment implements MessageAdapter.OnDataChangedListener{

    /*********************************************************************************************
     * UI components
     ********************************************************************************************/

    @BindView(R.id.fragment_chat_messages) RecyclerView messagesView;
    @BindView(R.id.fragment_chat_message_text) TextView messageText;
    @BindView(R.id.fragment_chat_message_button) Button messageButton;
    private MessageAdapter messageAdapter;

    /*********************************************************************************************
     * Constructors
     ********************************************************************************************/

    public ChatFragment(){
        super();
    }

    public ChatFragment(PlacesClient placesClient, LatLng userLocation, Workmate currentUser, List<Restaurant> restaurants) {
        super(placesClient, userLocation, currentUser, restaurants);
    }

    /*********************************************************************************************
     * Callbacks
     ********************************************************************************************/

    @Override
    public void onDataChanged() {
        this.messagesView.smoothScrollToPosition(0);
    }

    /*********************************************************************************************
     * BaseFragment methods
     ********************************************************************************************/

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_chat;
    }

    @Override
    protected void initializeView(Bundle SavedInstanceState) {
        initializeMessagesView();
        initializeMessageButton();
    }

    @Override
    public void onUserLocationReceived(LatLng userLocation) {

    }

    @Override
    public void onRestaurantsReceived(List<Restaurant> restaurants) {

    }

    /*********************************************************************************************
     * Initializations
     ********************************************************************************************/

    private void initializeMessagesView(){
        this.messageAdapter=new MessageAdapter(
                generateOptionsForAdapter(FirebaseQueriesMessage.getLast50Messages()),
                this.currentUser, Glide.with(this), this);
        this.messagesView.setAdapter(this.messageAdapter);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        this.messagesView.setLayoutManager(layoutManager);
    }

    private void initializeMessageButton(){
        this.messageButton.setOnClickListener(v -> {
            if(this.messageText.getText()!=null&&!this.messageText.getText().equals("")) {
                MainActivity activity = (MainActivity) getActivity();
                Date date = Calendar.getInstance().getTime();
                String text=this.messageText.getText().toString();
                Message message=new Message(date, this.currentUser, text);
                activity.createMessageInFirebase(message);
                this.messageText.setText("");
            }
        });
    }

    /*********************************************************************************************
     * Firebase management
     ********************************************************************************************/

    private FirestoreRecyclerOptions<Message> generateOptionsForAdapter(Query query){
        return new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .setLifecycleOwner(this)
                .build();
    }
}
