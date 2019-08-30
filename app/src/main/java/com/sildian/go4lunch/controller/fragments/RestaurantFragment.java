package com.sildian.go4lunch.controller.fragments;


import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

import com.sildian.go4lunch.R;
import com.sildian.go4lunch.controller.activities.RestaurantActivity;
import com.sildian.go4lunch.model.Restaurant;
import com.sildian.go4lunch.model.Workmate;
import com.sildian.go4lunch.utils.DateUtilities;
import com.sildian.go4lunch.utils.api.APIStreams;
import com.sildian.go4lunch.utils.firebase.FirebaseQueriesLunch;
import com.sildian.go4lunch.utils.listeners.OnFirebaseQueryResultListener;
import com.sildian.go4lunch.utils.listeners.OnPlaceQueryResultListener;
import com.sildian.go4lunch.view.WorkmateAdapter;
import com.sildian.go4lunch.view.WorkmateViewHolder;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;
import io.reactivex.disposables.Disposable;

/**************************************************************************************************
 * RestaurantFragment
 * Shows a restaurant's information
 *************************************************************************************************/

public class RestaurantFragment extends Fragment implements OnFirebaseQueryResultListener, OnPlaceQueryResultListener {

    /*********************************************************************************************
     * Members
     ********************************************************************************************/

    /**Data**/

    private PlacesClient placesClient;                  //The placesClient allowing to use Google Places API
    private Disposable disposable;                      //The disposable which gets the response from the API
    @State Workmate currentUser;                        //The current user
    @State Restaurant restaurant;                       //The restaurant

    /**UI Components**/

    @BindView(R.id.fragment_restaurant_image) ImageView imageView;
    @BindView(R.id.fragment_restaurant_name) TextView nameText;
    @BindView(R.id.fragment_restaurant_stars) RatingBar starsRatingBar;
    @BindView(R.id.fragment_restaurant_nb_likes) TextView nbLikesText;
    @BindView(R.id.fragment_restaurant_cuisineType) TextView cuisineTypeText;
    @BindView(R.id.fragment_restaurant_address) TextView addressText;
    @BindView(R.id.fragment_restaurant_button_call) Button callButton;
    @BindView(R.id.fragment_restaurant_button_like) Button likeButton;
    @BindView(R.id.fragment_restaurant_button_website) Button websiteButton;
    @BindView(R.id.fragment_restaurant_button_lunch) FloatingActionButton lunchButton;
    @BindView(R.id.fragment_restaurant_workmates) RecyclerView workmatesRecyclerView;
    private WorkmateAdapter workmateAdapter;

    /*********************************************************************************************
     * Constructors
     ********************************************************************************************/

    public RestaurantFragment(){

    }

    public RestaurantFragment(PlacesClient placesClient, Workmate currentUser, Restaurant restaurant) {
        this.placesClient=placesClient;
        this.currentUser=currentUser;
        this.restaurant=restaurant;
    }

    /*********************************************************************************************
     * Callbacks
     ********************************************************************************************/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_restaurant, container, false);
        ButterKnife.bind(this, view);
        Icepick.restoreInstanceState(this, savedInstanceState);
        if(this.placesClient==null){
            initializePlaces();
        }
        initializeView();
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onGetWorkmateResult(Workmate workmate) {

    }

    @Override
    public void onGetRestaurantResult(Restaurant restaurant) {
        if(restaurant!=null&&restaurant.getNbLikes()>0){
            this.restaurant.setNbLikes(restaurant.getNbLikes());
            updateNbLikes();
        }
    }

    @Override
    public void onGetWorkmatesEatingAtRestaurantResult(Restaurant restaurant, List<Workmate> workmates) {

    }

    @Override
    public void onGetGooglePlacesSearchResult(List<Restaurant> restaurants) {

    }

    @Override
    public void onGetRestaurantAllDetailsResult(Restaurant restaurant) {
        updateRestaurantDetails(restaurant);
    }

    /*********************************************************************************************
     * Initializations
     ********************************************************************************************/

    private void initializePlaces(){
        Places.initialize(getContext(), getString(R.string.google_maps_key));
        this.placesClient = Places.createClient(getContext());
    }

    private void initializeView(){
        RestaurantActivity activity=(RestaurantActivity) getActivity();
        APIStreams.streamGetRestaurantImage(this.placesClient, restaurant, this.imageView);
        this.nameText.setText(this.restaurant.getName());
        this.starsRatingBar.setRating(restaurant.getNbStars());
        activity.getRestaurantFromFirebase(this.restaurant.getPlaceId(), this);
        this.cuisineTypeText.setText("");
        this.addressText.setText(this.restaurant.getAddress());
        initializeCallButton();
        initializeLikeButton();
        initializeWebsiteButton();
        initializeLunchButton();
        initializeWorkmatesView();
        activity.runRestaurantAllDetailsQuery(this.restaurant, this);
    }

    private void initializeCallButton(){
        this.callButton.setOnClickListener(v -> {
            if(v.getTag()!=null){
                Intent callIntent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+v.getTag().toString()));
                startActivity(callIntent);
            }
        });
    }

    private void initializeLikeButton(){

        if(this.currentUser.getLikes().contains(this.restaurant)){
            disableButton(this.likeButton);
        }

        this.likeButton.setOnClickListener(v -> {
            if(this.currentUser.addLike(this.restaurant)) {
                RestaurantActivity activity = (RestaurantActivity) getActivity();
                activity.createOrUpdateRestaurantInFirebase(this.restaurant);
                activity.updateWorkmateLikesInFirebase(this.currentUser);
                activity.updateCurrentUser(this.currentUser);
                disableButton(this.likeButton);
                updateNbLikes();
            }
        });
    }

    private void initializeWebsiteButton(){
        this.websiteButton.setOnClickListener(v -> {
            if(v.getTag()!=null){
                Intent webIntent=new Intent(Intent.ACTION_VIEW, Uri.parse(v.getTag().toString()));
                startActivity(webIntent);
            }
        });
    }

    private void initializeLunchButton(){

        Date date= DateUtilities.Companion.getDate();
        Workmate.Lunch lunch= new Workmate.Lunch(date, this.restaurant);

        if(this.currentUser.getLunches().contains(lunch)){
            disableLunchButton();
        }

        this.lunchButton.setOnClickListener(v -> {

            RestaurantActivity activity = (RestaurantActivity) getActivity();
            activity.deleteLunchInFirebase(this.currentUser);

            if(this.currentUser.updateLunch(this.restaurant)) {
                activity.createOrUpdateRestaurantInFirebase(this.restaurant);
                activity.updateWorkmateLunchesInFirebase(this.currentUser);
                activity.updateCurrentUser(this.currentUser);
                String text=getString(R.string.toast_restaurant_lunch_confirmation)+" "+this.restaurant.getName()+".";
                Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
                disableLunchButton();
            }

            activity.createLunchInFirebase(this.restaurant, this.currentUser);
        });
    }

    private void initializeWorkmatesView(){
        this.workmateAdapter=new WorkmateAdapter(
                generateOptionsForAdapter(FirebaseQueriesLunch.getWorkmatesEatingAtRestaurant(this.restaurant)),
                WorkmateViewHolder.ID_RESTAURANT, Glide.with(this));
        this.workmatesRecyclerView.setAdapter(this.workmateAdapter);
        this.workmatesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    /*********************************************************************************************
     * UI Update methods
     ********************************************************************************************/

    private void updateRestaurantDetails(Restaurant restaurant){
        this.cuisineTypeText.setText(restaurant.getCuisineType());
        this.callButton.setTag(restaurant.getPhoneNumber());
        if(callButton.getTag()==null){
            disableButton(this.callButton);
        }
        this.websiteButton.setTag(restaurant.getWebUrl());
        if(this.websiteButton.getTag()==null){
            disableButton(this.websiteButton);
        }
    }

    private void updateNbLikes(){
        if(this.nbLikesText.getVisibility()!=View.VISIBLE) {
            this.nbLikesText.setVisibility(View.VISIBLE);
        }
        this.nbLikesText.setText(String.valueOf(this.restaurant.getNbLikes()));
    }

    private void disableButton(Button button){
        button.setEnabled(false);
        button.setTextColor(getResources().getColor(android.R.color.darker_gray));
        button.getCompoundDrawables()[1].mutate().setColorFilter
                (new PorterDuffColorFilter(getResources().getColor(android.R.color.darker_gray), PorterDuff.Mode.SRC_IN));
    }

    private void disableLunchButton(){
        this.lunchButton.setEnabled(false);
        this.lunchButton.getDrawable().mutate().setColorFilter
                (new PorterDuffColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN));
    }

    /*********************************************************************************************
     * Firebase management
     ********************************************************************************************/

    private FirestoreRecyclerOptions<Workmate> generateOptionsForAdapter(Query query){
        return new FirestoreRecyclerOptions.Builder<Workmate>()
                .setQuery(query, Workmate.class)
                .setLifecycleOwner(this)
                .build();
    }
}
