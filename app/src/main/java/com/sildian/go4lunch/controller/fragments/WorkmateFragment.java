package com.sildian.go4lunch.controller.fragments;


import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.firestore.Query;
import com.sildian.go4lunch.R;
import com.sildian.go4lunch.model.Restaurant;
import com.sildian.go4lunch.model.Workmate;
import com.sildian.go4lunch.utils.firebase.FirebaseQueriesWorkmate;
import com.sildian.go4lunch.view.ItemClickSupport;
import com.sildian.go4lunch.view.WorkmateAdapter;
import com.sildian.go4lunch.view.WorkmateViewHolder;

import java.util.List;

import butterknife.BindView;

/**************************************************************************************************
 * WorkmateFragment
 * Shows the list of all workmates
 *************************************************************************************************/

public class WorkmateFragment extends BaseFragment{

    /*********************************************************************************************
     * UI Components
     ********************************************************************************************/

    @BindView(R.id.fragment_workmate_workmates) RecyclerView workmatesView;
    private WorkmateAdapter workmateAdapter;

    /*********************************************************************************************
     * Constructors
     ********************************************************************************************/

    public WorkmateFragment(){
        super();
    }

    public WorkmateFragment(PlacesClient placesClient, LatLng userLocation, Workmate currentUser, List<Restaurant> restaurants) {
        super(placesClient, userLocation, currentUser, restaurants);
    }

    /*********************************************************************************************
     * BaseFragment methods
     ********************************************************************************************/

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_workmate;
    }

    @Override
    protected void initializeView(Bundle SavedInstanceState) {
        initializeWorkmatesView();
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

    private void initializeWorkmatesView(){

        /*Initializes the different items to create the RecyclerView*/

        this.workmateAdapter=new WorkmateAdapter(
                generateOptionsForAdapter(FirebaseQueriesWorkmate.getAllWorkmates()),
                WorkmateViewHolder.ID_WORKMATE, Glide.with(this));
        this.workmatesView.setAdapter(this.workmateAdapter);
        this.workmatesView.setLayoutManager(new LinearLayoutManager(getActivity()));

        /*Initializes the item click support*/

        ItemClickSupport.addTo(this.workmatesView, R.layout.list_workmate_item)
                .setOnItemClickListener((recyclerView, position, v) -> {

                    Workmate workmate=this.workmateAdapter.getItem(position);
                    Restaurant restaurant=workmate.getChosenRestaurantoday();
                    if(restaurant!=null) {
                        startRestaurantActivity(restaurant);
                    }
                });
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
