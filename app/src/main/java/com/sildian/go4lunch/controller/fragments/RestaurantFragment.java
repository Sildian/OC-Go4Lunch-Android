package com.sildian.go4lunch.controller.fragments;


import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sildian.go4lunch.R;
import com.sildian.go4lunch.controller.activities.MainActivity;
import com.sildian.go4lunch.model.Restaurant;
import com.sildian.go4lunch.model.Workmate;
import com.sildian.go4lunch.utils.api.APIStreams;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**************************************************************************************************
 * RestaurantFragment
 * Shows a restaurant's information
 *************************************************************************************************/

public class RestaurantFragment extends Fragment {

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
        this.currentUser=getActivity().getIntent().getParcelableExtra(MainActivity.KEY_BUNDLE_USER);
        this.restaurant=getActivity().getIntent().getParcelableExtra(MainActivity.KEY_BUNDLE_RESTAURANT);
        updateUI();
    }

    /**Updates UI components**/

    private void updateUI(){
        APIStreams.streamGetRestaurantImage(this.placesClient, restaurant, this.imageView);
        this.nameText.setText(this.restaurant.getName());
        this.starsRatingBar.setRating(restaurant.getNbStars());
        this.cuisineTypeText.setText("");
        this.addressText.setText(this.restaurant.getAddress());
        initializeCallButton();
        initializeLikeButton();
        initializeWebsiteButton();
        initializeLunchButton();
        runRestaurantAllDetailsQuery(this.restaurant);
    }

    /**Initializes the call button**/

    private void initializeCallButton(){
        this.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getTag()!=null){
                    Intent callIntent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+v.getTag().toString()));
                    startActivity(callIntent);
                }
            }
        });
    }

    /**Initializes the like button**/

    private void initializeLikeButton(){
        this.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUser.addLike(restaurant);
            }
        });
    }

    /**Initializes the website button**/

    private void initializeWebsiteButton(){
        this.websiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getTag()!=null){
                    Intent webIntent=new Intent(Intent.ACTION_VIEW, Uri.parse(v.getTag().toString()));
                    startActivity(webIntent);
                }
            }
        });
    }

    /**Initializes the lunch button**/

    private void initializeLunchButton(){
        this.lunchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUser.updateLunch(restaurant);
            }
        });
    }

    /**Disables a button and changes its UI**/

    private void disableButton(Button button){
        button.setEnabled(false);
        button.setTextColor(getResources().getColor(android.R.color.darker_gray));
        button.getCompoundDrawables()[1].mutate().setColorFilter
                (new PorterDuffColorFilter(getResources().getColor(android.R.color.darker_gray), PorterDuff.Mode.SRC_IN));
    }

    /**Runs a query to get all details about a restaurant from Google and Here APIs, and update the related fields
     * @param restaurant : the restaurant
     */

    private void runRestaurantAllDetailsQuery(Restaurant restaurant){
        //TODO change radius to value
        this.disposable= APIStreams.streamGetRestaurantAllDetails(getActivity(), restaurant, 100)
                .subscribeWith(new DisposableObserver<Restaurant>(){
                    @Override
                    public void onNext(Restaurant restaurantWithAllDetails) {
                        cuisineTypeText.setText(restaurantWithAllDetails.getCuisineType());
                        callButton.setTag(restaurantWithAllDetails.getPhoneNumber());
                        if(callButton.getTag()==null){
                            disableButton(callButton);
                        }
                        websiteButton.setTag(restaurantWithAllDetails.getWebUrl());
                        if(websiteButton.getTag()==null){
                            disableButton(websiteButton);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("TAG_API", e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
