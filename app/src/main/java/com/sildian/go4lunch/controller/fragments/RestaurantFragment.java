package com.sildian.go4lunch.controller.fragments;


import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;
import com.sildian.go4lunch.R;
import com.sildian.go4lunch.controller.activities.MainActivity;
import com.sildian.go4lunch.controller.activities.RestaurantActivity;
import com.sildian.go4lunch.model.Restaurant;
import com.sildian.go4lunch.model.Workmate;
import com.sildian.go4lunch.utils.api.APIStreams;
import com.sildian.go4lunch.utils.firebase.FirebaseQueriesLunch;
import com.sildian.go4lunch.utils.firebase.FirebaseQueriesWorkmate;
import com.sildian.go4lunch.utils.listeners.OnPlaceQueryResultListener;
import com.sildian.go4lunch.view.WorkmateAdapter;
import com.sildian.go4lunch.view.WorkmateViewHolder;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**************************************************************************************************
 * RestaurantFragment
 * Shows a restaurant's information
 *************************************************************************************************/

public class RestaurantFragment extends Fragment implements OnPlaceQueryResultListener {

    /**Data**/

    private PlacesClient placesClient;                  //The placesClient allowing to use Google Places API
    private Disposable disposable;                      //The disposable which gets the response from the API
    private Workmate currentUser;                       //The current user
    private Restaurant restaurant;                      //The restaurant

    /**UI Components**/

    @BindView(R.id.fragment_restaurant_image) ImageView imageView;
    @BindView(R.id.fragment_restaurant_name) TextView nameText;
    @BindView(R.id.fragment_restaurant_stars) RatingBar starsRatingBar;
    @BindView(R.id.fragment_restaurant_cuisineType) TextView cuisineTypeText;
    @BindView(R.id.fragment_restaurant_address) TextView addressText;
    @BindView(R.id.fragment_restaurant_button_call) Button callButton;
    @BindView(R.id.fragment_restaurant_button_like) Button likeButton;
    @BindView(R.id.fragment_restaurant_button_website) Button websiteButton;
    @BindView(R.id.fragment_restaurant_button_lunch) FloatingActionButton lunchButton;
    @BindView(R.id.fragment_restaurant_workmates) RecyclerView workmatesView;
    private WorkmateAdapter workmateAdapter;

    /**Constructor**/

    public RestaurantFragment(PlacesClient placesClient) {
        this.placesClient=placesClient;
    }

    /**Callbacks**/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_restaurant, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getActivity().getIntent()!=null) {
            this.currentUser = getActivity().getIntent().getParcelableExtra(MainActivity.KEY_BUNDLE_USER);
            this.restaurant = getActivity().getIntent().getParcelableExtra(MainActivity.KEY_BUNDLE_RESTAURANT);
            initializeView();
        }
    }

    @Override
    public void onGetGooglePlacesSearchResult(List<Restaurant> restaurants) {

    }

    @Override
    public void onGetRestaurantAllDetailsResult(Restaurant restaurant) {
        cuisineTypeText.setText(restaurant.getCuisineType());
        callButton.setTag(restaurant.getPhoneNumber());
        if(callButton.getTag()==null){
            disableButton(callButton);
        }
        websiteButton.setTag(restaurant.getWebUrl());
        if(websiteButton.getTag()==null){
            disableButton(websiteButton);
        }
    }

    /**Initializes the view items**/

    private void initializeView(){
        APIStreams.streamGetRestaurantImage(this.placesClient, restaurant, this.imageView);
        this.nameText.setText(this.restaurant.getName());
        this.starsRatingBar.setRating(restaurant.getNbStars());
        this.cuisineTypeText.setText("");
        this.addressText.setText(this.restaurant.getAddress());
        initializeCallButton();
        initializeLikeButton();
        initializeWebsiteButton();
        initializeLunchButton();
        initializeWorkmatesView();
        RestaurantActivity activity=(RestaurantActivity) getActivity();
        activity.runRestaurantAllDetailsQuery(this.restaurant, this);
    }

    /**Initializes the call button**/

    private void initializeCallButton(){
        this.callButton.setOnClickListener(v -> {
            if(v.getTag()!=null){
                Intent callIntent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+v.getTag().toString()));
                startActivity(callIntent);
            }
        });
    }

    /**Initializes the like button**/

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
            }
        });
    }

    /**Initializes the website button**/

    private void initializeWebsiteButton(){
        this.websiteButton.setOnClickListener(v -> {
            if(v.getTag()!=null){
                Intent webIntent=new Intent(Intent.ACTION_VIEW, Uri.parse(v.getTag().toString()));
                startActivity(webIntent);
            }
        });
    }

    /**Initializes the lunch button**/

    private void initializeLunchButton(){

        Calendar calendar= Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date=calendar.getTime();
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
                disableLunchButton();
            }

            activity.createLunchInFirebase(this.restaurant, this.currentUser);
        });
    }

    /**Initializes the workmates view**/

    private void initializeWorkmatesView(){
        this.workmateAdapter=new WorkmateAdapter(
                generateOptionsForAdapter(FirebaseQueriesLunch.getWorkmatesEatingAtRestaurant(this.restaurant)),
                WorkmateViewHolder.ID_RESTAURANT, Glide.with(this));
        this.workmatesView.setAdapter(this.workmateAdapter);
        this.workmatesView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    /**Creates options for FirebaseAdapter**/

    private FirestoreRecyclerOptions<Workmate> generateOptionsForAdapter(Query query){
        return new FirestoreRecyclerOptions.Builder<Workmate>()
                .setQuery(query, Workmate.class)
                .setLifecycleOwner(this)
                .build();
    }

    /**Disables a button and changes its UI**/

    private void disableButton(Button button){
        button.setEnabled(false);
        button.setTextColor(getResources().getColor(android.R.color.darker_gray));
        button.getCompoundDrawables()[1].mutate().setColorFilter
                (new PorterDuffColorFilter(getResources().getColor(android.R.color.darker_gray), PorterDuff.Mode.SRC_IN));
    }

    /**Disables the lunch button and changes its UI**/

    private void disableLunchButton(){
        this.lunchButton.setEnabled(false);
        this.lunchButton.getDrawable().mutate().setColorFilter
                (new PorterDuffColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN));
    }
}
