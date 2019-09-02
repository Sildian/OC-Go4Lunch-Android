package com.sildian.go4lunch.controller.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.sildian.go4lunch.R;
import com.sildian.go4lunch.utils.api.APIStreams;
import com.sildian.go4lunch.view.AutocompleteSuggestionAdapter;
import com.sildian.go4lunch.view.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;

/**************************************************************************************************
 * AutocompleteSearchActivity
 * Allow the user to search restaurants with Google places autocomplete API
 *************************************************************************************************/

public class AutocompleteSearchActivity extends AppCompatActivity {

    /**UI components**/

    private EditText keywordEditText;                           //The editText allowing to fill in the keyword
    private Button searchButton;                                //The button allowing to search the restaurants
    private RecyclerView suggestionsRecyclerView;               //The recyclerView containing the autocomplete predictions
    private AutocompleteSuggestionAdapter autocompleteSuggestionAdapter;    //The adapter monitoring the recyclerView

    /**Information**/

    private PlacesClient placesClient;                          //The places client allowing to use the Google places API
    private LatLng userLocation;                                //The user location
    private List<AutocompletePrediction> predictions;           //The list of predictions given by the places autocomplete API

    /**Callbacks**/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autocomplete_search);
        setSupportActionBar(findViewById(R.id.activity_autocomplete_search_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initializeInformation();
        initializePlaces();
        initializeKeywordEditText();
        initializesearchButton();
        initializeSuggestionRecyclerView();
    }

    @Override
    public void onBackPressed() {
        finishActivityCancel();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finishActivityCancel();
        return true;
    }

    /**Initializations**/

    private void initializeInformation(){
        double userLocationLatitude=getIntent().getDoubleExtra(MainActivity.KEY_BUNDLE_USER_LOCATION_LATITUDE, 0.0);
        double userLocationLongitude=getIntent().getDoubleExtra(MainActivity.KEY_BUNDLE_USER_LOCATION_LONGITUDE, 0.0);
        this.userLocation=new LatLng(userLocationLatitude, userLocationLongitude);
        this.predictions=new ArrayList<>();
    }

    private void initializePlaces(){
        Places.initialize(this, getString(R.string.google_maps_key));
        this.placesClient = Places.createClient(this);
    }

    private void initializeKeywordEditText(){

        this.keywordEditText=findViewById(R.id.activity_autocomplete_search_text_keyword);

        /*Runs the autocompleteRestaurants query each time the text is changed*/

        this.keywordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                APIStreams.streamGetAutocompleteRestaurants(
                        placesClient, userLocation, keywordEditText.getText().toString(),
                        predictions, autocompleteSuggestionAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initializesearchButton(){
        this.searchButton=findViewById(R.id.activity_autocomplete_search_button_search);
        this.searchButton.setOnClickListener(v -> {
            finishActivityOK(this.keywordEditText.getText().toString());
        });
    }

    private void initializeSuggestionRecyclerView(){

        this.suggestionsRecyclerView=findViewById(R.id.activity_autocomplete_search_list_suggestions);

        /*Initializes the different items to create the RecyclerView*/

        this.autocompleteSuggestionAdapter=new AutocompleteSuggestionAdapter(this.predictions);
        this.suggestionsRecyclerView.setAdapter(this.autocompleteSuggestionAdapter);
        this.suggestionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*Initializes the item click support*/

        ItemClickSupport.addTo(this.suggestionsRecyclerView, R.layout.list_autocomplete_suggestion_item)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    finishActivityOK(this.predictions.get(position).getPrimaryText(null).toString());
                });
    }

    /**Finishes the activity**/

    private void finishActivityOK(String keyword){
        Intent resultIntent=new Intent();
        resultIntent.putExtra(MainActivity.KEY_BUNDLE_KEYWORD, keyword);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void finishActivityCancel(){
        setResult(RESULT_CANCELED);
        finish();
    }
}
