package com.sildian.go4lunch.controller.fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sildian.go4lunch.R;
import com.sildian.go4lunch.controller.activities.MainActivity;
import com.sildian.go4lunch.model.Restaurant;

import butterknife.BindView;
import butterknife.ButterKnife;

/**************************************************************************************************
 * RestaurantFragment
 * Shows a restaurant's information
 *************************************************************************************************/

public class RestaurantFragment extends Fragment {

    /**Data**/

    private Restaurant restaurant;

    /**UI Components**/

    @BindView(R.id.fragment_restaurant_image) ImageView imageView;
    @BindView(R.id.fragment_restaurant_name) TextView nameText;
    @BindView(R.id.fragment_restaurant_cuisineType) TextView cuisineTypeText;
    @BindView(R.id.fragment_restaurant_address) TextView addressText;
    @BindView(R.id.fragment_restaurant_button_call) Button callButton;
    @BindView(R.id.fragment_restaurant_button_like) Button likeButton;
    @BindView(R.id.fragment_restaurant_button_website) Button websiteButton;
    @BindView(R.id.fragment_restaurant_button_lunch) FloatingActionButton lunchButton;
    @BindView(R.id.fragment_restaurant_workmates) RecyclerView workmatesView;

    /**Constructor**/

    public RestaurantFragment() {

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
        this.restaurant=getActivity().getIntent().getParcelableExtra(MainActivity.KEY_BUNDLE_RESTAURANT);
        updateUI();
    }

    /**Updates UI components**/

    private void updateUI(){
        this.nameText.setText(this.restaurant.getName());
    }
}
